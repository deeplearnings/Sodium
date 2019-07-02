$(function(){
    new TapLocker();//初始化单例的菜单锁
    $('#indexMenuBtn').addClass('active');
    initDataPicker();
    unComments('tpl-pagination');//去除分页注释
});

// 设置jQuery Ajax全局错误catch
$.ajaxSetup({
    type: "POST",
    error: function (jqXHR, textStatus, errorThrown) {
        switch (jqXHR.status) {
            case(500):
                errorCheckOnOffLine(true,jqXHR,"500");
                break;
            case(400):
                errorCheckOnOffLine(true,jqXHR,"400");
                break;
            case(401):
                errorCheckOnOffLine(true,jqXHR,"401");
                break;
            case(403):
                errorCheckOnOffLine(true,jqXHR,"403");
                break;
            case(404):
                errorCheckOnOffLine(false,jqXHR,"404");
                break;
            case(408):
                errorCheckOnOffLine(false,jqXHR,"408");
                break;
            case(518):
                errorCheckOnOffLine(false,jqXHR,"518");
                break;
            case(501):
                errorCheckOnOffLine(false,jqXHR,"501");
                break;
            case(200):
                errorCheckOnOffLine(false,jqXHR,"200");
                break;
            default:
                error();
        }
        $.AMUI.progress.done();
    }
});

/**
 * 执行错误提示
 * @param mode 为true时 在线提示错误
 * @param jqXHR
 * @param code
 */
function errorCheckOnOffLine(mode,jqXHR,code) {
    if (jqXHR.status == 500){
        alert("系统异常错误,请联系客服或技术人员解决。");
        return;
    }
    if (jqXHR.status == 200 && jqXHR.responseText.indexOf("登录页面")){
        $('#alert-modal').on('closed.modal.amui', function(){
            $(this).off("closed.modal.amui");
            parent.location.reload();
        });
        alert("登录已失效,请重新登陆!");
        return;
    }
    if(mode){
        window.location.href = "/error/"+code;
        return;
    }
    if((typeof(jqXHR.responseJSON)!= 'undefined')){
        window.location.href = "/error/"+code;
    }else{
        error();
    }
}

/**
 * 加工项目前缀
 * @param $url
 * @returns {*}
 */
function addCtxToUrl($url) {
    var $ctx = $('title').data('ctx');
    if (typeof($ctx) === "undefined"){
        $ctx = "";
    }
    return $ctx+$url;
}

/**
 * 后退上一地址
 */
function goBack() {
    window.history.back(-1);
}

/**
 * 清除上传文件的input
 * @param target
 */
function clearUploadImg(target){
    var $group = $(target).parents('.am-form-group');
    var $img = $group.find('img');
    $img.attr('src','');
    var $input = $group.find('input');
    $input.val('');

}

/**
 * 标签页跳转标签页
 * @param target 触发按钮自身
 */
function tabToTab(target) {
    var $menu = $(window.parent.document.body).find('#hideMenu');
    $menu.attr("data-name",$(target).attr("data-name"));
    $menu.attr("data-url",$(target).attr("data-url"));
    $menu.click();
}


/**
 * 从菜单新开标签
 * @param target 触发按钮自身
 */
var openNewTab = function (target) {
    var locker = new TapLocker();
    locker.setIsLock(true);//锁起来
    clearMenuActive();
    var $parentsMenu = $(target).parents('.parent-menu').children('.sidebar-nav-sub-title');//父级菜单
    if($parentsMenu.length > 0){
        $parentsMenu.addClass('active')
    }else{
        $(target).addClass('active');
    }
    if(locker.getIsLock()){
        var $tab = $(window.parent.document.body).find('#onebean-frame-container');
        var $nav = $tab.find('.am-tabs-nav');
        var $bd = $tab.find('.am-tabs-bd');
        var $link = $(target).data('url');
        if($link.indexOf('http://') != -1 || $link.indexOf('http://') != -1){
            window.open($link);
            return;
        }else{
            $link = addCtxToUrl($link);
        }
        var $name = $(target).data('name');
        var $onebeanTabs = $nav.children('li');
        var isRepeat = isRepeatTab($onebeanTabs,$link);
        // isRepeat = -1;
        if(isRepeat == -1){//不存在重复tab 新开
            var tab={
                name:'',
                url:''
            };
            tab.name = $name;
            tab.url = $link;
            $nav.append(template('tpl-onebean-tab', tab));
            /*面包屑*/
            var breadCrumbs = eachBreadCrumbs($link,$name,true);
            var operator = ($link.indexOf("?") != -1)?'&':'?';
            $link += operator+"breadCrumbsStr="+encodeURI(JSON.stringify(breadCrumbs));
            var $iframe= $("<iframe width='100%' src='"+$link+"' class='am-tab-panel onebean-frame' onload='reHeightonebeanFrame(this)'></iframe>");
            $bd.append($iframe);
            $tab.tabs('refresh');
            var index =$onebeanTabs.length;
            $tab.tabs('open', index);
            $tab.tabs('refresh');
        }else{//直接去往对应的tab
            $tab.tabs('open', isRepeat);
            $tab.tabs('refresh');
        }
    }
    locker.setIsLock(false);//解锁
};

