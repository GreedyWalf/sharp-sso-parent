package com.sharp.sso.server.common.token.base;

import com.sharp.sso.server.common.LoginUser;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TokenManager {

    public static final String TOKEN = "token";

    //令牌有效期，单位为秒，默认30分钟
    protected int tokenTimeout = 1800;

    public void setTokenTimeout(int tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }

    //每分钟执行一次，校验token是否失效
    public TokenManager() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                verifyExpired();
            }
        }, 60 * 1000, 60 * 1000);
    }

    /**
     * 验证失效
     */
    public abstract void verifyExpired();


    /**
     * 用户登录授权成功后，存入授权信息
     */
    public abstract void addToken(String token, LoginUser loginUser);

    /**
     * 移除令牌
     */
    public abstract void remove(String token);

    /**
     * 验证令牌有效性，有效则延长session生命周期
     */
    public abstract LoginUser validate(String token);
}