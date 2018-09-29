package com.sharp.sso.server.common.token;

import com.sharp.sso.server.common.LoginUser;
import com.sharp.sso.server.common.RedisCache;
import com.sharp.sso.server.common.token.base.TokenManager;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 分布式环境令牌管理
 */
public class RedisTokenManager extends TokenManager {

    //是否需要拓展token过期时间
    private Set<String> tokenSet = new CopyOnWriteArraySet<>();

    @Resource
    private RedisCache<LoginUser> redisCache;

    @Override
    public void verifyExpired() {
        tokenSet.clear();
    }

    @Override
    public void addToken(String token, LoginUser loginUser) {
        redisCache.set(token, loginUser, tokenTimeout);
    }

    @Override
    public void remove(String token) {
        redisCache.delete(token);
    }

    @Override
    public LoginUser validate(String token) {
        LoginUser loginUser = redisCache.get(token);
        if (loginUser != null && !tokenSet.contains(token)) {
            tokenSet.add(token);
            addToken(token, loginUser);
        }

        return loginUser;
    }
}