/**
 * 清除菜单中所有选中状态
 */
function clearMenuActive(){
    var menus = $('.sidebar-nav').find('a');
    $.each(menus,function (i,e) {
        if ($(e).hasClass('active'))
        $(e).removeClass('active');
    })
}

/**
 * 同时只能展开一个菜单
 */
$('body').on('click', '.sidebar-nav-sub-title',function(){
    $(this).attr('status','open');
    var subs = $('.sidebar-nav').find('.sidebar-nav-sub-ico');
    var menuA;
    $.each(subs,function (i,e) {
        menuA = $(e).parent('a');
        if ($(e).hasClass('sidebar-nav-sub-ico-rotate') && menuA.attr('status') != 'open'){
            menuA.click();
        }
    });
    $(this).attr('status','close');
    clearMenuActive();
    $(this).addClass('active');
});

/**
 * 刪除一行tr按钮
 */
$('body').on('click', '.del-line-btn',function(){
    $(this).parents('.onebean-line-tr').remove();
});


/**
 *switch按钮切换值替换成0/1
 */
$('body').on('switchChange.bootstrapSwitch init.bootstrapSwitch', 'input[data-am-switch]',function(event,state){
    if(state){
        $(this).val("1");
    }else {
        $(this).val("0");
    }
});



/**
 * 比较是否存在重复的tab
 * @param jtabs
 * @param href
 * @returns {number}
 */
function isRepeatTab($onebeanTabs,href) {
    var result = -1;
    $.each($onebeanTabs,function (i,e) {
        result = ($(e).children('a').data('url') == href)?i:result;
    });
    return result;
}

/**
 * 点击X按钮移除标签页
 */
$('body').on('click', '.am-icon-close', function() {
    var locker = new TapLocker();
    locker.setIsLock(true);//锁起来
    if(locker.getIsLock()){
        var $tab = $('#onebean-frame-container');
        var $nav = $tab.find('.am-tabs-nav');
        var $Jtabs = $nav.children('li');
        var $bd = $tab.find('.am-tabs-bd');
        var $item = $(this).closest('li');
        var index =$Jtabs.index($item);
        var tabsWidthSum = calSumWidth($Jtabs);
        reduceOneWidth($Jtabs[index],$Jtabs,tabsWidthSum);
        $item.remove();
        $bd.find('.am-tab-panel').eq(index).remove();
        $tab.tabs('open', index > 0 ? index - 1 : index + 1);
        $tab.tabs('refresh');
    }
    locker.setIsLock(false);//解锁
});

/**
 * 清空所有标签页
 * @param target
 */
function emptyTabs(target) {
    var locker = new TapLocker();
    locker.setIsLock(true);//锁起来
    if(locker.getIsLock()){
        var $tab = $('#onebean-frame-container');
        var $nav = $tab.find('.am-tabs-nav').children('li');
        var $bd = $tab.find('.am-tabs-bd').children('iframe');
        for(var i = 0;i<$nav.length;i++){
            if(i > 0){
                $nav[i].remove();
                $bd[i].remove();
            }
        }
        $tab.tabs('refresh');
        $(target).parents('.am-dropdown').dropdown('close');//隐藏下拉菜单
        $('.am-nav-tabs').animate({
            marginLeft: -0 + 'px'
        }, "fast");
    }
    locker.setIsLock(false);//解锁
}


/**
 * 关闭当前标签页
 * @param target
 */
