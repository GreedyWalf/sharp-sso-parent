package com.sharp.sso.client;

import java.io.Serializable;

/**
 * RPC回传用户对象
 *
 * @author qinyupeng
 * @since 2018-09-27 14:11:55
 */
public class RpcUser implements Serializable {

    private String account;

    public RpcUser(String account) {
        this.account = account;
    }

    public RpcUser() {

    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }
}
