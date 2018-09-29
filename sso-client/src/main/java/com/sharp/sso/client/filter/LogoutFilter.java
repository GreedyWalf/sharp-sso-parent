package com.sharp.sso.client.filter;

import com.sharp.sso.client.filter.base.ClientFilter;
import com.sharp.sso.client.utils.SessionUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 单点退出Filter
 *
 * @author QinYupeng
 * @since 2018-09-28 14:19:48
 */
public class LogoutFilter extends ClientFilter {

    //单点退出成功后跳转页
    private String ssoBackUrl;

    public void setSsoBackUrl(String ssoBackUrl) {
        this.ssoBackUrl = ssoBackUrl;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Assert.notNull(pattern, "pattern不能为空！");
        Assert.notNull(ssoBackUrl, "ssoBackUrl不能为空！");
    }

    @Override
    public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //将session中登录信息清除
        SessionUtils.invalidate(request);
        String logoutUrl = ssoServerUrl + "/logout?backUrl=" + getLocalUrl(request) + ssoBackUrl;
        response.sendRedirect(logoutUrl);
        return false;
    }

    //获取当前链接协议、服务名、端口、文根信息
    private String getLocalUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        String port = request.getServerPort() == 80 ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + "://" + serverName + port + contextPath;
    }
}
