$(document).ready(function(){
	//删除遮盖层
	$("#load").remove();
	//设置高亮菜单
	$("a[menu_type='checkIn']").addClass("active");
     
    //tab初始化
	$("#tab").tabs({
		width:"100%",
		height:580
     });
	/*------------------------------设置签到内容-==============================*/
    //签到时间和签到内容验证
    $("#checkInTime").validatebox({
        required:true,
        validType:"checkInTime"
    });
    $("#checkInContent").validatebox({
        required:true,
        validType:"length[1,256]"
    });
    $("#checkInScope").validatebox({
    	required:true,
        validType:"minute"
    });
    //保存数据
	$(".saveBtn").click(function(){
		var pre=$(this).prev();
		var paramName=$(pre).attr("name");
		var paramValue=$(pre).val();
		if($(pre).validatebox()&&$(pre).validatebox("isValid")){
    		$.ajax({
           	 url:header+"/web/system-setting/edit",
           	 type:"post",
           	 dataType:"json",
           	 data:{
           		paramName:paramName,
           		paramValue:paramValue
           	 },
           	 success:function(data){
           		 if(data.resultCode==1){
           			message.showMessage(message.messageText.saveSuccess);
           		 }else if(data.resultCode==2){
           			message.showMessage(message.messageText.saveError);
           		 }else if(data.resultCode==-1){
           			message.showOutMessage();
           		 }
           	 },
             error:function(){
	          	   message.showMessage(message.messageText.networkError);
	         }
            });    	
    	}
	});
    //开启和关闭事件
    $("#openCheck").click(function(){
        $("#setCheckIn").window("open");
        $("#checkInStatus").val("ENABLE");
    });
    $("#closeCheck").click(function(){
        $("#setCheckIn").window("open");
        $("#checkInStatus").val("DISABLE");
    });
    //设置开启或者关闭签日期面板
    $("#setCheckIn").window({
        width:500,
        height:260,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '设置日期',
        closable:false,
        closed:true,
        modal:true,
        zIndex:9998
    });
    //设置签到日期控件
    $("#setFromDate").datebox({
        required:true,
        validType:"isLtToday",
        width:220
    });
    $("#setEndDate").datebox({
        required:true,
        validType:"twoDate['#setFromDate']",
        width:220
    });
    //设置签到日期表单提交
    $('#setCheckInForm').form({
        onSubmit: function(){
            return $("#setCheckInForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
      			 $("#setCheckIn").window("close");
      		 }else if(data.resultCode==2){
      			message.showMessage(message.messageText.saveError);
      		 }else if(data.resultCode==-1){
      			message.showOutMessage();
      		 }
        }
    });
    //设置签到保存和取消按钮
    $("#reset").click(function(){
        $("#setCheckIn").window("close");
    });
    //查看已设置的签到日期控件
    $("#checkSetedFromDate").datebox({
        width:220
    });
    $("#checkSetedEndDate").datebox({
        validType:"twoDate['#checkSetedFromDate']",
        width:220
    });
    //查询已设置的签到按钮事件
    $("#checkSeted").click(function(){
    	if($("#checkSetedEndDate").datebox("isValid")){
    		 $("#checkSetedDateTable").datagrid("load",{
    			 fromDate:$("#checkSetedFromDate").datebox("getValue"),
	             toDate:$("#checkSetedEndDate").datebox("getValue")
    		 });
    	}
    });
    //查询已设置的签到面板
    $("#checkSetedDate").window({
        width:630,
        height:420,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '查看已设置的日期',
        closable:true,
        closed:true,
        modal:true,
        zIndex:9999
    });
    //查看已设置的签到表格
    $("#checkSetedDateTable").datagrid({
    	url:header+"/web/checkin-setting/"+currentUserId+"/find",
    	pageSize:10,
        width:610,
        queryParams:{
        	 fromDate:"",
             toDate:""
       },
        height:380,
        fitColumns:true,
        pagination:true,
        loadFilter:function(data){
            //判断是否正确了
        	if(data.resultCode==1){
        		 return data;
     		 }else if(data.resultCode==2){
     			message.showMessage(message.messageText.loadError);
     		 }else if(data.resultCode==-1){
     			message.showOutMessage();
     		 }
        },
        onLoadSuccess:function(data){
        	//显示面板
   		 	$("#checkSetedDate").window("open");
           /* if(data.total == 0){
               $("<tr style='height: 30px'><td style='width:600px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable"));
            }*/
        },
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        },
        onBeforeLoad:function(params){
        	if(params.fromDate==""){
        		//如果不是从查询按钮进入，直接终止
        		return false;
        	}
        },
        columns:[[
            {field:'settingDate',title:'日期',width:340,align:"center"},
            {field:'status',title:'状态',width:240,align:"center",formatter: function(value,row,index){
                return message.entry[value];
            }}
        ]]
    });
    
    /*----------------------------查看签到==========================*/
    //查看签到表格
    $("#checkTable").datagrid({
    	  url:header+"/web/checkin/"+currentUserId+"/find",
          noheader:true,
          minimizable:false,
          collapsible:false,
          maximizable:false,
          toolbar:"#checkTableTb",
          pagination:true,
          fitColumns:true,
          pageSize:15,
          striped: true,
          singleSelect:true,
          queryParams:{
        	  city:areaCode,
        	  fromDate:"",
        	  toDate:"",
        	  showAll:true,
        	  fieldWorkerName:""
          },
          columns:[
            [
                {field:'fieldWorkerName', title:'姓名',width:200, align:"center"},
                {field:'userCheckinDateTime', title:'签到时间', width:340, align:"center"},
                {field:'systemCheckinDateTime', title:'系统签到时间', width:340, align:"center"},
                {field:'departmentName', title:'是否签到', width:340, align:"center",formatter:function(value,row,index){
                	 if(row.userCheckinDateTime<row.systemCheckinDateTime){
                		 return "是";
                	 }else{
                		 return "否";
                	 }
                }}
            ]
        ],
        loadFilter:function(data){
            //增加一个字段
        	if(data.resultCode==1){
        		if(data.total!=0){
        			$.each(data.rows,function(index,r){
        				r.opt="opt";
        			});
        		}
        		return data;
        	}else if(data.resultCode==2){
        		message.showMessage(message.messageText.loadError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        },
        /*onBeforeLoad:function(params){
        	var tab = $('#tab').tabs('getSelected');
        	var index = $('#tab').tabs('getTabIndex',tab);
        	if(index!=1&&role!="ADMIN"){
        		//如果不是第一个tab，不请求数据
        		return false;
        	}
        },*/
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        }/*,
        onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width:800px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable:eq(0)"));
            }
        }*/
    });
    //查看签到工具栏设置
    $("#checkTableFromDate").datebox({
        width:120
    });
    $("#checkTableToDate").datebox({
        width:120,
        validType:"maxRangeTime['#checkTableFromDate',7]",
        invalidMessage:"截止日期应小于今天并大于开始日期并两个日期间隔不能超过7天"
    });
    $("#address").combobox({
        width:120,
        editable:false,
        panelHeight:180,
        onSelect:function(record){
        	$("#checkTable").datagrid("load",{
        		fromDate:$("#checkTableFromDate").datebox("getValue"),
    	    	toDate:$("#checkTableToDate").datebox("getValue"),
    	    	showAll:$("#showAll").val(),
    	    	fieldWorkerName:$("#checkTableName").val(),
                city:record.value
            });
        }
    });
    //查看签到搜索
    $("#checkTableSearch").click(function(){
    	//验证通过才加载
    	if($("#checkTableToDate").datebox("isValid")){
    		$("#checkTable").datagrid("load",{
    			city:!areaCode?$("#address").combobox("getValue"):areaCode,
				fromDate:$("#checkTableFromDate").datebox("getValue"),
				toDate:$("#checkTableToDate").datebox("getValue"),
    	    	showAll:$("#showAll").val(),
    	    	fieldWorkerName:$("#checkTableName").val()
    		});
    	}
	});
    //导出签到结果
    $("#checkOutBtn").click(function(){
    	if($("#checkTableToDate").datebox("isValid")){
	    	//设置form的值
	    	var city=!areaCode?$("#address").combobox("getValue"):areaCode;
	    	var fromDate=$("#checkTableFromDate").datebox("getValue");
			var toDate=$("#checkTableToDate").datebox("getValue");
	    	var showAll=$("#showAll").val();
	    	var fieldWorkerName=$("#checkTableName").val();
	    	$("#checkOutStartDay").val(fromDate);
	    	$("#checkOutEndDay").val(toDate);
	    	$("#checkOutAll").val(showAll);
	    	$("#checkOutName").val(fieldWorkerName);
	    	$("#checkOutCity").val(city);
	    	
	    	//提交表单
	    	$("#checkOutResultsForm").submit();
    	}
    });
});