function closeCurrentTabs(target) {
    var locker = new TapLocker();
    locker.setIsLock(true);//锁起来
    if(locker.getIsLock()){
        var $tab = $('#onebean-frame-container');
        var $nav = $tab.find('.am-tabs-nav').children('li');
        var $bd = $tab.find('.am-tabs-bd').children('iframe');
        var tabsWidthSum = calSumWidth($nav);
        var index;
        for(var i = 0;i<$nav.length;i++){
            if(i > 0){
                if($($nav[i]).hasClass('am-active')){
                    index = $nav.index($nav[i]);
                    reduceOneWidth($nav[i],$nav,tabsWidthSum);
                    $nav[i].remove();
                    $bd[i].remove();
                }

            }
        }
        $tab.tabs('open', index > 0 ? index - 1 : index + 1);
        $tab.tabs('refresh');
        $(target).parents('.am-dropdown').dropdown('close');//隐藏下拉菜单
    }
    locker.setIsLock(false);//解锁
}

/**
 * 处理当前tab分页最后一个标签关闭的动画
 * @param li
 * @param nav
 * @param tabsWidthSum
 */
function reduceOneWidth(li,nav,tabsWidthSum){
    var tempWidth = $(li).outerWidth(true);
    var liIndex = nav.index(li);
    var navLength = nav.length;
    if(liIndex == navLength-1){

        var tabWidth = $('#onebean-frame-container').outerWidth(true);//整个tab宽度
        var tabButtonWidth = $('.onebean-nav-buttons').outerWidth(true);//tab按钮区宽度
        var tabShowWidth = tabWidth - tabButtonWidth;//视距宽度
        var allPageSum = parseInt(tabsWidthSum/tabShowWidth);//tabs够分几页
        var allPageSumExcl = parseInt((tabsWidthSum - tempWidth)/tabShowWidth);//去除当前tab后的tabs够分几页
        if((allPageSum-allPageSumExcl)>=1){
            checkBarLeft();
        }
    }
}

/**
 * 关闭其他标签页
 * @param target
 */
function closeOtherTabs(target) {
    var locker = new TapLocker();
    locker.setIsLock(true);//锁起来
    if(locker.getIsLock()){
        var $tab = $('#onebean-frame-container');
        var $nav = $tab.find('.am-tabs-nav').children('li');
        var $bd = $tab.find('.am-tabs-bd').children('iframe');

        var index;
        for(var i = 0;i<$nav.length;i++){
            if(i > 0){
                if(!($($nav[i]).hasClass('am-active'))){
                    $nav[i].remove();
                    $bd[i].remove();
                }else{
                    index = $nav.index($nav[i]);
                }

            }
        }
        $tab.tabs('open', index);
        $tab.tabs('refresh');
        $(target).parents('.am-dropdown').dropdown('close');//隐藏下拉菜单
        $('.am-nav-tabs').animate({
            marginLeft: -0 + 'px'
        }, "fast");
    }
    locker.setIsLock(false);//解锁
}

/**
 * list页面搜索条件里的dic选择框变动后 重新加载页面
 */
$('body').on('change', '.onebean-param-select-box',function(){
    resetPageNumber();
    initDataTable();
});

// /**
//  * 按钮 通用绑定 点击事件
//  * button[type=button]
//  */
// $('body').on('click', '.action-button', function() {
//     routingPage($(this));
// })

/**
 * 查询按钮 点击事件
 * button[type=query]
 */
$('body').on('click', '.query-button', function() {
    resetPageNumber();
    initDataTable();
});

/**
 * 分页 数字页码 点击事件
 */
$('body').on('click', '.pagination-number', function() {
    var choosePage = $(this).children('a').html();
    $("#tpl-pagination").attr("currentPage",choosePage)
    initDataTable();
})

/**
 * 分页 返回首页 点击事件
 */
$('body').on('click', '.pagination-frist', function() {
    resetPageNumber();
    initDataTable();
})

/**
 * 分页 返回最后一页 点击事件
 */
$('body').on('click', '.pagination-end', function() {
    var choosePage = $("#tpl-pagination").attr("totalPages");
    $("#tpl-pagination").attr("currentPage",choosePage)
    initDataTable();
})

/**
 * 分页 下一页 点击事件
 */
$('body').on('click', '.pagination-next', function() {
    var choosePage = parseInt($("#tpl-pagination").attr("currentPage"))+1;
    $("#tpl-pagination").attr("currentPage",choosePage)
    initDataTable();
})

/**
 * 分页 上一页 点击事件
 */
$('body').on('click', '.pagination-previous', function() {
    var choosePage = parseInt($("#tpl-pagination").attr("currentPage"))-1;
    $("#tpl-pagination").attr("currentPage",choosePage)
    initDataTable();
});

/**
 * 重置筛选条件表单
 */
