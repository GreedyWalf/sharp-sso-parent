package com.sharp.sso.server.model;


import com.sharp.sso.server.model.base.PersistentModel;

import java.util.Date;

public class User extends PersistentModel {

    //登录名
    private String account;

    //密码
    private String password;

    //最后登录ip
    private String lastLoginIp;

    //登录总次数
    private Integer loginCount = 0;

    //创建时间
    private Date createTime;

    //最后登录时间
    private Date lastLoginTime;

    //是否启用
    private Boolean isEnable = true;


    //非持久化字段
    private String backUrl;

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", loginCount=" + loginCount +
                ", createTime=" + createTime +
                ", lastLoginTime=" + lastLoginTime +
                ", isEnable=" + isEnable +
                '}';
    }
}
