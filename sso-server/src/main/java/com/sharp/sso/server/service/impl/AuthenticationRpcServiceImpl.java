package com.sharp.sso.server.service.impl;

import com.sharp.sso.client.RpcUser;
import com.sharp.sso.rpc.AuthenticationRpcService;
import com.sharp.sso.server.common.LoginUser;
import com.sharp.sso.server.common.token.base.TokenManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "authenticationRpcService")
public class AuthenticationRpcServiceImpl implements AuthenticationRpcService {
    @Resource
    private TokenManager tokenManager;

    @Override
    public RpcUser findAuthInfo(String token) {
        LoginUser loginUser = tokenManager.validate(token);
        if (loginUser != null) {
            return new RpcUser(loginUser.getAccount());
        }

        return null;
    }

    @Override
    public boolean validate(String token) {
        return tokenManager.validate(token) != null;
    }
}
