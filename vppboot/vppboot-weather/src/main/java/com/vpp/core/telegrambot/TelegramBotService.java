package com.vpp.core.telegrambot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramBotService implements ITelegramBotService {
    private static final Logger logger = LogManager.getLogger(TelegramBotService.class);

    private static Integer ADD_INTITE_AMOUNT = 50;
    @Autowired
    private TelegramBotMapper telegramBotMapper;

    @Override
    public synchronized int register(TelegramBot telegramBot) {
        return telegramBotMapper.insertSelective(telegramBot);
    }

    @Override
    public synchronized TelegramBot getTelegramBotByKey(String key) {
        return telegramBotMapper.getTelegramBotByKey(key);
    }

    @Override
    public synchronized Integer getInviteUserCnt(String convertCode) {
        return telegramBotMapper.getInviteUserCnt(convertCode);
    }

    @Override
    public synchronized void updateTelegramBotByKey(TelegramBot telegramBot) {
        telegramBot.setGmtModified(new Date());
        telegramBot.setConvertAmount(100);
        telegramBotMapper.updateByPrimaryKeySelective(telegramBot);
        TelegramBot fromTele = telegramBotMapper.getTelegramBotByKey(telegramBot.getFromConvertCode());
        if (fromTele != null) {
            // /if ((fromTele.getTotalAmount() == null?0:fromTele.getTotalAmount()) < MAX_INTITE_AMOUNT) {
            Integer totalAmout = fromTele.getTotalAmount() == null ? 0 : fromTele.getTotalAmount();
            Integer newTotalAmout = totalAmout + ADD_INTITE_AMOUNT;
            fromTele.setTotalAmount(newTotalAmout);
            fromTele.setGmtModified(new Date());
            telegramBotMapper.updateByPrimaryKeySelective(fromTele);
            // }
        }
    }

    @Override
    public synchronized String cheackConvertCode(String userId, String groupId, String convertCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("telegramUserId", userId);
        Integer teles = telegramBotMapper.selectUserIdCntByMap(userId);
        if (teles > 0) {
            // return
            // "FAILURE! Your telegram account has been bound to a code and cannot be bound to other codes. 校验失败！您的telegram账号已经绑定过验证码，无法绑定其他验证码。";
            return "";
        }
        params.clear();
        params.put("convertCode", convertCode);
        TelegramBot telegram = telegramBotMapper.getTelegramBotByKey(convertCode);
        if (telegram == null) {
            return "";
        }
        if (null != telegram.getConvertAmount() && telegram.getConvertAmount() > 0) {
            return "FAILURE! Your code has been used before.校验失败！该验证码曾被激活过，无法获得VPP奖励。";
        }
        telegram.setTelegramUserId(userId);
        telegram.setTelegramGroupId(groupId);
        updateTelegramBotByKey(telegram);
        return "Congrats! You have successfully registered for the VPP Airdrop Event! For every friend you invite, you will get "
                + ADD_INTITE_AMOUNT
                + " more VPP tokens for FREE! All the tokens will be sent within 4 weeks.校验成功，恭喜你成功注册VPP空投活动，获得100 VPP奖励！之后每邀请一个好友，你将在收到"
                + ADD_INTITE_AMOUNT
                + "个VPP代币奖励。所有奖励将会在4周内空投到账。"
                + "Your share link （你的分享链接）：https://invite.valp.io?lang=cn&inv_code=" + convertCode;
    }

}
