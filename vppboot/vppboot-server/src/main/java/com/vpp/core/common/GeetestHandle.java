package com.vpp.core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.vpp.common.geetest.GeetestConfig;

/**
 * 极验验证 工具类
 * 
 * @author Lxl
 * @version V1.0 2018年5月25日
 */
@Component
public class GeetestHandle {
    private static final Logger logger = LogManager.getLogger(GeetestHandle.class);
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 验证是否通过安全校验
     * 
     * @author Lxl
     * @param mobile
     * @return
     */
    public boolean isSuccess(String mobile) {
        String key = GeetestConfig.GEETEST_REDIS_HASH_PREFIX + mobile;
        logger.debug("geetest key:::{}", key);
        if (redisTemplate.hasKey(key)) {
            Object suc = redisTemplate.opsForHash().get(key, GeetestConfig.GEETEST_REDIS_SUCCESS);
            logger.debug("geetest success:::{}", key);
            if (null == suc) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
