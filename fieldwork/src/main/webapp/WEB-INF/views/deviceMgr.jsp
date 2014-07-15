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
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/lib/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/app/main.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery.easyui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/easyui-lang-zh_CN.js" charset="utf-8"></script>
    
	<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/common.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/deviceMgr.js"></script>
    <script type="text/javascript">
           var header="<%=request.getContextPath()%>";
           var currentUserId="${currentUser.id}";
           var permissions='${rolePermission}';
    </script>
</head>
<body>
<div id="load">加载中......</div>
<%@ include file="header.jsp"%>
<div id="center">

    <table id="grid">
        <thead><tr><th>编号</th><th>MEID号</th><th>手机</th><th>手机型号</th><th>预存金额</th><th>手机套餐</th><th>手机状态</th><th>操作</th></tr></thead>
        <tbody>

        </tbody>
    </table>
    <!--工具栏-->
    <div id="tb" style="padding: 5px;height: auto">
        <div style="margin-bottom:5px">
         <shiro:hasPermission name="device:create"> 
            <button id="addBtn" class="addBtn" style="margin-right: 480px">增加</button>
         </shiro:hasPermission>
            MEID号: <input id="meid" type="text"  style="width:120px;margin-right:5px">
            手机: <input id="telephone" type="text" style="width:120px">
            <button id="searchBtn" class="searchBtn">搜索</button>
        </div>

    </div>
    <!--增加界面-->
    <div id="addPanel">
        <form id="submitForm" method="post" action="<%=request.getContextPath()%>/web/device/${currentUser.id}/create">
            <ul>
                <li><label class="labelTitle">MEID号:</label><input id="addMEID" type="text" name="meid" class="inputBox"></li>
                <li><label class="labelTitle">手机:</label><input id="addTelephone" type="text" name="phoneNo" class="inputBox"></li>
                <li><label class="labelTitle">手机型号:</label><input id="addTelephoneType" type="text" name="model" class="inputBox"></li>
                <li><label class="labelTitle">预存金额:</label><input id="addMoney" type="text" name="currentAmount" class="inputBox"></li>
                <li><label class="labelTitle">手机套餐:</label><input id="addMeal" type="text" name="consumeCategory" class="inputBox"></li>
                <li><label class="labelTitle">手机状态:</label><select id="addTelephoneStatus"  name="status" style="height: 35px">
                    <option value="ACTIVE">激活</option>
                    <option value="LOSS">挂失</option>
                    <option value="DAMAGE">报损</option>
                </select></li>
                 <li><label class="labelTitle">所属区域:</label><select
						id="addAddress" name="areaCode">
							<option value="长沙市">长沙市</option>
							<option value="株洲市">株洲市</option>
							<option value="湘潭市">湘潭市</option>
							<option value="衡阳市">衡阳市</option>
							<option value="邵阳市">邵阳市</option>
							<option value="常德市">常德市</option>
							<option value="张家界市">张家界</option>
							<option value="益阳市">益阳市</option>
							<option value="娄底市">娄底市</option>
							<option value="郴州市">郴州市</option>
							<option value="永州市">永州市</option>
							<option value="怀化市">怀化市</option>
							<option value="湘西州">湘西州</option>
							<option value="岳阳市">岳阳市</option>
					</select></li>
                <li class="btnLi">
                       <input type="submit" value="保存" class="inputBtn">
                       <input id="addReset" type="reset" value="关闭" class="inputBtn">
            </ul>
        </form>
    </div>
    <!--修改界面-->
    <div id="editPanel">
        <form id="editSubmitForm" method="post" action="<%=request.getContextPath()%>/web/device/${currentUser.id}/edit">
            <ul>
                 <li class="hiddenLi"><input id="editId" type="hidden" name="deviceId">
               		  <input id="oldMeid" type="hidden"></li>
                <li><label class="labelTitle">MEID号:</label><input id="editMEID" type="text" name="meid" class="inputBox"></li>
                <li><label class="labelTitle">手机:</label><input id="editTelephone" type="text" name="phoneNo" class="inputBox"></li>
                <li><label class="labelTitle">手机型号:</label><input id="editTelephoneType" type="text" name="model" class="inputBox"></li>
                <li><label class="labelTitle">预存金额:</label><input id="editMoney" type="text" name="currentAmount" class="inputBox"></li>
                <li><label class="labelTitle">手机套餐:</label><input id="editMeal" type="text" name="consumeCategory" class="inputBox"></li>
                <li><label class="labelTitle">手机状态:</label><select id="editTelephoneStatus"  name="status" style="height: 35px">
                    <option value="ACTIVE">激活</option>
                    <option value="LOSS">挂失</option>
                    <option value="DAMAGE">报损</option>
                </select></li>
                 <li><label class="labelTitle">所属区域:</label><select
						id="editAddress" name="areaCode">
							<option value="长沙市">长沙市</option>
							<option value="株洲市">株洲市</option>
							<option value="湘潭市">湘潭市</option>
							<option value="衡阳市">衡阳市</option>
							<option value="邵阳市">邵阳市</option>
							<option value="常德市">常德市</option>
							<option value="张家界市">张家界</option>
							<option value="益阳市">益阳市</option>
							<option value="娄底市">娄底市</option>
							<option value="郴州市">郴州市</option>
							<option value="永州市">永州市</option>
							<option value="怀化市">怀化市</option>
							<option value="湘西州">湘西州</option>
							<option value="岳阳市">岳阳市</option>
					</select></li>
                <li><label class="labelTitle">备注:</label><input type="text" name="memo" class="inputBox"></li>
                <li class="btnLi">
                    <input type="submit" value="保存" class="inputBtn">
                    <input id="editReset" type="reset" value="关闭" class="inputBtn">
            </ul>
        </form>
    </div>
    <!--查看以往记录界面-->
    <div id="checkOldPanel">
 		<table id="checkOldGrid">
	        <thead><tr><th>编号</th><th>操作类型</th><th>时间</th><th>备注</th></tr></thead>
	        <tbody>
	
	        </tbody>
    </table>
    </div>
</div>
<%@ include file="footer.jsp"%>
</body>
</html>