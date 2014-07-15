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
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/app/main.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery.easyui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/common.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/countMgr.js"></script>
    <script type="text/javascript">
       var header="<%=request.getContextPath()%>";
       var currentUserId="${currentUser.id}";
    </script>
</head>
<body>
<div id="load">加载中......</div>
<%@ include file="header.jsp"%>
<div id="center">
        <div id="tab">  
        	<!-- 修改基本信息tab项 -->
	           <div title="修改资料" style="padding:20px">  
			        <ul style="width:660px;margin-bottom: 0px;margin-left:0px;padding-left:0px">
		            <form id="countForm" method="post" action="<%=request.getContextPath()%>/web/person-setting/${currentUser.id}/edit">
		               
		                <li style="padding:0px"><input id="oldTelephone"  type="hidden"value="${currentUser.mobileNo}"><input id="oldEmail"  type="hidden"value="${currentUser.email}"></li>
		                <li><label class="labelTitle">姓名:</label><input id="editName"  type="text" name="fullname" class="inputCountBoxReadonly" readonly value="${currentUser.fullname}"></li>
		                <li><label class="labelTitle">手机:</label><input id="editTelephone" type="text" name="mobileNo" class="inputCountBox" value="${currentUser.mobileNo}"></li>
		                <li><label class="labelTitle">邮箱:</label><input id="editEmail" type="text" name="email" class="inputCountBox" value="${currentUser.email}"></li>
		                <li><label class="labelTitle">权限:</label><input type="text" id="role" name="roleShow" class="inputCountBoxReadonly" readonly value="超级管理员">
		                	<input type="hidden" name="role" value="${role}">
		                </li>
		                <li><label class="labelTitle">负责区域:</label><input type="text" class="inputCountBoxReadonly" readonly name="areaCode" value="${currentUser.areaCode}"></li>
		                <li><label class="labelTitle">账号:</label><input  type="text" name="username" class="inputCountBoxReadonly" readonly value="${currentUser.username}"></li>
		                <li><label class="labelTitle">座机:</label><input id="editPhone" name="telNo" type="text" class="inputCountBox" value="${currentUser.telNo}"></li>
		                <li class="btnLi">
		                    <input type="submit" value="保存" class="inputBtn">
		                </li>
		            </form>
		        </ul> 
	   		 </div>  
	   		 <!-- 修改密码tab项 -->
	   		 <div title="修改密码" style="padding:20px">  
	      	     <form id="changePassword"  method="post" action="<%=request.getContextPath()%>/web/person-setting/${currentUser.id}/update-my-password">
	      	         <ul>
	      	        	 <li><label class="labelTitle">原密码:</label><input id="editPassword" type="password" name="originalPassword" class="inputCountBox"></li>
	      	        	 <li><label class="labelTitle">新密码:</label><input id="editNewPassword" type="password" name="newPassword" class="inputCountBox"></li> 
		                <li><label class="labelTitle">确认密码:</label><input id="editConfirmPassword" type="password" name="confirmPassword" class="inputCountBox"></li>
		                 <li class="btnLi">
		                    <input type="submit" value="保存" class="inputBtn"> 
	      	         </ul>
	      	     </form>
	    	</div>  
        </div>
        
        <div class="tip">
            温馨提示:<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;此页面可修改与本账户相关的某些信息，无法修改的信息请联系超级管理员或维护人员，谢谢！
        </div>
    </div>
<%@ include file="footer.jsp"%>
</body>
</html>