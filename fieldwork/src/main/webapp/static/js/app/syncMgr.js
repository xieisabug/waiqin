//删除费用项目
function deleteCost(id,index){
    $.messager.confirm('确认','确认删除?',function(ok){
        if(ok){
             $.ajax({
	             url:header+'/web/expense-item/'+currentUserId+"/remove",
	             type:"post",
	             dataType:"json",
	             data:{
	            	 itemId:id 
	             },
	             success:function(data){
	            	 if(data.resultCode==1){
	            		 message.showMessage(message.messageText.deleteSuccess);
	                     $('#syncTable0').datagrid('deleteRow',index);
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
//删除部门
function deleteDepartment(id,index){
    $.messager.confirm('确认','确认删除?',function(ok){
        if(ok){
             $.ajax({
	             url:header+'/web/department/'+currentUserId+"/remove",
	             type:"post",
	             dataType:"json",
	             data:{
	            	 departmentId:id 
	             },
	             success:function(data){
	            	 if(data.resultCode==1){
	            		 message.showMessage(message.messageText.deleteSuccess);
	                     $('#syncTable1').datagrid('deleteRow',index);
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
//删除税务分局
function deleteBranch(id,index){
    $.messager.confirm('确认','确认删除?',function(ok){
        if(ok){
             $.ajax({
	             url:header+'/web/revenue/'+currentUserId+"/remove",
	             type:"post",
	             dataType:"json",
	             data:{
	            	 revenueId:id 
	             },
	             success:function(data){
	            	 if(data.resultCode==1){
	            		 message.showMessage(message.messageText.deleteSuccess);
	                     $('#syncTable2').datagrid('deleteRow',index);
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
//删除问题类别
function deleteType(id,index){
    $.messager.confirm('确认','确认删除?',function(ok){
        if(ok){
             $.ajax({
	             url:header+'/web/problem-type/'+currentUserId+"/remove",
	             type:"post",
	             dataType:"json",
	             data:{
	            	 problemTypeId:id 
	             },
	             success:function(data){
	            	 if(data.resultCode==1){
	            		 message.showMessage(message.messageText.deleteSuccess);
	                     $('#syncTable4').datagrid('deleteRow',index);
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
//删除问题类型
function deleteCategory(id,index){
    $.messager.confirm('确认','确认删除?',function(ok){
        if(ok){
             $.ajax({
	             url:header+'/web/problem-category/'+currentUserId+"/remove",
	             type:"post",
	             dataType:"json",
	             data:{
	            	 problemCategoryId:id 
	             },
	             success:function(data){
	            	 if(data.resultCode==1){
	            		 message.showMessage(message.messageText.deleteSuccess);
	                     $('#syncTable3').datagrid('deleteRow',index);
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
//删除产品类型
function deleteProduct(id,index){
    $.messager.confirm('确认','确认删除?',function(ok){
        if(ok){
             $.ajax({
	             url:header+'/web/product/'+currentUserId+"/remove",
	             type:"post",
	             dataType:"json",
	             data:{
	            	 productId:id 
	             },
	             success:function(data){
	            	 if(data.resultCode==1){
	            		 message.showMessage(message.messageText.deleteSuccess);
	                     $('#syncTable5').datagrid('deleteRow',index);
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
	//转化权限数组
    permissions=eval("("+permissions+")");
	//设置高亮菜单
	$("a[menu_type='syncMgr']").addClass("active");
	 //tab
	$("#tab").tabs({
		width:740,
		height:570
     });
	
    //初始化表格
    //项目费用表格
    $("#syncTable0").datagrid({
    	  url:header+"/web/expense-item/"+currentUserId+"/find",
          noheader:true,
          minimizable:false,
          collapsible:false,
          maximizable:false,
          toolbar:"#costTb",
          pagination:true,
          fitColumns:true,
          pageSize:15,
          striped: true,
          singleSelect:true,
          queryParams:{
          	searchCotent:$("#costSearch").val(),
          	loadFlag:0
          },
          columns:[
            [
                {field:'id', title:'ID',width:200, align:"center"},
                {field:'name', title:'项目名称', width:340, align:"center"},
                {field:'opt',title:'操作',align:"center",width:50,formatter: function(value,row,index){
                	if($.inArray("expenseItem:remove",permissions)!=-1||$.inArray("*:*",permissions)!=-1){
                		 return '<a href="javascript:void(0)" onclick="deleteCost('+row.id+','+index+')">删除</a>';
                	}else{
                		return "";
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
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        }/*,
        onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width:800px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable:eq(0)"));
            }
        }*/
    });
    //部门维护表
    $("#syncTable1").datagrid({
    	  url:header+"/web/department/"+currentUserId+"/find",
          noheader:true,
          minimizable:false,
          collapsible:false,
          maximizable:false,
          height:"auto",
          toolbar:"#departmentTb",
          pagination:true,
          pageSize:15,
          fitColumns:true,
          striped: true,
          singleSelect:true,
          queryParams:{
        	  searchCotent:$("#departmentSearch").val(),
        	  loadFlag:0
          },
          columns:[
            [
                {field:'id', title:'ID',width:100,align:"center"},
                {field:'departmentCode', title:'部门代码', width:115, align:"center"},
                {field:'departmentName', title:'部门名称', width:180, align:"center"},
                {field:'city', title:'所属城市', width:180, align:"center"},
                {field:'opt',title:'操作',align:"center",width:50,formatter: function(value,row,index){
                	if($.inArray("department:remove",permissions)!=-1||$.inArray("*:*",permissions)!=-1){
                		 return '<a href="javascript:void(0)" onclick="deleteDepartment('+row.id+','+index+')">删除</a>';
                	}else{
                		return "";
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
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        }/*,
        onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width:800px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable:eq(1)"));
            }
        }*/
    });
    //税务分局维护表
    $("#syncTable2").datagrid({
    	  url:header+"/web/revenue/"+currentUserId+"/find",
          noheader:true,
          minimizable:false,
          collapsible:false,
          maximizable:false,
          toolbar:"#branchTb",
          pagination:true,
          fitColumns:true,
          pageSize:15,
          striped: true,
          singleSelect:true,
          queryParams:{
        	  searchCotent:$("#branchTitle").val(),
        	  loadFlag:0
          },
          columns:[
            [
                {field:'id', title:'ID',width:100, align:"center"},
                {field:'revenueCode', title:'分局代码', width:120, align:"center"},
                {field:'revenueName', title:'分局名称', width:180, align:"center"},
                {field:'cityCode', title:'所属城市', width:180, align:"center"},
                {field:'opt',title:'操作',align:"center",width:50,formatter: function(value,row,index){
                	if($.inArray("revenue:remove",permissions)!=-1||$.inArray("*:*",permissions)!=-1){
                		return '<a href="javascript:void(0)" onclick="deleteBranch('+row.id+','+index+')">删除</a>';
                	}else{
                		return "";
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
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        }/*,
        onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width:800px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable:eq(2)"));
            }
        }*/
    });
    //问题类别表
    $("#syncTable3").datagrid({
    	  url:header+"/web/problem-category/"+currentUserId+"/find",
          noheader:true,
          minimizable:false,
          collapsible:false,
          maximizable:false,
          toolbar:"#categoryTb",
          pagination:true,
          pageSize:15,
          fitColumns:true,
          striped: true,
          singleSelect:true,
          queryParams:{
        	  searchCotent:$("#categorySearch").val(),
        	  loadFlag:0
          },
          columns:[
            [
                {field:'id', title:'ID',width:40,align:"center"},
                {field:'name', title:'类别名称', width:150, align:"center"},
                {field:'productId', title:'产品ID', width:150, align:"center"},
                {field:'productName', title:'产品名称', width:150, align:"center"},
                {field:'action',title:'操作',align:"center",width:50,formatter: function(value,row,index){
                	if($.inArray("problemCategory:remove",permissions)!=-1||$.inArray("*:*",permissions)!=-1){
                		return '<a href="javascript:void(0)" onclick="deleteCategory('+row.id+','+index+')">删除</a>';
                	}else{
                		return "";
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
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        }/*,
        onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width:800px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable:eq(3)"));
            }
        }*/
    });
    //问题类型表
    $("#syncTable4").datagrid({
    	url:header+"/web/problem-type/"+currentUserId+"/find",
          noheader:true,
          minimizable:false,
          collapsible:false,
          maximizable:false,
          toolbar:"#typeTb",
          pagination:true,
          fitColumns:true,
          pageSize:15,
          striped: true,
          singleSelect:true,
          queryParams:{
        	  searchCotent:$("#typeSearch").val(),
        	  loadFlag:0
          },
          columns:[
            [
                {field:'id', title:'ID',width:40, align:"center"},
                {field:'name', title:'类型名称', width:180, align:"center"},
                {field:'categoryId', title:'所属类别id', width:50, align:"center",formatter:function(value,row,index){
                	return row.category.id;
                }},
                {field:'categoryName', title:'所属类别名称', width:180, align:"center",formatter:function(value,row,index){
                	return row.category.name;
                }},
                {field:'opt',title:'操作',align:"center",width:50,formatter: function(value,row,index){
                	if($.inArray("problemType:remove",permissions)!=-1||$.inArray("*:*",permissions)!=-1){
                		return '<a href="javascript:void(0)" onclick="deleteType('+row.id+','+index+')">删除</a>';
                	}else{
                		return "";
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
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        }/*,
        onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width:800px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable:eq(4)"));
            }
        }*/
    });
   //产品分类列表
    $("#syncTable5").datagrid({
    	url:header+"/web/product/"+currentUserId+"/find",
          noheader:true,
          minimizable:false,
          collapsible:false,
          maximizable:false,
          toolbar:"#productTb",
          pagination:true,
          fitColumns:true,
          pageSize:15,
          striped: true,
          singleSelect:true,
          queryParams:{
        	  loadFlag:0
          },
          columns:[
            [
                {field:'id', title:'ID',width:40, align:"center"},
                {field:'name', title:'产品名称', width:180, align:"center"},
                {field:'opt',title:'操作',align:"center",width:50,formatter: function(value,row,index){
                	if($.inArray("product:remove",permissions)!=-1||$.inArray("*:*",permissions)!=-1){
                		return '<a href="javascript:void(0)" onclick="deleteProduct('+row.id+','+index+')">删除</a>';
                	}else{
                		return "";
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
        onLoadError:function(){
        	message.showMessage(message.messageText.networkError);
        }/*,
        onLoadSuccess:function(data){
            if(data.total == 0){
                $("<tr style='height: 30px'><td style='width:800px;text-align: center'>没有记录</td></tr>").appendTo($(".datagrid-btable:eq(4)"));
            }
        }*/
    });
    //初始化面板
    $("#costAddPanel").window({
    	width:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加费用项目',
        closed:true,
        modal:true,
        zIndex:9999
    });
    $("#departmentAddPanel").window({
        width:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加部门',
        closed:true,
        modal:true,
        zIndex:9999
    });
    $("#branchAddPanel").window({
        width:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加税务分局',
        closed:true,
        modal:true,
        zIndex:9999
    });
    $("#typeAddPanel").window({
        width:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加问题类型',
        closed:true,
        modal:true,
        zIndex:9999
    });
    $("#categoryAddPanel").window({
        width:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加问题类别',
        closed:true,
        modal:true,
        zIndex:9999
    });
    $("#productAddPanel").window({
        width:500,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加产品分类',
        closed:true,
        modal:true,
        zIndex:9999
    });
   
    //增加按钮
    $("#costAddBtn").click(function(){
    	$("#costAddPanel").window("open");
    });
    $("#departmentAddBtn").click(function(){
    	$("#departmentAddPanel").window("open");
    });
    $("#branchAddBtn").click(function(){
    	$("#branchAddPanel").window("open");
    });
    $("#typeAddBtn").click(function(){
    	//加载问题类别数据
    	$("#typeCategory").combobox("reload",header+"/web/problem-category/"+currentUserId+"/load");
    	$("#typeAddPanel").window("open");
    });
    $("#categoryAddBtn").click(function(){
    	$("#relatedProduct").combobox("reload",header+"/web/product/"+currentUserId+"/load");
    	$("#categoryAddPanel").window("open");
    });
    $("#productAddBtn").click(function(){
    	$("#productAddPanel").window("open");
    });
    //关闭按钮
    $("#costAddReset").click(function(){
    	 $("#costAddPanel").window("close");
    });
    $("#departmentAddReset").click(function(){
   	 	$("#departmentAddPanel").window("close");
   });
    $("#branchAddReset").click(function(){
   	 	$("#branchAddPanel").window("close");
   });
    $("#typeAddReset").click(function(){
   	 	$("#typeAddPanel").window("close");
   });
    $("#categoryAddReset").click(function(){
   	 	$("#categoryAddPanel").window("close");
   });
    $("#productAddReset").click(function(){
   	 	$("#productAddPanel").window("close");
   });
   
	
	//添加项目维护表单
	$("#costId").validatebox({
		required:true,
		validType:"length[1,9]"
	});
	$("#costContent").validatebox({
		required:true,
		validType:"length[1,50]"
	});
	$("#costSubmitForm").form({
		onSubmit: function(){
            return $("#costSubmitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#syncTable0").datagrid("load",{
                	searchContent:$("#costSearch").val()
                });
                $("#costSubmitForm .inputBox").val("");
                $("#costAddPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
	});
	//添加部门表单
	$("#departmentId").validatebox({
		required:true,
		validType:"length[1,9]"
	});
	$("#departmentCode").validatebox({
		required:true,
		validType:"length[1,10]"
	});
	$("#departmentName").validatebox({
		required:true,
		validType:"length[1,64]"
	});
	$("#departmentCity").combobox({
		required:true,
		width:180
	});
	$("#departmentSubmitForm").form({
		onSubmit: function(){
            return $("#departmentSubmitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#syncTable1").datagrid("load",{
                	searchContent:$("#departmentSearch").val()
                });
                $("#departmentSubmitForm .inputBox").val("");
                $("#departmentAddPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
	});
	//添加税务分局表单
	$("#branchId").validatebox({
		required:true,
		validType:"length[1,9]"
	});
	$("#branchCode").validatebox({
		required:true,
		validType:"length[1,24]"
	});
	$("#branchName").validatebox({
		required:true,
		validType:"length[1,64]"
	});
	$("#branchCity").combobox({
		required:true,
        width:180
	});
	$("#branchSubmitForm").form({
		onSubmit: function(){
            return $("#branchSubmitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#syncTable2").datagrid("load",{
                	searchContent:$("#branchSearch").val()
                });
                $("#branchSubmitForm .inputBox").val("");
                $("#branchAddPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
	});
	//添加问题类别表单
	$("#categoryId").validatebox({
		required:true,
		validType:"length[1,9]"
	});
	$("#categoryContent").validatebox({
		required:true,
		validType:"length[1,24]"
	});
	$("#relatedProduct").combobox({ 
		required:true,
		//url:header+"/web/problem-category/"+currentUserId+"/load",
		width:180,
	    valueField:'id',  
		textField:'name',
		editable:false,
		onLoadSuccess:function(){
	        var data=$(this).combobox("getData");
	        if(data.length!=0){
	        	$(this).combobox("setValue",data[0]["id"]);
	        }
	    },
		method:"get",
		panelHeight:"auto"
	}); 
	
	$("#categorySubmitForm").form({
		onSubmit: function(){
            return $("#categorySubmitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#syncTable3").datagrid("load",{
                	searchContent:$("#categorySearch").val()
                });
                $("#categorySubmitForm .inputBox").val("");
                $("#categoryAddPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
	});
	//添加问题类型表单
	$("#typeId").validatebox({
		required:true,
		validType:"length[1,9]"
	});
	$("#typeContent").validatebox({
		required:true,
		validType:"length[1,24]"
	});
	$("#typeCategory").combobox({ 
		required:true,
		//url:header+"/web/problem-category/"+currentUserId+"/load",
		width:180,
	    valueField:'id',  
		textField:'name',
		editable:false,
		onLoadSuccess:function(){
	        var data=$(this).combobox("getData");
	        if(data.length!=0){
	        	$(this).combobox("setValue",data[0]["id"]);
	        }
	    },
		method:"get",
		panelHeight:"auto"
	}); 
	$("#typeSubmitForm").form({
		onSubmit: function(){
            return $("#typeSubmitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#syncTable4").datagrid("load",{
                	searchContent:$("#typeSearch").val()
                });
                $("#typeSubmitForm .inputBox").val("");
                $("#typeAddPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
	});
	//添加产品分类
	$("#productId").validatebox({
		required:true,
		validType:"length[1,9]"
	});
	$("#productName").validatebox({
		required:true,
		validType:"length[1,60]"
	});
	$("#productSubmitForm").form({
		onSubmit: function(){
            return $("#productSubmitForm").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	if(data.resultCode==1){
        		message.showMessage(message.messageText.saveSuccess);
                //刷新表格
                $("#syncTable5").datagrid("load");
                $("#productSubmitForm .inputBox").val("");
                $("#productAddPanel").window("close");
            }else if(data.resultCode==2){
            	message.showMessage(message.messageText.saveError);
     		 }else if(data.resultCode==-1){
     			 message.showOutMessage();
     		 }
        }
	});
	
	//搜索事件
	$("#costSearchBtn").click(function(){
		$("#syncTable0").datagrid("load",{
			searchContent:$("#costSearch").val()
		});
	});
	$("#departmentSearchBtn").click(function(){
		$("#syncTable1").datagrid("load",{
			searchContent:$("#departmentSearch").val()
		});
	});
	$("#branchSearchBtn").click(function(){
		$("#syncTable2").datagrid("load",{
			searchContent:$("#branchSearch").val()
		});
	});
	$("#typeSearchBtn").click(function(){
		$("#syncTable4").datagrid("load",{
			searchContent:$("#typeSearch").val()
		});
	});
	$("#categorySearchBtn").click(function(){
		$("#syncTable3").datagrid("load",{
			searchContent:$("#categorySearch").val()
		});
	});
});