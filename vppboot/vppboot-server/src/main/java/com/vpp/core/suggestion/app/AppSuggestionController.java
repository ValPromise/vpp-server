package com.vpp.core.suggestion.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vdurmont.emoji.EmojiParser;
import com.vpp.common.vo.ResultVo;
import com.vpp.core.common.CommonController;
import com.vpp.core.suggestion.bean.Suggestion;
import com.vpp.core.suggestion.service.ISuggestionService;

@RestController
@RequestMapping("/app/suggestion")
public class AppSuggestionController extends CommonController {

    @Autowired
    private ISuggestionService suggestionService;

    @RequestMapping("/insert")
    public ResultVo insertSuggestion(HttpServletResponse response, Suggestion suggestion, String token) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (!checkLogin(token)) {
            return ResultVo.setResultError(getMessage("token"), TOKEN_FAIL_ERROR_CODE);
        }
        String check = "^[a-zA-Z0-9][a-zA-Z0-9-_.]*@[a-zA-Z0-9]+\\.[a-zA-Z]+$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(suggestion.getTitle());
        boolean flag = matcher.matches();
        if (!flag) {
            return ResultVo.setResultError(getMessage("email_error")); // 邮箱号不正确
        }
        String desc = EmojiParser.removeAllEmojis(suggestion.getDescription());
        suggestion.setDescription(desc);
        int ret = suggestionService.insertService(suggestion);
        return ret > 0 ? ResultVo.setResultSuccess(getMessage("suggestion_success"))
                : ResultVo.setResultError(getMessage("suggestion_fail"));
    }

}
