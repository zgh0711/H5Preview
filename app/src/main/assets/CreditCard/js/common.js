
/**
 * 判断内容是否为空
 */
function isnull(val) {
    var str = $.trim(val);//去除空格;

    if (str == '' || str == undefined || str == null || str.length == 0) {
        return true;
    } else {
        return false;
    }
}

/**
 * 判断是否为手机号
 * @param str
 * @returns {boolean}
 */
function isPhoneNum(str) {
    return /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|166|199|18[0-9]|14[57])[0-9]{8}$/.test(str)
}

/**
 * 设置验证码倒计时
 * @param obj
 */
function invokeSettime(obj){
    var countdown=60;
    settime(obj);
    function settime(obj) {
        if (countdown == 0) {
            obj.attr("disabled",false);
            obj.text("获取验证码");
            countdown = 60;
            return;
        } else {
            obj.attr("disabled",true);
            obj.text(countdown + "s 重新发送");
            countdown--;
        }
        setTimeout(function() {
                settime(obj) }
            ,1000)
    }
}