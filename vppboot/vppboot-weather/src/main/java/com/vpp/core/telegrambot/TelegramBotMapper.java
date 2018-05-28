package com.vpp.core.telegrambot;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TelegramBotMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TelegramBot record);

    int insertSelective(TelegramBot record);

    TelegramBot selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TelegramBot record);

    int updateByPrimaryKey(TelegramBot record);

    TelegramBot getTelegramBotByKey(@Param("key") String key);

    Integer getInviteUserCnt(@Param("convertCode") String convertCode);

    Integer selectUserIdCntByMap(@Param("telegramUserId") String telegramUserId);
}