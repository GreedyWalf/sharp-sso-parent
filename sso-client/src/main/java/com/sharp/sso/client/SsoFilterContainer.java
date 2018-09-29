package com.sharp.sso.client;

import com.caucho.hessian.client.HessianProxyFactory;
import com.sharp.sso.client.filter.base.ClientFilter;
import com.sharp.sso.client.filter.base.ParamFilter;
import com.sharp.sso.rpc.AuthenticationRpcService;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;

public class SsoFilterContainer extends ParamFilter implements Filter {

    //是否为服务端，默认为false
    private Boolean isServer;

    private ClientFilter[] filters;

    private PathMatcher pathMatcher = new AntPathMatcher();

    public void setIsServer(Boolean isServer) {
        this.isServer = isServer;
    }

    public void setFilters(ClientFilter[] filters) {
        this.filters = filters;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (isServer) {
            ssoServerUrl = filterConfig.getServletContext().getContextPath();
        }

        if (ssoServerUrl == null) {
            throw new IllegalArgumentException("ssoServerUrl不能为空");
        }

        if (authenticationRpcService == null) {
            String rpcFullUrl = ssoServerUrl + "/rpc/authenticationRpcService";
            try {
                authenticationRpcService = (AuthenticationRpcService) new HessianProxyFactory().create(AuthenticationRpcService.class, rpcFullUrl);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("authenticationRpcService初始化失败", e);
            }
        }

        if (filters == null || filters.length == 0) {
            throw new IllegalArgumentException("filters不能为空");
        }

        for (ClientFilter filter : filters) {
            filter.setSsoServerUrl(ssoServerUrl);
            filter.setAuthenticationRpcService(authenticationRpcService);
            filter.init(filterConfig);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        for (ClientFilter filter : filters) {
            if (matchPath(filter.getPattern(), httpRequest.getServletPath())
                    && !filter.isAccessAllowed(httpRequest, httpResponse)) {
                return;
            }
        }

        chain.doFilter(request, response);
    }

    //没有配置pattern表示默认匹配全部
    private boolean matchPath(String pattern, String path) {
        return pattern == null || pathMatcher.match(pattern, path);
    }

    @Override
    public void destroy() {

    }
}
