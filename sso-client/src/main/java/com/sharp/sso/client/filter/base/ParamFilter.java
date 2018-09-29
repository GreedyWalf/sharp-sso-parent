package com.sharp.sso.client.filter.base;

import com.sharp.sso.rpc.AuthenticationRpcService;

/**
 * 参数注入Filter
 *
 * 作为登录、登出、权限校验过滤器的公用类
 */
public class ParamFilter {

    //单点登录服务端URL
    protected String ssoServerUrl;

    //单点登录服务端提供RPC服务，由spring注入
    protected AuthenticationRpcService authenticationRpcService;


    public void setSsoServerUrl(String ssoServerUrl) {
        this.ssoServerUrl = ssoServerUrl;
    }

    public void setAuthenticationRpcService(AuthenticationRpcService authenticationRpcService) {
        this.authenticationRpcService = authenticationRpcService;
    }

    public AuthenticationRpcService getAuthenticationRpcService() {
        return authenticationRpcService;
    }
}
