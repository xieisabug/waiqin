//相关点击事件函数
function showRow(id){
    $.ajax({
     url:header+"/web/user/"+currentUserId+"/load",
     type:"post",
     dataType:"json",
     data:{
    	 userId:id
     },
     success:function(data){
    	 if(data.resultCode==1){
             $("#checkUserName").text(data.user.fullname);
             $("#checkUserTelephone").text(data.user.mobileNo);
             $("#checkUserEmail").text(data.user.email);
             $("#checkUserRole").text("管理员");
             $("#checkUserAddress").text(data.user.areaCode);
             $("#checkUserUserName").text(data.user.username);
             $("#checkUserPhone").text(data.user.telNo);
             $("#checkUserDetail").window("open");
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
function editRow(id){
	 $.ajax({
	     url:header+"/web/user/"+currentUserId+"/load",
	     type:"post",
	     dataType:"json",
	     data:{
	    	 userId:id
	     },
	     success:function(data){
	    	 if(data.resultCode==1){
	    		 $("#editId").val(data.user.id);
	             $("#editName").val(data.user.fullname);
	             $("#editTelephone").val(data.user.mobileNo);
	             $("#oldMobileNo").val(data.user.mobileNo);
	             $("#editEmail").val(data.user.email);
	             $("#oldEmail").val(data.user.email);
	             $("#editWorkerType").combobox('setValue',data.user.roleName);
	             $("#editAddress").combobox('setValue',data.user.areaCode);
	             $("#editUserName").val(data.user.username);
	             $("#oldUsername").val(data.user.username);
	             $("#editPassword").val(data.user.password);
	             $("#editConfirmPassword").val(data.user.password);
	             $("#editPhone").val(data.user.telNo);
	             $("#editPanel").window("open");
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
function deleteRow(id,index){
    $.messager.confirm('确认','确认删除?',function(ok){
        if(ok){
             $.ajax({
	             url:header+'/web/user/'+currentUserId+"/remove",
	             type:"post",
	             dataType:"json",
	             data:{
	            	 userId:id 
	             },
	             success:function(data){
	            	 if(data.resultCode==1){
	            		 message.showMessage(message.messageText.deleteSuccess);
	                     $('#userGrid').datagrid('deleteRow',index);
	                 }else if(data.resultCode==2){
	                	 message.showMessage(message.messageText.deleteError);
	          		 }else if(data.resultCode==-1){
	          			 message.showOutMessage();
	          		 }
	             },
	             error:function(){
	          	     message.showMessage(message.messageText.networkError);
	             }
             });
        }
    })
};

$(document).ready(function(){
	//删除遮盖层
	$("#load").remove();
	//设置高亮菜单
	$("a[menu_type='workerMgr']").addClass("active");
	
	 /*-------------------------查看相关代码=======================================================*/
    $("#checkUserDetail").window({
        width:450,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '查看员工信息',
        closed:true,
        modal:true,
        zIndex:9999
    });

    /*----------------------------增加、修改相关代码-----------------------------------------------*/
    //增加修改面板
    $("#addPanel").window({
        width:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加',
        closed:true,
        modal:true,
        zIndex:9999
    });
    $("#editPanel").window({
        width:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '修改',
        //closable:true,
        closed:true,
        modal:true,
        zIndex:9999
    });
  //工具栏相关代码
    $("#addBtn").click(function(){
            $("#addPanel").window("open");
        });
    $("#worksType").combobox({
        width:200,
        panelHeight:80,
        onSelect:function(record){
            if(record.value!="1"){
                window.location.href=header+"/web/fieldworker/manage";
            }
        }
    });
    $("#address").combobox({
        width:120,
        panelHeight:180
    });
    $("#searchBtn").click(function(){
            $("#userGrid").datagrid("load",{
            	fullname:$("#name").val(),
            	mobileNo:$("#telephone").val(),
            	areaCode:$("#address").combobox("getValue")
            });
        });
   
	//表格
    $("#userGrid").datagrid({
        url:header+'/web/user/'+currentUserId+"/find",
        noheader:true,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        toolbar:"#userTb",
        pagination:true,
        fitColumns:true,
        pageSize:15,
        striped: true,
        singleSelect:true,
        queryParams:{
        	fullname:$("#name").val(),
        	mobileNo:$("#telephone").val(),
        	areaCode:$("#address").combobox("getValue")
        },
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
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        },
        onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width: 1023px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable"));
            }
            
        },
        columns:[[
            {field:'id',title:'编号',width:120,align:"center",hidden:true},
            {field:'fullname',title:'名字',width:120,align:"center"},
            {field:'mobileNo',title:'手机',width:180,align:"center"},
            {field:'email',title:'邮箱',width:240,align:"center"},
            {field:'roleName',title:'权限',width:150,align:"center",formatter:function(value,row,index){
            	return "管理员";
            }},
            {field:'areaCode',title:'负责区域',width:180,align:"center"},
            {field:'action',title:'操作',width:140,align:"center",formatter: function(value,row,index){
                return '<a href="javascript:void(0)" onclick="showRow('+row.id+')">查看</a> <a href="javascript:void(0)" onclick="editRow('+row.id+')">修改</a> <a href="javascript:void(0)" onclick="deleteRow('+row.id+','+index+')">删除</a>';
            }}
        ]]
    });
   
	
   
    $("#addName").validatebox({
        required:true,
        validType:"length[1,20]"
    });
    $("#addTelephone").validatebox({
        required:true,
        validType:"telephone&remote['"+header+"/web/user/"+currentUserId+"/check-mobileno','mobileNo']"
    });
    $("#addEmail").validatebox({
        required:true,
        validType:"email&length[1,30]&remote['"+header+"/web/user/"+currentUserId+"/check-email','email']"
    });
    $("#addUserName").validatebox({
        required:true,
        validType:"length[1,20]&remote['"+header+"/web/user/"+currentUserId+"/check-username','username']"
    });
    $("#addPassword").validatebox({
        required:true,
        validType:"length[6,20]"
    });
    $("#addConfirmPassword").validatebox({
        required:true,
        validType:"equals['#addPassword']"
    });
    $("#addPhone").validatebox({
        required:true,
        validType:"phone&length[1,20]"
    });
    //保存和取消按钮
    $('#addSubmitForm').form({
        onSubmit: function(){
            return $("#addSubmitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#userGrid").datagrid("load",{
                	fullname:$("#name").val(),
                	mobileNo:$("#telephone").val(),
                	areaCode:$("#address").val()
                });
                //清空填写的记录
                $("#addSubmitForm .inputBox").val("");
                $("#addPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
    });
    $("#addReset").click(function(){
        $("#addPanel").window("close");
    });
  
    $("#editName").validatebox({
        required:true,
        validType:"length[1,20]"
    });
    $("#editTelephone").validatebox({
        required:true,
        validType:"telephone&hasMobileNo['#oldMobileNo']"
    });
    $("#editEmail").validatebox({
        required:true,
        validType:"email&length[1,30]&hasEmail['#oldEmail']"
    });
    $("#editUserName").validatebox({
        required:true,
        validType:"length[1,20]&hasUserName['#oldUsername']"
    });
    $("#editPassword").validatebox({
        required:true,
        validType:"length[6,20]"
    });
    $("#editConfirmPassword").validatebox({
        required:true,
        validType:"equals['#editPassword']"
    });
    $("#editPhone").validatebox({
        required:true,
        validType:"phone&length[1,20]"
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
                $("#userGrid").datagrid("load",{
                	fullname:$("#name").val(),
                	mobileNo:$("#telephone").val(),
                	areaCode:$("#address").val()
                });
                $("#editPanel").window("close");
            }else if(data.resultCode==2){
            	 message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
    });
    $("#reset").click(function(){
        $("#editPanel").window("close");
    });
    //combobox控件
    $("#addWorkerStatus").combobox({
        width:185,
        height:35,
        panelHeight:80,
        required:true
    });
    $("#addWorkerType").combobox({
        width:185,
        height:35,
        panelHeight:80,
        required:true
    });
    $("#addAddress").combobox({
        width:185,
        panelHeight:80,
        required:true
    })
    $("#editWorkerStatus").combobox({
        width:185,
        height:35,
        panelHeight:80,
        required:true
    });
    $("#editWorkerType").combobox({
        width:185,
        height:35,
        panelHeight:80,
        required:true
    });
    $("#editAddress").combobox({
        width:185,
        panelHeight:80,
        required:true
    })
});