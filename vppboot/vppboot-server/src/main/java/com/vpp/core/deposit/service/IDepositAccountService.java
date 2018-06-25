package com.vpp.core.deposit.service;

import com.vpp.core.deposit.bean.DepositAccount;

public interface IDepositAccountService {

    int insertDepositAccount(DepositAccount depositAccount);

    /**
     * 根据钱包地址查询账号信息
     * 
     * @author Lxl
     * @param account
     * @return
     * @throws Exception
     */
    DepositAccount findByAccount(String account) throws Exception;

}
