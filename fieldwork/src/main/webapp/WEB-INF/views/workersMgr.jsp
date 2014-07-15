<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
	src="<%=request.getContextPath()%>/static/js/app/workersMgr.js"></script>

<script type="text/javascript">
    var header="<%=request.getContextPath()%>";
	var currentUserId = "${currentUser.id}";
	var areaCode = "${currentUser.areaCode}";
	var permissions='${rolePermission}';
</script>
</head>
<body>
	<div id="load">加载中......</div>
	<%@ include file="header.jsp"%>
	<div id="center">
		<div id="tab">
			<!-- 外勤人员管理tab项 -->
			<shiro:hasPermission name="fieldWorker:list">
			<div title="外勤人员管理">
				<table id="workerGrid">
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
				<!--外勤人员管理工具栏-->
				<div id="workerTb">
					姓名: <input id="workerName" type="text" style="width: 120px">
					手机: <input id="workerTelephone" type="text" style="width: 120px">
					<button id="searchWorker" class="searchBtn">搜索</button>
					<!-- 所管理的城市为空，说明是系统管理员，显示城市下拉框 -->
					  <c:if test="${empty currentUser.areaCode}">
						<span style="margin-left: 450px">部门:<select id="address">
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
								<option value="张家界市">张家界服务部</option>
						</select>
						</span>
					</c:if>
				</div>
			</div>
			</shiro:hasPermission>
			<!-- 管理员管理 tab项-->
			<shiro:hasPermission name="user:list">
			<div title="管理人员管理">
				<table id="userGrid">
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
				<div id="userTb">
					<div style="margin-bottom: 5px">
					<!-- 控制增加按钮 -->
					  <shiro:hasPermission name="user:create"> 
						<button id="addBtn" class="addBtn" style="margin-right: 500px">增加</button>
					 </shiro:hasPermission>
						姓名: <input id="userFullName" type="text" style="width: 120px; margin-right: 5px"> 
						手机: <input id="userTelephone" type="text" style="width: 120px; margin-right: 5px" autocomplete="off">
						<button id="searchUser" class="searchBtn">搜索</button>
					</div>
				</div>

			</div>
            </shiro:hasPermission>
			<!-- 角色管理 tab项 -->
			<shiro:hasPermission name="role:list">
			<div title="角色管理">
				<table id="roleGrid">
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
				<div id="roleTb">
					<div style="margin-bottom: 5px">
					<!-- 控制增加按钮 -->
					  <shiro:hasPermission name="role:create"> 
						<button id="addRoleBtn" class="addBtn" style="margin-right: 670px">增加</button>
					  </shiro:hasPermission>
						角色名: <input id="roleName" type="text" style="width: 120px" autocomplete="off">
						<button id="searchRole" class="searchBtn">搜索</button>
					</div>
				</div>
			</div>
           </shiro:hasPermission>
		</div>
		
		<!--查看外勤人员详细信息-->
		<div id="checkWorkerDetail">
			<ul>
				<li><label class='labelTitle'>工号：</label><span
					id="checkWorkerNo" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>姓名：</label><span
					id="checkWorkerName" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>部门：</label><span
					id="checkWorkerDepartment" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>所属城市：</label><span
					id="checkWorkerCity" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>手机号：</label><span
					id="checkWorkerTelephone" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>MEID号：</label><span
					id="checkWorkerMEID" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>型号：</label><span
					id="checkWorkerModal" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>套餐：</label><span
					id="checkWorkerCategory" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>预交额：</label><span
					id="checkWorkerAmount" class='spanText'>湖南中恩</span></li>
			</ul>
		</div>
		<!--修改外勤人员界面-->
		<div id="editWorkerPanel">
			<form id="editWorkerSubmitForm" method="post"
				action="<%=request.getContextPath()%>/web/fieldworker/${currentUser.id}/reset-password">
				<ul>
					<li class="hiddenLi"><input id="editWorkerId" type="hidden"
						name="fieldWorkerId"></li>
					<li><label class="labelTitle">工号:</label><span
						id="editWorkerNo" class='spanText'>湖南中恩</span></li>
					<li><label class="labelTitle">姓名:</label><span
						id="editWorkerName" class='spanText'>湖南中恩</span></li>
					<li><label class="labelTitle">密码:</label><input id="password"
						type="password" name="newPassword" class="inputBox"></li>
					<li><label class="labelTitle">确认密码:</label><input
						id="confirmPassword" type="password" name="confirmpassword"
						class="inputBox"></li>

					<li class="btnLi"><input type="submit" value="保存"
						class="inputBtn"> <input id="editWorkerReset" type="reset"
						value="关闭" class="inputBtn"></li>
				</ul>
			</form>
		</div>
		<!-- 绑定设备界面 -->
		<div id="bindWorkerPanel">
			<table id="bindGrid">
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
		<!-- 查看管理人员详细信息 -->
		<div id="checkUserDetail" style="font-size: 14px">
			<ul>

				<li><label class='labelTitle'>姓名：</label><span
					id="checkUserFullName" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>手机：</label><span
					id="checkUserTelephone" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>邮箱：</label><span
					id="checkUserEmail" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>权限：</label><span
					id="checkUserRole" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>负责部门：</label><span
					id="checkUserAddress" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>账号：</label><span
					id="checkUserName" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>座机：</label><span
					id="checkUserPhone" class='spanText'>湖南中恩</span></li>
			</ul>
		</div>
		<!-- 增加管理人员详细信息 -->
		<div id="addUserPanel">
			<form id="addSubmitForm" method="post"
				action="<%=request.getContextPath()%>/web/user/${currentUser.id}/create">
				<ul>

					<li><label class="labelTitle">姓名:</label><input
						id="addUserFullName" type="text" name="fullname" class="inputBox"></li>
					<li><label class="labelTitle">手机:</label><input
						id="addUserTelephone" type="text" name="mobileNo" class="inputBox"></li>
					<li><label class="labelTitle">邮箱:</label><input
						id="addUserEmail" type="text" name="email" class="inputBox"></li>
					<li><label class="labelTitle">角色:</label><input id="addUserRoleType" name="roleId" ></li>
					<li><label class="labelTitle">负责部门:</label><select
						id="addUserAddress" name="areaCode">
							<option value="长沙市">长沙服务部</option>
							<option value="株洲市">株洲服务部</option>
							<option value="湘潭市">湘潭服务部</option>
							<option value="衡阳市">衡阳服务部</option>
							<option value="邵阳市">邵阳服务部</option>
							<option value="常德市">常德服务部</option>
							<option value="张家界市">张家界服务部</option>
							<option value="益阳市">益阳服务部</option>
							<option value="娄底市">娄底服务部</option>
							<option value="郴州市">郴州服务部</option>
							<option value="永州市">永州服务部</option>
							<option value="怀化市">怀化服务部</option>
							<option value="湘西州">湘西州服务部</option>
							<option value="岳阳市">岳阳服务部</option>
					</select></li>
					<li><label class="labelTitle">账号:</label><input
						id="addUserName" type="text" name="username" class="inputBox"></li>
					<li><label class="labelTitle">密码:</label><input
						id="addUserPassword" type="password" name="password"
						class="inputBox"></li>
					<li><label class="labelTitle">确认密码:</label><input
						id="addUserConfirmPassword" type="password"
						name="workerConfirmPassword" class="inputBox"></li>
					<li><label class="labelTitle">座机:</label><input
						id="addUserPhone" name="telNo" type="text" class="inputBox"></li>
					<li class="btnLi"><input type="submit" value="保存"
						class="inputBtn"> <input id="addUserReset" type="reset"
						value="关闭" class="inputBtn"></li>
				</ul>
			</form>
		</div>
		<!-- 修改管理人员详细信息 -->
		<div id="editUserPanel">
		 <div id="tabUser">
		   <div title="基本资料">
			<form id="editUserSubmitForm" method="post"
				action="<%=request.getContextPath()%>/web/user/${currentUser.id}/edit">
				<ul>

					<li><label class="labelTitle">姓名:</label><input
						id="editUserFullName" type="text" name="fullname" class="inputBox"></li>
					<li><label class="labelTitle">手机:</label><input
						id="editUserTelephone" type="text" name="mobileNo"
						class="inputBox"></li>
					<li class="hiddenLi"><input class="editUserId" name="id"
						type="hidden"> <input id="oldUserName" type="hidden">
						<input id="oldUserEmail" type="hidden"> <input
						id="oldUserMobileNo" type="hidden"></li>
					<li><label class="labelTitle">邮箱:</label><input
						id="editUserEmail" type="text" name="email" class="inputBox"></li>
					<li><label class="labelTitle">角色:</label><input id="editUserRoleType" name="roleId" ></li>
					<li><label class="labelTitle">负责部门:</label><select
						id="editUserAddress" name="areaCode">
							<option value="长沙市">长沙服务部</option>
							<option value="株洲市">株洲服务部</option>
							<option value="湘潭市">湘潭服务部</option>
							<option value="衡阳市">衡阳服务部</option>
							<option value="邵阳市">邵阳服务部</option>
							<option value="常德市">常德服务部</option>
							<option value="张家界市">张家界服务部</option>
							<option value="益阳市">益阳服务部</option>
							<option value="娄底市">娄底服务部</option>
							<option value="郴州市">郴州服务部</option>
							<option value="永州市">永州服务部</option>
							<option value="怀化市">怀化服务部</option>
							<option value="湘西州">湘西州服务部</option>
							<option value="岳阳市">岳阳服务部</option>
					</select></li>
					<li><label class="labelTitle">账号:</label><input
						id="editUserName" type="text" name="username"
						class="inputBoxReadonly" readonly></li>
					<li><label class="labelTitle">座机:</label><input
						id="editUserPhone" name="telNo" type="text" class="inputBox"></li>
					<li class="btnLi"><input type="submit" value="保存"
						class="inputBtn"> <input class="editUserReset inputBtn" type="reset"
						value="取消"></li>
				</ul>
			</form>
			</div>
			<div title="登陆密码">
			     <form id="editUserPasswordForm" method="post"
				action="<%=request.getContextPath()%>/web/user/${currentUser.id}/change-password">
				<ul>
					<li class="hiddenLi"><input class="editUserId" name="userId"
						type="hidden"></li>
					<li><label class="labelTitle">密码:</label><input
						id="editUserPassword" type="password" name="newPassword"
						class="inputBox"></li>
					<li><label class="labelTitle">确认密码:</label><input
						id="editUserConfirmPassword" type="password"
						name="workerConfirmPassword" class="inputBox"></li>
					<li class="btnLi"><input type="submit" value="保存"
						class="inputBtn"> <input class="editUserReset inputBtn" type="reset"
						value="取消" ></li>
				</ul>
			</form>
			</div>
			</div>
		</div>
		<!-- 增加角色详细信息 -->
		<div id="addRolePanel">
			<form id="addRoleForm" method="post"
				action="<%=request.getContextPath()%>/web/role/${currentUser.id}/create">
				<ul>
					<li class="hiddenLi"><input id="addPermission" type="hidden"
						name="permissions"></li>
					<li><label class="labelTitle">角色名:</label><input
						id="addRoleName" type="text" name="roleName" class="inputBox"></li>

					<li><label class="labelTitle" style="vertical-align:top">权限*:</label>
					<ul id="addPermissionTree">
							
					</ul>
					<li class="btnLi"><input type="submit" value="保存"
						class="inputBtn"><input id="addRoleReset" type="reset"
						value="取消" class="inputBtn"></li>
				</ul>
			</form>
		</div>
		<!-- 修改角色详细信息 -->
		<div id="editRolePanel">
			<form id="editRoleForm" method="post"
				action="<%=request.getContextPath()%>/web/role/${currentUser.id}/edit">
				<ul>
					<li class="hiddenLi"><input id="editPermission" type="hidden"
						name="permissions">
						<input id="editRoleId" type="hidden"
						name="roleId">
					</li>
					<li><label class="labelTitle">角色名:</label><input
						id="editRoleName" type="text" name="roleName" class="inputBox"></li>

					<li><label class="labelTitle" style="vertical-align:top">权限*:</label>
					<ul id="editPermissionTree">
							
					</ul>
					<li class="btnLi"><input type="submit" value="保存"
						class="inputBtn"><input id="editRoleReset" type="reset"
						value="取消" class="inputBtn"></li>
				</ul>
			</form>
		</div>
	</div>
	<%@ include file="footer.jsp"%>
</body>
</html>