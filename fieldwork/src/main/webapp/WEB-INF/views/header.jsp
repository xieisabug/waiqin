<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="shiro-ext" uri="http://shiro.apache.org/tags-ext" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="header">
    <img src="<%=request.getContextPath()%>/static/images/header_img.png">
    <div id="headerRight">
        <%-- <a class="countMgr" href="<%=request.getContextPath()%>/web/person-setting/${currentUser.id}">账户管理</a> --%>
        <a class="logout" href="<%=request.getContextPath()%>/web/logout">退出</a>
    </div>
</div>
<div id="menu">
    <ul>
        <shiro-ext:hasAnyPermissions name="fieldworker:list,user:list,role:list">
        	<li><a menu_type="workerMgr" href="<%=request.getContextPath()%>/web/fieldworker/manage">员工管理</a></li>
        </shiro-ext:hasAnyPermissions>
        
        <shiro-ext:hasAnyPermissions name="checkin:list,checkinSetting:edit">
       		<li><a menu_type="checkIn" href="<%=request.getContextPath()%>/web/checkin-setting/">签到管理</a></li>
       	</shiro-ext:hasAnyPermissions>
       	
       	 <shiro:hasPermission name="workOder:listTimeout">
       		<li><a menu_type="checkOrder" href="<%=request.getContextPath()%>/web/order/">超时工单</a></li>
       	</shiro:hasPermission>
       	
        <shiro:hasPermission name="fieldWorker:locate"> 
        	<li><a menu_type="locationMgr" href="<%=request.getContextPath()%>/web/fieldworker/">定位管理</a></li>
        </shiro:hasPermission>
        
        <shiro:hasPermission name="trackinfo:list">
        	<li><a menu_type="trackMgr" href="<%=request.getContextPath()%>/web/trackinfo/">轨迹管理</a></li>
        </shiro:hasPermission>
        
        <shiro:hasPermission name="customer:list">
       		 <li><a menu_type="clientMgr" href="<%=request.getContextPath()%>/web/customer/">客户管理</a></li>
        </shiro:hasPermission>
        
        <shiro:hasPermission name="device:list">
        	<li><a menu_type="deviceMgr" href="<%=request.getContextPath()%>/web/device/">设备管理</a></li>
        </shiro:hasPermission>
        
        <shiro:hasPermission name="notification:list">
       		<li><a menu_type="messageMgr" href="<%=request.getContextPath()%>/web/notification/">公告通知</a></li>
       	</shiro:hasPermission>
       	
       	<shiro-ext:hasAnyPermissions name="problemType:list,problemCategory:list,revenue:list,expenseItem:list,department:list,product:list">
       		<li><a menu_type="syncMgr" href="<%=request.getContextPath()%>/web/department/">同步数据</a></li>
        </shiro-ext:hasAnyPermissions>
        
        <shiro:hasPermission name="*:*">
       		<li><a menu_type="systemParams" href="<%=request.getContextPath()%>/web/system-setting/">系统参数</a></li>
        </shiro:hasPermission>
        
        <li><a menu_type="countMgr" href="<%=request.getContextPath()%>/web/person-setting/${currentUser.id}">账号管理</a></li>
    </ul>
</div>