$(".reset-button").on("click",function(){
    $(".paramFrom")[0].reset();
    $("#limitSelector").children('option').eq(1).attr('selected', true);
    $("#orderBySelector").children('option').eq(0).attr('selected', true);
    $("#limitSelector").trigger('changed.selected.amui');
    $("#orderBySelector").trigger('changed.selected.amui');
    $('.onebean-param-select-box').selected('destroy');
    $('.onebean-param-select-box').selected();
    initDataTable()
});


/**
 * 回车键搜索表单
 */
$(".paramFrom").keydown(function(e){
    var e = e || event,
    keycode = e.which || e.keyCode;
    if (keycode==13) {
        initDataTable()
    }
});

/**
 * 重置页码
 */
function resetPageNumber() {
    $("#tpl-pagination").attr("currentPage",1)
}


/**
 * 初始化异步加载树
 * @param title
 * @param selfId
 * @param url
 */
function initTreeAsyncSingleSelect(title,selfId,url) {
    $('.am-popup-title').html(title);
    $treeTemplate = $('#tree-template');
    $treeTemplate.tree({
        dataSource: function(options, callback) {
            if(options.type === "folder" || typeof(options.type) === "undefined"){
                if(options.childList != null && typeof(options.childList) === "object"){
                    callback({data: options.childList});
                }else{
                    doPost(url,{parentId:options.id,selfId:selfId},function(res){
                        callback({data:res.data});
                    })
                }

            }
        },
        multiSelect: false,
        cacheItems: true,
        folderSelect: true
    });
    $treeTemplate.on('loaded.tree.amui', function (event, data) {
        $treeTemplate.tree('selectItemSafe', $('.selected-item'));
    });
}

/**
 * 初始化同步加载树
 * @param title
 * @param selfId
 * @param url
 */
function initTreeSyncMultiSelect(title,roleId,url,$treeTemplate) {
    $('.am-popup-title').html(title);
    $treeTemplate.tree({
        dataSource: function(options, callback) {
            if(options.type === "folder" || typeof(options.type) === "undefined"){
                if(options.childList != null && typeof(options.childList) === "object"){
                    callback({data: options.childList});
                }else{
                    doPost(url,{roleId:roleId},function(res){
                        callback({data:res.data});
                    })
                }

            }
        },
        cacheItems: true,
        multiSelect: true,
        folderSelect: true,
        itemSelect: true,
        itemSelectedIcon:'file',
        folderIcon:'folder',
        folderOpenIcon:'folder-open',
        itemIcon:'file'
    });
    $treeTemplate.on('loaded.tree.amui', function (event, data) {
        $treeTemplate.tree('selectItemSafe', $('.selected-item'));
    });


    $treeTemplate.on('updated.tree.amui', function (event, data) {
        var childs = $(data.item).find('.am-tree-item,.am-tree-branch');
        var parents = $(data.item).parents('.am-tree-branch');

        //遍历父节点
        $.each(parents, function(index,item) {
            if(data.eventType === "selected"){
                if(!$(item).is('.am-tree-selected')){
                    $treeTemplate.tree('selectItemSafe', $(item));
                }
            }
        });

        //遍历子节点
        $.each(childs, function(index,item) {
            if(data.eventType === "selected"){
                if (!$(item).is('.am-tree-selected')){
                    $treeTemplate.tree('selectItemSafe', $(item));
                }
            }else if (data.eventType === "deselected"){
                if ($(item).is('.am-tree-selected')){
                    $treeTemplate.tree('selectItemSafe', $(item));
                }
            }
        })
    });
}


/**
 * 机构树模态方法
 */
function treeTipsModal($tips) {
    $tips.modal('open');
}

/**
 * 页面 通用 数据数量 改变事件
 * #limitSelector
 */
$("#limitSelector").on("change",function(){
    resetPageNumber();
    initDataTable();
});


/**
 * 页面 通用 排序方式 改变事件
 * #orderBySelector
 */
$("#orderBySelector").on("change",function(){
    initDataTable()
});


/**
 * 数据列表 删除按钮
 */
$('body').on('click', '.list-del-button', function() {
    $(".confirm-modal-title").html("警告");
    $(".confirm-modal-message").html("你，确定要删除这条记录吗？");
    $(".confirm-modal-btn-cancel").html("取消");
    $(".confirm-modal-btn-confirm").html("确定");
    $('#confirm-modal').modal({
        relatedTarget: this,
        onConfirm: function(){
            var completeHandler = function(res){
                if(res.flag){
                    initDataTable();
                }else{
                    alert(res.msg)
                }
            };
            var $link = $(this.relatedTarget).data('url');
            doGet($link,null,completeHandler)
        },
        onCancel: function(){}
    });
});

