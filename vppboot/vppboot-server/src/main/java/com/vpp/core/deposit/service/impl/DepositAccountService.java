package com.vpp.core.deposit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vpp.core.deposit.bean.DepositAccount;
import com.vpp.core.deposit.mapper.DepositAccountMapper;
import com.vpp.core.deposit.service.IDepositAccountService;

@Service
public class DepositAccountService implements IDepositAccountService {

    @Autowired
    private DepositAccountMapper depositAccountMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insertDepositAccount(DepositAccount depositAccount) {
        return depositAccountMapper.insertSelective(depositAccount);
    }

    @Override
    public DepositAccount findByAccount(String account) throws Exception {
        return depositAccountMapper.findByAccount(account);
    }

}
