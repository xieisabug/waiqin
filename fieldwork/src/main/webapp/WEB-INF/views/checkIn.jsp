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
	src="<%=request.getContextPath()%>/static/js/app/checkIn.js"></script>

<script type="text/javascript">
    var header="<%=request.getContextPath()%>";
	var currentUserId = "${currentUser.id}";
	var role = "${role}";
	var areaCode = "${currentUser.areaCode}";
</script>
</head>
<body>
	<div id="load">加载中......</div>
	<%@ include file="header.jsp"%>
	<div id="center">
		<div id="tab">
			<!-- 查看签到tab项--------------------- -->
			<shiro:hasPermission name="checkin:list">
			<div title="查看签到">
				<table id="checkTable">
					<thead>
						<th>编号</th>
						<th>标题</th>
					</thead>
					<tbody></tbody>
				</table>
				<!--查看签到工具栏-->
				<div id="checkTableTb">
					姓名: <input id="checkTableName" type="text" class="searchInput"
						style="width: 120px; margin-right: 5px"> 日期起：<input
						type="text" id="checkTableFromDate"> 日期止：<input
						type="text" id="checkTableToDate"> 签到：
						<select	id="showAll">
						<option value="true">全部</option>
						<option value="false">未签到</option>
					</select>
					<button id="checkTableSearch" class="searchBtn">搜索</button>
					<button id="checkOutBtn" class="inputBtn"
						style="margin-right: 50px">导出</button>
					<!-- 所管理的城市为空，显示城市下拉框 -->
					  <c:if test="${empty currentUser.areaCode}">
		                                   部门:<select id="address">
							<option value="">全部</option>
							<option value="长沙市">长沙服务部</option>
							<option value="常德市">常德服务部</option>
							<option value="郴州市">郴州服务部</option>
							<option value="怀化市">怀化服务部</option>
							<option value="衡阳市">衡阳服务部</option>
							<option value="娄底市">娄底服务部</option>
							<option value="邵阳市">邵阳服务部</option>
							<option value="湘潭市">湘潭服务部</option>
							<option value="湘西州">湘西州服务部</option>
							<option value="益阳市">益阳服务部</option>
							<option value="岳阳市">岳阳服务部</option>
							<option value="永州市">永州服务部</option>
							<option value="株洲市">株洲服务部</option>
							<option value="张家界">张家界服务部</option>
						</select>
					</c:if>
				</div>
			</div>
			</shiro:hasPermission>
			<!-- 设置签到tab项 -->
			<shiro:hasPermission name="checkinSetting:edit">
				<div title="设置签到">
					<p style="text-align: center">温馨提醒：系统默认周一至周五发送未签到提醒，周六周日只做签到考勤。如果需要特殊开启或者关闭哪一天不签到，请点击开启签到/关闭签到进行设置。</p>
					<ul>
						<li><label class="labelTitle">签到内容:</label> <textarea
								class="textareaBox" id="checkInContent" name="checkin_sms_message">${checkInMessage}</textarea>
							<input type="button" value="保存" class="saveBtn inputBtn"></li>
						<li><label class="labelTitle">签到时间（格式08:00）:</label><input
							type="text" id="checkInTime" name="checkin_time"
							value="${checkInTime}"> <input type="button"
							value="保存" class="saveBtn inputBtn"></li>
						<li><label class="labelTitle">签到范围（提前分钟数）:</label><input
							type="text" id="checkInScope" name="checkin_ahead_minutes"
							value="${checkInAheadTime}"><input type="button"
							value="保存" class="saveBtn inputBtn"></li>
						<li><label class="labelTitle">签到提醒:</label>
						<select name="checkin_sms_enable">
						  <c:choose>
							   <c:when test="${checkInSmsEnable=='1'}">
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
						<li><label class="labelTitle">查看已设置:</label> <input
							type="text" id="checkSetedFromDate"> 至<input type="text"
							id="checkSetedEndDate"> <input style="margin-left: 10px"
							type="button" value="查询" id="checkSeted" class="inputBtn"></li>

						<li><label class="labelTitle">设置签到日期:</label> <input
							type="button" id="openCheck" value="开启签到" class="inputBtn">
							<input type="button" id="closeCheck" value="关闭签到"
							class="inputBtn"></li>
					</ul>
					<!--开启和关闭（设置）签到面板-->
					<div id="setCheckIn">
						<form id="setCheckInForm" method="post"
							action="<%=request.getContextPath()%>/web/checkin-setting/${currentUser.id}/create"
							style="padding-top: 30px">
							<ul>
								<li class="hiddenLi"><input type="hidden"
									name="checkStatus" id="checkInStatus"></li>
								<li><label class="labelTitle">开始日期:</label><input
									type="text" name="fromDate" id="setFromDate"></li>
								<li><label class="labelTitle">截止日期:</label><input
									type="text" name="toDate" id="setEndDate"></li>
								<li style="margin-left: 150px"><input type="submit"
									value="保存" class="inputBtn"> <input id="reset"
									type="reset" value="关闭" class="inputBtn"></li>
							</ul>
						</form>
					</div>
					<!--查看已设置的签到日期-->
					<div id="checkSetedDate">
						<table id="checkSetedDateTable">
							<thead>
								<tr>
									<th>日期</th>
									<th>状态</th>
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>
					</div>
				</div>
			</shiro:hasPermission>
		</div>
	</div>
	<!-- 导出查询结果 -->
	<form id="checkOutResultsForm" style="display:none" method="post"
		action="<%=request.getContextPath()%>/web/checkin/${currentUser.id}/export">
		<ul>
			<li><input type="text" name="fromDate" id="checkOutStartDay">
			<input type="text" name="toDate" id="checkOutEndDay">
			<input type="text" name="showAll" id="checkOutAll">
			<input type="text" name="fieldWorkerName" id="checkOutName">
			<input type="text" name="city" id="checkOutCity">
			</li>
			
		</ul>
	</form>
	<%@ include file="footer.jsp"%>
</body>
</html>