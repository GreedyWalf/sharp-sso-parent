package com.sharp.sso.server.common;

import java.io.Serializable;

/**
 * 登录成功的用户对象
 */
public class LoginUser implements Serializable {

    private Integer userId;

    private String account;

    public LoginUser(Integer userId, String account) {
        this.userId = userId;
        this.account = account;
    }

    public LoginUser() {

    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "userId=" + userId +
                ", account='" + account + '\'' +
                '}';
    }
}
