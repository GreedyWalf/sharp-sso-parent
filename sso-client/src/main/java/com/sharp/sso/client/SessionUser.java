package com.sharp.sso.client;

import java.io.Serializable;

/**
 * 已登录用户信息
 *
 * @author QinYupeng
 * @since 2018-09-27 10:27:15
 */
public class SessionUser implements Serializable {

    private String token;

    private String account;

    public SessionUser() {

    }

    public SessionUser(String token, String account) {
        this.token = token;
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
