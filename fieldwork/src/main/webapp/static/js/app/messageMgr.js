//查看通知详细信息
function showRow(id){
    $.ajax({
     url:header+"/web/notification/"+currentUserId+"/load-by-id",
     type:"post",
     dataType:"json",
     data:{
    	 notificationId:id
     },
     success:function(data){
    	 if(data.resultCode==1){
             $("#checkTitle").text(data.notification.title);
             $("#checkContent").text(data.notification.content);
             $("#checkCity").text(data.notification.city?data.notification.city:"湖南省");
             $("#checkDate").text(data.notification.publishDate);
             $("#checkPanel").window("open");
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
	$("a[menu_type='messageMgr']").addClass("active");
	
	//城市下拉框
    $("#address").combobox({
        width:120,
        editable:false,
        panelHeight:180,
        onSelect:function(record){
        	$("#grid").datagrid("load",{
                title:"",
                city:record.value
            });
        }
    });
	
	//查看通知详细信息面板
    $("#checkPanel").window({
        width:450,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '查看公告信息',
        closed:true,
        modal:true,
        zIndex:9999
    });

    //增加修改面板
    $("#addPanel").window({
        width:500,
        height:300,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        title: '增加',
        closed:true,
        modal:true,
        zIndex:9999
    });
  
    //工具栏相关代码
    $("#addBtn").click(function(){
            $("#addPanel").window("open");
    });
    //搜索按钮
    $("#searchBtn").click(function(){
            $("#grid").datagrid("load",{
            	title:$("#searchTitle").val(),
            	city:!areaCode?$("#address").combobox("getValue"):areaCode
        });
    });

	//表格
    $("#grid").datagrid({
        url:header+'/web/notification/'+currentUserId+"/find",
        noheader:true,
        minimizable:false,
        collapsible:false,
        maximizable:false,
        toolbar:"#tb",
        pagination:true,
        fitColumns:true,
        pageSize:15,
        striped: true,
        singleSelect:true,
        queryParams:{
        	title:"",
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
            {field:'id',title:'id',width:0,align:"center",hidden:true},
            {field:'title',title:'标题',width:200,align:"center"},
            {field:'city',title:'区域',width:150,align:"center",formatter: function(value,row,index){
                   if(!value){
                	   value="湖南省";
                   }
                 return value;
            }},
            {field:'content',title:'内容',width:610,align:"center"},
            {field:'publishDate',title:'日期',width:180,align:"center"},
            {field:'action',title:'操作',width:80,align:"center",formatter: function(value,row,index){
                return '<a href="javascript:void(0)" onclick="showRow('+row.id+')">查看</a>';
            }}
        ]]
    });
   
	
    //增加时所有字段的验证
    $("#title").validatebox({
        required:true,
        validType:"length[1,80]"
    });
    $("#content").validatebox({
        required:true,
        validType:"length[1,512]"
    });
    $("#date").datebox({
        required:true,
        width:180
    });
    $("#addMessageCity").combobox({
        width:185,
        editable:false,
        panelHeight:80,
        required:true
    })
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
                $("#grid").datagrid("load",{
                	title:$("#searchTitle").val()
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
   
});