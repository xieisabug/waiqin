<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>湖南航天信息外服人员管理系统</title>
<link rel="shortcut icon"
	href="<%=request.getContextPath()%>/static/images/logo.ico">

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/static/css/lib/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/static/css/lib/icon.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/static/css/app/main.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/lib/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/lib/jquery.easyui.min.js"
	charset="utf-8"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/lib/easyui-lang-zh_CN.js"
	charset="utf-8"></script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/app/common.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/app/systemParams.js"></script>

<script type="text/javascript">
    var header="<%=request.getContextPath()%>";
</script>
</head>
<body>
	<div id="load">加载中......</div>
	<%@ include file="header.jsp"%>
	<div id="center">
		<ul class="systemUl">
			<li><label class="labelTitle">工单超时时间（分钟）:</label><input
				type="text" id="orderOverTime" name="order_timeout"
				value="${order_timeout}"><input type="button"
				value="保存" class="saveBtn inputBtn"></li>	
				<li><label class="labelTitle">维护工单号前缀:</label><input
				type="text" id="repairPrefix" name="repair_token_prefix"
				value="${repair_token_prefix}"><input type="button"
				value="保存" class="saveBtn inputBtn"></li>	
				<li><label class="labelTitle">回访工单号前缀:</label><input
				type="text" id=visitePrefix name="visit_token_prefix"
				value="${visit_token_prefix}"><input type="button"
				value="保存" class="saveBtn inputBtn"></li>
				<li><label class="labelTitle">客户提醒:</label>
				<select name="sms_customer_on_accept_enable">
						  <c:choose>
							   <c:when test="${sms_customer_on_accept_enable=='1'}">
							       <option value="1" selected="selected">是</option>
						           <option value="0">否</option>
							   </c:when>
							   <c:otherwise>  
                                   <option value="1">是</option>
						           <option value="0" selected="selected">否</option>
							   </c:otherwise>
							</c:choose>
						</select><input type="button"
				value="保存" class="saveBtn inputBtn"></li>
				<li><label class="labelTitle">客户提醒内容:</label>
				<textarea class="textareaBox" id="clientMessageContent" name="sms_customer_on_accept_message">${sms_customer_on_accept_message}</textarea>
				<input type="button"
				value="保存" class="saveBtn inputBtn"></li>
				<li><label class="labelTitle">工单超时提醒:</label>
				<select name="sms_fieldworker_on_timeout_enable">
						  <c:choose>
							   <c:when test="${sms_fieldworker_on_timeout_enable=='1'}">
							       <option value="1" selected="selected">是</option>
						           <option value="0">否</option>
							   </c:when>
							   <c:otherwise>  
                                   <option value="1">是</option>
						           <option value="0" selected="selected">否</option>
							   </c:otherwise>
							</c:choose>
			    </select><input type="button"
				value="保存" class="saveBtn inputBtn"></li>
				<li><label class="labelTitle">工单超时提醒内容:</label>
				<textarea class="textareaBox" id="orderMessageContent" name="sms_fieldworker_on_timeout_message">${sms_fieldworker_on_timeout_message}</textarea>
				<input type="button"
				value="保存" class="saveBtn inputBtn"></li>						
		</ul>
	</div>
	<%@ include file="footer.jsp"%>
</body>
</html>