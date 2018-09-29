<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>认证中心-统一登录</title>

    <script src="${_staticPath}/js/jquery.js"></script>
</head>
<body>
<form id="loginForm" name="loginForm" action="login" method="post">
    <input type="hidden" name="backUrl" value="${backUrl}">
    用户名：<input type="text" name="account" id="account"/><br/>
    密码：<input type="text" name="password" id="password"/><br/>
    <input type="submit" value="登录"/>
</form>
</body>
</html>
