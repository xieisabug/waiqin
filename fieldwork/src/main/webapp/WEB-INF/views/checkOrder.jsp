<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/checkOrder.js"></script>
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
        <thead>
        <tr><th>*</th><th>*</th><th>*</th><th>*</th><th>*</th><th>*</th><th>*</th><th>*</th></tr>
        </thead>
        <tbody>

        </tbody>
    </table>
    <!--工具栏-->
    <div id="tb" style="padding: 5px;height: auto">
        <div style="margin-bottom:5px">
            员工姓名: <input type="text" id="workerName" style="width:120px">
            <button id="searchBtn" class="searchBtn">搜索</button>
            <!-- 所管理的城市为空，说明是系统管理员，显示城市下拉框 -->
			  <c:if test="${empty currentUser.areaCode}">
				<span style="margin-left: 580px">部门:<select id="address">
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
    <!--查看工单界面-->
    <div id="checkOrderPanel">
          <ul>
				<li><label class='labelTitle'>工单号：</label><span
					id="orderId" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>客户姓名：</label><span
					id="consumerName" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>客户联系电话：</label><span
					id="consumerTels" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>客户地址：</label><span
					id="consumerAddress" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>问题描述：</label><span
					id="problemDetail" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>问题类型：</label><span
					id="problemCategory" class='spanText'>湖南中恩</span></li>
				<li><label class='labelTitle'>问题类别：</label><span
					id="problemType" class='spanText'>湖南中恩</span></li>
			</ul>
    </div>
   
</div>
<%@ include file="footer.jsp"%>
</body>
</html>