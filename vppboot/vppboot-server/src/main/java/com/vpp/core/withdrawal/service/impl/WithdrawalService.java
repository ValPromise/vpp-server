package com.vpp.core.withdrawal.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.common.utils.ConstantsServer;
import com.vpp.common.utils.DealUtil;
import com.vpp.common.vo.DepositVo;
import com.vpp.core.cashlog.service.ICustomerCashLogService;
import com.vpp.core.common.EthController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.mapper.CustomerMapper;
import com.vpp.core.withdrawal.bean.Withdrawal;
import com.vpp.core.withdrawal.bean.WithdrawalAccount;
import com.vpp.core.withdrawal.mapper.WithdrawalAccountMapper;
import com.vpp.core.withdrawal.mapper.WithdrawalMapper;
import com.vpp.core.withdrawal.service.IWithdrawalService;

@Service
public class WithdrawalService implements IWithdrawalService {

    private static final Logger logger = LogManager.getLogger(WithdrawalService.class);

    @Autowired
    private WithdrawalAccountMapper withdrawalAccountMapper;
    @Autowired
    private WithdrawalMapper withdrawalMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ICustomerCashLogService customerCashLogService;

    @Override
    public Withdrawal selectByPrimaryKey(Long id) {
        return withdrawalMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Withdrawal record) {
        return withdrawalMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 提现付款账号
     */
    @Value("${withdrawal.pay.account}")
    public String withdrawalPayAccount;

    @Override
    public int insertWithdrawalAccount(WithdrawalAccount withdrawalAccount) {
        int ret = 0;
        try {
            ret = withdrawalAccountMapper.insertSelective(withdrawalAccount);
        } catch (Exception e) {
            ret = 0;
            logger.error("发生异常:" + e.getMessage());
        }
        return ret;
    }

    @Override
    public List<WithdrawalAccount> selectWithdrawalAccountByCustomerId(Long customerId) {
        List<WithdrawalAccount> list = null;
        try {
            list = withdrawalAccountMapper.selectWithdrawalAccountByCustomerId(customerId);
        } catch (Exception e) {
            logger.error("发生异常:" + e.getMessage());
        }
        return list;
    }

    @Override
    public int updateWithdrawalAccount(WithdrawalAccount withdrawalAccount) {
        int ret = 1;
        try {
            ret = withdrawalAccountMapper.updateByPrimaryKeySelective(withdrawalAccount);
        } catch (Exception e) {
            ret = 0;
            logger.error("发生异常:" + e.getMessage());
        }
        return ret;
    }

    @Override
    public Page<Withdrawal> selectWithdrawalList(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Withdrawal> rows = withdrawalMapper.selectWithdrawalList(map);
        return rows;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void withdrawal(Withdrawal withdrawal) throws Exception {
        // 录入提现数据
        withdrawalMapper.insertSelective(withdrawal);
        Long customerId = withdrawal.getCustomerId();
        Customer customer = customerMapper.selectByPrimaryKey(customerId);
        Customer ctm = new Customer();
        ctm.setId(customerId);
        ctm.setBalance(DealUtil.priceSubtract(customer.getBalance(), withdrawal.getVpp()));
        // 减客户钱包金额
        customerMapper.updateByPrimaryKeySelective(ctm);
        // 提现流水
        customerCashLogService.insertWithdrawal(customerId, withdrawal.getVpp(), "申请提现");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void syncWithdrawalData() throws Exception {

        // 1：：查询未处理的提现列表
        // 2：：根据区块号查询链上提现列表并按区块号升序排序
        // 3：：比对是否有处理完成的数据，如果有则提现成功
        List<Withdrawal> withdrawals = withdrawalMapper.findWaitListByAddress();
        Iterator<Withdrawal> withdrawalIterator = withdrawals.iterator();

        Long fromBlock = withdrawalMapper.findMaxBlockNumber();
        fromBlock = null == fromBlock ? 0L : fromBlock;
        List<DepositVo> depositVos = EthController.queryVppWithdrawal(withdrawalPayAccount, fromBlock, "");
        this.sort(depositVos);// 排序
        // Iterator<DepositVo> iterator = depositVos.iterator();

        if (null == depositVos || depositVos.isEmpty()) {
            logger.debug("公链无提现打款数据--------- ");
        } else {
            int i = 0;
            while (withdrawalIterator.hasNext()) {
                Withdrawal wd = withdrawalIterator.next();
                String payee = wd.getPayeeAddress();
                // if (payee.equals("0x13328a9cd26a5d7cc51b6521ce76b2d31fe2e280")) {
                // logger.debug("wd ::: {}", payee);
                // }
                //

                for (int j = 0; j < depositVos.size(); j++) {
                    // while (iterator.hasNext()) {
                    DepositVo vo = depositVos.get(j);
                    // if (payee.toLowerCase().equals(vo.getTo().toLowerCase())) {
                    // logger.debug("wd--------------- ::: {},,,{}", payee, wd);
                    // }
                    //
                    // if (payee.toLowerCase().equals(vo.getTo().toLowerCase())) {
                    // logger.debug("--------- ::: {}", wd);
                    // logger.debug("vo ::: {},{},{},{},{},{}", withdrawalPayAccount, vo.getFrom(), payee, vo.getTo(),
                    // wd.getVpp().floatValue(), vo.getValue().floatValue());
                    // logger.debug(withdrawalPayAccount.toLowerCase().equals(vo.getFrom().toLowerCase())
                    // && payee.toLowerCase().equals(vo.getTo().toLowerCase())
                    // && wd.getVpp().floatValue() == vo.getValue().floatValue());
                    //
                    // }
                    // 判断数据是否相同 支付账号，收款账号，金额全部相等
                    if (withdrawalPayAccount.toLowerCase().equals(vo.getFrom().toLowerCase())
                            && payee.toLowerCase().equals(vo.getTo().toLowerCase())
                            && wd.getVpp().floatValue() == vo.getValue().floatValue()) {
                        // 修改提现记录为已经提现
                        wd.setCashNo(vo.getTransactionHash());
                        wd.setPayerAddress(vo.getFrom());
                        wd.setGmtModified(new Date());
                        wd.setState(ConstantsServer.WITHDRAWAL_STATE_SUCCESS);
                        wd.setBcTimeStamp(vo.getTimeStamp());
                        wd.setBcBlockNumber(vo.getBlockNumber().intValue());
                        wd.setBcReceipt(vo.toString());
                        // 修改数据
                        withdrawalMapper.updateByPrimaryKeySelective(wd);
                        logger.debug("查询到公链提现数据，处理--------- ::: {}", wd);
                        depositVos.remove(j);// 移除当前已经处理过的链上充值记录
                        j -= 1;
                        break;
                    }
                }
                // if (payee.equals("0x13328a9cd26a5d7cc51b6521ce76b2d31fe2e280")) {
                // i++;
                // }
                withdrawalIterator.remove();
            }
            // logger.debug("i:::{}", i);
        }
    }

    /**
     * 排序
     * 
     * @author Lxl
     * @param depositVos
     */
    private void sort(List<DepositVo> depositVos) {
        Collections.sort(depositVos, new Comparator<DepositVo>() {
            @Override
            public int compare(DepositVo o1, DepositVo o2) {
                return o1.getBlockNumber().intValue() - o2.getBlockNumber().intValue();
            }
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void reject(Withdrawal record) {
        // insertWithdrawalReject
        // 修改提现状态为拒绝
        // 新增提现拒绝资金流水
        // 恢复账户冻结钱包余额
        record.setState(ConstantsServer.WITHDRAWAL_STATE_REJECT);// 拒绝
        withdrawalMapper.updateByPrimaryKeySelective(record);
        Long customerId = record.getCustomerId();
        customerCashLogService.insertWithdrawalReject(customerId, record.getVpp(), "提现拒绝，恢复账户余额结束");

        Customer customer = customerMapper.selectByPrimaryKey(customerId);
        Customer ctm = new Customer();
        ctm.setId(customerId);
        ctm.setBalance(DealUtil.priceAdd(customer.getBalance(), record.getVpp()));
        // 减客户钱包金额
        customerMapper.updateByPrimaryKeySelective(ctm);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void fail(Withdrawal record) {
        // insertWithdrawalReject
        // 修改提现状态为打款失败
        // 新增提现拒绝资金流水
        // 恢复账户冻结钱包余额
        record.setState(ConstantsServer.WITHDRAWAL_STATE_FAIL);// 打款失败
        withdrawalMapper.updateByPrimaryKeySelective(record);
        Long customerId = record.getCustomerId();
        customerCashLogService.insertWithdrawalFail(customerId, record.getVpp(), "提现打款失败，恢复账户余额结束");

        Customer customer = customerMapper.selectByPrimaryKey(customerId);
        Customer ctm = new Customer();
        ctm.setId(customerId);
        ctm.setBalance(DealUtil.priceAdd(customer.getBalance(), record.getVpp()));
        // 减客户钱包金额
        customerMapper.updateByPrimaryKeySelective(ctm);
    }

}
