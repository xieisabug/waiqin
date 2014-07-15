if ($.fn.pagination){
	$.fn.pagination.defaults.beforePageText = '第';
	$.fn.pagination.defaults.afterPageText = '页，共{pages}页';
	$.fn.pagination.defaults.displayMsg = '显示{from}到{to},共{total}记录';
    //另外添加，不显示每页显示的数量和刷新按钮
    $.fn.pagination.defaults.showRefresh=false;
    $.fn.pagination.defaults.showPageList=false;
}
if ($.fn.datagrid){
	$.fn.datagrid.defaults.loadMsg = '正在处理，请稍待。。。';
}
if ($.fn.treegrid && $.fn.datagrid){
	$.fn.treegrid.defaults.loadMsg = $.fn.datagrid.defaults.loadMsg;
}
if ($.messager){
	$.messager.defaults.ok = '确定';
	$.messager.defaults.cancel = '取消';
}
if ($.fn.validatebox){
	$.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
	$.fn.validatebox.defaults.rules.email.message = '请输入有效的电子邮件地址';
	$.fn.validatebox.defaults.rules.url.message = '请输入有效的URL地址';
	$.fn.validatebox.defaults.rules.length.message = '输入内容长度必须介于{0}和{1}之间';
	$.fn.validatebox.defaults.rules.remote.message = '该项已经被使用，请更换';
}
if ($.fn.numberbox){
	$.fn.numberbox.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combobox){
	$.fn.combobox.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combotree){
	$.fn.combotree.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combogrid){
	$.fn.combogrid.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.calendar){
	$.fn.calendar.defaults.weeks = ['日','一','二','三','四','五','六'];
	$.fn.calendar.defaults.months = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
}
if ($.fn.datebox){
	$.fn.datebox.defaults.currentText = '今天';
	$.fn.datebox.defaults.closeText = '关闭';
	$.fn.datebox.defaults.okText = '确定';
	$.fn.datebox.defaults.missingMessage = '该输入项为必输项';
	$.fn.datebox.defaults.formatter = function(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	};
	$.fn.datebox.defaults.parser = function(s){
		if (!s) return new Date();
		var ss = s.split('-');
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		var d = parseInt(ss[2],10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
			return new Date(y,m-1,d);
		} else {
			return new Date();
		}
	};
}
if ($.fn.datetimebox && $.fn.datebox){
	$.extend($.fn.datetimebox.defaults,{
		currentText: $.fn.datebox.defaults.currentText,
		closeText: $.fn.datebox.defaults.closeText,
		okText: $.fn.datebox.defaults.okText,
		missingMessage: $.fn.datebox.defaults.missingMessage
	});
}

