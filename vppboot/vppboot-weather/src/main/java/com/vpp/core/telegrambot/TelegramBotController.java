package com.vpp.core.telegrambot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import com.vpp.common.filter.MD5Utils;
import com.vpp.vo.ResultVo;

@RestController
@RequestMapping("/telegrambot")
public class TelegramBotController {
    private static final Logger logger = LogManager.getLogger(TelegramBotController.class);

    @Autowired
    private ITelegramBotService telegramBotService;

    @RequestMapping(value = "/register")
    @ResponseBody
    public synchronized ResultVo register(TelegramBot telegramBot, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> result = new HashMap<String, Object>();
        String convertCode = MD5Utils.getMD5String(telegramBot.getUserWalletUrl());
        TelegramBot tele = telegramBotService.getTelegramBotByKey(convertCode);
        int isShow = 1;
        if (tele == null) {
            telegramBot.setConvertCode(convertCode);
            telegramBot.setGmtCreate(new Date());
            telegramBotService.register(telegramBot);
            isShow = 0;
        }
        TelegramBot telegram = telegramBotService.getTelegramBotByKey(convertCode);
        Integer inviteUserCnt = telegramBotService.getInviteUserCnt(convertCode);
        result.put("isShow", isShow);
        result.put("convertCode", telegram.getConvertCode());
        Integer totalAmout = (telegram.getTotalAmount() == null ? 0 : telegram.getTotalAmount())
                + (telegram.getConvertAmount() == null ? 0 : telegram.getConvertAmount());
        result.put("totalAmount", totalAmout > 10000 ? 10000 : totalAmout);
        result.put("inviteUserCnt", inviteUserCnt);
        return ResultVo.setResultSuccess(result);
    }

    public static final String BOT_TOKEN = "485846342:AAGgSSXaUmgsULcdjJfU1MvRU0wMHAxdb78";

    /**
     * 通过 https://api.telegram.org/bot[bottoken]/setWebhook 接口设置信息同步接口
     * [{"key":"Content-Type","value":"application/json","description":""}]
     * 
     * @author Lxl
     * @param map
     * @param response
     */
    @RequestMapping(value = "/webhook")
    @ResponseBody
    public void webhook(@RequestBody(required = true) Map<String, Object> map, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Object message = map.get("message");
            if (null == message) {
                return;
            }
            JSONObject msgJson = JSONObject.fromObject(message);
            Gson gson = new Gson();
            Message msg = gson.fromJson(msgJson.toString(), Message.class);
            if (null == msg) {
                return;
            }
            Chat chat = msg.chat();// 分组信息
            if (null == chat) {
                return;
            }
            Long chatId = chat.id();// 分组ID

            // logger.info("chatId ::: {}", chatId);

            // 过滤群组
            boolean boo = this.filterGroup(chatId);
            if (!boo) {
                return;
            }

            // User{id=388652523, is_bot=false, first_name='lei', last_name='xianglin', username='leixianglin',
            // language_code='zh-CN'}
            User user = msg.from();// 用户信息
            Integer userId = user.id();
            Integer messageId = msg.messageId();// 消息ID，用于回复消息

            String text = null != msg.text() ? msg.text() : "";// 接收消息内容
            // logger.info("userId ::: {}", userId);
            this.deleteMessage(chatId, messageId, userId, text);

            text = text.replace("/", "");// 接收消息内容

            if (StringUtils.isNoneBlank(text) && text.length() > 30 && text.length() < 60) {
                // 回复消息，停止添加代币 2018-02-05
                // String res =
                // "Thank you !This round of airdrop has ended. 5,000,000 VPP token candy has been shared by more than 100,000 fans in 48 hours!Join our 【OFFICIAL NEWS GROUP】 to get the latest progress of ValPromise. https://t.me/ValPromise 谢谢!本轮空投已经结束。请关注【官方新闻群】获得最新下一轮空投计划和项目进展。https://t.me/ValPromise";
                String res = "Thank you !This round of airdrop has ended. 5,000,000 VPP token candy has been shared by more than 100,000 fans in 48 hours!  谢谢!本轮空投已经结束。";
                this.sendMessage(chatId, messageId, res);
            }

            // logger.info("text ::: {}", text);

            // 添加代币 ------------start
            // String res = telegramBotService.cheackConvertCode(userId + "", chatId + "", text);
            // if (StringUtils.isNoneBlank(res)) {
            // logger.info("接收消息---chatId:{},userId:{},messageId:{},text:{}", chatId, userId, messageId, text);
            // logger.info("--------------------回复 start");
            //
            // // 发送消息
            // // String stext = MessageFormat.format(REPLY, text);
            // this.sendMessage(chatId, messageId, res);
            // logger.info("--------------------回复 end");
            // }
            // 添加代币 ------------end
        } catch (Exception e) {
            logger.error("webhook error ::: {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        String text = "asdfas@";

        System.out.println(text.indexOf("@"));
        System.out.println(text.lastIndexOf("@"));

        System.out.println(text.split("@").length);

        if (text.contains("http://") || text.contains("https://") || text.contains("//") || text.contains("www.")
                || text.indexOf("/") > 0) {
            System.out.println("titititi");
        }

    }

    /**
     * -1001273270890 -274212239 过滤VPP2018 , ValPromise Core群组
     * 
     * @author Lxl
     * @param chatId
     * @return
     */
    private boolean filterGroup(Long chatId) {
        boolean boo = false;
        List<Long> chatIds = new ArrayList<Long>();
        chatIds.add(-1001273270890L);// ValPromise Core
        chatIds.add(-274212239L);
        chatIds.add(-1001260764443L);// ValPromise Official
        for (Long id : chatIds) {
            if (id.longValue() == chatId.longValue()) {
                boo = true;
            }
        }
        return boo;
    }

    private void sendMessage(Long chatId, Integer messageId, String text) {

        SendMessage request = new SendMessage(chatId, text).parseMode(ParseMode.HTML).disableWebPagePreview(true)
                .disableNotification(true).replyToMessageId(messageId).replyMarkup(new ForceReply());

        // sync
        com.pengrad.telegrambot.TelegramBot bot = new com.pengrad.telegrambot.TelegramBot(BOT_TOKEN);
        SendResponse sendResponse = bot.execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
        logger.info("send response isOk::{}", ok);
        // logger.info("response message ::{}", message.);
        // logger.info("send response message::{}",message);

    }

    private void deleteMessage(Long chatId, Integer messageId, Integer userId, String text) {
        // 白名单
        // 388652523 lxl 546144588 laopa 534875345 guojun 496559698 haoyuan
        List<Integer> whiteList = new ArrayList<Integer>();
        whiteList.add(388652523);
        whiteList.add(546144588);
        whiteList.add(534875345);
        whiteList.add(496559698);
        boolean boo = false;
        // 广告白名单
        for (Integer id : whiteList) {
            if (id.intValue() == userId.intValue()) {
                boo = true;
            }
        }
        if (boo) {
            return;
        }

        int fNum = text.indexOf("@");
        int lNum = text.lastIndexOf("@");

        if (text.contains("http") || text.contains("//") || text.contains("www.") || text.indexOf("/") > 0
                || text.contains("t.me") || (fNum != lNum)) {
            DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);

            com.pengrad.telegrambot.TelegramBot bot = new com.pengrad.telegrambot.TelegramBot(BOT_TOKEN);
            BaseResponse response = bot.execute(deleteMessage);

            boolean ok = response.isOk();
            // , response.description()
            logger.info("delete message isOk::{},{}", ok);
        }
    }
}
