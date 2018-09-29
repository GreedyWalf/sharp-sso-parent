package com.sharp.sso.server.controller;

import com.sharp.sso.client.filter.SsoFilter;
import com.sharp.sso.server.common.LoginUser;
import com.sharp.sso.server.common.token.base.TokenManager;
import com.sharp.sso.server.model.User;
import com.sharp.sso.server.service.UserService;
import com.sharp.sso.server.utils.CookieUtils;
import com.sharp.sso.server.utils.Result;
import com.sharp.sso.server.utils.ResultCode;
import com.sharp.sso.server.utils.UuidGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private static final String LOGIN_PATH = "/login";

    @Resource
    private TokenManager tokenManager;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }


    /**
     * 登录页
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, String backUrl) {
        String token = CookieUtils.getCookie(request, TokenManager.TOKEN);
        if (StringUtils.isNotBlank(token) && tokenManager.validate(token) != null) {
            return "redirect:" + authBackUrl(backUrl, token);
        } else {
            request.setAttribute("backUrl", backUrl);
            return "/login";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(User user, HttpServletRequest request, HttpServletResponse response) {
//        Result result = userService.login(user);
        String backUrl = user.getBackUrl();
        Result result = new Result();
        if ("admin".equals(user.getAccount()) && "123456".equals(user.getPassword())) {
            result.setMessage("登录成功！");
            result.setCode(ResultCode.SUCCESS);

            LoginUser loginUser = new LoginUser(user.getId(), user.getAccount());
            String token = createToken(loginUser);
            addTokenInCookie(token, request, response);
            return "redirect:" + authBackUrl(backUrl, token);
        } else {
            request.setAttribute("backUrl", backUrl);
            return "/login";
        }
    }


    private String createToken(LoginUser loginUser) {
        String token = UuidGenerator.generate();
        tokenManager.addToken(token, loginUser);
        return token;
    }

    private String authBackUrl(String backUrl, String token) {
        StringBuilder sbf = new StringBuilder(backUrl);
        if (backUrl.indexOf("?") > 0) {
            sbf.append("&");
        } else {
            sbf.append("?");
        }

        sbf.append(SsoFilter.SSO_TOKEN_NAME).append("=").append(token);
        return sbf.toString();
    }

    private void addTokenInCookie(String token, HttpServletRequest request, HttpServletResponse response) {
        // Cookie添加token
        Cookie cookie = new Cookie(TokenManager.TOKEN, token);
        cookie.setPath("/");
        if ("https".equals(request.getScheme())) {
            cookie.setSecure(true);
        }
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
