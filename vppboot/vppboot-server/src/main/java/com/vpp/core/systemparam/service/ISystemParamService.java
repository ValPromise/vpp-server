package com.vpp.core.systemparam.service;

import com.vpp.core.systemparam.bean.SystemParam;

public interface ISystemParamService {
    SystemParam selectByParamName(String ParamName);

    SystemParam selectByParamTypeAndName(String paramType, String paramName);
}
