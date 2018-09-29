package com.sharp.sso.client;

/**
 * 单点登录响应码
 *
 * @author qinyupeng
 * @since 2018-09-27 14:22:46
 */
public interface SsoResultCode {

    //token未授权
    String SSO_TOKEN_ERROR = "1001";

    //没有权限
    String SSO_EPRMISSION_ERROR="1002";
}
