package com.vpp.common.filter;

import java.text.MessageFormat;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

public class SendSmsUtil {
    private static final Logger logger = LogManager.getLogger(SendSmsUtil.class);

    public static final String ACCESS_KEY_ID = "LTAI5pVs1RPgStE6";

    public static final String ACCESS_KEY_SECRET = "zq2GPWnNBL1nOJZk5AdJTLjKBsAZOm";

    public static final String SIGN_NAME = "天气宝";

    public static final String CAPTCHA_TEL_CODE = "SMS_99925010";

    /** 请求地址 **/
    public static final String CHUANG_LAN_URL_CH_URL_INTL = "http://intapi.253.com/send/json";

    public static final String CHUANG_LAN_URL_CH = "http://smssh1.253.com/msg/send/json";

    /** API账号，50位以内。必填 国际产品 **/
    public static final String ACCOUNT_INTL = "I6317300";

    /** API账号对应密钥，联系客服获取。必填 国际产品 **/
    public static final String PASSWORD_INTL = "rE7JXOhykN3640";

    /** API账号，50位以内。必填 **/
    public static final String ACCOUNT_CH = "N7561667";

    /** API账号对应密钥，联系客服获取。必填 **/
    public static final String PASSWORD_CH = "zys21wdFNe27f8";

    /** 正则表达式：验证手机号 **/
    public static final String REGEX_MOBILE = "^\\+?[\\d- ]+$";

    /**
     * 生成验证码
     * 
     * @return
     */
    public static String getCaptcha() {
        String str = "0,1,2,3,4,5,6,7,8,9";
        String str2[] = str.split(",");// 将字符串以,分割
        Random rand = new Random();// 创建Random类的对象rand
        int index = 0;
        String randStr = "";// 创建内容为空字符串对象randStr
        randStr = "";// 清空字符串对象randStr中的值
        for (int i = 0; i < 4; ++i) {
            index = rand.nextInt(str2.length - 1);// 在0到str2.length-1生成一个伪随机数赋值给index
            randStr += str2[index];// 将对应索引的数组与randStr的变量值相连接
        }
        return randStr;
    }

    /**
     * 阿里短信的通用配置
     * 
     * @throws ClientException
     */
    /*
     * public static IAcsClient aliSmsConfig() { //设置超时时间-可自行调整 System.setProperty("sun.net.client.defaultConnectTimeout",
     * "10000"); System.setProperty("sun.net.client.defaultReadTimeout", "10000"); //初始化ascClient需要的几个参数 final String product =
     * "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改） final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改） //替换成你的AK
     * final String accessKeyId = ACCESS_KEY_ID;//你的accessKeyId,参考本文档步骤2 final String accessKeySecret =
     * ACCESS_KEY_SECRET;//你的accessKeySecret，参考本文档步骤2 //初始化ascClient,暂时不支持多region（请勿修改） IClientProfile profile =
     * DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret); try { DefaultProfile.addEndpoint("cn-hangzhou",
     * "cn-hangzhou", product, domain); } catch (ClientException e) { e.printStackTrace(); } IAcsClient acsClient = new
     * DefaultAcsClient(profile); return acsClient; }
     */

    /**
     * @param templateCode 短信模板编号
     * @param telephone 手机号，可多个，以','隔开，最多1000
     * @param templateParam 变量内容
     * @return
     * @throws ServerException
     * @throws ClientException
     */
    /*
     * public static String sendSms(String templateCode, String telephone, String templateParam){ IAcsClient acsClient =
     * aliSmsConfig(); //组装请求对象 SendSmsRequest request = new SendSmsRequest(); //使用post提交 request.setMethod(MethodType.POST);
     * //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式 request.setPhoneNumbers(telephone);
     * //必填:短信签名-可在短信控制台中找到 request.setSignName(SIGN_NAME); //必填:短信模板-可在短信控制台中找到 request.setTemplateCode(templateCode);
     * //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
     * //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
     * if(!StringUtil.isEmpty(templateParam)){ request.setTemplateParam(templateParam); }
     * //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段) //request.setSmsUpExtendCode("90997");
     * //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者 // request.setOutId("yourOutId"); //请求失败这里会抛ClientException异常 SendSmsResponse
     * sendSmsResponse = null; try { sendSmsResponse = acsClient.getAcsResponse(request); } catch (ServerException e) {
     * e.printStackTrace(); return "fail"; } catch (ClientException e) { e.printStackTrace(); return "fail"; }
     * if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) { //请求成功 logger.info("短息发送成功！手机号：" +
     * telephone); return "success"; } else { logger.error("短信发送失败！手机号：" + telephone + "|返回错误码：" + sendSmsResponse.getCode());
     * return "fail"; } }
     */

    /******** 235 云通讯 ********/

    /**
     * @param mobile 手机号
     * @param mobileCode 随机码
     * @return
     */
    /*
     * public static String sendSmsIntl(String mobile,String mobileCode){ String msg ="【vpp】您的验证码是："+mobileCode; String
     * resultValue = ""; try { //组装请求参数 JSONObject map=new JSONObject(); map.put("account", ACCOUNT_INTL); map.put("password",
     * PASSWORD_INTL); map.put("msg", msg); map.put("mobile", mobile); String params =map.toString(); logger.info("请求参数为:" +
     * params); String result=HttpUtil.post(CHUANG_LAN_URL_CH_URL_INTL,params); logger.info("返回参数为:" + result); JSONObject
     * jsonObject = JSON.parseObject(result); String code = jsonObject.get("code").toString(); String msgid =
     * jsonObject.get("msgid").toString(); String error = jsonObject.get("error").toString(); logger.info("状态码:" + code +
     * ",状态码说明:" + error + ",消息id:" + msgid); if(code.equals("0")){ resultValue = "success"; }else{ resultValue = "fail"; } }
     * catch (JSONException e) { resultValue = "fail"; logger.error("发生异常"+e); } return resultValue; }
     */

