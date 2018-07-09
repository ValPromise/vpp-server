package com.vpp.core.coinguess.app;

import static com.vpp.common.utils.StringUtils.isPureDigital;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.vpp.core.coinguess.mapper.CoinguessMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.vpp.common.page.PageInfo;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.OrderUtil;
import com.vpp.common.vo.CoinguessOrderListVo;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.coinguess.bean.Coinguess;
import com.vpp.core.coinguess.service.ICoinguessService;
import com.vpp.core.common.CommonController;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguess;
import com.vpp.core.standardized.productcoinguess.service.IProductCoinguessService;

@RestController
@RequestMapping("/app/coinguess")
public class CoinguessController extends CommonController {

    @Autowired
    private ICoinguessService coinguessService;

    @Autowired
    private IProductCoinguessService productCoinguessService;

    @Autowired
    private ICustomerService customerService;

    private static final Logger logger = LogManager.getLogger(CoinguessController.class);

    @RequestMapping(value = "/add")
    public ResultVo add(String token, String targetId, BigDecimal orderAmt, String orderDir, String orderTs,
                        BigDecimal orderPrice, HttpServletResponse response) {
//        System.out.println("targetId" + targetId);
//        System.out.println("orderAmt" + orderAmt);
//        System.out.println("orderDir" + orderDir);
//        System.out.println("orderTs" + orderTs);
//        System.out.println("orderPrice" + orderPrice);


        Long startTimestamp = System.currentTimeMillis();
        Long d0 = System.currentTimeMillis();
        // 支持跨域
        response.addHeader("Access-Control-Allow-Origin", "*");

        Coinguess coinguess = new Coinguess();

        // 获得coinguess产品参数
        ProductCoinguess productCoinguess = productCoinguessService.selectByProductId(targetId);


        // 校验产品是否合法
        if (productCoinguess == null) {
            return ResultVo.setResultError(MessageFormat.format(getMessage("product_not_exist"), targetId)); //"产品: " + targetId + " 不存在"
        }

        if (!"1".equals(productCoinguess.getProductState().toString())) {
            return ResultVo.setResultError(MessageFormat.format(getMessage("product_not_active"), targetId)); //("产品: " + targetId + " 未启用");
        }

        // 客户号
        coinguess.setCustomerId(getCustomerId(token));

        Long contractOwnerId = productCoinguess.getCustomerId(); // 合约所有者id


        Customer customer = customerService.selectCustomerById(getCustomerId(token)); // 用户
        Customer contractOwner = customerService.selectCustomerById(productCoinguess.getCustomerId()); // 合约所有者

        // 订单号，系统生成
        coinguess.setOrderId(OrderUtil.createId("C"));

        // 产品号
        coinguess.setTargetId(targetId);

        // 下单金额
        if (orderAmt == null) {
            return ResultVo.setResultError(getMessage("order_amt_must_input")); //"下单金额必输"
        }

        if (orderAmt.compareTo(productCoinguess.getMinBet()) < 0) {
            return ResultVo.setResultError(MessageFormat.format(getMessage("order_amt_should_great_then"), productCoinguess.getMinBet()));  //"下单金额必须 >= " + productCoinguess.getMinBet()
        } else if (orderAmt.compareTo(productCoinguess.getMaxBet()) > 0) {
            return ResultVo.setResultError(MessageFormat.format(getMessage("order_amt_should_less_then"), productCoinguess.getMaxBet())); //"下单金额必须 <= " + productCoinguess.getMaxBet())
        } else {
            coinguess.setOrderAmt(orderAmt);
        }

        // 竞猜方向

        if (!"0".equals(orderDir) && !"1".equals(orderDir)) {
            return ResultVo.setResultError(getMessage("invalid_order_dir")); //"竞猜方向不合法（合法值为0-猜涨，1-猜跌）");
        } else {
            coinguess.setOrderDir(Integer.valueOf(orderDir));
        }

        // 下单时间戳
        Long orderTsLong = null;
        try {
            orderTsLong = Long.parseLong(orderTs);
        } catch (NumberFormatException e) {
            return ResultVo.setResultError(getMessage("invalid_order_timestamp")); //"时间戳未输入或非法");
        }

        System.out.println("订单号：" + coinguess.getOrderId() + "系统时间:" + DateUtil.unixTimestampToTimestamp(startTimestamp + "") + "订单时间:" + DateUtil.unixTimestampToTimestamp(orderTs) + "相差：" + (startTimestamp - orderTsLong));

        if (isPureDigital(orderTs) == false) {
            return ResultVo.setResultError(getMessage("timestamp_shoud_be_valid_unix_ts")); //"下单时间戳必须为正整数，毫秒unix时间戳");
        }
//        // todo: 测试时放到30秒，系统上线放到15秒
        else if (orderTsLong < startTimestamp - 15000L || orderTsLong > startTimestamp + 15000L) { // 下单时间在系统时间正负20秒(20000毫秒）内
//            return ResultVo.setResultError("非法下单时间戳" + "系统时间:" + startTimestamp + "orderts:" + orderTsLong + "相差：" + (startTimestamp - orderTsLong)); //非法下单时间戳
            return ResultVo.setResultError(""); //非法下单时间戳

        } else {
            coinguess.setOrderTsUnix(orderTs); // 记录unix下单时间戳
            String strTimestamp = DateUtil.unixTimestampToTimestamp(orderTs); // unix时间戳转换为yyyyMMddHHmmssSSS时间戳
            coinguess.setOrderTs(strTimestamp);
        }


        // 预期收益 = orderAmt * （1+利润率）
        coinguess.setProfit(orderAmt.multiply(new BigDecimal(1 + productCoinguess.getProfitRate())));

        // 有合约所有者则检查合约所有者是否能足额赔付, 不能赔付时不接受订单
        if (contractOwnerId != 0 && contractOwnerId != null) {
            if (contractOwner.getBalance().compareTo(coinguess.getProfit()) < 0) {
                return ResultVo.setResultError(getMessage("margin_money_not_enough")); //"下单金额过大，赔付保证金不足";
            }
        }

        // 推算判定时间(productCoinguess.getLotteryInterval()秒 * 1000毫秒 开一次奖）
        long interval = productCoinguess.getLotteryInterval().longValue() * 1000L; // 开奖时间周期

        long base = orderTsLong / interval; // 取整除数
        long lotteryTimeInUnix = (base + 1) * interval;
        // 如果是在开奖前productCoinguess.getOrderDeadline()分钟，则开奖时间延后一期
        if ((lotteryTimeInUnix - orderTsLong) < productCoinguess.getOrderDeadline() * 1000L) {   //30秒算当前投注周期
            lotteryTimeInUnix += interval;
        }
        String lotteryTime = DateUtil.unixTimestampToTimestamp(Long.toString(lotteryTimeInUnix)); // 开奖时间
        coinguess.setLotteryTime(lotteryTime);  //开奖时间（人眼识别）
        coinguess.setLotteryTimeUnix(Long.toString(lotteryTimeInUnix));  //unix时间

        // 风控：单人单期最多下N单， N = productCoinguess.getOrderAllowed()，数据库字段product_coinguess.order_allowed

        if(coinguessService.getTotalOrders(coinguess.getCustomerId(),lotteryTime) >= productCoinguess.getOrderAllowed()){
            return ResultVo.setResultError(MessageFormat.format(getMessage("order_num_limit"),productCoinguess.getOrderAllowed())); //"每期限下" + productCoinguess.getOrderAllowed() + "单");
        }

        // 单期下单金额汇总
        BigDecimal totalOrderAmtInOneTerm = coinguessService.getTotalOrderAmountByLotteryTime(lotteryTime);

        BigDecimal maxBetWithinOneTerm = productCoinguess.getSingleTermLimit();

        if(maxBetWithinOneTerm.compareTo(BigDecimal.ZERO) == 0){
            return ResultVo.setResultError(MessageFormat.format(getMessage("total_order_amt_limit"),20000)); //"单期系统接受的订单已经超过上限金额：" + 20000 + " 请在下期下单");
        }


        // 风控：当期第一单判断，下单金额不能超过maxBetWithinOneTerm金额
        if (totalOrderAmtInOneTerm == null && // 当期下单金额汇总为0，代表为当期第一单
                maxBetWithinOneTerm != null && // maxBetWithinOneTerm 为非空
                !(maxBetWithinOneTerm.compareTo(BigDecimal.ZERO) == 0) && // maxBetWithinOneTerm 不为0
                orderAmt.compareTo(maxBetWithinOneTerm) > 0) { // 下单金额超上限
            return ResultVo.setResultError(MessageFormat.format(getMessage("total_order_amt_limit"),maxBetWithinOneTerm)); //"单期系统接受的订单已经超过上限金额：" + maxBetWithinOneTerm + " 请在下期下单");
        }

        // 风控：当期后续订单判断，历史下单金额 + 新下单金额 不能超过maxBetWithinOneTerm金额
        if (totalOrderAmtInOneTerm != null && // totalOrderAmtInOneTerm 数据库有值
                maxBetWithinOneTerm != null && // maxBetWithinOneTerm 数据库有值
                !(maxBetWithinOneTerm.compareTo(BigDecimal.ZERO) == 0) && // maxBetWithinOneTerm 不为0
                totalOrderAmtInOneTerm.add(orderAmt).compareTo(maxBetWithinOneTerm) > 0) { // 单期下注汇金额 + 本次投注金额 超过
            // 系统允许的总金额，该期不接受新订单
            return ResultVo.setResultError(MessageFormat.format(getMessage("total_order_amt_limit"),maxBetWithinOneTerm)); //"单期系统接受的订单已经超过上限金额：" + maxBetWithinOneTerm + " 请在下期下单");
        }

        // 订单状态：0-待开奖，1-输，2-赢
        coinguess.setStatus(0);

        // if(customer.getBalance().compareTo(orderAmt) >= 0){
        // //减少用户余额
        // try {
        //// customerService.expenditureBalance(customer.getId(),orderAmt, "猜币下单-用户，订单ID： " + coinguess.getOrderId());
        // } catch (Exception e) {
        // return ResultVo.setResultError("余额不足或扣款失败");
        // }
        // //有合约所有者则增加合约所有者余额
        // if(contractOwnerId != 0 && contractOwnerId != null){
        //// customerService.incomeBalance(contractOwnerId,orderAmt,"猜币下单-庄家，订单ID：" + coinguess.getOrderId());
        // }
        // }
        // else{
        // return ResultVo.setResultError("账户余额不足，请充值！");
        // }

        if (customer.getBalance().compareTo(orderAmt) < 0) {
            return ResultVo.setResultError(getMessage("insufficient_balance")); //"账户余额不足，请充值！");
        }
        // // 插入记录
        // if (coinguessService.checkOrderExistence(coinguess.getCustomerId(),coinguess.getOrderTs()) == 1){
        // return ResultVo.setResultError("订单已存在，请勿重复提交");
        // }
        // else{
        // int ret = coinguessService.insertCoinguessOrder(coinguess);
        // if(ret == 0){
        // return ResultVo.setResultError("添加失败,请稍候再试");
        // }
        // }

        try {
            int ret = coinguessService.insertCoinguessOrder(coinguess, customer.getId(), contractOwnerId, orderAmt);
            if (ret == 0) {
                return ResultVo.setResultError(""); //添加失败,请稍候再试
            }
        } catch (Exception e) {
            logger.error("insertCoinguessOrder error ::: {} ", e.getMessage());
            return ResultVo.setResultError("");  //添加失败,请稍候再试
        }

        // 返回自增长订单id
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", coinguess.getId());


        Long dn = System.currentTimeMillis();

        System.out.println("下单id：" + coinguess.getId() + "timestamp" + d0 + "  "+ dn + " 下单处理时间: " + (dn - d0));
        return ResultVo.setResultSuccess(result);


    }

