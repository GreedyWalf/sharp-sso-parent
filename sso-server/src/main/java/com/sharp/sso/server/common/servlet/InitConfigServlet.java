package com.sharp.sso.server.common.servlet;

import com.sharp.sso.server.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * 项目启动后初始化该servlet，加载静态资源配置等项目配置
 *
 * @since 2018-09-27 10:12:32
 * @author QinYupeng
 */
public class InitConfigServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        String contextPath = servletContext.getContextPath();
        servletContext.setAttribute("_path", contextPath);
        try {
            String staticPath = ConfigUtils.getProperty("static.url");
            if (staticPath == null) {
                logger.warn("在属性资源中没有找到静态资源路径配置，key=static.url");
            } else if (staticPath.startsWith("http")) {
                //静态资源如果配置为远程服务器，则从远程服务器中获取
                servletContext.setAttribute("_staticPath", staticPath);
            } else {
                //否则根据contextPath和配置的staticPath加载静态资源
                servletContext.setAttribute("_staticPath", contextPath + staticPath);
            }

            servletContext.setAttribute("_systemName", ConfigUtils.getProperty("system.name"));
        } catch (Exception e) {
            logger.error("系统参数化配置有误", e);
        }
    }
}
