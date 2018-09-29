package com.sharp.sso.rpc;

import com.sharp.sso.client.RpcUser;

public interface AuthenticationRpcService {

    RpcUser findAuthInfo(String token);

    boolean validate(String token);
}
