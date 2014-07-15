<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <title>湖南航天信息外服人员管理系统</title>
     <link rel="shortcut icon" href="<%=request.getContextPath()%>/static/images/logo.ico">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/lib/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/app/login.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery.easyui.min.js" charset="utf-8"></script>
      <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery.cookie.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/login.js"></script>

</head>
<body>
 <div id="center">
     <form id="loginForm" action="<%=request.getContextPath()%>/web/login" method="post">
         <ul>
             <li><label class="lableTitle">账号</label></li>
             <li><input id="username" class="inputBox" name="username" type="text"></li>
             <li style="margin-top: 20px"><label>密码</label></li>
             <li><input id="password" class="inputBox" name="password" type="password"></li>
             <li style="margin-top: 5px;font-size: 14px"><input type="checkbox" id="rememberme" name="rememberMe" class="inputCheck">记住我   <span id="forgetPw">忘记密码?</span> </li>
             <li style="color:red">${error_login_generic}</li>
         </ul>
         <input id="submitBtn" type="submit" value="">
     </form>
     <%@ include file="footer.jsp"%>
 </div>
</body>
</html>