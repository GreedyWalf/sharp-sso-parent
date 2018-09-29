package com.sharp.demo;

import com.sharp.sso.client.SessionUser;
import com.sharp.sso.client.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionUser sessionUser = SessionUtils.getSessionUser(request);
        //获取登录用户名
        request.setAttribute("userName", sessionUser.getAccount());
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
