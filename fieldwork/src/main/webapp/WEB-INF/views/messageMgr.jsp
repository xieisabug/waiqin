<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
        .labelTitle{width:100px}
        .btnLi{padding-left:100px!important}
    </style>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/common.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/messageMgr.js"></script>
    <script type="text/javascript">
       	var header="<%=request.getContextPath()%>";
       	var currentUserId="${currentUser.id}";
       	var areaCode="${currentUser.areaCode}";
    </script>
</head>
<body>
<div id="load">加载中......</div>
<%@ include file="header.jsp"%>

    <div id="center">

        <table id="grid">
            <thead><tr><th>标题</th><th>内容</th><th>时间</th><th>操作</th></tr></thead>
            <tbody>

            </tbody>
        </table>
        <!--工具栏-->
        <div id="tb" style="padding: 5px;height: auto">
             <shiro:hasPermission name="notification:create"> 
               <button id="addBtn" class="addBtn">新建</button>
             </shiro:hasPermission>
                	 标题: <input id="searchTitle" type="text"  style="width:120px">
                <button id="searchBtn" class="searchBtn">搜索</button>
                <!-- 所管理的城市为空，说明是系统管理员，显示城市下拉框 -->
					  <c:if test="${empty currentUser.areaCode}">
						<span style="margin-left: 450px">区域:<select id="address">
							<option value="">湖南省</option>
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
						</select>
						</span>
					</c:if>
        </div>
        <!--新建-->
        <div id="addPanel">
            <form id="addSubmitForm" method="post" action="<%=request.getContextPath()%>/web/notification/${currentUser.id}/create">
                <ul>
                    <li><label class="labelTitle">标题:</label><input id="title" type="text" name="title" class="inputBox"></li>
                    <c:choose>
	                    <c:when test="${empty currentUser.areaCode}">
	                    	<li><label class="labelTitle">区域:</label>
	                    	<select name="city" id="addMessageCity">
							<option value="">湖南省</option>
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
						</select>
	                    	</li>
	                    </c:when>
	                    <c:otherwise>
	                    	<li class="hiddenLi"><input type="text" name="city" value="${currentUser.areaCode}"></li>
	                    </c:otherwise>
                    </c:choose>                    
                    <li><label class="labelTitle" style="vertical-align: top">内容:</label><textarea id="content" type="text" name="content" style="height:80px;width:180px"></textarea></li>
                    <!-- <li><label class="labelTitle">时间:</label><input id="date" type="text" name="publishDate" class="inputBox"></li> -->
                    <li class="btnLi">
                        <input type="submit" value="发送" class="inputBtn">
                        <input id="addReset" type="reset" value="关闭" class="inputBtn"></li>
                </ul>
            </form>
        </div>
        <!--查看详细信息-->
        <div id="checkPanel" style="font-size:14px">
            <ul> 
                <li><label class='labelTitle'>标题：</label><span id="checkTitle" class='spanText'>湖南中恩</span></li>
                <li><label class='labelTitle'>区域：</label><span id="checkCity" class='spanText'>湖南中恩</span></li>
                <li><label class='labelTitle'>内容：</label><span id="checkContent" class='spanText'>湖南中恩</span></li>
                <li><label class='labelTitle'>日期：</label><span id="checkDate" class='spanText'>湖南中恩</span></li>
            </ul>
        </div>

    </div>
    <%@ include file="footer.jsp"%>
</body>
</html>