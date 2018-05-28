package com.vpp.common.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class BaseController {

    @RequestMapping(value = "/toerror")
    public ModelAndView toerror(HttpServletRequest request) {
        for (String key : request.getParameterMap().keySet()) {
            System.out.println(ToStringBuilder.reflectionToString(request.getParameter(key)));
        }
        // model.addAttribute("message", "空页面");
        return new ModelAndView("error");
    }

}
