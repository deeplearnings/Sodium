/**
 * 自定义表单校验提示
 */
$(function(){
	jQuery.extend(jQuery.validator.messages, {
        required: "必填字段",
		  remote: "请修正该字段",
		  email: "请输入正确格式的电子邮件",
		  url: "请输入合法的网址",
		  date: "请输入合法的日期",
		  dateISO: "请输入合法的日期 (ISO).",
		  number: "请输入合法的数字",
		  digits: "只能输入整数",
		  creditcard: "请输入合法的信用卡号",
		  equalTo: "请再次输入相同的值",
		  accept: "请输入拥有合法后缀名的字符串",
		  maxlength: jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
		  minlength: jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
		  rangelength: jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
		  range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
		  max: jQuery.validator.format("请输入一个最大为{0} 的值"),
		  min: jQuery.validator.format("请输入一个最小为{0} 的值")
		});
});

/**
 * 自定义手机号验证
 */
jQuery.validator.addMethod("isMobile", function(value, element) {
    var length = value.length;
    var mobile = /^1[34578]\d{9}$/;/*/^1(3|4|5|7|8)\d{9}$/*/
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写您的手机号码");

/*分表前缀标识正则*/
$.validator.addMethod("tablePrefix",function(value,element){
    value = trim(value);
    var patrn = new RegExp("^[a-z]+_$");
    return patrn.test(value);
},"分表前缀必须以 小写英文开头,并只能包含小写英文,并以 _ 结尾!");
/*权限标识正则*/
$.validator.addMethod("premissionName",function(value,element){
    value = trim(value);
    var patrn = new RegExp("^PERM_.*?[a-zA-Z0-9]$");
    return patrn.test(value);
},"权限标识必须以 'PERM_' 开头,并以数字或英文结尾!");

/*角色标识正则*/
$.validator.addMethod("roleName",function(value,element){
    value = trim(value);
    var patrn = new RegExp("^ROLE_.*?[a-zA-Z0-9]$");
    return patrn.test(value);
},"角色标识必须以 'ROLE_' 开头,并以数字或英文结尾!");

/*不包含中文*/
$.validator.addMethod("noChinese",function(value,element){
    var patrn = new RegExp('[\u4e00-\u9fa5]');
    return (!patrn.test(value));
},"不能包含汉字!");

/*树设为必填项*/
$.validator.addMethod("treeRequired",function(value,element){
	var b1 = value.indexOf("未选择") == -1;
	var b2 = value !='';
	var b3 = typeof(value) != 'undefined';
    return (b1 && b2 && b3)?true:false;
},"必填字段");

/*不为空字符串*/
$.validator.addMethod("notEmpty",function(value,element){
    return (isEmptyStr(value))?false:true;
},"该字段不能为空值");

/*正数*/
$.validator.addMethod("positiveNumber",function(value,element){
    var patrn = new RegExp('^[0-9]*$');
    var number = patrn.test(value);
    return (number && value >= 0)?true:false;
},"该字段不能为负数");