/**
 * 添加面包屑到url
 * @param url
 * @returns {string|*}
 */
function addbreadCrumbsToUrl(url) {
    var breadCrumbs = justEachBreadCrumbs(false);
    var operator = (url.indexOf("?") != -1)?'&':'?';
    url += operator+"breadCrumbsStr="+encodeURI(JSON.stringify(breadCrumbs));
    return url;
}

/**
 * Http post 请求
 * @param url 请求地址
 * @param param 请求参数
 * @param completeHandler 回调函数
 */
function doPost(url,param,completeHandler) {
    url = addCtxToUrl(url);
    $.AMUI.progress.start();
    try{url=addbreadCrumbsToUrl(url)}catch(err){}//若没有面包屑 不做处理
    $.ajax({
        url:url,
        type:"POST",//请求方式
        dataType:"json",//返回参数的类型
        // contentType:"utf-8",//发送请求的编码方式
        data:param,
        success:function (data) {//请求成功后调用的函数
            completeHandler(data);
            $.AMUI.progress.done();
        }
    })
}

/**
 * Http post 同步请求
 * @param url 请求地址
 * @param param 请求参数
 * @param completeHandler 回调函数
 */
function doPostSync(url,param,completeHandler) {
    url = addCtxToUrl(url);
    $.AMUI.progress.start();
    $.ajax({
        url:url,
        async: false,
        type:"POST",//请求方式
        dataType:"json",//返回参数的类型
        // contentType:"utf-8",//发送请求的编码方式
        data:param,
        success:function (data) {//请求成功后调用的函数
            completeHandler(data);
            $.AMUI.progress.done();
        }
    })
}

/**
 * Http get 请求
 * @param url 请求地址
 * @param param 请求参数
 * @param completeHandler 回调函数
 */
function doGet(url,param,completeHandler) {
    url = addCtxToUrl(url);
    $.AMUI.progress.start();
    try{url=addbreadCrumbsToUrl(url)}catch(err){}//若没有面包屑 不做处理
    $.ajax({
        url:url,
        type:"GET",//请求方式
        dataType:"json",//返回参数的类型
        contentType:"utf-8",//发送请求的编码方式
        data:param,
        success:function (data) {//请求成功后调用的函数
            if(data.errorCode && data.errorCode != '0'){
                window.location.href = "/error/"+500+"?errorCode="+data.errorCode;
            }
            completeHandler(data);
            $.AMUI.progress.done();
        }
    })
}



/**
 * 统一的弹框提示
 */
function alert(title,message,buttonTitle){
    $(".alert-modal-message").html(message);
    $(".alert-modal-title").html(title);
    $(".alert-modal-button").html(buttonTitle);
    $('#alert-modal').modal('open');
}

/**
 * 统一的弹框提示
 */
function alert(message){
    $(".alert-modal-message").html(message);
    $(".alert-modal-title").html("提示");
    $(".alert-modal-button").html("好的");
    $('#alert-modal').modal('open');
}

/**
 * 加载动画
 */
function loadingModal() {
    $('#loading-modal').modal('open');
}

/**
 * 结束加载动画
 */
function cloasloadingModal() {
    $('#loading-modal').modal('close');
}
/**
 * 统一的错误提示
 */
function error() {
    $(".alert-modal-message").html("程序出了一丢丢小意外,赶紧联系程序猿解决吧!");
    $(".alert-modal-title").html("诶呀麻鸭,服务见鬼啦! |*´Å`)ﾉ ");
    $(".alert-modal-button").html("额...好吧");
    $('#alert-modal').modal('open');
}


/**
 * 显示上传文件的弹框
 */
function uploadFile(completeHandler,target) {
    var disabled = $(target).attr('disabled');
    if(disabled == 'disabled'){
        return;
    }
    initUploadModal(completeHandler);
    $('#upload-modal').modal('open');

}



/*初始化上传文件控件模态窗*/
function initUploadModal(completeHandler) {
    var $uploadTips = $('#upload-modal');
    $uploadTips.html(template('tpl-uploadTips',null));
    var $link = '/upload/uploadmultipartfile';
    $link = addCtxToUrl($link);
    var upload=$('#uploadFileWidget').AmazeuiUpload({
        url : $link,
        downloadUrl :'/upload/downfile',
        maxFiles: 5, // 单次上传的数量
        maxFileSize: 10, // 单个文件允许的大小 (M)
        multiThreading: false, // true为同时上传false为队列上传
        useDefTemplate: true, //是否使用表格模式
        dropType: true, //是否允许拖拽
        pasteType: false //是否允许粘贴
    });
    upload.init(); //对象初始化

    /*监听弹框关闭事件*/
    $('body').on('close.modal.amui', '#upload-modal', function() {
        upload.destory();
    });
    /*上传文件控件确认按钮事件*/
    $('body').on("click",'.upload-file-submit',function(){
        var arr=upload.selectResult();
        if(arr.length > 0){
            completeHandler(arr)
        }
    });
}





