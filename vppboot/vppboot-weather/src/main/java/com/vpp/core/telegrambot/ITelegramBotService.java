package com.vpp.core.telegrambot;


public interface ITelegramBotService {

    int register(TelegramBot telegramBot);

    TelegramBot getTelegramBotByKey(String key);

    Integer getInviteUserCnt(String convertCode);

    void updateTelegramBotByKey(TelegramBot telegramBo);

    String cheackConvertCode(String userId, String groupId, String convertCode);
}
