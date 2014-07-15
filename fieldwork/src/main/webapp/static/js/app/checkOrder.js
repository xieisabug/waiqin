//显示工单详情
function showOrderDetail(workOrderNo,customerId){
	  $.ajax({
		     url:header+"/web/order/"+currentUserId+"/show",
		     type:"get",
		     dataType:"json",
		     data:{
		    	 workOrderNo:workOrderNo,
		    	 customerId:customerId
		     },
		     success:function(data){
		    	 if(data.resultCode==1){
		    		 var strProblemDetail="";
		    		 var strProblemCategory="";
		    		 var strProblemType="";
		    		 var tels="";
		    		 if(data.problems.length!=0){
		    			 $.each(data.problems,function(index,p){
			    			 strProblemDetail+=index+1+":"+p.problemDetail+"; ";
			    			 strProblemCategory+=index+1+":"+p.problemCategoryName+"; ";
			    			 strProblemType+=index+1+":"+p.problemTypeName+"; ";
			    		 });
		    		 }
		    		
		    		 //组装电话号码
		    		 if(data.customer.tel&&data.customer.mobile){
		    			 tels=data.customer.tel+" / "+data.customer.mobile;
		    		 }else if(!data.customer.tel&&data.customer.mobile){
		    			 tels=data.customer.mobile;
		    		 }else{
		    			 tels=data.customer.tel;
		    		 }
		             $("#orderId").text(workOrderNo);
		             $("#consumerName").text(data.customer.customerName);
		             $("#consumerTels").text(tels);
		             $("#consumerAddress").text(data.customer.customerAddress);
		             $("#problemDetail").text(strProblemDetail);
		             $("#problemCategory").text(strProblemCategory);
		             $("#problemType").text(strProblemType);
		             $("#checkOrderPanel").window("open");
		         }else if(data.resultCode==2){
		        	 message.showMessage(message.messageText.loadError);
		  		 }else if(data.resultCode==-1){
		  			 message.showOutMessage();
		  		 } 
		       },
		       error:function(){
		    	   message.showMessage(message.messageText.networkError);
		       }
	  	});
}

$(document).ready(function(){
	//删除遮盖层
	$("#load").remove();
	
	//设置高亮菜单
	$("a[menu_type='checkOrder']").addClass("active");
	
	//城市下拉框
    $("#address").combobox({
        width:120,
        editable:false,
        panelHeight:180,
        onSelect:function(record){
        	$("#grid").datagrid("load",{
        		fullname:"",
                city:record.value
            });
        }
    });
	 //超时工单表格
    $("#grid").datagrid({
   	 	url:header+"/web/order/"+currentUserId+"/list",
        noheader:true,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        pageSize:15,
        striped: true,
        pagination:true,
        toolbar:"#tb",
        fitColumns:true,
        queryParams:{
       	 	fullname:"",
            city:areaCode
        },
        singleSelect:true,
        loadFilter:function(data){
            //增加一个字段
       	 if(data.resultCode==1){
         		/*if(data.total!=0){
         			$.each(data.rows,function(index,r){
         				r.opt="opt";
         			});
         		}*/
         		return data;
         	}else if(data.resultCode==2){
         		message.showMessage(message.messageText.loadError);
      		 }else if(data.resultCode==-1){
      			 message.showOutMessage();
      		 }
        },
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        },/*
        onLoadSuccess:function(data){
       	 $("#bindWorkerPanel").window("open");
        },*/
        columns:[[
                  {field:'workOrderNo',title:'工单号',width:80,align:"center"},
                  {field:'serviceDate',title:'派工时间',width:150,align:"center"},
                  {field:'workOrderType',title:'服务类型',width:100,align:"center",formatter:function(value,row,inde){
                  		return message.workOrderTypeText[value];
                  }},
                  {field:'fullname',title:'服务人员',width:80,align:"center"},
                  {field:'departmentName',title:'所属部门',width:80,align:"center"},
                  {field:'customerName',title:'客户名称',width:500,align:"center"},
                  {field:'customerId',title:'操作',width:80,align:"center",formatter: function(value,row,index){
                      return '<a href="javascript:void(0)" onclick="showOrderDetail('+row.workOrderNo+','+value+')">查看工单</a>';
                  }}
       ]]
    });
  //搜索按钮事件
    $("#searchBtn").click(function(){
            $("#grid").datagrid("load",{
            	fullname:$("#workerName").val(),
                city:!areaCode?$("#address").combobox("getValue"):areaCode
            });
     });
    //查看相关代码
    $("#checkOrderPanel").window({
        width:550,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '查看员工信息',
        closed:true,
        modal:true,
        zIndex:9999
    });    
});