    /***
     * 短信验证码
     * 
     * @param mobile
     * @param mobileCode
     * @return
     */
    public static String sendSmsChuangLan(String mobile, String mobileCode) {
        // 短信内容
        String msg = "【ValPromise】" + mobileCode + "(登录/注册验证码)，五分钟有效。请勿转发或者透露给他人。如非您本人操作，请联系我们。";
        // 状态报告
        String report = "true";
        String result = "";
        try {
            SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT_CH, PASSWORD_CH, msg, mobile, report);
            String requestJson = JSON.toJSONString(smsSingleRequest);
            String response = HttpUtil.sendSmsByPost(CHUANG_LAN_URL_CH, requestJson);
            SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
            logger.info(new Gson().toJson(smsSingleResponse));
            if (smsSingleResponse.getCode().equals("0")) {
                result = "success";
            } else {
                result = "fail";
            }
            logger.info(smsSingleRequest);
        } catch (Exception e) {
            result = "fail";
            logger.error("发生异常" + e);
        }
        return result;
    }

    /**
     * 高温合约购买短信
     * 
     * @author Lxl
     * @param mobile
     * @param orderId
     * @param productName
     * @param contractDate
     * @param triggerDate
     * @return
     */
    public static String sendSmsBuySuccess(String mobile, String orderId, String productName, String contractDate,
            String triggerDate) {
        // 短信内容【ValPromise】 订单号为 1000001的 高温合约 已购买成功。合约时间 2018-05-18，触发结果判定时间2018-05-20。
        // String msg = "【ValPromise】 订单号为{0}的{1}已购买成功。合约时间{2}，触发结果判定时间{3}";
        String msg = "【ValPromise】 亲爱的小伙伴：您订单号为{0}的{1}已购买成功。合约时间{2}，触发结果判定时间{3}。更多详情，请关注官方公众号：ValPromise";
        String smsContent = MessageFormat.format(msg, orderId, productName, contractDate, triggerDate);
        // 状态报告
        String result = sendSms(mobile, smsContent);
        return result;
    }

    /**
     * 高温合约触发
     * 
     * @author Lxl
     * @param mobile
     * @param orderId
     * @param productName
     * @param contractDate
     * @param triggerDate
     * @return
     */
    public static String sendSmsTrigger(String mobile, String orderId, String productName, Integer amount) {
        // 短信内容【ValPromise】 订单号为 1000001的 高温合约已触发。收到100个履约vpp代币，请登录APP查看。。
        // String msg = "【ValPromise】 订单号为{0}的{1}已触发。收到{2}个履约vpp代币，请登录APP查看。";
        String msg = "【ValPromise】 恭喜小伙伴！您订单号为{0}的{1}已触发。{2}个履约vpp代币已打款，快去APP查收我们的心意吧。更多详情，请关注官方公众号：ValPromise";
        String smsContent = MessageFormat.format(msg, orderId, productName, amount);
        // 状态报告
        String result = sendSms(mobile, smsContent);
        return result;
    }

    public static String sendSmsNoTrigger(String mobile, String orderId, String productName) {
        // 短信内容【ValPromise】 订单号为 1000001的 高温合约已触发。收到100个履约vpp代币，请登录APP查看。。
        // String msg = "【ValPromise】 订单号为{0}的{1}已触发。收到{2}个履约vpp代币，请登录APP查看。";
        String msg = "【ValPromise】 再试一次！您订单号为{0}的{1}本次未触发。别灰心，再试一次。更多详情，请关注官方公众号：ValPromise";
        String smsContent = MessageFormat.format(msg, orderId, productName);
        // 状态报告
        String result = sendSms(mobile, smsContent);
        return result;
    }

    private static String sendSms(String mobile, String smsContent) {
        // 状态报告
        String report = "true";
        String result = "";
        try {
            SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT_CH, PASSWORD_CH, smsContent, mobile, report);
            String requestJson = JSON.toJSONString(smsSingleRequest);
            String response = HttpUtil.sendSmsByPost(CHUANG_LAN_URL_CH, requestJson);
            SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
            logger.info(new Gson().toJson(smsSingleResponse));
            if (smsSingleResponse.getCode().equals("0")) {
                result = "success";
            } else {
                result = "fail";
            }
            logger.info(smsSingleRequest);
        } catch (Exception e) {
            result = "fail";
            logger.error("发生异常" + e);
        }
        return result;
    }

    /**
     * 校验手机号
     * 
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    public static void main(String[] args) {
        String code = getCaptcha();
        // System.out.println(sendSmsChuangLan("18244916590",code));

        String mobile = "17322370015";
        String orderId = "888811232";
        String productName = "高温合约";
        String contractDate = "2018-05-18";
        String triggerDate = "2018-05-20";
         System.out.println(sendSmsBuySuccess(mobile,orderId,productName,contractDate,triggerDate));

        Integer amount = 100;
        String result = sendSmsTrigger(mobile, orderId, productName, amount);
        System.out.println(result);
        
        result = sendSmsNoTrigger(mobile, orderId, productName);
        System.out.println(result);
        
        // System.out.println("验证码为：" + code);
        // String templateCode =CAPTCHA_TEL_CODE;
        // String recNum = "18244916590";
        // String templateParam = "{\"code\":\"" + code +"\"}";
        // System.out.println("templateParam：" + templateParam);
        // sendSms(templateCode, recNum, templateParam);

    }
}