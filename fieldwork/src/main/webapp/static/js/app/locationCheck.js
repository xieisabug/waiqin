var workersMarker={};//记录员工的位置信息的标签,记录下来，到时候需要使用的时候直接显示与隐藏,key员工工号
var workers={};//记录下所有的员工key员工id,当他没有经纬度的时候提示给用户
var workersIdToIndex={};//记录每个id对应的行号，在更新表格行的时候使用
var workersError=[];//记录没有登录也就是无法定位到的人
//获取对象的属性个数
function getObjCount(obj){
	var count=0;
	for(var key in obj){
		count++;
	}
	return count;
}
//通过工号去请求地址信息标注
var loadPoints=function(list){
	 $.ajax({
	        url:header+"/web/fieldworker/"+currentUserId+"/load-trackinfo",
	        dataType:"json",
	        type:"post",
	        data:{
	        	workerNo:list
	        },
	        success:function(data){
	            if(data.resultCode==1){
	            	var mark=null;
	            	$.each(data.trackInfoList,function(index,l){
	            		if(l.latestAddress){
	            			mark=new mapClass.workerMarker(new BMap.Point(l.latestLongitude,l.latestLatitude),l.fieldWorkerName,"当前位置："+l.latestAddress+";未完成工单："+l.pendingWorkOrderSize);
	            		}else{
	            			mark=new mapClass.workerMarker(new BMap.Point(l.latestLongitude,l.latestLatitude),l.fieldWorkerName,"当前位置：无位置描述;未完成工单："+l.pendingWorkOrderSize);
	            		}
	            		//此处不添加，在下面一次性添加
	            		//mapClass.ownMap.addOverlay(mark);
	            		workersMarker[l.fieldWorkerId]=mark;
	            		//更新表格行
	            		if(l.lastLocateTime){
	            			var index=workersIdToIndex[l.fieldWorkerId];
	            			/*//先根据index选中表格行，然后获取到选中行的数据
	            			$("#workersTable").datagrid("selectRow",index);
	            			var record=$("#workersTable").datagrid("getSelected");*/
	            			var time=l.lastLocateTime.substr(11,5);
	            			$("#workersTable").datagrid('updateRow',{
	            				index: workersIdToIndex[l.fieldWorkerId],
	            				row: {
	            					//device:"ddd",
	            					//id: record.id,
	            					//fullname:record.fullname,
	            					//city:record.city,
	            					time:time
	            				}
	            			});
	            		}
	            	});
	            	//取消选中的所有行，如果不取消会选中最后一个存在时间的
	            	$("#workersTable").datagrid("unselectAll");
	            	//重新添加所有的mark，上面做了全不选，会移除所有的标签
	            	for(var key in workersMarker){
	            		mapClass.ownMap.addOverlay(workersMarker[key]);
	                }
	            	//显示为获取到路径的人
	            	if(getObjCount(workers)!=getObjCount(workersMarker)){
		            		//提示没有经纬度的人
		            		for(var key in workers){
		            			if(!workersMarker[key]){
		            				workersError.push(workers[key]);
		            			}
		            		}
		            		$.messager.alert("警告",workersError.join("、")+"等人没有登录或其他原因，无法定位到");
	            	}
	            }else if(data.resultCode==2){
	            	message.showMessage(message.messageText.loadError);
	      		 }else if(data.resultCode==-1){
	      			message.showOutMessage();
	      		 }
	        },
            error:function(){
	          	   message.showMessage(message.messageText.networkError);
	        }
	    })
};
$(document).ready(function(){
	//设置高亮菜单
	$("a[menu_type='locationMgr']").addClass("active");
	
    //初始化地图
	if(areaCode==""){
		mapClass.init("湖南省",9);
	}else{
		mapClass.init(areaCode,12);
	}
    //初始化面板
    $("#checkPanel").panel({
        width:"100%",
        noheader:true,
        style:{overflow:"hidden"},
        height:640
    });
    $("#workersPanel").panel({
        width:202,
        title:"员工列表",
        height:635
    });
    //数据加载提示
    $("#loadingMsg").window({
        height:30,
        width:150,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        closable:false,
        modal:true,
        closed:true,
        content:'<div class="loadingMsgDetail">数据加载中......</div>',
        noheader:true,
        border:false
    });
    //选择地区
    $("#address").combobox({
        width:150,
        editable:false,
        panelHeight:180,
        onSelect:function(record){
        	//删除覆盖物和复选框
        	$("#workersTable").datagrid("uncheckAll");
        	var city=record.value;
        	if(city=="湖南省"){
        		$(".datagrid-btable tr").removeClass("hidden");
        		//切换地图
        		mapClass.init("湖南省",9);
        	}else{
        		//切换地图
        		mapClass.init(city,12);
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
        searcher:function(value,name){
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
        height:553,
        noheader:false,
        //singleSelect:true,
        fitColumns:true,
        loadFilter:function(data){
            //增加一个字段
        	if(data.resultCode==1){
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
        columns:[[
            {field:'device',checkbox:true},
            {field:'id',title:'编号',hidden:true},
            {field:'fullname',title:'姓名',width:100,align:"center"},
            {field:'cityName', title:'城市', hidden:true,formatter:function(value,row,index){
            	return row.department.city;
            }},
            {field:'time',title:'时间',width:100,align:"center"}
            ]],
         onLoadSuccess:function(data){
        	 //数据加载完成
    		var str=[];
    		//将每个员工的id记录下来，好获取位置信息，并且将id和fullname组成对象，在提示错误时候使用
            $.each(data.rows,function(index,c){
                str.push(c.id);
                workers[c.id]=c.fullname;
                //设置每行的时间为空
                workersIdToIndex[c.id]=index;
            });
            loadPoints(str.toString());
         },
        onCheck:function(rowIndex, rowData){
        	if(!workersMarker[rowData.id]){
            		//如果没有标签，直接提示没有登录或者当前无法获取位置
            		$.messager.alert("警告","此人没有登录或其他原因，无法定位到");
            		//不选中此行
            		$("#workersTable").datagrid("uncheckRow",rowIndex);
            	}else{
            		for(var key in workersMarker){
	            		if(key!=rowData.id){
	                		mapClass.ownMap.removeOverlay(workersMarker[key]);
	                	}else{
	                		mapClass.ownMap.addOverlay(workersMarker[key]);
	                	}
            	}
            }
        },
        onCheckAll:function(rows){
        	for(var key in workersMarker){
        		mapClass.ownMap.addOverlay(workersMarker[key]);
            }
        },
        onUncheckAll:function(rows){
        	/*for(var key in workersMarker){
        		mapClass.ownMap.removeOverlay(workersMarker[key]);
        	}*/
        	mapClass.ownMap.clearOverlays();
        },
        onUncheck:function(rowIndex,rowData){
        	//不选中此行
    		//$("#workersTable").datagrid("uncheckRow",rowIndex);
        	for(var key in workersMarker){
            	if(key==rowData.id){
            		mapClass.ownMap.removeOverlay(workersMarker[key]);
            	}
            }
        }
    });
});