    @RequestMapping(value = "/order/getOrderList")
    public ResultVo getCoinguessList(String token, Integer currentPage, Integer pageSize, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        if (currentPage == null || isPureDigital(currentPage.toString()) == false) {
            currentPage = new Integer(1);
        }
        if (pageSize == null || isPureDigital(pageSize.toString()) == false) {
            pageSize = new Integer(15);
        }

        Page<Coinguess> list = coinguessService.selectCoinguessInfoByCustomerId(currentPage, pageSize, getCustomerId(token)); // getCustomerId(token)即为客户号
        PageInfo<Coinguess> pageInfos = new PageInfo<Coinguess>(list);
        List<CoinguessOrderListVo> voList = this.putVo(pageInfos.getList());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", voList);
        result.put("total", list.getTotal());
        result.put("currentPage", list.getPageNum());
        result.put("pageSize", list.getPageSize());

        return ResultVo.setResultSuccess(result);
    }

    private List<CoinguessOrderListVo> putVo(List<Coinguess> csList) {
        List<CoinguessOrderListVo> voList = new ArrayList<>();

        for (Coinguess cc : csList) {
            CoinguessOrderListVo vo = new CoinguessOrderListVo();
            vo.setInnerOrderId(cc.getOrderId());
            vo.setTitle(cc.getTargetId());
            String content = 0 == cc.getOrderDir() ? getMessage("bullish"):getMessage("bearish"); //"看涨" : "看跌";
            vo.setContent(content);
            vo.setGmtCreate(DateUtil.removeS(cc.getGmtCreate()));
            String status = "";
            String statusString = "";
            String payout = "0";

            if (0 == cc.getStatus()) {
                statusString = getMessage("waiting_for_judge"); //"等待判定";
            } else if (1 == cc.getStatus()) {
                statusString = getMessage("lose"); //"已判定，无需履行";
            } else if (2 == cc.getStatus()){
                payout = cc.getProfit().toString();
                statusString = getMessage("win"); //"已判定，已履行";
            } else{
                statusString = getMessage("refund"); //"网络故障，已退款";
            }

            vo.setAmount(cc.getOrderAmt().toString() + "-" + payout);

            vo.setStatusString(statusString);
            vo.setStatus(cc.getStatus().toString());
            voList.add(vo);
        }
        return voList;
    }


