package com.sharp.sso.client.utils;

import com.sharp.sso.client.SessionUser;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 当前已登录用户session工具类
 *
 * @author QinYupeng
 * @since 2018-09-27 10:39:14
 */
public class SessionUtils {

    //用户信息
    public static final String SESSION_USER = "_sessionUser";

    /**
     * 获取sessionUser
     */
    public static SessionUser getSessionUser(HttpServletRequest request) {
        return (SessionUser) WebUtils.getSessionAttribute(request, SESSION_USER);
    }

    /**
     * 添加sessionUser
     */
    public static void setSessionUser(HttpServletRequest request, SessionUser sessionUser) {
//        WebUtils.setSessionAttribute(request, SESSION_USER, sessionUser);
        if (sessionUser != null) {
            request.getSession().setAttribute(SESSION_USER, sessionUser);
        } else {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(SESSION_USER);
            }
        }
    }

    /**
     * 失效sessionUser
     */
    public static void invalidate(HttpServletRequest request) {
        setSessionUser(request, null);
    }
}
