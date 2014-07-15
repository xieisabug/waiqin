var fieldworkerId="";//绑定时记录下员工的id
var treeData=null;//记录下后台给予的树的数据，好在新建和修改的时候加载同一个，不用到后台加载
var hasPermission={};//记录下已经拥有的权限，在修改的时候可以勾选中
/*----------外勤人员表点击相关事件====================*/
//显示外勤人员信息
function showWorker(index){
	$("#workerGrid").datagrid("selectRow",index);
	var row=$("#workerGrid").datagrid("getSelected");
	$("#checkWorkerNo").text(row.workerNo);
	$("#checkWorkerName").text(row.fullname);
	$("#checkWorkerDepartment").text(row.department.departmentName);
	$("#checkWorkerCity").text(row.department.city);
	if(row.device){
		$("#checkWorkerTelephone").text(row.device.phoneNo);
		$("#checkWorkerMEID").text(row.device.meid);
		$("#checkWorkerModal").text(row.device.model);
		$("#checkWorkerCategory").text(row.device.consumeCategory);
		$("#checkWorkerAmount").text(row.device.currentAmount);
	}else{
		$("#checkWorkerTelephone").text("未绑定");
		$("#checkWorkerMEID").text("未绑定");
		$("#checkWorkerModal").text("未绑定");
		$("#checkWorkerCategory").text("未绑定");
		$("#checkWorkerAmount").text("未绑定");
	}
	
    $("#checkWorkerDetail").window("open");
}
//修改外勤人员信息，主要是登陆密码
function editWorker(id,workerNo,name){
    $("#editWorkerId").val(id);
    $("#editWorkerNo").text(workerNo);
    $("#editWorkerName").text(name);
    $("#editWorkerPanel").window("open");
}
//获取可以的设备
function bindRow(id){
    fieldworkerId=id;//设置要绑定的员工的id
    //加载可用设备
    $("#bindGrid").datagrid("load",{
    	loadFlag:1
    });
}
//绑定选择
function selectRow(id){
    $.ajax({
    	url:header+"/web/fieldworker/"+currentUserId+"/attach-device",
    	data:{
    		fieldWorkerId:fieldworkerId,
    		deviceId:id
    	},
    	dataType:"json",
    	type:"post",
    	success:function(data){
    		if(data.resultCode==1){
    			message.showMessage("绑定成功！");
    			$("#bindWorkerPanel").window("close");
    			//重载表格
       		 	$("#workerGrid").datagrid("load",{
                    fullname:$("#workerName").val(),
                    phoneNo:$("#workerTelephone").val(),
                    city:!areaCode?$("#address").combobox("getValue"):areaCode
                });
         	}else if(data.resultCode==2){
         		message.showMessage("绑定失败，请稍后重试！");
      		 }else if(data.resultCode==-1){
      			 message.showOutMessage();
      		 }
    	},
        error:function(){
       	    message.showMessage(message.messageText.networkError);
        }
    })
}
//取消绑定
function unBindRow(id){
    $.ajax({
        url:header+"/web/fieldworker/"+currentUserId+"/unattach-device",
        type:"post",
        dataType:"json",
        data:{
        	fieldWorkerId:id
        },
        success:function(data){
        	if(data.resultCode==1){
        		message.showMessage("解绑成功！");
        		//重载表格
        		 $("#workerGrid").datagrid("load",{
                     fullname:$("#workerName").val(),
                     phoneNo:$("#workerTelephone").val(),
                     city:!areaCode?$("#address").combobox("getValue"):areaCode
                 });
         	}else if(data.resultCode==2){
         		message.showMessage("解绑失败，请稍后重试！");
      		 }else if(data.resultCode==-1){
      			 message.showOutMessage();
      		 }
        },
        error:function(){
       	   message.showMessage(message.messageText.networkError);
          }
    });
}
/*---------------------管理员表格-==============*/
//查看管理人员信息
function showUser(id){
    $.ajax({
     url:header+"/web/user/"+currentUserId+"/load",
     type:"post",
     dataType:"json",
     data:{
    	 userId:id
     },
     success:function(data){
    	 if(data.resultCode==1){
             $("#checkUserFullName").text(data.user.fullname);
             $("#checkUserTelephone").text(data.user.mobileNo);
             $("#checkUserEmail").text(data.user.email);
             $("#checkUserRole").text(data.user.role.roleName);
             $("#checkUserAddress").text(data.user.areaCode+"服务部");
             $("#checkUserName").text(data.user.username);
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
//修改管理人员信息
function editUser(id){
	 $.ajax({
	     url:header+"/web/user/"+currentUserId+"/load",
	     type:"post",
	     dataType:"json",
	     data:{
	    	 userId:id
	     },
	     success:function(data){
	    	 if(data.resultCode==1){
	    		 $(".editUserId").val(data.user.id);
	             $("#editUserFullName").val(data.user.fullname);
	             $("#editUserTelephone").val(data.user.mobileNo);
	             $("#oldUserMobileNo").val(data.user.mobileNo);
	             $("#editUserEmail").val(data.user.email);
	             $("#oldUserEmail").val(data.user.email);
	             //加载已有角色
	             $("#editUserRoleType").combobox("reload",header+"/web/role/"+currentUserId+"/list-all");
	             $("#editUserRoleType").combobox("setValue",data.user.roleId);
	             $("#editUserAddress").combobox('setValue',data.user.areaCode);
	             $("#editUserName").val(data.user.username);
	             $("#oldUserName").val(data.user.username);
	             //$("#editUserPassword").val(data.user.password);
	             //$("#editUserConfirmPassword").val(data.user.password);
	             $("#editUserPhone").val(data.user.telNo);
	             $("#editUserPanel").window("open");
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
//删除管理人员
function deleteUser(id,index){
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
/*--------------------------------------------------------角色相关点击事件============================*/
//加载树数据
function loadTreeData(){
	$.ajax({
		url:header+"/web/role/load-all-permission",
        type:"get",
        success:function(data){
       	 	treeData=eval("("+data+")");
        }
	});
}
//修改角色信息
function editRole(index){
	$("#roleGrid").datagrid("selectRow",index);
	 var row=$("#roleGrid").datagrid("getSelected");
	 $("#editRoleName").val(row.roleName);
	 $("#editRoleId").val(row.id);
	 //将已有的权限做成hash表，如果存在了，在组成树数据的时候checkbox设置为勾选
	 //console.log(row);
	 var permission=row.permissions;//一个数组
	 //清空已有权限的hsah表，因为每个人的都不一样，加载的时候要重新初始化
	 hasPermission={};
	 for(var i=0;i<permission.length;i++){
		 hasPermission[permission[i]]=true;//设置为true，代表这个权限有
	 }
	 //加载树数据
	 $("#editPermissionTree").tree("loadData",treeData);
	 //打开编辑面板
	 $("#editRolePanel").window("open");
}
//删除角色
function deleteRole(id,index){
    $.messager.confirm('确认','确认删除?',function(ok){
        if(ok){
             $.ajax({
	             url:header+'/web/role/'+currentUserId+"/remove",
	             type:"post",
	             dataType:"json",
	             data:{
	            	 roleId:id 
	             },
	             success:function(data){
	            	 if(data.resultCode==1){
	            		 message.showMessage(message.messageText.deleteSuccess);
	                     $('#roleGrid').datagrid('deleteRow',index);
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
/*-------------------------角色表点击事件==================*/
$(document).ready(function(){
	//删除遮盖层
	$("#load").remove();
	//转化权限数组
	permissions=eval("("+permissions+")");
	//设置高亮菜单
	$("a[menu_type='workerMgr']").addClass("active");
	//加载树数据
	loadTreeData();
	//tab项
	$("#tab").tabs({
		width:"100%",
		height:580
     });
	$("#tabUser").tabs({
		width:"100%",
		height:430			
	});
	/*-----------------外勤人员管理界面相关代码============================*/
	//城市下拉框
    $("#address").combobox({
        width:120,
        editable:false,
        panelHeight:180,
        onSelect:function(record){
        	$("#workerGrid").datagrid("load",{
                fullname:"",
                phoneNo:"",
                city:record.value
            });
        }
    });
    //搜索按钮事件
    $("#searchWorker").click(function(){
            $("#workerGrid").datagrid("load",{
                fullname:$("#workerName").val(),
                phoneNo:$("#workerTelephone").val(),
                city:!areaCode?$("#address").combobox("getValue"):areaCode
            });
     });
    //外勤人员表格
     $("#workerGrid").datagrid({
         url:header+"/web/fieldworker/"+currentUserId+"/find",
         noheader:true,
         minimizable:false,
         collapsible:false,
         maximizable:false,
         pageSize:15,
         striped: true,
         toolbar:"#workerTb",
         fitColumns:true,
         pagination:true,
         singleSelect:true,
         queryParams:{
        	 fullname:"",
             phoneNo:"",
             city:areaCode
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
         /*onLoadSuccess:function(data){
             if(data.total == 0){
                $("<tr style='height: 30px'><td style='width: 1023px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable"));
             }
         },*/
         columns:[[
             {field:'id',title:'编号',hidden:true},
             {field:'workerNo',title:'员工编号',width:120,align:"center"},
             {field:'fullname',title:'姓名',width:120,align:"center"},
             {field:'department',title:'部门',width:150,align:"center",formatter:function(value,row,index){
            	 return row.department.departmentName;
             }},
             {field:'city',title:'所属城市',width:180,align:"center",formatter:function(value,row,index){
            	 return row.department.city;
             }},
             {field:'phoneNo',title:'号码',width:140,align:"center",formatter:function(value,row,index){
            	  if(row.device){
            		  return row.device.phoneNo;
            	  }else{
            		  return "未绑定";
            	  }
             }},
             {field:'meid',title:'所绑设备号',width:120,align:"center",formatter:function(value,row,index){
            	  if(row.device){
            		  return row.device.meid;
            	  }else{
            		  return "未绑定";
            	  }
             }},
             {field:'action',title:'操作',width:200,align:"center",formatter: function(value,row,index){
            	 if(row.device){
            		 if(($.inArray("fieldworker:edit",permissions)!=-1&&$.inArray("fieldworker:device",permissions)!=-1)||$.inArray("*:*",permissions)!=-1){
            			 return '<a href="javascript:void(0)" onclick="showWorker('+index+')">查看</a> <a href="javascript:void(0)" onclick="editWorker('+row.id+','+row.workerNo+',\''+row.fullname+'\')">修改</a> '+
                		 '<a href="javascript:void(0)" onclick="bindRow('+row.id+')">绑定</a> <a href="javascript:void(0)" onclick="unBindRow('+row.id+')">解绑</a>';
            		 }else if($.inArray("fieldworker:device",permissions)!=-1&&$.inArray("fieldworker:edit",permissions)==-1){
            			 return '<a href="javascript:void(0)" onclick="showWorker('+index+')">查看</a> '+
                		 '<a href="javascript:void(0)" onclick="bindRow('+row.id+')">绑定</a> <a href="javascript:void(0)" onclick="unBindRow('+row.id+')">解绑</a>';
            		 }else if($.inArray("fieldworker:device",permissions)==-1&&$.inArray("fieldworker:edit",permissions)!=-1){
            			 return '<a href="javascript:void(0)" onclick="showWorker('+index+')">查看</a> <a href="javascript:void(0)" onclick="editWorker('+row.id+','+row.workerNo+',\''+row.fullname+'\')">修改</a>';
                		
            		 }else{
            			 return '<a href="javascript:void(0)" onclick="showWorker('+index+')">查看</a>'; 
            		 }
            	 }else{
            		 if(($.inArray("fieldworker:edit",permissions)!=-1&&$.inArray("fieldworker:device",permissions)!=-1)||$.inArray("*:*",permissions)!=-1){
            			 return '<a href="javascript:void(0)" onclick="showWorker('+index+')">查看</a> <a href="javascript:void(0)" onclick="editWorker('+row.id+','+row.workerNo+',\''+row.fullname+'\')">修改</a> '+
                		 '<a href="javascript:void(0)" onclick="bindRow('+row.id+')">绑定</a>';
            		 }else if($.inArray("fieldworker:device",permissions)!=-1&&$.inArray("fieldworker:edit",permissions)==-1){
            			 return '<a href="javascript:void(0)" onclick="showWorker('+index+')">查看</a> '+
                		 '<a href="javascript:void(0)" onclick="bindRow('+row.id+')">绑定</a>';
            		 }else if($.inArray("fieldworker:device",permissions)==-1&&$.inArray("fieldworker:edit",permissions)!=-1){
            			 return '<a href="javascript:void(0)" onclick="showWorker('+index+')">查看</a> <a href="javascript:void(0)" onclick="editWorker('+row.id+','+row.workerNo+',\''+row.fullname+'\')">修改</a>';            			 
            		 }else{
            			 return '<a href="javascript:void(0)" onclick="showWorker('+index+')">查看</a>'; 
            		 }
            		
            	 }
             }}
        ]]
     });
     //设备列表
     $("#bindGrid").datagrid({
    	 url:header+"/web/device/"+currentUserId+"/find-idle",
         noheader:true,
         minimizable:false,
         collapsible:false,
         maximizable:false,
         pageSize:15,
         striped: true,
         pagination:true,
         fitColumns:true,
         singleSelect:true,
         queryParams:{
        	 loadFlag:0
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
         onBeforeLoad:function(params){
        	 if(params.loadFlag==0){
        		 //如果传入的加载数据标志为0则返回false不加载数据
        		 return false;
        	 }
         },
         onLoadError:function(){
         	message.showMessage(message.messageText.networkError);
         },
         onLoadSuccess:function(data){
        	 $("#bindWorkerPanel").window("open");
         },
         columns:[[
                   {field:'id',title:'编号',width:40,align:"center"},
                   {field:'meid',title:'MEID号',width:100,align:"center"},
                   {field:'phoneNo',title:'手机号',width:120,align:"center"},
                   {field:'model',title:'手机型号',width:120,align:"center"},
                   {field:'currentAmount',title:'预存金额',width:80,align:"center"},
                   {field:'consumeCategory',title:'手机套餐',width:180,align:"center"},
                   {field:'areaCode',title:'所属区域',width:100,align:"center"},
                   {field:'status',title:'手机状态',width:80,align:"center",formatter:function(value,row,inde){
                   		return message.entry[value];
                   }},
                   {field:'action',title:'操作',width:50,align:"center",formatter: function(value,row,index){
                       return '<a href="javascript:void(0)" onclick="selectRow('+row.id+')">选择</a>';
                   }}
        ]]
     });
     //查看相关代码
    $("#checkWorkerDetail").window({
        width:450,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '查看员工信息',
        closed:true,
        modal:true,
        zIndex:9999
    });    
      //增加修改面板
    $("#editWorkerPanel").window({
        width:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '修改',
        closable:false,
        closed:true,
        modal:true,
        zIndex:9999
    });
    //绑定设备面板
    $("#bindWorkerPanel").window({
    	width:800,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '绑定设备',
        closed:true,
        modal:true,
        zIndex:9999
    });
     //密码验证
    $("#password").validatebox({
        required:true,
        validType:"length[6,20]"
    });
    //确认密码验证
    $("#confirmPassword").validatebox({
        required:true,
        validType:"equals['#password']"
    });
    //保存和取消按钮
    $('#editWorkerSubmitForm').form({
        onSubmit: function(){
           return $("#editWorkerSubmitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
       	 	if(data.resultCode==1){
       	 		message.showMessage(message.messageText.saveSuccess);
      			 $("#editWorkerPanel").window("close");
      		 }else if(data.resultCode==2){
      			message.showMessage(message.messageText.saveError);
      		 }else if(data.resultCode==-1){
      			 message.showOutMessage();
      		 }
        }
    });
    //取消按钮
    $("#editWorkerReset").click(function(){
         $("#editWorkerPanel").window("close");
    });
    /*------------------------------管理员管理界面相关代码=================*/
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
        	fullname:"",
        	mobileNo:"",
        	areaCode:""
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
        /*onBeforeLoad:function(params){
        	var tab = $('#tab').tabs('getSelected');
        	var index = $('#tab').tabs('getTabIndex',tab);
        	if(index!=1){
        		//如果不是第一个tab，不请求数据
        		return false;
        	}
        },*/
       /* onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width: 1023px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable"));
            }
            
        },*/
        columns:[[
            {field:'id',title:'编号',width:120,align:"center",hidden:true},
            {field:'fullname',title:'名字',width:140,align:"center"},
            {field:'mobileNo',title:'手机',width:180,align:"center"},
            {field:'email',title:'邮箱',width:240,align:"center"},
            {field:'role',title:'角色',width:150,align:"center",formatter:function(value,row,index){
            	return value.roleName;
            }},
            {field:'areaCode',title:'负责部门',width:180,align:"center",formatter: function(value,row,index){
            	return value+"服务部";
            }},
            {field:'action',title:'操作',width:140,align:"center",formatter: function(value,row,index){
            	if(($.inArray("user:edit",permissions)!=-1&&$.inArray("user:remove",permissions)!=-1)||$.inArray("*:*",permissions)!=-1){
            		 return '<a href="javascript:void(0)" onclick="showUser('+row.id+')">查看</a> '
                     +'<a href="javascript:void(0)" onclick="editUser('+row.id+')">修改</a> '
                     +'<a href="javascript:void(0)" onclick="deleteUser('+row.id+','+index+')">删除</a>';
            	}else if($.inArray("user:edit",permissions)==-1&&$.inArray("user:remove",permissions)!=-1){
            		 return '<a href="javascript:void(0)" onclick="showUser('+row.id+')">查看</a> '
                     +'<a href="javascript:void(0)" onclick="deleteUser('+row.id+','+index+')">删除</a>';
            	}else if($.inArray("user:edit",permissions)!=-1&&$.inArray("user:remove",permissions)==-1){
            		 return '<a href="javascript:void(0)" onclick="showUser('+row.id+')">查看</a> '
                     +'<a href="javascript:void(0)" onclick="editUser('+row.id+')">修改</a>';
            	}else{
            		 return '<a href="javascript:void(0)" onclick="showUser('+row.id+')">查看</a>';
            	}
            }}
        ]]
    });
    //工具栏相关代码
    $("#addBtn").click(function(){
    	//加载已有角色
        $("#addUserRoleType").combobox("reload",header+"/web/role/"+currentUserId+"/list-all");
        $("#addUserPanel").window("open");
    });
    //搜索管理人员按钮事件
    $("#searchUser").click(function(){
            $("#userGrid").datagrid("load",{
            	fullname:$("#userFullName").val(),
            	mobileNo:$("#userTelephone").val(),
            	areaCode:""
            });
        });
    //查看管理人员详细信息
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
    //增加管理人员面板
    $("#addUserPanel").window({
        width:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加',
        closed:true,
        modal:true,
        zIndex:9999
    });
    //增加管理人员验证
    $("#addUserFullName").validatebox({
        required:true,
        validType:"length[1,20]"
    });
    $("#addUserTelephone").validatebox({
        validType:"telephone&remote['"+header+"/web/user/"+currentUserId+"/check-mobileno','mobileNo']"
    });
    $("#addUserEmail").validatebox({
        validType:"email&length[1,30]&remote['"+header+"/web/user/"+currentUserId+"/check-email','email']"
    });
    $("#addUserName").validatebox({
        required:true,
        validType:"length[1,20]&remote['"+header+"/web/user/"+currentUserId+"/check-username','username']"
    });
    $("#addUserPassword").validatebox({
        required:true,
        validType:"length[6,20]"
    });
    $("#addUserConfirmPassword").validatebox({
        required:true,
        validType:"equals['#addUserPassword']"
    });
    $("#addUserPhone").validatebox({
        validType:"phone&length[1,20]"
    });
    //combobox控件
    $("#addUserRoleType").combobox({
        width:185,
        height:35,
        panelHeight:80,
        editable:false,
        valueField:'id',  
        onLoadSuccess:function(){
        	var data=$(this).combobox("getData");
        	if(data.length!=0){
        		$(this).combobox("setValue",data[0]["id"]);
        	}
        },
		textField:'roleName',
        required:true
    });
    $("#addUserAddress").combobox({
        width:185,
        editable:false,
        panelHeight:80,
        required:true
    })
    //增加管理人员保存和取消按钮
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
                	fullname:$("#userFullName").val(),
                	mobileNo:$("#userTelephone").val(),
                	areaCode:""
                });
                //清空填写的记录
                $("#addSubmitForm .inputBox").val("");
                $("#addUserPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
    });
    $("#addUserReset").click(function(){
        $("#addUserPanel").window("close");
    });
    //修改管理人员面板
    $("#editUserPanel").window({
        width:500,
        height:480,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '修改',
        //closable:true,
        closed:true,
        modal:true,
        zIndex:9999
    });
    //修改管理人员验证
    $("#editUserFullName").validatebox({
        required:true,
        validType:"length[1,20]"
    });
    $("#editUserTelephone").validatebox({
        validType:"telephone&hasMobileNo['#oldUserMobileNo']"
    });
    $("#editUserEmail").validatebox({
        validType:"email&length[1,30]&hasEmail['#oldUserEmail']"
    });
    $("#editUserName").validatebox({
        required:true,
        validType:"length[1,20]&hasUserName['#oldUserName']"
    });
    $("#editUserPassword").validatebox({
        required:true,
        validType:"length[6,20]"
    });
    $("#editUserConfirmPassword").validatebox({
        required:true,
        validType:"equals['#editUserPassword']"
    });
    $("#editUserPhone").validatebox({
        validType:"phone&length[1,20]"
    });
    //修改管理人员使用的combobox
    $("#editUserStatus").combobox({
        width:185,
        height:35,
        panelHeight:80,
        required:true
    });
    $("#editUserRoleType").combobox({
        width:185,
        height:35,
        panelHeight:80,
        editable:false,
        valueField:'id',  
		textField:'roleName',
        required:true
    });
    $("#editUserAddress").combobox({
        width:185,
        editable:false,
        panelHeight:80,
        required:true
    })
    //修改管理人员保存和取消按钮
    $('#editUserSubmitForm').form({
        onSubmit: function(){
            return $("#editUserSubmitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#userGrid").datagrid("load",{
                	fullname:$("#userFullName").val(),
                	mobileNo:$("#userTelephone").val(),
                	areaCode:""
                });
                $("#editUserPanel").window("close");
            }else if(data.resultCode==2){
            	 message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
    });
    //修改管理人员保存和取消按钮
    $('#editUserPasswordForm').form({
        onSubmit: function(){
            return $("#editUserPasswordForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#userGrid").datagrid("load",{
                	fullname:$("#userFullName").val(),
                	mobileNo:$("#userTelephone").val(),
                	areaCode:""
                });
                $("#editUserPanel").window("close");
            }else if(data.resultCode==2){
            	 message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
    });
    $(".editUserReset").click(function(){
        $("#editUserPanel").window("close");
    });
    /*------------------------------角色管理界面相关代码=================*/
    //表格
    $("#roleGrid").datagrid({
        url:header+'/web/role/'+currentUserId+"/find",
        noheader:true,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        toolbar:"#roleTb",
        pagination:true,
        fitColumns:true,
        pageSize:15,
        striped: true,
        singleSelect:true,
        queryParams:{
        	roleName:""
        },
        loadFilter:function(data){
            if(data.resultCode==2){
        		message.showMessage(message.messageText.loadError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }else{
     			 return data;
     		 }
        },
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        },
        /*onBeforeLoad:function(params){
        	var tab = $('#tab').tabs('getSelected');
        	var index = $('#tab').tabs('getTabIndex',tab);
        	if(index!=2){
        		//如果不是第一个tab，不请求数据
        		return false;
        	}
        },
        onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width: 1023px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable"));
            }
            
        },*/
        columns:[[
            {field:'id',title:'编号',width:120,align:"center",hidden:true},
            {field:'roleName',title:'角色名',width:80,align:"center"},
            {field:'permissions',title:'权限',width:400,align:"center",formatter:function(value,row,index){
                 var str="";
                 for(var i=0;i<value.length;i++){
                	 str+=message.roleText[value[i]]+",";
                 }
                 return str.substring(0,str.length-1);
            }},
            {field:'createTime',title:'操作',width:80,align:"center",formatter: function(value,row,index){
            	if(($.inArray("role:edit",permissions)!=-1&&$.inArray("role:remove",permissions)!=-1)||$.inArray("*:*",permissions)!=-1){
            		return '<a href="javascript:void(0)" onclick="editRole('+index+')">修改</a> <a href="javascript:void(0)" onclick="deleteRole('+row.id+','+index+')">删除</a>';
            	}else if($.inArray("role:edit",permissions)==-1&&$.inArray("role:remove",permissions)!=-1){
            		return '<a href="javascript:void(0)" onclick="deleteRole('+row.id+','+index+')">删除</a>';
            	}else if($.inArray("role:edit",permissions)!=-1&&$.inArray("role:remove",permissions)==-1){
            		return '<a href="javascript:void(0)" onclick="editRole('+index+')">修改</a>';
            	}else{
            		return '';
            	}
                
            }}
        ]]
    });
    //工具栏相关代码
    $("#addRoleBtn").click(function(){
    	//加载树
    	$("#addPermissionTree").tree("loadData",treeData);
        $("#addRolePanel").window("open");
    });
    //搜索管理人员按钮事件
    $("#searchRole").click(function(){
        $("#roleGrid").datagrid("load",{
        	roleName:$("#roleName").val()
        });
    });
    //增加角色面板
    $("#addRolePanel").window({
        width:500,
        height:520,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加',
        closed:true,
        modal:true,
        zIndex:9999
    });
    ////转换数据成树能解析的数据格式
    function addConver(data){
    	var treeNodes=[];
        for(var obj in data){
        	var treeNode={};
        	if(obj!="checkin"&&obj!="checkinSetting"){
        		treeNode.text=message.roleText[obj];
            	treeNode.state="closed";
            	treeNode.children=[];
            	for(var i=0;i<data[obj].length;i++){
            		treeNode.children.push({
            			"text":message.roleText[obj+":"+data[obj][i]],
            			"id":obj+":"+data[obj][i]
            		});
            	}
            	//保存树节点
            	treeNodes.push(treeNode);
        	}
        }
        //将签到整合到一起
        treeNodes.push({
        	"text":"签到管理",
        	"state":"closed",
        	"children":[{
        		"id":"checkin:list",
        		"text":"查看签到"
        	},{
        		"text":"设置签到",
        		"id":"checkinSetting:edit"
        	}]
        });
        
        return treeNodes;
    }
   //权限树
	$("#addPermissionTree").tree({
       //url:header+"/web/role/load-all-permission",
       checkbox:true,
       loadFilter:function(data){
           return addConver(data);
       }
    });
	//增加角色表单的验证
	$("#addRoleName").validatebox({
        required:true,
        validType:"length[0,20]"
    });
    //增加角色的提交表单
	$('#addRoleForm').form({
        onSubmit: function(){
        	var nodes=$("#addPermissionTree").tree("getChecked");
        	if(nodes.length==0){
        		//如果没有选择权限，给予最大权限
        		$("#addPermission").val("*:*");
        	}else{
            	var str="";
        		for(var i=0;i<nodes.length;i++){
            		if(nodes[i].id){
            			str+=nodes[i].id+",";
            		}
            	}
            	$("#addPermission").val(str.substring(0, str.length-1));
        	}
        	//验证表单
        	return $("#addRoleForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#roleGrid").datagrid("load",{
                	roleName:$("#roleName").val(),
                });
                //清空填写的记录
                $("#addRoleForm .inputBox").val("");
                $("#addRolePanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
    });
	 //修改角色面板
    $("#editRolePanel").window({
        width:500,
        height:520,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加',
        closed:true,
        modal:true,
        zIndex:9999
    });
    //转换数据成树能解析的数据格式
    function editConver(data){
    	var treeNodes=[];
        for(var obj in data){
        	var treeNode={};
        	if(obj!="checkin"&&obj!="checkinSetting"){
        		treeNode.text=message.roleText[obj];
            	treeNode.state="closed";
            	treeNode.children=[];
            	for(var i=0;i<data[obj].length;i++){
            		//如果权限中存在*:*则全部选中
            		if(hasPermission["*:*"]){
            			treeNode.children.push({
                			"text":message.roleText[obj+":"+data[obj][i]],
                			"id":obj+":"+data[obj][i],
                			"checked":true
                		});
            		}else{
            			treeNode.children.push({
                			"text":message.roleText[obj+":"+data[obj][i]],
                			"id":obj+":"+data[obj][i],
                			"checked":hasPermission[obj+":"+data[obj][i]]?true:false
                		});
            		}
            	}
            	//保存树节点
            	treeNodes.push(treeNode);
        	}
        }
        //将签到整合到一起
        if(hasPermission["*:*"]){
        	treeNodes.push({
            	"text":"签到管理",
            	"state":"closed",
            	"children":[{
            		"id":"checkin:list",
            		"text":"查看签到",
            		"checked":true
            	},{
            		"text":"设置签到",
            		"id":"checkinSetting:edit",
            		"checked":true
            	}]
            });
		}else{
			treeNodes.push({
            	"text":"签到管理",
            	"state":"closed",
            	"children":[{
            		"id":"checkin:list",
            		"text":"查看签到",
            		"checked":hasPermission["checkin:list"]?true:false
            	},{
            		"text":"设置签到",
            		"id":"checkinSetting:edit",
            		"checked":hasPermission["checkinSetting:edit"]?true:false
            	}]
            });
		}
        //返回数据
        return treeNodes;
    }
   //权限树
	$("#editPermissionTree").tree({
       //url:header+"/web/role/load-all-permission",
       checkbox:true,
       loadFilter:function(data){
           return editConver(data);
       }
    });
	//增加角色表单的验证
	$("#editRoleName").validatebox({
        required:true,
        editable:false,
        validType:"length[0,20]"
    });
    //增加角色的提交表单
	$('#editRoleForm').form({
        onSubmit: function(){
        	var nodes=$("#editPermissionTree").tree("getChecked");
        	if(nodes.length==0){
        		//如果没有选择权限，给予最大权限
        		$("#editPermission").val("*:*");
        	}else{
	        	var str="";
	        	for(var i=0;i<nodes.length;i++){
	        		if(nodes[i].id){
	        			str+=nodes[i].id+",";
	        		}
	        	}
	        	$("#editPermission").val(str.substring(0, str.length-1));
        	}
        	
        	return $("#editRoleForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#roleGrid").datagrid("load",{
                	roleName:$("#roleName").val(),
                });
                //清空填写的记录
                $("#editRoleForm .inputBox").val("");
                $("#editRolePanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
    });
	 $("#addRoleReset").click(function(){
        $("#addRolePanel").window("close");
	 });
	 $("#editRoleReset").click(function(){
        $("#editRolePanel").window("close");
	 });
});