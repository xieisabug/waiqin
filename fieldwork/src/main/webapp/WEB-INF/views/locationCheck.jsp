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
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery.easyui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/easyui-lang-zh_CN.js" charset="utf-8"></script>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/mapClass.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/common.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/locationCheck.js"></script>
    <script type="text/javascript">
        var header="<%=request.getContextPath()%>";
        var areaCode="${currentUser.areaCode}";
        var currentUserId="${currentUser.id}";
    </script>
</head>
<body>

<%@ include file="header.jsp"%>
<div id="center">

    <div id="checkPanel">
        <div id="mapContainer" style="width: 820px;float:left;display:inline-block;"></div>
        <div id="workersPanel">
        <!-- 所管理的城市为空，显示城市下拉框 -->
	    <c:if test="${empty currentUser.areaCode}">
        <!--  城市: -->
  		  <select id="address">
	                 <option value="湖南省">全部</option>
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
             </c:if>
            <!-- ---------搜索 -->
            <input id="search">
             <table id="workersTable">
                 <thead><tr><th>ck</th><th>编号</th><th>姓名</th></tr></thead>
                 <tbody>
                     <!--  <tr><td>1</td><td>2</td><td>3</td></tr> -->
                 </tbody>
             </table>
        </div>
    </div>
    <!--数据加载提示-->
    <div id="loadingMsg">

    </div>
</div>
<%@ include file="footer.jsp"%>
</body>
</html>