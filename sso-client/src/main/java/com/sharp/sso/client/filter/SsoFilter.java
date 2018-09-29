package com.sharp.sso.client.filter;

import com.sharp.sso.client.RpcUser;
import com.sharp.sso.client.SessionUser;
import com.sharp.sso.client.SsoResultCode;
import com.sharp.sso.client.filter.base.ClientFilter;
import com.sharp.sso.client.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class SsoFilter extends ClientFilter {

    public static final String SSO_TOKEN_NAME = "__vt_param__";

    @Override
    public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //session中获取token
        String token = getLocalToken(request);
        if (token == null) {
            //请求参数中获取token
            token = request.getParameter(SSO_TOKEN_NAME);
            if (token != null) {
                //校验token，并存储在session中
                invokeAuthInfoInSession(request, token);
                //再次请求当前的url，将url中的token参数去除
                response.sendRedirect(getRemoveTokenBackUrl(request));
                //发现了一个问题(Cannot forward after response has been committed)，这里需要返回false，重新请求一次后就可以去除queryString中的token了
                return false;
            }

            //校验不通过，则跳转到认证中心登录页
            redirectLogin(request, response);
            return false;
        }

        if (authenticationRpcService.validate(token)) {
            return true;
        }

        return false;
    }


    /**
     * 获取session中token
     */
    private String getLocalToken(HttpServletRequest request) {
        SessionUser sessionUser = SessionUtils.getSessionUser(request);
        return sessionUser == null ? null : sessionUser.getToken();
    }

    /**
     * 存储sessionUser
     */
    private void invokeAuthInfoInSession(HttpServletRequest request, String token) {
        RpcUser rpcUser = authenticationRpcService.findAuthInfo(token);
        if (rpcUser != null) {
            SessionUtils.setSessionUser(request, new SessionUser(token, rpcUser.getAccount()));
        }
    }

    /**
     * 跳转登录
     */
    private void redirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //判断是否为ajax请求
        if (isAjaxRequest(request)) {
            responseJson(response, SsoResultCode.SSO_EPRMISSION_ERROR, "未登录或登录超时");
        } else {
            //将session失效
            SessionUtils.invalidate(request);
            //将访问链接作为backUrl参数，在认证中心成功登录后，回调该链接
            String ssoLoginUrl = ssoServerUrl + "/login?backUrl=" + URLEncoder.encode(getBackUrl(request), "UTF-8");
            response.sendRedirect(ssoLoginUrl);
        }
    }


    /**
     * 去除返回地址中的token参数
     */
    private String getRemoveTokenBackUrl(HttpServletRequest request) {
        String backUrl = getBackUrl(request);
        return backUrl.substring(0, backUrl.indexOf(SSO_TOKEN_NAME));
    }

    /**
     * 获取当前访问地址，作为backUrl
     */
    private String getBackUrl(HttpServletRequest request) {
        return request.getRequestURL().append((request.getQueryString() == null ? "" : request.getQueryString())).toString();
    }
}
