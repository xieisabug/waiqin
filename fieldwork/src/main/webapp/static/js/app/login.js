function saveUserInfo() {
	if(!$.cookie("username")||$.cookie("username")!=$("#username").val()){
		var appUsername = $("#username").val();
		var expiration = new Date((new Date()).getTime() + 7*24*60* 60000);//设置时间
		$.cookie("username", appUsername, { expires: expiration }); // 存储一个带15分钟期限的 cookie
	}
    if ($("#rememberme").attr("checked") == "checked") {
        var password = $("#password").val();
        var expiration = new Date((new Date()).getTime() + 7*24*60* 60000);//设置时间
        $.cookie("rememberme", "true", { expires: expiration }); // 存储一个带15分钟期限的 cookie
        $.cookie("password", password, { expires: expiration }); // 存储一个带15分钟期限的 cookie
    }else {
        $.cookie("rememberme", "false", { expires: -1 });
        //$.cookie("username", '', { expires: -1 });
        $.cookie("password", '', { expires: -1 });
    }
}
$(document).ready(function(){
	//看cookie中是否有用户名
	 $("#username").val($.cookie("username"));
	if ($.cookie("rememberme") == "true") {
        $("#rememberme").attr("checked", true);
        $("#password").val($.cookie("password"));
    }
    $("#username").validatebox({
        required:true,
        validType:"length[1,20]"
    });
    $("#password").validatebox({
        required:true,
        validType:"length[6,20]"
    });
    $("#forgetPw").click(function(){
    	$.messager.alert("忘记密码提示","如果你是超级管理员，请联系开发人员重置密码；如果你是管理员，请联系超级管理员重置密码！");
    });
     $("#submitBtn").click(function(){
    	 //记住用户名
    	 saveUserInfo();
       	 return $("#loginForm").form("validate");
       });
});