/**
 * 通用 数据列表 拼接 查询参数 及 条件
 * @returns {string}
 */
function formatQueryFromParam() {
    var conditionStr = "";
    var paramList = $(".paramFrom").find(".paramInput");
    for(var i=0;i<paramList.length;i++){
        var $tepm = $(paramList[i]);
        if ($tepm.val() != "") {

            if (conditionStr.length > 0) {
                conditionStr += "^"
            }
            conditionStr += $tepm.attr("param-pattern") + $tepm.val()
        }
    }
    return conditionStr;
}


/**
 * j-frame高度自适应
 */
function reHeightonebeanFrame(target){
    var mainheight = $(target).contents().find("body").height();
    $(target).height(mainheight);
}
/**
 * 查看tab左边的标签页
 */
function checkBarLeft(){
    var locker = new TapLocker();
    locker.setIsLock(true);//锁起来
    if(locker.getIsLock()){
        var marginLeftVal = (Math.abs(parseInt($('.am-nav-tabs').css('margin-left'))));//当前偏移量
        var tabWidth = $('#onebean-frame-container').outerWidth(true);//整个tab宽度
        var tabButtonWidth = $('.onebean-nav-buttons').outerWidth(true);//tab按钮区宽度
        var $tab = $('#onebean-frame-container');
        var $nav = $tab.find('.am-tabs-nav');
        var $Jtabs = $nav.find('li');//所有tab页
        var tabsWidthNum = 0;//遍历的tab累计宽度
        var offsetVal = 0;//偏移值
        var tabShowWidth = tabWidth - tabButtonWidth;//视距宽度
        var xPoint = marginLeftVal-tabShowWidth;//去往目标的偏移量
        var tempWidth = 0;
        if(xPoint > 0){
            var $item  = $Jtabs.first();
            while (tabsWidthNum < xPoint) {//遍历tabs离目标最近的tabs的宽度
                tempWidth = $($item).outerWidth(true);
                tabsWidthNum += tempWidth;
                $item = $Jtabs.next();
            }
            offsetVal = tabsWidthNum;//离目标最近的tabs累计宽度便是偏移量
        }else{
            offsetVal = 0;
        };
        $('.am-nav-tabs').animate({
            marginLeft: -offsetVal + 'px'
        }, "fast");
    }
    locker.setIsLock(false);//解锁
}
/**
 * 查看tab右边的标签页
 */
function checkBarRight(){
    var locker = new TapLocker();
    locker.setIsLock(true);//锁起来
    if(locker.getIsLock()){

        var marginLeftVal = Math.abs(parseInt($('.am-nav-tabs').css('margin-left')));//左边距
        var tabWidth = $('#onebean-frame-container').outerWidth(true);//tab栏宽度
        var tabButtonWidth = $('.onebean-nav-buttons').outerWidth(true);//tab按钮栏宽度
        var $tab = $('#onebean-frame-container');
        var $nav = $tab.find('.am-tabs-nav');
        var $Jtabs = $nav.children('li');//tabs
        var tabsWidthSum = calSumWidth($Jtabs);//所有tabs宽度
        var tabsWidthNum = 0;//tabs的累计宽度
        var offsetVal = 0;//偏移值
        var tabShowWidth = tabWidth - tabButtonWidth;//tabs的视距宽度
        var leftLength = marginLeftVal+tabShowWidth;//最右边的tab距离最左端的偏移量
        var $item  = $Jtabs.first();
        while (tabsWidthNum < leftLength) {//遍历tabs获取视距内最右边的tab计算出偏移量
            var tempWidth = $($item).outerWidth(true);
            tabsWidthNum += tempWidth;
            offsetVal = ((marginLeftVal + tabShowWidth - tabsWidthNum) < 0 )
                ?(tabsWidthNum - tempWidth):(tabsWidthNum + tempWidth);
            $item = $Jtabs.next();
        }
        // if(true){//右边有余量的时候滚动
        if((tabsWidthSum - marginLeftVal - tabShowWidth)>0){//右边有余量的时候滚动
            $('.am-nav-tabs').animate({
                marginLeft: -offsetVal + 'px'
            }, "fast");
        }
    }
    locker.setIsLock(false);//解锁
}
//计算元素集合的总宽度
function calSumWidth(target) {
    var width = 0;
    $.each(target, function(index,item) {
        width += $(item).outerWidth(true);
    })
    return width;
}

