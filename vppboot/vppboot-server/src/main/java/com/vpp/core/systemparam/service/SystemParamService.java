package com.vpp.core.systemparam.service;

import com.vpp.core.systemparam.bean.SystemParam;
import com.vpp.core.systemparam.mapper.SystemParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemParamService implements ISystemParamService{
    @Autowired
    private SystemParamMapper systemParamMapper;

    @Override
    public SystemParam selectByParamName(String paramName){
        return systemParamMapper.selectByParamName(paramName);
    }

    @Override
    public SystemParam selectByParamTypeAndName(String paramType, String paramName){
        return systemParamMapper.selectByParamTypeAndName(paramType, paramName);
    }

}
