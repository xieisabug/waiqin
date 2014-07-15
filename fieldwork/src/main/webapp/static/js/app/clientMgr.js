var clientMarkerError={};//记录下没有能解析的地址告诉用户，在mapClass.js中使用
//var clientMarkers={};//记录下所有的客户的覆盖物，以后做搜索用，采用hash表，公司名称做键值
var clientHasLoad=0;//已经加载了客户的数量
var clientHasId=0;//已经加载了的数据库的id
var clientProHasLoad=0;//当选择湖南的时候加载了的人数
var clientProHasId=0;//当选择了湖南的时候加载了的id
var loadLimit=100;//每次加载的客户数量
var timer=null;//延迟加载的标志，当还没有加载完成，又选择了别的城市的时候，直接删除这个timer来停止加载，并且进行新的加载
//var cityBoundary=[];//记录下行政区域覆盖物
//加载客户函数，每次加载200个
var loadClient=function(city){
	if(city==""){
		//加载湖南省数据
		$.ajax({
	         url:header+"/web/customer/"+currentUserId+"/load",
	         dataType:"json",
	         type:"post",
	         data:{
	        	idStart:clientProHasId,
	            limit:loadLimit,
	            areaCode:city
	         },
	         error:function(data){
	        	 message.showMessage(message.messageText.loadError);
	         },
	         success:function(data){
	             if(data.resultCode==1){
	            	 if(data.customers.length!=0){
	            		 clientProHasId=data.customers[data.customers.length-1].id;
	            		 clientProHasLoad+=data.customers.length;
	                     //地图上面显示
	                     $.each(data.customers,function(index,c){
	                    	 if(c.tel||c.mobile){
	                    		 if(c.tel){
	                    			if(c.mobile){
	                    				c.tel=c.tel+"/"+c.mobile;
	                    			}
	                    		 }else{
	                    			 c.tel=c.mobile;
	                    		 }
	                    	 }else{
	                    		 c.tel="无";
	                    	 }
	                         mapClass.addClientMarker(c.areaCode,c,header+"/static/images/client_blue_label.png");
	                     }); 
	                    
	                    //如果数据小于总数据，再次加载	
	                	 if(clientProHasLoad<clientCount){
	                		 timer=setTimeout(function(){
	                			 loadClient(city);//递归调用
	                		 },1000); 
	                	 }
	            	 }
	            	
	             }else if(data.resultCode==2){
	            	    message.showMessage(message.messageText.loadError);
	       		 }else if(data.resultCode==-1){
	       			message.showOutMessage();
	       		 }
	             
	             //关闭等待窗口
	             $("#loadingMsg").window("close");
	         },
	         error:function(){
	        	 //关闭等待窗口
	             $("#loadingMsg").window("close");
	        	   message.showMessage(message.messageText.networkError);
	           }
	     });
	}else{
		//加载市数据
		$.ajax({
	         url:header+"/web/customer/"+currentUserId+"/load",
	         dataType:"json",
	         type:"post",
	         data:{
	        	idStart:clientHasId,
	            limit:loadLimit,
	            areaCode:city
	         },
	         error:function(data){
	        	 message.showMessage(message.messageText.loadError);
	         },
	         success:function(data){
	             if(data.resultCode==1){
	            	 if(data.customers.length!=0){
	            		 clientHasId=data.customers[data.customers.length-1].id;
	                     clientHasLoad+=data.customers.length;
	                     //地图上面显示
	                     $.each(data.customers,function(index,c){
	                    	 if(c.tel||c.mobile){
	                    		 if(c.tel){
	                    			if(c.mobile){
	                    				c.tel=c.tel+"/"+c.mobile;
	                    			}
	                    		 }else{
	                    			 c.tel=c.mobile;
	                    		 }
	                    	 }else{
	                    		 c.tel="无";
	                    	 }
	                         mapClass.addClientMarker(c.areaCode,c,header+"/static/images/client_blue_label.png");
	                     }); 
	                    
	                   
	                   //如果数据小于总数据，再次加载	
	                	 if(clientHasLoad<clientCount){
	                		 timer=setTimeout(function(){
	                			 loadClient(city);//递归调用
	                		 },1000); 
	                	 } 
	            	 }
	            	//关闭等待窗口
	            	 setTimeout(function(){
	            		 $("#loadingMsg").window("close");
	            	 },3000);  
	             }else if(data.resultCode==2){
	            	 //关闭等待窗口
		             $("#loadingMsg").window("close");
	            	    message.showMessage(message.messageText.loadError);
	       		 }else if(data.resultCode==-1){
	       			 //关闭等待窗口
		             $("#loadingMsg").window("close");
	       			message.showOutMessage();
	       		 }
	         },
	         error:function(){
	        	 //关闭等待窗口
	             $("#loadingMsg").window("close");
	        	   message.showMessage(message.messageText.networkError);
	           }
	     });
	}     
};
//显示一个客户，主要针对搜索出来的人
function showClient(index){
	$("#searchResultGrid").datagrid("selectRow",index);
	var row=$("#searchResultGrid").datagrid("getSelected");

	//组成需要的数据对象
	var obj={};
	obj.customerName=row.customerName;
	obj.latitude=row.latitude;
	obj.longitude=row.longitude;
	obj.customerAddress=row.customerAddress;
	obj.areaCode=row.areaCode;
	if(row.tel||row.mobile){
		 if(row.tel){
 			if(row.mobile){
 				obj.tel=row.tel+"/"+row.mobile;
 			}
 		 }else{
 			 obj.tel=row.mobile;
 		 }
	}else{
		obj.tel="无";	
	}
	
	mapClass.addClientMarker(obj.areaCode,obj,header+"/static/images/client_red_label.png");
	$("#searchResultPanel").window("close");
	//重新设置地图中心点
	//mapClass.ownMap.setCenter(new BMap.Point(obj.longitude,obj.latitude));
	mapClass.ownMap.setCenter(row.areaCode);
}
//标记重点函数
function mark(id){
	$("#clientId").val(id);
	$("#markClientPanel").window("open");
}
$(document).ready(function(){
	//设置高亮菜单
	$("a[menu_type='clientMgr']").addClass("active");
	//数据加载提示
    $("#loadingMsg").window({
         height:30,
        width:150,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        closable:false,
        closed:false,
        modal:true,
        content:'<div class="loadingMsgDetail">数据加载中......</div>',
        noheader:true,
        border:false
    });
	//城市下拉框控件
    $("#address").combobox({
        width:185,
        editable:false,
        panelHeight:180,
        onSelect:function(record){
        	//初始化地图
        	if(record.value==""){
			    	mapClass.ownMap.centerAndZoom("湖南省",9);
		    }else{
		    	//开启等待窗口
		    	$("#loadingMsg").window("open");
		    	mapClass.ownMap.centerAndZoom(record.value,12);		    	
		    }
        	
        	 //清除原来的timeout
        	//clearTimeout(timer);
        	//清除已加载的id
        	clientHasId=0;
        	clientHasLoad=0;
        	
        	//加载数据
        	loadClient(record.value);
        	      	
        }
    });
	//tab初始化
	$("#tab").tabs({
		width:"100%",
		height:700,
		onSelect:function(title,index){
			if(index==0){
				 //初始化地图，重新加载数据，因为可能改了重点
			    if(areaCode==""){
			    	mapClass.init("湖南省",9);
			    }else{
			    	mapClass.init(areaCode,12);
			    }
			    //重新设置combobox的值为默认值
			    $("#address").combobox("setValue",areaCode);
			    //添加地图点击事件
				mapClass.ownMap.addEventListener("click", function(e){
					//显示点击获取数据的界面
					$("#clickResultGrid").datagrid("load",{
						loadFlag:1,
			        	latitude:e.point.lat,
			        	longitude:e.point.lng,
					});
					$("#clickResultPanel").window("open");
				});
				//设置所有的数据为0，表示从新加载
				clientHasLoad=0;
				clientHasId=0;
				clientProHasLoad=0;
				clientProHasId=0;
			    //加载数据
				loadClient(areaCode);
			}else{
				//取消掉加载
				clearTimeout(timer);
			}
		}
     });
	
	
    /*------------------------点击事件=======================*/
  //显示结果面板
    $("#clickResultPanel").window({
    	width:800,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '结果显示面板',
        closed:true,
        modal:true,
        zIndex:9999
    });
    //结果列表
     $("#clickResultGrid").datagrid({
    	 url:header+"/web/customer/"+currentUserId+"/findByCoordinate",
         noheader:true,
         minimizable:false,
         collapsible:false,
         maximizable:false,
         pageSize:15,
         striped: true,
         fitColumns:true,
         pagination:true,
         singleSelect:true,
         queryParams:{
        	 loadFlag:0,
        	 latitude:"",
        	 longitude:"",
         },
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
        	 $("#clickResultPanel").window("open");
         },
         columns:[[
                   {field:'customerName',title:'客户名称',width:300,align:"center"},
                   {field:'customerAddress',title:'客户地址',width:400,align:"center"},
                   {field:'tel',title:'联系电话',width:120,align:"center"}
        ]]
     });
    /*---------------------------搜索事件====================*/
    //显示结果面板
    $("#searchResultPanel").window({
    	width:800,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '搜索结果显示面板',
        closed:true,
        modal:true,
        zIndex:9999
    });
    //结果列表
     $("#searchResultGrid").datagrid({
    	 url:header+"/web/customer/"+currentUserId+"/find",
         noheader:true,
         minimizable:false,
         collapsible:false,
         maximizable:false,
         pageSize:10,
         fitColumns:true,
         striped: true,
         pagination:true,
         singleSelect:true,
         queryParams:{
        	 loadFlag:0,
        	 areaCode:"",
        	 customerAddress:"",
        	 customerName:"",
        	 tels:""
         },
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
        	$("#searchResultPanel").window("open");
         },
         columns:[[
                   {field:'customerName',title:'客户名称',width:300,align:"center"},
                   {field:'customerAddress',title:'客户地址',width:300,align:"center"},
                   {field:'areaCode',title:'城市',width:80,align:"center"},
                   {field:'tel',title:'联系电话',width:100,align:"center"},
                   {field:'longitude',title:'经度',width:80,align:"center",hidden:true},
                   {field:'latitude',title:'纬度',width:80,align:"center",hidden:true},
                   {field:'county',title:'操作',width:50,align:"center",formatter: function(value,row,index){
                       return '<a href="javascript:void(0)" onclick="showClient('+index+')">显示</a>';
                   }}
        ]]
     });
     //搜索事件
     $("#searchClient").click(function(){
    	 var value=$("#name").val();
    	  $("#searchResultGrid").datagrid("load",{
    		  loadFlag:1,
    		  areaCode:!areaCode?$("#address").combobox("getValue"):areaCode,
         	  customerAddress:"",
         	  customerName:value,
         	  tels:""
    	  });
     })
   /*------------------------客户设置=====================*/
     //表格
    $("#clientGrid").datagrid({
        url:header+"/web/customer/"+currentUserId+"/find",
        noheader:true,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        toolbar:"#clientTb",
        pagination:true,
        fitColumns:true,
        pageSize:19,
        striped: true,
        singleSelect:true,
        queryParams:{
        	tels:"",
        	customerName:"",
        	customerAddress:"",
        	areaCode:areaCode
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
        columns:[[
            {field:'id',title:'编号',width:120,align:"center",hidden:true},
            {field:'customerName',title:'客户名称',width:300,align:"center"},
            {field:'customerAddress',title:'客户地址',width:400,align:"center"},
            {field:'areaCode',title:'城市',width:120,align:"center"},
            {field:'tag',title:'重点类型',width:120,align:"center",formatter:function(value,row,index){
            	return message.clientTag[value];
            }},
            {field:'county',title:'操作',width:140,align:"center",formatter: function(value,row,index){
                return '<a href="javascript:void(0)" onclick="mark('+row.id+')">标重点</a>';
            }}
        ]]
    });
    //城市下拉框控件
    $("#listAddress").combobox({
        width:185,
        panelHeight:180,
        onSelect:function(record){
        	 $("#clientGrid").datagrid("load",{
             	tels:"",
             	customerName:"",
             	customerAddress:"",
             	areaCode:record.value
             });
        }
    });
    //搜索客户列表按钮事件
    $("#listSearchClient").click(function(){
        $("#clientGrid").datagrid("load",{
        	tels:"",
        	customerName:$("#clientName").val(),
        	customerAddress:"",
        	areaCode:!areaCode?$("#listAddress").combobox("getValue"):areaCode
        });
    }); 
    //设置重点面板
    $("#markClientPanel").window({
    	width:400,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '设置重点面板',
        closed:true,
        modal:true,
        zIndex:9999
    });
    //设置重点
    $('#markClientForm').form({
    	onSubmit:function(){
    		return true;
    	},
        success:function(data){
        	data=eval("("+data+")");
       	 	if(data.resultCode==1){
       	 		 message.showMessage(message.messageText.saveSuccess);
	       	 	 $("#clientGrid").datagrid("load",{
	             	tels:"",
	             	customerName:$("#clientName").val(),
	             	customerAddress:"",
	             	areaCode:!areaCode?$("#listAddress").combobox("getValue"):areaCode
	             });
      			$("#markClientPanel").window("close");
      		 }else if(data.resultCode==2){
      			message.showMessage(message.messageText.saveError);
      		 }else if(data.resultCode==-1){
      			 message.showOutMessage();
      		 }
        }
    });
   //取消按钮
    $("#markClientReset").click(function(){
         $("#markClientPanel").window("close");
    });
});