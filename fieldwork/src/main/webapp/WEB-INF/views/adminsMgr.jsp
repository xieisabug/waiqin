<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
   
    <style type="text/css">
        body{overflow:hidden}
    </style>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/common.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/adminsMgr.js"></script>
    <script type="text/javascript">
       	var header="<%=request.getContextPath()%>";
       	var currentUserId="${currentUser.id}";
    </script>
</head>
<body>
<!-- 解决ie渲染慢，直接将页面隐藏，然后加载完了，删除遮盖层 -->
<div id="load">加载中......</div>
<%@ include file="header.jsp"%>

    <div id="center">

        <table id="userGrid">
            <thead><tr><th>编号</th><th>姓名</th><th>手机</th><th>邮箱</th><th>权限</th><th>负责区域</th><th>操作</th></tr></thead>
            <tbody>

            </tbody>
        </table>
        <!--工具栏-->
        <div id="userTb" style="padding: 5px;height: auto">
            <div style="margin-bottom:5px">
               <button id="addBtn" class="addBtn">增加</button>

                <span style="margin-left: 675px">
                    员工类型:<select id="worksType">
                        <option value="1">管理员</option>
                        <option value="2">外勤人员</option>
                    </select>
                </span>
            </div>
            <div>
                姓名: <input id="name" type="text"  style="width:120px;margin-right:5px">
                手机: <input id="telephone" type="text" style="width:120px;margin-right:5px">
                城市:<select id="address">
                 <option value="">湖南省</option>
                 <option value="长沙市">长沙市</option>
                 <option value="株洲市">株洲市</option>
                 <option value="湘潭市">湘潭市</option>
                 <option value="衡阳市">衡阳市</option>
                 <option value="邵阳市">邵阳市</option>
                 <option value="常德市">常德市</option>
                 <option value="张家界">张家界</option>
                 <option value="益阳市">益阳市</option>
                 <option value="娄底市">娄底市</option>
                 <option value="郴州市">郴州市</option>
                 <option value="永州市">永州市</option>
                 <option value="怀化市">怀化市</option>
                 <option value="湘西州">湘西州</option>
                 <option value="岳阳市">岳阳市</option>
                 </select>
                <button id="searchBtn" class="searchBtn">搜索</button>
            </div>
        </div>
        <!--增加管理员-->
        <div id="addPanel">
            <form id="addSubmitForm" method="post" action="<%=request.getContextPath()%>/web/user/${currentUser.id}/create">
                <ul>
                   
                    <li><label class="labelTitle">姓名:</label><input id="addName" type="text" name="fullname" class="inputBox"></li>
                    <li><label class="labelTitle">手机:</label><input id="addTelephone" type="text" name="mobileNo" class="inputBox"></li>
                    <li><label class="labelTitle">邮箱:</label><input id="addEmail" type="text" name="email" class="inputBox"></li>
                    <li><label class="labelTitle">权限:</label><select id="addWorkerType"  name="roleName" style="height: 35px">
                        <option value="ADMIN">管理员</option>
                    </select></li>
                    <li><label class="labelTitle">负责区域:</label><select id="addAddress" name="areaCode">
                       <option value="长沙市">长沙市</option>
                       <option value="株洲市">株洲市</option>
                       <option value="湘潭市">湘潭市</option>
                       <option value="衡阳市">衡阳市</option>
                       <option value="邵阳市">邵阳市</option>
                       <option value="常德市">常德市</option>
                       <option value="张家界">张家界</option>
                       <option value="益阳市">益阳市</option>
                       <option value="娄底市">娄底市</option>
                       <option value="郴州市">郴州市</option>
                       <option value="永州市">永州市</option>
                       <option value="怀化市">怀化市</option>
                       <option value="湘西州">湘西州</option>
                       <option value="岳阳市">岳阳市</option>
        			</select></li>
                    <li><label class="labelTitle">账号:</label><input id="addUserName" type="text" name="username" class="inputBox"></li>
                    <li><label class="labelTitle">密码:</label><input id="addPassword" type="password" name="password" class="inputBox"></li>
                    <li><label class="labelTitle">确认密码:</label><input id="addConfirmPassword" type="password" name="workerConfirmPassword" class="inputBox"></li>
                    <li><label class="labelTitle">座机:</label><input id="addPhone" name="telNo" type="text" class="inputBox"></li>
                    <li class="btnLi">
                        <input type="submit" value="保存" class="inputBtn">
                        <input id="addReset" type="reset" value="关闭" class="inputBtn"></li>
                </ul>
            </form>
        </div>
        <!--查看员工详细信息-->
        <div id="checkUserDetail" style="font-size:14px">
            <ul> 
              
                <li><label class='labelTitle'>姓名：</label><span id="checkUserName" class='spanText'>湖南中恩</span></li>
                <li><label class='labelTitle'>手机：</label><span id="checkUserTelephone" class='spanText'>湖南中恩</span></li>
                <li><label class='labelTitle'>邮箱：</label><span id="checkUserEmail" class='spanText'>湖南中恩</span></li>
                <li><label class='labelTitle'>权限：</label><span id="checkUserRole" class='spanText'>湖南中恩</span></li>
                <li><label class='labelTitle'>负责区域：</label><span id="checkUserAddress" class='spanText'>湖南中恩</span></li>
                <li><label class='labelTitle'>账号：</label><span id="checkUserUserName" class='spanText'>湖南中恩</span></li>
                <li><label class='labelTitle'>座机：</label><span id="checkUserPhone" class='spanText'>湖南中恩</span></li>
            </ul>
        </div>
        <!--增加、修改员工界面-->
        <div id="editPanel">
            <form id="submitForm" method="post" action="<%=request.getContextPath()%>/web/user/${currentUser.id}/edit">
               <ul>
                 
                   <li><label class="labelTitle">姓名:</label><input id="editName" type="text" name="fullname" class="inputBox"></li>
                   <li><label class="labelTitle">手机:</label><input id="editTelephone" type="text" name="mobileNo" class="inputBox"></li>
                   <li class="hiddenLi">
                   <input id="editId" name="id" type="hidden">
                   <input id="oldUsername" type="hidden" >
                   <input id="oldEmail" type="hidden">
                   <input id="oldMobileNo" type="hidden"></li>
                   <li><label class="labelTitle">邮箱:</label><input id="editEmail" type="text" name="email" class="inputBox"></li>
                   <li><label class="labelTitle">权限:</label><select id="editWorkerType"  name="roleName" style="height: 35px">
                       <option value="ADMIN">管理员</option>
                   </select></li>
                   <li><label class="labelTitle">负责区域:</label><select id="editAddress" name="areaCode">
                       <option value="长沙市">长沙市</option>
                       <option value="株洲市">株洲市</option>
                       <option value="湘潭市">湘潭市</option>
                       <option value="衡阳市">衡阳市</option>
                       <option value="邵阳市">邵阳市</option>
                       <option value="常德市">常德市</option>
                       <option value="张家界">张家界</option>
                       <option value="益阳市">益阳市</option>
                       <option value="娄底市">娄底市</option>
                       <option value="郴州市">郴州市</option>
                       <option value="永州市">永州市</option>
                       <option value="怀化市">怀化市</option>
                       <option value="湘西州">湘西州</option>
                       <option value="岳阳市">岳阳市</option>
        			</select></li>
                   <li><label class="labelTitle">账号:</label><input id="editUserName" type="text" name="username" class="inputBoxReadonly" readonly></li>
                   <li><label class="labelTitle">密码:</label><input id="editPassword" type="password" name="password" class="inputBox"></li>
                   <li><label class="labelTitle">确认密码:</label><input id="editConfirmPassword" type="password" name="workerConfirmPassword" class="inputBox"></li>
                   <li><label class="labelTitle">座机:</label><input id="editPhone" name="telNo" type="text" class="inputBox"></li>
                   <li class="btnLi">
                       <input type="submit" value="保存" class="inputBtn">
                       <input id="reset" type="reset" value="取消" class="inputBtn"></li>
               </ul>
            </form>
        </div>

    </div>
    
    <%@ include file="footer.jsp"%>
</body>
</html>