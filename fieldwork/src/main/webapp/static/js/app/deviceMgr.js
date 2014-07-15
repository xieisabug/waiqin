//修改设备函数
function editRow(index){
	$("#grid").datagrid("selectRow",index);
	 var row=$("#grid").datagrid("getSelected");
	$("#editId").val(row.id);
	$("#oldMeid").val(row.meid);
	$("#editMEID").val(row.meid);
	$("#editTelephone").val(row.phoneNo);
	$("#editTelephoneType").val(row.model);
	$("#editMoney").val(row.currentAmount);
	$("#editMeal").val(row.consumeCategory);
	$('#editTelephoneStatus').combobox('setValue',row.status);
	$("#editAddress").combobox("setValue",row.areaCode);
	$("#editPanel").window("open");
}
//查看设备的以往记录
function checkOld(id){
	$("#checkOldGrid").datagrid("load",{
		deviceId:id
    });
	$("#checkOldPanel").window("open");
}
$(document).ready(function(){
	//删除遮盖层
	$("#load").remove();
	//转化权限数组
	permissions=eval("("+permissions+")");
	//设置高亮菜单
	$("a[menu_type='deviceMgr']").addClass("active");
    //工具栏相关代码
    $("#addBtn").click(function(){
            $("#addPanel").window("open");
    });
    //搜索按钮事件
    $("#searchBtn").click(function(){
            $("#grid").datagrid("load",{
                meid:$("#meid").val(),
                phoneNo:$("#telephone").val()
            });
        });
   
    //表格
    $("#grid").datagrid({
        url:header+"/web/device/"+currentUserId+"/find",
        noheader:true,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        pageSize:15,
        striped: true,
        singleSelect:true,
        fitColumns:true,
        queryParams:{
        	meid:$("#meid").val(),
        	phoneNo:$("#telephone").val()
        },
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
        },
        /*onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width:1023px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable"));
            }
        },*/
        columns:[[
            {field:'id',title:'编号',width:50,align:"center"},
            {field:'meid',title:'MEID号',width:180,align:"center"},
            {field:'phoneNo',title:'手机号',width:150,align:"center"},
            {field:'model',title:'手机型号',width:180,align:"center"},
            {field:'currentAmount',title:'预存金额',width:80,align:"center"},
            {field:'consumeCategory',title:'手机套餐',width:180,align:"center"},
            {field:'areaCode',title:'所属区域',width:80,align:"center"},
            {field:'status',title:'手机状态',width:80,align:"center",formatter:function(value,row,inde){
            	return message.entry[value];
            }},
            {field:'action',title:'操作',width:150,align:"center",formatter: function(value,row,index){
            	if($.inArray("device:edit",permissions)!=-1||$.inArray("*:*",permissions)!=-1){
            		return '<a href="javascript:void(0)" onclick="editRow('+index+')">修改</a> <a href="javascript:void(0)" onclick="checkOld('+row.id+')">以往记录</a>';
            	}else{
            		return '<a href="javascript:void(0)" onclick="checkOld('+row.id+')">以往记录</a>';
            	}
                
            }}
        ]],
        toolbar:"#tb",
        pagination:true
    });
    //查看以往记录表格
    $("#checkOldGrid").datagrid({
        url:header+"/web/device/"+currentUserId+"/load-journal",
        noheader:true,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        striped: true,
        singleSelect:true,
        fitColumns:true,
        queryParams:{
        	deviceId:""
        },
        loadFilter:function(data){
        	if(data.resultCode==1){
        		data.rows=data.journals;//转化数据
        		data.journals=null;
        		return data;
        	}else if(data.resultCode==2){
        		message.showMessage(message.messageText.loadError);
     		 }else if(data.resultCode==-1){
     			message.showOutMessage();
     		 }
        },
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        },
        /*onLoadSuccess:function(data){
            if(data.rows.length == 0){
                $("<tr style='height: 30px'><td style='width:790px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable:eq(1)"));
            }
        },*/
        onBeforeLoad:function(params){
        	if(params.deviceId==""){
        		//如果不是从查询按钮进入，直接终止
        		return false;
        	}
        },
        columns:[[
            {field:'createTime',title:'时间',width:140,align:"center"},
            {field:'state',title:'前一状态',width:500,align:"center",formatter:function(value,row,index){
            	row.previousState=eval("("+row.previousState+")");
            	var str="meid号:"+row.previousState.meid+";型号:"+row.previousState.model+";套餐:"+row.previousState.consumeCategory+";号码:"+row.previousState.phoneNo
            	+";预交额:"+row.previousState.currentAmount+";状态:"+message.entry[row.previousState.status];
            	return "<span title='"+str+"'>"+str+"</span";
            }},
            {field:'memo',title:'备注',width:140,align:"center",formatter:function(value,row,index){
            	return "<span title='"+value+"'>"+value+"</span>";
            }}
        ]]
    });


    /*----------------------------增加、修改相关代码-----------------------------------------------*/
    //增加面板
    $("#addPanel").window({
        width:500,
        height:380,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加',
        //closable:true,
        closed:true,
        modal:true,
        zIndex:9999
    });
    //修改面板
    $("#editPanel").window({
        width:500,
        height:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '修改',
        //closable:true,
        closed:true,
        modal:true,
        zIndex:9999
    });
    //查看以往记录面板
    $("#checkOldPanel").window({
        width:800,
        height:380,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '以往记录',
        //closable:true,
        closed:true,
        modal:true,
        zIndex:9999
    });
    //增加操作所有字段的验证
    $("#addMEID").validatebox({
        required:true,
        validType:"length[1,64]&remote['"+header+"/web/device/"+currentUserId+"/check-meid','meid']"
    });
    $("#addTelephone").validatebox({
        required:true,
        validType:"telephone"
    });
    $("#addTelephoneType").validatebox({
        required:true,
        validType:"length[1,64]"
    });
    $("#addMoney").validatebox({
        required:true,
        validType:"money"
    });
    $("#addMeal").validatebox({
        required:true,
        validType:"length[1,64]"
    });
    //修改操作的所有字段验证
    $("#editMEID").validatebox({
        required:true,
        validType:"length[1,64]&hasMeid['#oldMeid']"
    });
    $("#editTelephone").validatebox({
        required:true,
        validType:"telephone"
    });
    $("#editTelephoneType").validatebox({
        required:true,
        validType:"length[1,64]"
    });
    $("#editMoney").validatebox({
        required:true,
        validType:"money"
    });
    $("#editMeal").validatebox({
        required:true,
        validType:"length[1,64]"
    });

    //保存和取消按钮
    $('#submitForm').form({
        onSubmit: function(){
            return $("#submitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
            if(data.resultCode==1){
            	message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#grid").datagrid("load",{
                    meid:$("#meid").val(),
                    telephone:$("#telephone").val()
                });
                //清空填写的记录
                $("#submitForm .inputBox").val("");
                $("#addPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			message.showOutMessage();
     		 }
        }
    });
    //增加操作的取消按钮
    $("#addReset").click(function(){
        $("#addPanel").window("close");
    });
    //修改表单提交
    $('#editSubmitForm').form({
        onSubmit: function(){
            return $("#editSubmitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
            if(data.resultCode){
            	message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#grid").datagrid("load",{
                    meid:$("#meid").val(),
                    telephone:$("#telephone").val()
                });   
                $("#editPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			message.showOutMessage();
     		 }
        }
    });
    //修改取消按钮
    $("#editReset").click(function(){
        $("#editPanel").window("close");
    });
    
    //combobox控件，状态
    $("#addTelephoneStatus").combobox({
        width:185,
        editable:false,
        height:35,
        panelHeight:80,
        required:true
    });
    $("#editTelephoneStatus").combobox({
        width:185,
        editable:false,
        height:35,
        panelHeight:80,
        required:true
    });
  //combobox控件，状态
    $("#addAddress").combobox({
        width:185,
        height:35,
        editable:false,
        panelHeight:80,
        required:true
    });
    $("#editAddress").combobox({
        width:185,
        height:35,
        editable:false,
        panelHeight:80,
        required:true
    });
});