// extend the 'equals' rule
if($.fn.validatebox){
    $.extend($.fn.validatebox.defaults.rules, {
        equals: {
            validator: function(value,param){
                return value == $(param[0]).val();
            },
            message: '两次输入的密码不一样'
        }
    });
    //签到时间验证
     $.extend($.fn.validatebox.defaults.rules, {
        checkInTime: {
            validator: function(value,param){
            	var reg=/^[0-1][0-9]|[2][0-3]:[0-5][0-9]$/;
            	if(reg.test(value)){
            	      return true;
            	}else{
            		return false;
            	}
            },
            message: '时间格式不正确，请重新输入'
        }
    });
  //修改是验证邮箱是否存在
    $.extend($.fn.validatebox.defaults.rules, {
        hasEmail: {
            validator: function(value,param){
                //如果修改的时候使用的还是原来的邮箱不发ajax请求,将原来的邮箱作为参数传入
            	if(value!=$(param[0]).val()){
            		var result=$.ajax({
            			url:header+"/web/user/"+currentUserId+"/check-email",
            			type:"post",
            			dataType:"json",
            			async:false,
            			data:{
            				email:value
            			}
            		}).responseText;
            		return result=="true";
            	}else{
            		return true;
            	}
            },
            message: '该项已经被使用，请更换'
        }
    });
  //修改是验证用户名是否存在
    $.extend($.fn.validatebox.defaults.rules, {
        hasUserName: {
            validator: function(value,param){
                //如果修改的时候使用的还是原来的用户名不发ajax请求,将原来的用户名作为参数传入
            	if(value!=$(param[0]).val()){
            		var result=$.ajax({
            			url:header+"/web/user/"+currentUserId+"/check-username",
            			type:"post",
            			dataType:"json",
            			async:false,
            			data:{
            				username:value
            			}
            		}).responseText;
            		return result=="true";
            	}else{
            		return true;
            	}
            },
            message: '该项已经被使用，请更换'
        }
    });
  //修改是验证电话号码是否存在
    $.extend($.fn.validatebox.defaults.rules, {
        hasMobileNo: {
            validator: function(value,param){
                //如果修改的时候使用的还是原来的电话号码不发ajax请求,将原来的电话号码作为参数传入
            	//console.log($(param[0]).val());
            	if(value!=$(param[0]).val()){
            		var result=$.ajax({
            			url:header+"/web/user/"+currentUserId+"/check-mobileno",
            			type:"post",
            			dataType:"json",
            			async:false,
            			data:{
            				mobileNo:value
            			}
            		}).responseText;
            		return result=="true";
            	}else{
            		return true;
            	}
            },
            message: '该项已经被使用，请更换'
        }
    });
  //修改是验证meid号是否存在
    $.extend($.fn.validatebox.defaults.rules, {
        hasMeid: {
            validator: function(value,param){
                //如果修改的时候使用的还是原来的meid号不发ajax请求,将原来的meid号作为参数传入
            	if(value!=$(param[0]).val()){
            		var result=$.ajax({
            			url:header+"/web/device/"+currentUserId+"/check-meid",
            			type:"post",
            			dataType:"json",
            			async:false,
            			data:{
            				meid:value
            			}
            		}).responseText;
            		return result=="true";
            	}else{
            		return true;
            	}
            },
            message: '该项已经被使用，请更换'
        }
    });
    //验证手机号码
    $.extend($.fn.validatebox.defaults.rules, {
        telephone: {
            validator: function(value,param){
                var strP=/^1[0-9]{10}$/;
                if(strP.test(value)){
                	return true;
                }else{
                	return false;
                };
            },
            message: '请输入正确的手机号码'
        }
    });
  //验证座机号码
    $.extend($.fn.validatebox.defaults.rules, {
        phone: {
            validator: function(value,param){
                var strP=/^[0-9]{4}[-][0-9]{7,8}$|^[0-9]{7,8}$|^[0-9]{11,12}$/;//座机号码中包含数字和“-”
                if(strP.test(value)){
                	return true;
                }else{
                	return false;
                };
            },
            message: '请输入正确的座机号码'
        }
    });
    //是否是两位小数的金额
    $.extend($.fn.validatebox.defaults.rules, {
        money: {
            validator: function(value,param){
                var strP=/^[1-9][0-9]*[.]{0,1}[0-9]{0,2}$/;//金钱有小数点2位
                if(strP.test(value)){
                    return true;
                }else{
                    return false;
                }
            },
            message: '请输入正确的金额,可以保留两位小数'
        }
    });
    //是否是大于0的并且小于720分钟的分钟数 
    $.extend($.fn.validatebox.defaults.rules, {
        minute: {
            validator: function(value,param){
                var strP=/^[1-9][0-9]*$/;//金钱有小数点2位
                if(strP.test(value)&&value<=720){
                    return true;
                }else{
                    return false;
                }
            },
            message: '请输入正确的分钟数，并且需要小于720'
        }
    });
  //开始日期应该大于今天
    $.extend($.fn.validatebox.defaults.rules, {
        isLtToday: {
            validator: function(value,param){
            	value=value.replace(/[-]/g,"/");
                if(Date.parse(value)>new Date().getTime()){
                   return true;
                }else{
                    return false;
                }
            },
            message: '不能设置过往日期'
        }
    });
  //前面的日期不能小于后面的日期
    $.extend($.fn.validatebox.defaults.rules, {
        twoDate: {
            validator: function(value,param){
                var startValue=$(param[0]).datebox("getValue");
                if(value>=startValue){
                   return true;
                }else{
                    return false;
                }
            },
            message: '结束日期应不小于开始日期'
        }
    });
   
    //时间跨度不能超过15天,并且截止日期应该小于今天
    $.extend($.fn.validatebox.defaults.rules, {
        maxRangeTime: {
            validator: function(value,param){
                var startValue=Date.parse($(param[0]).datetimebox("getValue").replace(/[-]/g,"/"));
                var days=param[1];
                var endValue=Date.parse(value.replace(/[-]/g,"/"));
                
                if(startValue<=endValue&&endValue-startValue<=days*24*60*60*1000&&endValue<=new Date().getTime()){
                    return true;
                }else{
                    return false;
                }
            },
            message: '截止日期应小于今天并大于开始日期并两个日期间隔不能超过天'
        }
    });
  
}
