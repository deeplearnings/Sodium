function initModalRU() {
    var $link = '/sysrole/findbyname?name=%QUERY';
    /*猎犬 异步数据*/
    var rolenames = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('chName'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {//在文本框输入字符时才发起请求
            url:$link,
            wildcard: '%QUERY'
            ,filter: function(result) {
                return $.map(result.data, function(item) {
                    return {
                        chName:item.chName,
                        name:item.name,
                        id:item.id
                    };
                });
            }
        }
    });

    rolenames.initialize();
    /*tags input初始化*/
    $('#input-sg').tagsinput(
        {
            typeaheadjs: {
                displayKey: 'chName',
                source: rolenames.ttAdapter()
            },
            freeInput:false,
            trimValue: 'true',
            itemValue: 'id',
            itemText: 'chName'}
    );
}

/*关闭弹框重置模糊搜索框*/
$('body').on('close.modal.amui', '#data-bind-modal', function() {
    $('#input-sg').tagsinput('destroy');
});

/*初始化RU列表*/
function initRoleUserList() {
    var userId = $('#rolesList').data("uid")
    doGet("/sysrole/findbyuid",{userId:userId},function(res){
        $('#rolesList').html(template('tpl-sysRoleList',res.data));
    })
}

/*初始化RU列表*/
function modalRU(uid,name){
    initModalRU();
    $('#rolesList').data("uid",uid);
    $(".data-bind-title").html("为用户: "+name+" 绑定角色");
    initRoleUserList();
    $('#data-bind-modal').modal('open');
}

/*添加RU*/
function addRU() {
    var userId = $('#rolesList').data("uid");
    var $val = $('#input-sg').val();
    if($val.length>0){
        doPost("/sysrole/addroleuser",{userId:userId,roleIds:$val},function(res){
            if(res.flag){
                initRoleUserList(userId);
                $('#input-sg').tagsinput('removeAll');
            }
        })
    }
}
/*删除RU*/
function removeRU() {
    var userId = $('#rolesList').data("uid");
    var $rlist = $('input[name="roleList"]');
    var ids = "";
    $.each($rlist, function(key, val) {
        if($(val).is(':checked')){
            ids += $(val).val()+",";
        }
    });
    ids = ids.substr(0,ids.length-1);
    doPost("/sysrole/removeroleuser",{urIds:ids},function(res){
        if(res.flag){
            initRoleUserList(userId);
        }
    })
}

/**
 * 重置用户密码
 */
function resetPassword($link){
    $(".confirm-modal-title").html("警告");
    $(".confirm-modal-message").html("你，确定要重置用户密码为123456吗？");
    $(".confirm-modal-btn-cancel").html("取消");
    $(".confirm-modal-btn-confirm").html("确定");
    $('#confirm-modal').modal({
        relatedTarget: this,
        onConfirm: function(){
            // var $link = $(this.relatedTarget).data('url');
            doGet($link,null,function(res){
                if(res.flag){
                    alert("操作提示","重置密码成功!","好的");
                }
            })
        },
        onCancel: function(){}
    });
}

/**
 * 上传图片成功回调
 * @param arr
 */
var uploadCompleteHandler = function (arr) {
    if(arr.length == 1){
        $('#icon').val(arr[0]);
        $('#iconImg').attr("src",arr[0]);
    }else{
        $(".alert-modal-message").html("上传文件错误");
        $(".alert-modal-title").html("只能上传一张图片作为头像!");
        $(".alert-modal-button").html("额...好吧");
        $('#alert-modal').modal('open');
    }
    $('#upload-modal').modal('close');
};