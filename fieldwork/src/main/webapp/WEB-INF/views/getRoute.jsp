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
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/lib/jquery.easyui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/mapClass.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/app/getRoute.js"></script>
    <style type="text/css">
        html{
            width: 100%;
            height: 100%
        }
        body{
            width: 100%;
            height: 100%;
            margin: 0px;
            padding: 0px;
            overflow:hidden;
        }
        #mapContainer{
            width: 100%;
            height: 100%
        }
        #showDiv{
            background:#0000FF;
            color:white;
            position:absolute;
            widht:100%;
            height:18px;
            bottom:0px;
            right:10px;
            display:none;
            zIndex:9999;
            cursor:pointer;
        }
        .datagrid-cell{
            white-space: normal!important;
        }
    </style>
    <script>
       var header="<%=request.getContextPath()%>";
       var areaCode="${areaCode}";
       var customerAddress="${customerAddress}";
       var fieldWorkerInfoList='${fieldWorkerInfoList}';
       var latitude="${latitude}";
       var longitude="${longitude}}";
    </script>
</head>
<body>
       <div id="mapContainer"></div>
       <!--显示结果-->
       <div id="showResultsPanel">
           <table id="showResultsTable">
               <thead><tr><td>姓名</td><td>工单数</td><td>所需时间</td><td>路线</td></tr></thead>
               <tbody>
                    
               </tbody>
           </table>
       </div>
       <!--数据加载提示-->
       <div id="loadingMsg">

       </div>
       <div id="showDiv">显示结果</div>
</body>
</html>