/**
 * 单列模式的菜单锁
 */
var TapLocker = (function() {
    var instance;
    var TapLocker = function() {
        if (instance) {
            return instance;
        }
        return instance = this;
    };
    TapLocker.prototype.isLock = false;
    TapLocker.prototype.setIsLock = function(bool) {
        this.isLock = bool;
    };
    TapLocker.prototype.getIsLock = function() {
        return this.isLock;
    };
    return TapLocker;
})();

/**
 * 去除字符串空格
 * @param str
 * @returns {XML|string|void}
 * @constructor
 */
function trim(str){
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 是否是空字符串
 * @param str
 * @returns {boolean}
 */
function isEmptyStr(str) {
    if ( str == "" ) return true;
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    return re.test(str);
}


/**
 * 序列化子列表数据成json字符串
 */
$.fn.serializeChildListJson = function(parent){
    var $from  = this;
    var $trs = $from.find('.onebean-line-tr');
    var jsonArr = new Array();
    var tempValue = '';
    $trs.each(function(i,e){
        var $obj = new Object();
        var $inPuts = $(e).find('.onebean-child-list-item');
        $inPuts.each(function(index,element){
            var value = $(element).val();
            var name = $(element).attr('name');
            tempValue = '';
            if(value != null && Object.prototype.toString.call(value) == '[object Array]'){
                $(value).each(function(i_,e_){
                    tempValue += e_+",";
                });
                tempValue = tempValue.substr(0,tempValue.length-1);
                value = tempValue;
            }
            $obj[name] = value;
        });
        jsonArr.push($obj)
    });
    parent['childList']=jsonArr;
    return JSON.stringify(parent);
};

/**
 * 序列化from成json
 * @returns {{}}
 */
$.fn.serializeJson = function() {
    var arr = this.serializeArray();
    var json = {};
    arr.forEach(function(item) {
        var name = item.name;
        var value = trim(item.value);

        if (!json[name]) {
            json[name] = value;
        } else if ($.isArray(json[name])) {
            json[name].push(value);
        } else {
            json[name] = [json[name], value];
        }
    });

    var $radio = $('input[type=radio],input[type=checkbox]', this);
    var temp = {};
    $.each($radio, function () {
        if (!temp.hasOwnProperty(this.name)) {
            if ($("input[name='" + this.name + "']:checked").length == 0) {
                temp[this.name] = "";
                json[this.name] = this.value;
            }
        }
    });
    return json;
};


/**
 * 序列化子from成json 一对多关系
 * @returns {{}}
 */
function serializeChildFromJson(arr) {
    var $length;
    var $obj;
    var json = new Array()
    for (var Key in arr){
        if(typeof($length) != "undefined"){
            break;
        }
        $length = arr[Key].length;
    }

    for (var i=0;i<$length;i++){
        $obj = new Object();
        for (var key in arr){
            $obj[key] = arr[key][i];
        }
        json.push($obj);
    }
    return json;
}

/**
 * 路由页面
 * @param target
 */
function routingPage($url,$title) {
    $url = addCtxToUrl($url);
    var breadCrumbs = eachBreadCrumbs($url,$title,false);
    var operator = ($url.indexOf("?") != -1)?'&':'?';
    $url += operator+"breadCrumbsStr="+encodeURI(JSON.stringify(breadCrumbs));
    window.location.href = $url;
}

/**
 * 面包屑按钮点击事件
 */
$('body').on('click','.onebean-bread-crumbs-group a',function () {
    var $url = $(this).data('url');
    var $title = $(this).html();
    var breadCrumbs = eachBreadCrumbs($url,$title,false)
    var operator = ($url.indexOf("?") != -1)?'&':'?';
    $url += operator+"breadCrumbsStr="+encodeURI(JSON.stringify(breadCrumbs));
    window.location.href = $url;
})

/**
 * 遍历面包屑生成json 数组
 * @returns {Array}
 */
function eachBreadCrumbs($url,$title,$isStartPage) {
    var $temp;
    var breadCrumbs = justEachBreadCrumbs($isStartPage)
    $temp = {};
    $temp.breadCrumbsUrl = $url;
    $temp.breadCrumbsTitle = $title;
    breadCrumbs.push($temp)
    return breadCrumbs;
}

/**
 * 遍历面包屑生成json 数组
 * @returns {Array}
 */
function justEachBreadCrumbs($isStartPage) {
    var $temp;
    var breadCrumbs = new Array();
    if(!$isStartPage){
        var $breadCrumbs = $('.onebean-bread-crumbs-group').find('a');
        $.each($breadCrumbs,function (i,e) {
            $temp = {};
            $temp.breadCrumbsUrl = $(e).data('url');
            $temp.breadCrumbsTitle = $(e).html();
            breadCrumbs.push($temp)
        })
    }
    return breadCrumbs;
}


/**
 * 跳转地址
 * @param url
 */
function goUrl(url) {
    if(typeof(parent.location.href) != 'undefined'){
        parent.location.href = url;
        return;
    }
    window.location.href = url;
}


/**
 * 折叠树 递归函数入口
 * @param target
 */
function foldingChildTree(target) {
    var pid = $(target).parents('tr').data('id');
    foldingChildTree2ed(target,pid)
}

/**
 * 折叠树 递归函数主体
 * @param target
 * @param ppid
 */
function foldingChildTree2ed(target,ppid) {
    var pid = $(target).parents('tr').data('id');
    var $childTrees = $('#example-r').find($('tr[data-pid='+pid+']'));
    var $parent = $('#example-r').find($('tr[data-id='+pid+']'));
    var $temp;
    for(var i = 0;i<$childTrees.length;i++){
        $temp = $($childTrees.get(i));
        var isHide = $temp.attr("isHide");
        var hideChild = $parent.attr("hideChild");
        if(pid == ppid){   //直接操作子元素
            foldingChildTree3th($temp,target,isHide,$parent,ppid,hideChild,false,i)
        }else{//跨级操作派生元素
            if((isHide == "false" && $parent.attr("hideChild") == "false")|| //子元素未被隐藏
                (isHide == "true" && $parent.attr("hideChild") == "false")){//子元素被根隐藏,未被父元素隐藏
                foldingChildTree3th($temp,target,isHide,$parent,ppid,hideChild,true,i)
            }
        }
    }
}

/**
 * 折叠树 折叠操作方法
 * @param $temp
 * @param target
 * @param isHide
 * @param $parent
 * @param ppid
 * @param cross
 */
function foldingChildTree3th($temp,target,isHide,$parent,ppid,hideChild,cross,index) {
    $temp.toggle();
    var $icon = $(target).find('i');
    isHide = (isHide == "true")?false:true;
    hideChild = (hideChild == "true")?true:false;
    hideChild = (!cross && index == 0)?!hideChild:hideChild;
    if(hideChild){
        $icon.addClass('am-icon-folder');
        $icon.removeClass('am-icon-folder-open');
    }else {
        $icon.addClass('am-icon-folder-open');
        $icon.removeClass('am-icon-folder');
    }
    $temp.attr("isHide",isHide);
    $parent.attr("hideChild",hideChild);
    foldingChildTree2ed($temp.find('a'),ppid)
}

/**
 * 获取当前时间字符串 YYYY-MM-DD HH MM
 * @returns {string}
 */
function getTodayDataStr() {
    var myDate = new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1 < 10 ? "0" + (myDate.getMonth() + 1): myDate.getMonth() + 1;
    var day = myDate.getDate() < 10 ? "0" + myDate.getDate() : myDate.getDate();
    myDate.getFullYear()+'-'+month
    var dateStr = year + "-" + month + "-" + day+' 00:00:00';
    return dateStr
}

/**
 * 初始化时间选择控件
 */
function initDataPicker() {
    $('.onebean-data-picker-data').datetimepicker({
        language:  'zh-CN',
        format: 'yyyy-mm-dd hh:ii:00',
        autoclose: true,
        todayBtn: true,
        startDate: getTodayDataStr()
    });
    $('.onebean-data-picker-time').datetimepicker({
        language:  'zh-CN',
        format: 'yyyy-mm-dd hh:ii:00',
        autoclose: true,
        startView:1,
        minView:0,
        maxView:1
    });
}

/**
 * 反注释HTML代码块
 * @param domIDName
 */
function unComments(domIDName) {
    try {
        var content = document.getElementById(domIDName);
        content.innerHTML = content.innerHTML.replace('<!--', '').replace('-->', '');
    }catch (e) {
        //do nothing
    }
}

