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
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/trackMgr.js"></script>
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

    <!-- <div id="trackPanel" style="overflow:hidden"> -->
      <div style="width:820px;float:left;display:inline-block">
      	<!-- 地图 -->
        <div id="mapContainer" style="width: 820px;height:430px"></div>
        <!--文字记录显示-->
        <div id="trackDetailPanel">
            <textarea id="trackText" readonly="readonly" style="width: 99%;height: 100px"></textarea>
            <div style="margin-left: auto;width:85px">
           		 <shiro:hasPermission name="trackinfo:export">
                       <input id="checkOut" type="button" value="导出结果" class="inputBtn">
                 </shiro:hasPermission>
            </div>
        </div>
       </div>
        <!--外勤人员列表面板-->
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
            <!-- 外勤人员列表 -->
            <table id="workersTable">
                <thead><tr><th>ck</th><th>编号</th><th>姓名</th></tr></thead>
                <tbody>
               
                </tbody>
            </table>
            <div style="margin:5px auto 0 auto;width: 170px">
                <input id="check" type="button" value="查看轨迹" class="inputBtn" style="margin-right:5px">
                <shiro:hasPermission name="trackinfo:export">
                	<input id="checkOutAll" type="button" value="批量导出" class="inputBtn">
                </shiro:hasPermission>
            </div>
        </div>
        <!--设置查询的时间-->
        <div id="checkPanel">
            <form id="checkForm" method="get" action="<%=request.getContextPath()%>/web/trackinfo/${currentUser.id}/list"  style="padding-top: 30px">
                <ul>
                    <li class="hiddenLi"><input id="userId" type="hidden" name="fieldWorkerId"></li>
                    <li><label class="labelTitle">开始日期:</label><input type="text" name="fromTime" id="startTime"></li>
                    <li><label class="labelTitle">截止日期:</label><input type="text" name="toTime" id="endTime"></li>
                    <li style="margin-left: 150px"><input type="submit"   value="查询" class="inputBtn">
                        <input id="reset" type="reset"  value="取消" class="inputBtn"></li>
                </ul>
            </form>
        </div>
        <!--设置导出的时间-->
        <div id="checkOutPanel">
         <!--   <span>温馨提示：选择好时间确定后，请到列表中选择人，然后点击“导出所选”按钮进行导出</span>-->
            <form id="checkOutForm" method="get" style="padding-top: 30px" action="<%=request.getContextPath()%>/web/trackinfo/${currentUser.id}/export">
                <ul>
                    <li class="hiddenLi"><input id="userIds" type="hidden" name="fieldWorkerId"></li>
                    <li><label class="labelTitle">开始日期:</label><input type="text" name="fromTime" id="startOutTime"></li>
                    <li><label class="labelTitle">截止日期:</label><input type="text" name="toTime" id="endOutTime"></li>
                    <li style="margin-left: 150px"><input type="submit" id="checkOutSubmit" value="导出" class="inputBtn">
                        <input id="checkOutReset" type="reset"  value="取消" class="inputBtn"></li>
                </ul>
            </form>
        </div>
       
    <!-- </div> -->
    <!--数据加载提示-->
    <div id="loadingMsg">

    </div>
</div>
<%@ include file="footer.jsp"%>
</body>
</html>