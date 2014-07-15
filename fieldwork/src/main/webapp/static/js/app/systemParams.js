$(document).ready(function(){
	//删除遮盖层
	$("#load").remove();
	//设置高亮菜单
	$("a[menu_type='systemParams']").addClass("active");
	
	
	 $("#orderOverTime").validatebox({
	    	required:true,
	        validType:"minute"
	 });
	 $("#visitePrefix").validatebox({
	    	required:true,
	    	validType:"length[1,12]"
	 });
	 $("#repairPrefix").validatebox({
	    	required:true,
	    	validType:"length[1,12]"
	 });
	 $("#clientMessageContent").validatebox({
	    	required:true,
	    	validType:"length[1,256]"
	 });
	 $("#orderMessageContent").validatebox({
	    	required:true,
	    	validType:"length[1,256]"
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
            })    	
    	}
	});
});