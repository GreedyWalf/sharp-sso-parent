package com.sharp.sso.server.common.token;

import com.sharp.sso.server.common.LoginUser;
import com.sharp.sso.server.common.token.base.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用ConcurrentHashMap作为令牌存储的容器，在内存中维护令牌的失效时间，适合单个实例系统部署使用；
 *
 * @author qinyupeng
 * @since 2018-09-27 15:06:32
 */
public class LocalTokenManager extends TokenManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //令牌存储结构
    private final ConcurrentHashMap<String, DummyUser> tokenMap = new ConcurrentHashMap<>();

    @Override
    public void verifyExpired() {
        Date now = new Date();
        for (Map.Entry<String, DummyUser> entry : tokenMap.entrySet()) {
            String token = entry.getKey();
            DummyUser dummyUser = entry.getValue();
            //当前时间大于过期时间
            if (now.compareTo(dummyUser.expired) > 0) {
                //已过期，清除对应token
                tokenMap.remove(token);
                logger.info("---token: " + token + "已失效！");
            }
        }

    }

    @Override
    public void addToken(String token, LoginUser loginUser) {
        DummyUser dummyUser = new DummyUser();
        dummyUser.loginUser = loginUser;
        extendExpired(dummyUser);
        tokenMap.putIfAbsent(token, dummyUser);
    }

    /**
     * 拓展过期时间（每次延长tokenTimeout时间）
     */
    private void extendExpired(DummyUser dummyUser) {
        dummyUser.expired = new Date(System.currentTimeMillis() + tokenTimeout * 1000);
    }

    @Override
    public void remove(String token) {
        tokenMap.remove(token);
    }

    @Override
    public LoginUser validate(String token) {
        DummyUser dummyUser = tokenMap.get(token);
        if (dummyUser == null) {
            return null;
        }

        extendExpired(dummyUser);
        return dummyUser.loginUser;
    }

    /**
     * 复合结构体，包含loginUser和过期时间两个成员
     */
    private class DummyUser {
        //当前登录人员
        private LoginUser loginUser;

        //过期时间
        private volatile Date expired;
    }
}
