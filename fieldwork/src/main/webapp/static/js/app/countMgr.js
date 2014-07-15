$(document).ready(function(){
	//删除遮盖层
	$("#load").remove();
	//设置高亮菜单
	$("a[menu_type='countMgr']").addClass("active");
	 //tab
	$("#tab").tabs({
		width:740,
		height:565
	});
    //修改基本资料字段验证
    $("#editTelephone").validatebox({
        required:true,
        validType:"telephone&hasMobileNo['#oldTelephone']"
    });
    $("#editName").validatebox({
    	required:true,
    	validType:"length[1,20]&hasEmail['#oldEmail']"
    });
    $("#editEmail").validatebox({
        required:true,
        validType:"email&length[1,30]"
    });
    $("#editPhone").validatebox({
        required:true,
        validType:"phone&length[1,20]"
    });
    //修改基本资料表单提交
    $('#countForm').form({
        onSubmit: function(){
            return $("#countForm").form("validate");
        },
        success:function(data){
        	data=JSON.parse(data);
        	 if(data.resultCode==1){
        		 message.showMessage(message.messageText.saveSuccess);
       		 }else if(data.resultCode==2){
       			message.showMessage(message.messageText.saveError);
       		 }else if(data.resultCode==-1){
       			message.showOutMessage();
       		 }
        }
    });
    //修改密码字段验证
    $("#editPassword").validatebox({
        required:true,
        validType:"length[6,20]"
    });
    $("#editNewPassword").validatebox({
        required:true,
        validType:"length[6,20]"
    });
    $("#editConfirmPassword").validatebox({
        required:true,
        validType:"equals['#editNewPassword']"
    });
    //修改密码表单提交
    $('#changePassword').form({
        onSubmit: function(){
            return $("#changePassword").form("validate");
        },
        success:function(data){
        	data=eval("("+data+")");
        	 if(data.resultCode==1){
        		 $.fn.window.defaults.closable=false;//隐藏关闭按钮，只能点击确定，以便执行回调函数
        		 $.messager.alert('信息提示',"保存成功，请重新登陆！",'info',function(){
        			 window.location.href=header+"/web/logout";
        		 });
       		 }else if(data.resultCode==2){
       			 if(data.error=="password_no_match"){
       				message.showMessage("输入的原始密码不匹配,请输入正确的原始密码！");
       			 }else{
       				message.showMessage(message.messageText.saveError); 
       			 }
       		 }else if(data.resultCode==-1){
       			message.showOutMessage();
       		 }
        }
    });
});