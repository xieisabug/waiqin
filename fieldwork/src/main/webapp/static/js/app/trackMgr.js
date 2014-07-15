var checkStartTime=0;//记录下查询开始的时间，为导出结果记录
var checkEndTime=0;//记录下查询开始的时间，为导出结果记录
var checkId=0;//记录下查询的人的id
//格式化时间
Date.prototype.format = function(format)
{
	var o = {
		"M+" : this.getMonth()+1, //month
		"d+" : this.getDate(), //day
		"h+" : this.getHours(), //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter
		"S" : this.getMilliseconds() //millisecond
	};
	if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
	(this.getFullYear()+"").substr(4 - RegExp.$1.length));
	for(var k in o)if(new RegExp("("+ k +")").test(format))
	format = format.replace(RegExp.$1,
	RegExp.$1.length==1 ? o[k] :
	("00"+ o[k]).substr((""+ o[k]).length));
	return format;
}
$(document).ready(function () {
	//删除遮盖层
	$("#load").remove();
	//设置高亮菜单
	$("a[menu_type='trackMgr']").addClass("active");
	
	var startTime=new Date();
	startTime.setTime(startTime.getTime()-86400000);
	startTime=startTime.format("yyyy-MM-dd hh:mm:ss");
	var endTime=new Date().format("yyyy-MM-dd hh:mm:ss");
	//加载人
	//loadWorkers();
    //初始化面板
	 $("#trackDetailPanel").panel({
	        width:820,
	        height:160,
	        title:"轨迹文字记录"
	 });
    /*$("#trackPanel").panel({
        width:"100%",
        noheader:true,
        height:800
    });*/
    //数据加载提示
    $("#loadingMsg").window({
        height:30,
        width:150,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        closable:false,
        closed:true,
        modal:true,
        content:" <div class='loadingMsgDetail'>数据加载中......</div>",
        noheader:true,
        border:false
    });
    //显示外勤人员面板
    $("#workersPanel").panel({
        width:203,
        style:{
       	   display:"inline-block"
        },
        title:"员工列表",
        height:590
    });
    //选择地区
    $("#address").combobox({
        width:150,
        editable:false,
        panelHeight:180,
        onSelect:function(record){
        	//清除复选
        	$("#workersTable").datagrid("uncheckAll");
        	var city=record.value;
        	if(city=="湖南省"){
        		$(".datagrid-btable tr").removeClass("hidden");
        	}else{
        		$(".datagrid-btable tr").each(function (index) {
                    if ($(this).find("td:eq(3)").text().match(city) == null) {
                        $(this).addClass("hidden");
                    }else{
                    	$(this).removeClass("hidden");
                    }
                });
        	}
        }
    });
    //搜索控件
    $("#search").searchbox({
        prompt:'请输入姓名',
        searcher:function (value, name) {
        	//清除复选
        	$("#workersTable").datagrid("uncheckAll");
        	var city=$("#address").combobox("getValue");
        	if(city=="湖南省"){
        		if(value!=""){
               	 $(".datagrid-btable tr").each(function(index){
                        if($(this).find("td:eq(2)").text().match(value)!=null){
                            $(this).removeClass("hidden");
                        }else{
                       	 $(this).addClass("hidden");
                        }
                   });
               }else{
               	$(".datagrid-btable tr").removeClass("hidden");
               }
        	}else{
        		//如果不是湖南省，则要同时判断人是否在所选城市
        		if(value!=""){
               	 $(".datagrid-btable tr").each(function(index){
                        if($(this).find("td:eq(2)").text().match(value)!=null&&$(this).find("td:eq(3)").text().match(city) != null){
                            $(this).removeClass("hidden");
                        }else{
                       	 	$(this).addClass("hidden");
                        }
                   });
               }else{
            	   $(".datagrid-btable tr").each(function(index){
                       if($(this).find("td:eq(3)").text().match(city) != null){
                           $(this).removeClass("hidden");
                       }
                  });
               }
        	}            
        }
    });
    //表格控件
    $("#workersTable").datagrid({
    	url:header+"/web/fieldworker/"+currentUserId+"/find",
        width:"100%",
        height:478,
        fitClomuns:true,
        loadFilter:function(data){
            //增加一个字段
        	if(data.resultCode==1){
        		/*if(data.rows.length!=0){
        			$.each(data.rows,function(index,r){
        				r.cityName=r.department.city;
        			});
        		}*/
        		return data;
        	}else if(data.resultCode==2){
        		message.showMessage(message.messageText.loadError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        },
        queryParams:{
        	city:areaCode
        },
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        },
        /*onLoadSuccess:function(data){
            if(data.rows.length == 0){
                $("<tr style='height: 30px'><td style='width: 190px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable"));
            }
        },*/
        columns:[
            [
                {field:'device', checkbox:true},
                {field:'id', title:'编号', hidden:true},
                {field:'fullname', title:'姓名', width:100, align:"center"},
                {field:'cityName', title:'城市', hidden:true,formatter:function(value,row,index){
                	return row.department.city;
                }}
            ]
        ]
    });
    //查询按钮,如果大于一个，提示给用户，选择一个员工
    $("#check").click(function () {
    	var rows=$("#workersTable").datagrid("getChecked");
        if (rows.length==1) {
            //打开设置时间面板
        	$("#userId").val(rows[0].id);
        	checkId=rows[0].id;
        	$("#checkPanel").window("open");
        } else if(rows.length>1){
        	message.showMessage("为了更好的展示员工轨迹，请仅选择一个员工！");
        }else{
        	message.showMessage("请选择一个员工！");
        }
    });
    //设置时间面板,批量导出
    $("#checkOutPanel").window({
        width:600,
        height:260,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title:'设置时间',
        closable:true,
        closed:true,
        modal:true,
        zIndex:9998
    });
    //日期控件
    $("#startOutTime").datetimebox({
        required:true,
        value:startTime,
        width:220
    });
    $("#endOutTime").datetimebox({
        required:true,
        value:endTime,
        validType:"maxRangeTime['#startOutTime',15]",
        invalidMessage:"截止日期应小于今天并大于开始日期并两个日期间隔不能超过15天",
        width:220
    });

    //保存和取消按钮
    $("#reset").click(function () {
        $("#checkPanel").window("close");
    });
    //设置时间面板,查询
    $("#checkPanel").window({
        width:600,
        height:260,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title:'设置时间',
        closable:true,
        closed:true,
        modal:true,
        zIndex:9998
    });
    //日期控件
    $("#startTime").datetimebox({
        required:true,
        value:startTime,
        width:220
    });
    $("#endTime").datetimebox({
        required:true,
        value:endTime,
        validType:"maxRangeTime['#startTime',15]",
        invalidMessage:"截止日期应小于今天并大于开始日期并两个日期间隔不能超过15天",
        width:220
    });
    //设置日期表单提交
    $('#checkForm').form({
        onSubmit:function () {
            return $("#checkForm").form("validate");
        },
        success:function (data) {
        	data=eval("("+data+")");
            if (data.resultCode==1) {
                checkStartTime = $("#startTime").datetimebox("getValue");
                checkEndTime = $("#endTime").datetimebox("getValue");
                if(data.trackInfo.length!=0){
                	mapClass.addPloy(data.trackInfo);
                	var strText="";
                	$.each(data.trackInfo,function(index,t){
                		if(t.address!=""){
                			//有地址
                			strText+=t.createTime+":"+t.address+";\r\n";
                		}else{
                			strText+=t.createTime+":"+"无位置描述;\r\n";
                		}
                		
                	});
                	//换行，下面显示工单
                	strText+="此段时间内完成的工单\r\n";
                	$.each(data.workOrder,function(index,t){
                		strText+="纸质工单号："+t.orderToken+";客户名称:"+t.customerName+";工单类型："+message.workOrderTypeText[t.workOrderType]+";完成时间:"+t.finishTime+"\r\n";
                	});
                	//显示文字记录
                	$("#trackText").text(strText);
                }else{
                	message.showMessage("没有轨迹记录！");
                }
                $("#checkPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage("查询失败，请稍后重试！");
      		 }else if(data.resultCode==-1){
      			 message.showOutMessage();
      		 }
        }
    });
    
    // 这里要下载文件
    /*$("#checkOutSubmit").click(function(){
        $("#checkOutForm").form();
    });
*/
    //保存和取消按钮
    $("#checkOutReset").click(function(){
        $("#checkOutPanel").window("close");
    });
    //导出
    $("#checkOutAll").click(function () {
        //判断是否选择了人
    	var rows=$("#workersTable").datagrid("getChecked");
        if (rows.length>=1) {
        	var str=[];
        	for(var i=0,len=rows.length;i<len;i++){
        		str.push(rows[i].id);
        	}
            //打开设置时间面板
        	$("#userIds").val(str.join(","));
        	$("#checkOutPanel").window("open");
        }else{
        	message.showMessage("请选择员工，可以多选！");
        }
    });
    $("#checkOut").click(function () {
        //直接通过导出全部表单下载
    	if($("#trackText").text()!=""){
    		$("#userIds").val(checkId);
        	$("#startOutTime").datebox("setValue",checkStartTime);
        	$("#endOutTime").datebox("setValue",checkEndTime);
        	//$("#checkOutForm").form("submit");
        	$("#checkOutForm").submit();
    	}else{
    		message.showMessage("没有记录可以导出！");
    	}    	
    });
    //初始化地图
    if(areaCode==""){
    	mapClass.init("湖南省",9);
    }else{
    	mapClass.init(areaCode,12);
    }
    
});