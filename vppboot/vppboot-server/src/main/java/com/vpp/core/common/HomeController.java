package com.vpp.core.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vpp.common.vo.ResultVo;

@RestController
@RequestMapping("/home")
public class HomeController extends CommonController {

    @RequestMapping("/getBanner")
    public ResultVo getBanner(String token, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
//        if (!checkLogin(token)) {
//            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
//        }
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("banners", this.getBanner());
        returnMap.put("notices", this.getNotice());
        return ResultVo.setResultSuccess(returnMap);
    }

    public List<Map<String, Object>> getBanner() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 2; i++) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("linkUrl", "http://valp.io/?lang=zh-tw");
            result.put("imgUrl", "http://p3pzply3v.bkt.clouddn.com/banner.png");
            list.add(result);
        }
        return list;
    }

    public List<Map<String, Object>> getNotice() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 2; i++) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("content", "欢迎来到ValPromise的新世界。");
            list.add(result);
        }
        return list;
    }

    // @RequestMapping("/getNotice")
    // public ResultVo getNotice(String token,HttpServletResponse response){
    // response.addHeader("Access-Control-Allow-Origin", "*");
    // if(!checkLogin(token)){
    // return ResultVo.setResultError("没有登录哦");
    // }
    // Map<String, Object> result = new HashMap<String, Object>();
    // result.put("notice", "这是公告");
    // return ResultVo.setResultSuccess(result);
    // }
}
