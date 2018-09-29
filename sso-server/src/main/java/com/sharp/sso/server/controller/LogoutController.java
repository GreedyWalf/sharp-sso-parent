package com.sharp.sso.server.controller;

import com.sharp.sso.client.utils.SessionUtils;
import com.sharp.sso.server.common.token.base.TokenManager;
import com.sharp.sso.server.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 认证中心登录退出
 *
 * @author QinYupeng
 * @since 2018-09-28 19:10:29
 */
@Controller
public class LogoutController {

    @Resource
    private TokenManager tokenManager;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, String backUrl) {
        String token = CookieUtils.getCookie(request, TokenManager.TOKEN);
        if (StringUtils.isNotBlank(token)) {
            //移除令牌
            tokenManager.remove(token);
        }

        //清除认证中心登录session
        SessionUtils.invalidate(request);
        return "redirect:" + (StringUtils.isBlank(backUrl) ? "/index" : backUrl);
    }

}