    @RequestMapping("/getDetail")
    public ResultVo coinguessDetail(String token, String orderId, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (orderId == null) {
            return ResultVo.setResultError(getMessage("id_must_input")); //"id必输");
        }

        Coinguess coinguess = coinguessService.selectCoinguessOrderByOrderId(orderId);
        if (coinguess == null) {
            return ResultVo.setResultError(getMessage("no_order_detail")); //"未查询到订单详情");
        } else {
            // 时间戳毫秒变为秒再返回
            coinguess.setOrderTs(coinguess.getOrderTs().substring(0, 19));
            coinguess.setLotteryTime(coinguess.getLotteryTime().substring(0, 19));
            return ResultVo.setResultSuccess(coinguess);
        }
    }

//    @RequestMapping("/getResult")
//    public ResultVo coinguessResult(String token, String ids, HttpServletResponse response) {
//        Long d0 = System.currentTimeMillis();
//        int totalNums = 0;
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        if(ids == null){
//            return ResultVo.setResultError("id必输");
//        }
//
//        //对该客户进行开奖
//        coinguessService.runTheLottery(getCustomerId(token));
//
//        BigDecimal totalOrderAmt = new BigDecimal(0);
//        BigDecimal totalWin = new BigDecimal(0);
//        String lotteryTime = null;
//
//        if(ids.contains(",")){   //一期下单多次
//            String[] idList = ids.split(",");
//            for(int i=0; i<idList.length; i++){
//                totalNums = i;
//                if (!isPureDigital(idList[i])){
//                    return ResultVo.setResultError("id应为纯数字" + "id");
//                }
//                Coinguess coinguess = coinguessService.selectCoinguessOrderById(Long.parseLong(idList[i]));
//                if(coinguess != null && coinguess.getLotteryTime() != null){
////					if(lotteryTime == null){
////						lotteryTime = coinguess.getLotteryTime();
////						logger.info("id:" + coinguess.getId() + "lotteryTime: " + lotteryTime);
////					}else if(!lotteryTime.equals(coinguess.getLotteryTime())){
////						logger.info("多个id在同一期");
////
////						return ResultVo.setResultError("多个id必须都在同一期");
////					}
//
//                    totalOrderAmt = totalOrderAmt.add(coinguess.getOrderAmt());   //累加下单金额
//                    if(coinguess.getStatus() == 2){ 			//2-赢
//                        totalWin = totalWin.add(coinguess.getProfit());  //用户赢的情况下累加收益
//                    }
//                }else{
//                    return ResultVo.setResultError("订单id: "+ idList[i] + "在订单表中不存在");
//                }
//            }
//        }else{ //一期下单一次
//            if (!isPureDigital(ids)){
//                return ResultVo.setResultError("id应为纯数字，或由英文逗号分隔");
//            }else{
//                Coinguess coinguess = coinguessService.selectCoinguessOrderById(Long.parseLong(ids));
//                if(coinguess == null){
//                    return ResultVo.setResultError("订单id: "+ ids + "在订单表中不存在");
//                }
//                totalOrderAmt = totalOrderAmt.add(coinguess.getOrderAmt());
//                if(coinguess.getStatus() == 2){ 			//2-赢
//                    totalWin = totalWin.add(coinguess.getProfit());  //用户赢的情况下累加收益
//                }
//                lotteryTime = coinguess.getLotteryTime();
//            }
//        }
//
////		System.out.println("totalOrderAmt" + totalOrderAmt);
////		System.out.println("totalWin" + totalWin);
//
//        Map<String, Object> result = new HashMap<String, Object>();
//        result.put("msg","本期您收益为 " + totalWin.toString() + " VPP");
//        result.put("lotteryTime", lotteryTime );
//        result.put("totalOrderAmt", totalOrderAmt.toString() );
//        result.put("totalWin", totalWin.toString() );
//
//        Long d1 = System.currentTimeMillis();
//        System.out.println("开奖id: " + ids + " 开奖条数: " + totalNums + " 开奖时间: " + (d1-d0));
//        return ResultVo.setResultSuccess(result);
//    }

    @RequestMapping("/getResult")
    public ResultVo coinguessResult(String token, String lotteryTime, HttpServletResponse response) {
        Long lotteryTimeOrigin = Long.parseLong(lotteryTime);
        Long realLotteryTime = lotteryTimeOrigin/60000L * 60000L;

        Long d0 = System.currentTimeMillis();
        response.addHeader("Access-Control-Allow-Origin", "*");

        //对该客户进行开奖
        BigDecimal totalWin = coinguessService.runTheLottery(getCustomerId(token), realLotteryTime.toString());

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msg",MessageFormat.format(getMessage("total_profit_in_one_term"),totalWin.toString())); //"本期您收益为 " + totalWin.toString() + " VPP");

        result.put("lotteryTime", lotteryTime );
        //result.put("totalOrderAmt", totalOrderAmt.toString() );
        result.put("totalWin", totalWin.toString() );

        Long d1 = System.currentTimeMillis();
        System.out.println("开奖时间: " + (d1-d0));
        return ResultVo.setResultSuccess(result);
    }

}
