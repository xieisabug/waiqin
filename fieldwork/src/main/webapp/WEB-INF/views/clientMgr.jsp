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
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/lib/jquery.easyui.min.js"
	charset="utf-8"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/lib/easyui-lang-zh_CN.js"
	charset="utf-8"></script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/app/mapClass.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/app/common.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/static/js/app/clientMgr.js"></script>
<script type="text/javascript">
    var header="<%=request.getContextPath()%>";
	var areaCode = "${currentUser.areaCode}";
	var currentUserId = "${currentUser.id}";
	var clientCount = "${customerCount}";
</script>
</head>
<body>
	<%@ include file="header.jsp"%>
	<!--数据加载提示-->
    <div id="loadingMsg">

    </div>
	<div id="center">
		<div id="tab">
			<!-- 客户查看tab项 -->
			<shiro:hasPermission name="customer:list">
			<div title="客户查看">
				<div style="margin: 2px 0px">
					<!-- ----搜索条件------------------------ -->
					客户名称: <input id="name" type="text" style="width: 120px;">
					<button id="searchClient" class="searchBtn"
						style="margin-right: 510px">搜索</button>
					<!-- ----------搜索条件结束 -->
					<!-- 所管理的城市为空，显示城市下拉框 -->
					  <c:if test="${empty currentUser.areaCode}">
	                       	城市:<select id="address">
							<option value="">湖南省</option>
							<option value="长沙市">长沙市</option>
							<option value="常德市">常德市</option>
							<option value="郴州市">郴州市</option>
							<option value="怀化市">怀化市</option>
							<option value="衡阳市">衡阳市</option>
							<option value="娄底市">娄底市</option>
							<option value="邵阳市">邵阳市</option>
							<option value="湘潭市">湘潭市</option>
							<option value="湘西州">湘西州</option>
							<option value="益阳市">益阳市</option>
							<option value="岳阳市">岳阳市</option>
							<option value="永州市">永州市</option>
							<option value="株洲市">株洲市</option>
							<option value="张家界市">张家界</option>
						</select>
						</c:if>
				</div>
				<!-- 地图显示模块 -->
				<div id="mapContainer"
					style="position: relative; top: 0px; height: 640px;"></div>
			</div>
			</shiro:hasPermission>
			<!-- 客户设置tab项 -->
			 <shiro:hasPermission name="customer:mark"> 
			<div title="客户设置">
				<table id="clientGrid">
					<thead>
						<tr>
							<th>编号</th>
							<th>姓名</th>
							<th>手机</th>
							<th>邮箱</th>
							<th>权限</th>
							<th>负责区域</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>

					</tbody>
				</table>
				<!--管理人员表格工具栏-->
				<div id="clientTb">
					<div>
						客户名称: <input id="clientName" type="text"
							style="width: 120px; margin-right: 5px">
						<button id="listSearchClient" style="margin-right: 500px"
							class="searchBtn">搜索</button>
						<!-- 所管理的城市为空，显示城市下拉框 -->
					 	 <c:if test="${empty currentUser.areaCode}">
	                      	 选择地区:<select id="listAddress">
								<option value="">湖南省</option>
								<option value="长沙市">长沙市</option>
								<option value="常德市">常德市</option>
								<option value="郴州市">郴州市</option>
								<option value="怀化市">怀化市</option>
								<option value="衡阳市">衡阳市</option>
								<option value="娄底市">娄底市</option>
								<option value="邵阳市">邵阳市</option>
								<option value="湘潭市">湘潭市</option>
								<option value="湘西州">湘西州</option>
								<option value="益阳市">益阳市</option>
								<option value="岳阳市">岳阳市</option>
								<option value="永州市">永州市</option>
								<option value="株洲市">株洲市</option>
								<option value="张家界市">张家界</option>
							</select>
							</c:if>
					</div>
				</div>
			</div>
		  </shiro:hasPermission>
		</div>
		<!-- 显示搜索结果界面 -->
		<div id="searchResultPanel">
			<table id="searchResultGrid">
				<thead>
					<tr>
						<th>编号</th>
						<th>姓名</th>
						<th>手机</th>
						<th>邮箱</th>
						<th>权限</th>
						<th>负责区域</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>

				</tbody>
			</table>
		</div>
		<!-- 显示点击获取客户结果界面 -->
		<div id="clickResultPanel">
			<table id="clickResultGrid">
				<thead>
					<tr>
						<th>编号</th>
						<th>姓名</th>
						<th>手机</th>
						<th>邮箱</th>
						<th>权限</th>
						<th>负责区域</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>

				</tbody>
			</table>
		</div>
		<!-- 显示设置界面 -->
		<div id="markClientPanel">
			<form id="markClientForm" method="post"
				action="<%=request.getContextPath()%>/web/customer/${currentUser.id}/mark">
				
				<ul style="padding:0px">
					<li class="hiddenLi"><input id="clientId" type="hidden" name="customerId"></li>
					<li><label class="labelTitle"> 重点类型:</label>
					<select id="tagType" name="tag" style="width:200px">
								<option value="0">非重点</option>
								<option value="1">市重点</option>
								<option value="2">省重点</option>
								<option value="3">省市重点</option>
					</select>
					</li>
					<li class="btnLi"><input type="submit" value="保存"
						class="inputBtn"> <input id="markClientReset" type="reset"
						value="关闭" class="inputBtn"></li>
				</ul>
				
			</form>
		</div>
	</div>
	<%@ include file="footer.jsp"%>
</body>
</html>