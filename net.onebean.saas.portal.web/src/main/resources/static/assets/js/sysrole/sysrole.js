/*保存树上数据结果*/
function prTreeSubmitAction() {
    var ids  = "";
    var roleId = $('#PrTree-tips').data("roleId");
    $.each($('#PrTree-template').tree("selectedItems"),function (i,e) {
        ids+=e.id+",";
    });
    ids = ids.substr(0,ids.length-1)
    doPost("/syspremission/savepremissionrole",{premIds:ids,roleId:roleId},function(res){
        if(res.flag){
            $('#PrTree-tips').modal('close');
            alert("数据更新成功!");
        }
    })
}

/*监听树初始化完成事件 展开所有节点*/
$('body').on('loaded.tree.amui', '#PrTree-template', function() {
    $('#PrTree-template').tree('discloseAll');
});

/*关闭弹框销毁树*/
$('body').on('close.modal.amui', '#PrTree-tips', function() {
    $('#PrTree-template').tree('destroy');
    $('#PrTree-tips').empty();
});

/*关闭弹框销毁树*/
$('body').on('close.modal.amui', '#treeTips', function() {
    $('#tree-template').tree('destroy');
    $('#treeTips').empty();
});

/*权限角色绑定树初始化 点击触发事件*/
function intiPrTree(roleId) {
    $('#PrTree-tips').html(template('tpl-PrTreeTempl',null));
    $('#PrTree-tips').data("roleId",roleId);
    var url = "/syspremission/getrolepremission";
    var title = "选中菜单,为该角色添加权限";
    var $treeTemplate = $('#PrTree-template');
    var $treeTips = $('#PrTree-tips');
    initTreeSyncMultiSelect(title,roleId,url,$treeTemplate);
    treeTipsModal($treeTips);
}
