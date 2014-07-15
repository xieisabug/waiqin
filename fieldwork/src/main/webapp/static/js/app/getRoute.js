var routeLines=[];//记录下已经计算过的路径折线图，最后画到地图上，在mapClass.js中使用
var routeResults=[];//记录下路径结果，包括没人有多少工单，到哪里要多久
var routeError=0;//记录下未导出路径的数量
//复制内容到剪贴板，只实现ie浏览器
function copyData(data){
	//ie原生方法
	window.clipboardData.clearData(); 
    window.clipboardData.setData("Text",data); 
    alert("路径已复制到剪贴板！");
}
$(document).ready(function(){
	//初始化梯度
	mapClass.init(areaCode,15);
    //数据加载提示
    $("#loadingMsg").window({
        height:30,
        width:150,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        closable:false,
        modal:true,
        closed:false,
        content:"<div class='loadingMsgDetail'>路径计算中......</div>",
        noheader:true,
        border:false
    });
    //显示结果面板
    $("#showResultsPanel").window({
        height:600,
        width:805,
        minimizable:true,
        collapsible:false,
        maximizable:false,
        closable:false,
        modal:true,
        closed:true,
        title:"路径结果",
        onMinimize:function(){
        	$("#showDiv").css("display","block");
        }
    });
    //显示结果表格
    $('#showResultsTable').datagrid({
        width:770,
        height:570,
        singleSelect:true,
        fitColumns:true,
        columns:[[
            {field:'name',title:'姓名',width:80,align:"center"},
            {field:'orderNumber',title:'工单数',width:50,align:"center"},
            {field:'time',title:"所需时间",width:120,align:"center"},
            {field:'line',title:'路线',width:520,align:"center"},
            {field:'opt',title:'操作',width:80,align:"center",formatter: function(value,row,index){
            	//var data=row.line.replace(/<[^>]*>/g,"");
            	return "<a href='javascript:copyData(\""+row.line+"\")'>复制路径</a>";
           }}
        ]]
    });
    //显示结果
    $("#showDiv").click(function(){
    	  $("#showResultsPanel").window("open");
    	  $(this).css("display","none");
    });
    //如果传了经纬度则直接定位
    if(latitude){
    	 if(fieldWorkerInfoList!="[]"){
         	//转成数组对象
             fieldWorkerInfoList=eval("("+fieldWorkerInfoList+")");
            
             var mark=null;
             $.each(fieldWorkerInfoList,function(index,f){
            		//添加外勤人员位置
                  	mark=new mapClass.workerMarker(new BMap.Point(f.latestLongitude,f.latestLatitude),f.fieldWorkerName,"未完成工单："+f.pendingWorkOrderSize);
              		mapClass.ownMap.addOverlay(mark);
              		//mapClass.getRoute(f,customerAddress,areaCode,longitude,latitude);
              		mapClass.getRouteByBus(f,customerAddress,areaCode,parseFloat(longitude),parseFloat(latitude));
            	               
             });
         }else{
         	$.messager.alert("警告","无法定位到外勤人员，无法获取路劲");
         	 $("#loadingMsg").window("close");
         }  
    }else{
    	//解析客户地址
        var myGeo = new BMap.Geocoder();
        // 将地址解析结果显示在地图上,并调整地图视野
        myGeo.getPoint(customerAddress, function(point){
            if(point){
            	//导航
                if(fieldWorkerInfoList!="[]"){
                	//转成数组对象
                    fieldWorkerInfoList=eval("("+fieldWorkerInfoList+")");
                    
                   
                    var mark=null;
                    $.each(fieldWorkerInfoList,function(index,f){
                    		//添加外勤人员位置
                        	mark=new mapClass.workerMarker(new BMap.Point(f.latestLongitude,f.latestLatitude),f.fieldWorkerName,"未完成工单："+f.pendingWorkOrderSize);
                    		mapClass.ownMap.addOverlay(mark);
                    		//mapClass.getRoute(f,customerAddress,areaCode,longitude,latitude);
                    		mapClass.getRouteByBus(f,customerAddress,areaCode,point.lng,point.lat);
                    			                    	                        
                    });
                }else{
                	$.messager.alert("警告","无法定位到外勤人员，无法获取路劲");
                	 $("#loadingMsg").window("close");
                }  
            }else{
            	$.messager.alert("警告","无法定位到客户地址，无法获取路劲");
            	$("#loadingMsg").window("close");
            	return ;
            }
        },areaCode);
    }    
 });