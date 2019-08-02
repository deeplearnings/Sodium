$(function () {
    onLoadBreadCrumbs();
    initDataPicker();
    unComments('tpl-pagination');//去除分页注释
});

// 设置jQuery Ajax全局错误catch
$.ajaxSetup({
    type: "POST",
    error: function (jqXHR, textStatus, errorThrown) {
        switch (jqXHR.status) {
            case(500):
                errorCheckOnOffLine(true, jqXHR, "500");
                break;
            case(400):
                errorCheckOnOffLine(true, jqXHR, "400");
                break;
            case(401):
                errorCheckOnOffLine(true, jqXHR, "401");
                break;
            case(403):
                errorCheckOnOffLine(true, jqXHR, "403");
                break;
            case(404):
                errorCheckOnOffLine(false, jqXHR, "404");
                break;
            case(408):
                errorCheckOnOffLine(false, jqXHR, "408");
                break;
            case(518):
                errorCheckOnOffLine(false, jqXHR, "518");
                break;
            case(501):
                errorCheckOnOffLine(false, jqXHR, "501");
                break;
            case(200):
                errorCheckOnOffLine(false, jqXHR, "200");
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
function errorCheckOnOffLine(mode, jqXHR, code) {
    if (jqXHR.status == 500) {
        alert("挂了,挂了,朕的服务挂了,大清亡了!");
        return;
    }
    if (jqXHR.status == 200 && jqXHR.responseText.indexOf("登录页面")) {
        $('#alert-modal').on('closed.modal.amui', function () {
            $(this).off("closed.modal.amui");
            parent.location.reload();
        });
        alert("登录已失效,请重新登陆!");
        return;
    }
    if (mode) {
        window.location.href = "/error/" + code;
        return;
    }
    if ((typeof (jqXHR.responseJSON) != 'undefined')) {
        window.location.href = "/error/" + code;
    } else {
        error();
    }
}

/**
 * 加工项目前缀
 * @param $url
 * @returns {*}
 */
function addCtxToUrl($url) {
    var $ctx = $('title').attr('data-ctx');
    if (typeof ($ctx) === "undefined") {
        $ctx = "";
    }
    return $ctx + $url;
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
function clearUploadImg(target) {
    var $group = $(target).parents('.am-form-group');
    var $img = $group.find('img');
    $img.attr('src', '');
    var $input = $group.find('input');
    $input.val('');

}


/**
 * 面包屑加载
 */
var onLoadBreadCrumbs = function () {
    var $link = window.location.pathname;
    var $name = $(document).attr("title");
    /*面包屑*/
    var breadCrumbs = eachBreadCrumbs($link, $name, false);
    delCookie('breadCrumbsStr');
    setCookie('breadCrumbsStr', breadCrumbs);
    var $breadCrumbsArr = getCookie('breadCrumbsStr');
    try {
        $('.onebean-bread-crumbs-group').html(template('tpl-breadCrumbs', $breadCrumbsArr));
    } catch (e) {
    }
};

/**
 * 从菜单新开标签
 * @param target 触发按钮自身
 */
var openNewTab = function (target) {
    var $link = $(target).attr('data-url');
    var $name = $(target).attr('data-name');
    /*面包屑*/
    var breadCrumbs = eachBreadCrumbs($link, $name, true);
    delCookie('breadCrumbsStr');
    setCookie('breadCrumbsStr', breadCrumbs);
    window.location.href = $link;
};

/**
 * 路由页面
 * @param target
 */
function routingPage($url, $title) {
    $url = addCtxToUrl($url);
    var breadCrumbs = eachBreadCrumbs($url, $title, false);
    delCookie('breadCrumbsStr');
    setCookie('breadCrumbsStr', breadCrumbs);
    window.location.href = $url;
}

/**
 * 面包屑按钮点击事件
 */
$('body').on('click', '.onebean-bread-crumbs-group a', function () {
    var $url = $(this).attr('data-url');
    var $title = $(this).html();
    var breadCrumbs = eachBreadCrumbs($url, $title, false);
    delCookie('breadCrumbsStr');
    setCookie('breadCrumbsStr', breadCrumbs);
    window.location.href = $url;
});

/**
 * 激活菜单选中状态
 */
function activeMenuOnLoad() {
    var $link = window.location.pathname;
    var $breadCrumbsArr = getCookie('breadCrumbsStr');
    if ($breadCrumbsArr && $breadCrumbsArr.length > 1) {
        $link = $breadCrumbsArr[0].breadCrumbsUrl;
    }
    var endWithNumber = /^\S+([0-9]|\/){1}$/;
    if (endWithNumber.test($link)) {
        var $index = $link.lastIndexOf("\/");
        $link = $link.substring(0, $index);
    }
    var $parents = $('.sidebar-nav').find('.sidebar-nav-link').find('a');
    $.each($parents, function (i, e) {
        var $dataUrl = $(e).attr('data-url');
        if ($dataUrl === $link) {
            var $parentsMenu = $(e).parents('.parent-menu').children('.sidebar-nav-sub-title');//父级菜单
            if ($parentsMenu.length > 0) {
                $parentsMenu.click();
            } else {
                $('#indexMenuBtn').addClass('active');
            }
        }
    });
}

/**
 * 清除菜单中所有选中状态
 */
function clearMenuActive() {
    var menus = $('.sidebar-nav').find('a');
    $.each(menus, function (i, e) {
        if ($(e).hasClass('active'))
            $(e).removeClass('active');
    })
}

/**
 * 同时只能展开一个菜单
 */
$('body').on('click', '.sidebar-nav-sub-title', function () {
    $(this).attr('status', 'open');
    var subs = $('.sidebar-nav').find('.sidebar-nav-sub-ico');
    var menuA;
    $.each(subs, function (i, e) {
        menuA = $(e).parent('a');
        if ($(e).hasClass('sidebar-nav-sub-ico-rotate') && menuA.attr('status') != 'open') {
            menuA.click();
        }
    });
    $(this).attr('status', 'close');
    clearMenuActive();
    $(this).addClass('active');
});

/**
 * 刪除一行tr按钮
 */
$('body').on('click', '.del-line-btn', function () {
    $(this).parents('.onebean-line-tr').remove();
});


/**
 *switch按钮切换值替换成0/1
 */
$('body').on('switchChange.bootstrapSwitch init.bootstrapSwitch', 'input[data-am-switch]', function (event, state) {
    if (state) {
        $(this).val("1");
    } else {
        $(this).val("0");
    }
});


/**
 * list页面搜索条件里的dic选择框变动后 重新加载页面
 */
$('body').on('change', '.onebean-param-select-box', function () {
    resetPageNumber();
    initDataTable();
});

/**
 * 查询按钮 点击事件
 * button[type=query]
 */
$('body').on('click', '.query-button', function () {
    resetPageNumber();
    initDataTable();
});

/**
 * 分页 数字页码 点击事件
 */
$('body').on('click', '.pagination-number', function () {
    var choosePage = $(this).children('a').html();
    $("#tpl-pagination").attr("currentPage", choosePage)
    initDataTable();
})

/**
 * 分页 返回首页 点击事件
 */
$('body').on('click', '.pagination-frist', function () {
    resetPageNumber();
    initDataTable();
})

/**
 * 分页 返回最后一页 点击事件
 */
$('body').on('click', '.pagination-end', function () {
    var choosePage = $("#tpl-pagination").attr("totalPages");
    $("#tpl-pagination").attr("currentPage", choosePage)
    initDataTable();
})

/**
 * 分页 下一页 点击事件
 */
$('body').on('click', '.pagination-next', function () {
    var choosePage = parseInt($("#tpl-pagination").attr("currentPage")) + 1;
    $("#tpl-pagination").attr("currentPage", choosePage)
    initDataTable();
})

/**
 * 分页 上一页 点击事件
 */
$('body').on('click', '.pagination-previous', function () {
    var choosePage = parseInt($("#tpl-pagination").attr("currentPage")) - 1;
    $("#tpl-pagination").attr("currentPage", choosePage)
    initDataTable();
});

/**
 * 重置筛选条件表单
 */
$(".reset-button").on("click", function () {
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
$(".paramFrom").keydown(function (e) {
    var e = e || event,
        keycode = e.which || e.keyCode;
    if (keycode == 13) {
        initDataTable()
    }
});

/**
 * 重置页码
 */
function resetPageNumber() {
    $("#tpl-pagination").attr("currentPage", 1)
}


/**
 * 初始化异步加载树
 * @param title
 * @param selfId
 * @param url
 */
function initTreeAsyncSingleSelect(title, selfId, url) {
    $('.am-popup-title').html(title);
    $treeTemplate = $('#tree-template');
    $treeTemplate.tree({
        dataSource: function (options, callback) {
            if (options.type === "folder" || typeof (options.type) === "undefined") {
                if (options.childList != null && options.childList.length > 0) {
                    callback({data: options.childList});
                } else {
                    doPost(url,{data:{parentId: options.id, selfId: selfId}}, function (res) {
                        callback({data: res.datas});
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
function initTreeSyncMultiSelect(title, roleId, url, $treeTemplate) {
    $('.am-popup-title').html(title);
    $treeTemplate.tree({
        dataSource: function (options, callback) {
            if (options.type === "folder" || typeof (options.type) === "undefined") {
                if (options.childList != null && typeof (options.childList) === "object") {
                    callback({data: options.childList});
                } else {
                    doPost(url, {data: roleId}, function (res) {
                        callback({data: res.datas});
                        $treeTemplate.tree('selectItemSafe', $('.selected-item'));
                    })
                }

            }
        },
        cacheItems: true,
        multiSelect: true,
        folderSelect: true,
        itemSelect: true,
        itemSelectedIcon: 'file',
        folderIcon: 'folder',
        folderOpenIcon: 'folder-open',
        itemIcon: 'file'
    });
    // $treeTemplate.on('loaded.tree.amui', function (event, data) {
    //     debugger
    //     $treeTemplate.tree('selectItemSafe', $('.selected-item'));
    // });

    $treeTemplate.on('updated.tree.amui', function (event, data) {
        var childs = $(data.item).find('.am-tree-item,.am-tree-branch');
        var parents = $(data.item).parents('.am-tree-branch');

        //遍历父节点
        $.each(parents, function (index, item) {
            if (data.eventType === "selected") {
                if (!$(item).is('.am-tree-selected')) {
                    $treeTemplate.tree('selectItemSafe', $(item));
                }
            }
        });

        //遍历子节点
        $.each(childs, function (index, item) {
            if (data.eventType === "selected") {
                if (!$(item).is('.am-tree-selected')) {
                    $treeTemplate.tree('selectItemSafe', $(item));
                }
            } else if (data.eventType === "deselected") {
                if ($(item).is('.am-tree-selected')) {
                    $treeTemplate.tree('selectItemSafe', $(item));
                }
            }
        })
    });
}


/**
 * 页面 通用 数据数量 改变事件
 * #limitSelector
 */
$("#limitSelector").on("change", function () {
    resetPageNumber();
    initDataTable();
});


/**
 * 页面 通用 排序方式 改变事件
 * #orderBySelector
 */
$("#orderBySelector").on("change", function () {
    initDataTable()
});

/**
 * 数据列表 删除按钮
 */
$('body').on('click', '.list-del-button', function () {
    $(".confirm-modal-title").html("警告");
    $(".confirm-modal-message").html("你，确定要删除这条记录吗？");
    $(".confirm-modal-btn-cancel").html("取消");
    $(".confirm-modal-btn-confirm").html("确定");
    $('#confirm-modal').modal({
        relatedTarget: this,
        onConfirm: function () {
            var completeHandler = function (res) {
                if (res.errCode === '0') {
                    initDataTable();
                } else {
                    alert(res.errMsg)
                }
            };
            var $link = $(this.relatedTarget).attr('data-url');
            doPost($link, null, completeHandler)
        },
        onCancel: function () {
        }
    });
});

/**
 * Http post 请求
 * @param url 请求地址
 * @param param 请求参数
 * @param completeHandler 回调函数
 */
function doPost(url, param, completeHandler) {
    url = addCtxToUrl(url);
    $.AMUI.progress.start();
    $.ajax({
        url: url,
        type: "POST",//请求方式
        contentType: 'application/json;charset=utf-8',
        dataType: "json",//返回参数的类型
        data: JSON.stringify(param),
        success: function (data) {//请求成功后调用的函数
            if (data.errCode === '0'){
                completeHandler(data);
            }else{
                alert("请求异常,错误码 ["+data.errCode+"],"+"错误信息 ["+data.errMsg+"]");
            }
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
function doGet(url, param, completeHandler) {
    url = addCtxToUrl(url);
    $.AMUI.progress.start();
    $.ajax({
        url: url,
        type: "GET",//请求方式
        dataType: "json",//返回参数的类型
        contentType: "utf-8",//发送请求的编码方式
        data: param,
        success: function (data) {//请求成功后调用的函数
            completeHandler(data);
            $.AMUI.progress.done();
        }
    })
}

/**
 * Http get html 请求
 * @param url 请求地址
 * @param param 请求参数
 * @param completeHandler 回调函数
 */
function doGetHtml(url, param, completeHandler) {
    url = addCtxToUrl(url);
    $.ajax({
        url: url,
        type: "GET",//请求方式
        dataType: "html",//返回参数的类型
        contentType: "utf-8",//发送请求的编码方式
        data: param,
        success: function (data) {//请求成功后调用的函数
            completeHandler(data);
        }
    })
}



/**
 * 统一的弹框提示
 */
function alert(message) {
    $(".alert-modal-message").html(message);
    $(".alert-modal-title").html("提示");
    $(".alert-modal-button").html("好哒");
    $('#alert-modal').modal('open');
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
function uploadFile(completeHandler, target) {
    var disabled = $(target).attr('disabled');
    if (disabled == 'disabled') {
        return;
    }
    initUploadModal(completeHandler);
    $('#upload-modal').modal('open');

}


/*初始化上传文件控件模态窗*/
function initUploadModal(completeHandler) {
    var $uploadTips = $('#upload-modal');
    $uploadTips.html(template('tpl-uploadTips', null));
    var $link = '/upload/uploadMultipartFile';
    $link = addCtxToUrl($link);
    var upload = $('#uploadFileWidget').AmazeuiUpload({
        url: $link,
        downloadUrl: '/upload/downfile',
        maxFiles: 5, // 单次上传的数量
        maxFileSize: 10, // 单个文件允许的大小 (M)
        multiThreading: false, // true为同时上传false为队列上传
        useDefTemplate: true, //是否使用表格模式
        dropType: true, //是否允许拖拽
        pasteType: false //是否允许粘贴
    });
    upload.init(); //对象初始化

    /*监听弹框关闭事件*/
    $('body').on('close.modal.amui', '#upload-modal', function () {
        upload.destory();
    });
    /*上传文件控件确认按钮事件*/
    $('body').on("click", '.upload-file-submit', function () {
        var arr = upload.selectResult();
        if (arr.length > 0) {
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
    for (var i = 0; i < paramList.length; i++) {
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
 * 去除字符串空格
 * @param str
 * @returns {XML|string|void}
 * @constructor
 */
function trim(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 是否是空字符串
 * @param str
 * @returns {boolean}
 */
function isEmptyStr(str) {
    if (str == "") return true;
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    return re.test(str);
}


/**
 * 序列化子列表数据成json字符串
 */
$.fn.serializeChildListJson = function (parent) {
    var $from = this;
    var $trs = $from.find('.onebean-line-tr');
    var jsonArr = new Array();
    var tempValue = '';
    $trs.each(function (i, e) {
        var $obj = new Object();
        var $inPuts = $(e).find('.onebean-child-list-item');
        $inPuts.each(function (index, element) {
            var value = $(element).val();
            var name = $(element).attr('name');
            tempValue = '';
            if (value != null && Object.prototype.toString.call(value) == '[object Array]') {
                $(value).each(function (i_, e_) {
                    tempValue += e_ + ",";
                });
                tempValue = tempValue.substr(0, tempValue.length - 1);
                value = tempValue;
            }
            $obj[name] = value;
        });
        jsonArr.push($obj)
    });
    parent['childList'] = jsonArr;
    return parent;
};

/**
 * 序列化from成json
 * @returns {{}}
 */
$.fn.serializeJson = function () {
    var arr = this.serializeArray();
    var json = {};
    arr.forEach(function (item) {
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
    for (var Key in arr) {
        if (typeof ($length) != "undefined") {
            break;
        }
        $length = arr[Key].length;
    }

    for (var i = 0; i < $length; i++) {
        $obj = new Object();
        for (var key in arr) {
            $obj[key] = arr[key][i];
        }
        json.push($obj);
    }
    return json;
}


/**
 * 遍历面包屑生成json 数组
 * @returns {Array}
 */
function eachBreadCrumbs($url, $title, $isStartPage) {

    /*拆除以斜杠结尾的url的斜杠*/
    var endWithNumber = /^\S+[\/]{1}$/;
    if (endWithNumber.test($url)) {
        var $index = $url.lastIndexOf("\/");
        $url = $url.substring(0, $index);
    }

    var $temp;
    var breadCrumbs = justEachBreadCrumbs($isStartPage, $url);
    $temp = {};
    $temp.breadCrumbsUrl = $url;
    $temp.breadCrumbsTitle = $title;
    breadCrumbs.push($temp);
    return breadCrumbs;
}

/**
 * 遍历面包屑生成json 数组
 * @returns {Array}
 */
function justEachBreadCrumbs($isStartPage, $url) {
    var $breadCrumbs = [];
    if (!$isStartPage) {
        $breadCrumbs = getCookie("breadCrumbsStr");
        $.each($breadCrumbs, function (i, e) {
            if ($url === e.breadCrumbsUrl) {
                $breadCrumbs = $breadCrumbs.slice(0, i);
            }
        })
    }
    return $breadCrumbs;
}


/**
 * 跳转地址
 * @param url
 */
function goUrl(url) {
    window.location.href = url;
}


/**
 * 折叠树 递归函数入口
 * @param target
 */
function foldingChildTree(target) {
    var pid = $(target).parents('tr').attr('data-id');
    foldingChildTree2ed(target, pid)
}

/**
 * 折叠树 递归函数主体
 * @param target
 * @param ppid
 */
function foldingChildTree2ed(target, ppid) {
    var pid = $(target).parents('tr').attr('data-id');
    var $childTrees = $('#example-r').find($('tr[data-pid=' + pid + ']'));
    var $parent = $('#example-r').find($('tr[data-id=' + pid + ']'));
    var $temp;
    for (var i = 0; i < $childTrees.length; i++) {
        $temp = $($childTrees.get(i));
        var isHide = $temp.attr("isHide");
        var hideChild = $parent.attr("hideChild");
        if (pid == ppid) {   //直接操作子元素
            foldingChildTree3th($temp, target, isHide, $parent, ppid, hideChild, false, i)
        } else {//跨级操作派生元素
            if ((isHide == "false" && $parent.attr("hideChild") == "false") || //子元素未被隐藏
                (isHide == "true" && $parent.attr("hideChild") == "false")) {//子元素被根隐藏,未被父元素隐藏
                foldingChildTree3th($temp, target, isHide, $parent, ppid, hideChild, true, i)
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
function foldingChildTree3th($temp, target, isHide, $parent, ppid, hideChild, cross, index) {
    $temp.toggle();
    var $icon = $(target).find('i');
    isHide = (isHide == "true") ? false : true;
    hideChild = (hideChild == "true") ? true : false;
    hideChild = (!cross && index == 0) ? !hideChild : hideChild;
    if (hideChild) {
        $icon.addClass('am-icon-folder');
        $icon.removeClass('am-icon-folder-open');
    } else {
        $icon.addClass('am-icon-folder-open');
        $icon.removeClass('am-icon-folder');
    }
    $temp.attr("isHide", isHide);
    $parent.attr("hideChild", hideChild);
    foldingChildTree2ed($temp.find('a'), ppid)
}

/**
 * 获取当前时间字符串 YYYY-MM-DD HH MM
 * @returns {string}
 */
function getTodayDataStr() {
    var myDate = new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1 < 10 ? "0" + (myDate.getMonth() + 1) : myDate.getMonth() + 1;
    var day = myDate.getDate() < 10 ? "0" + myDate.getDate() : myDate.getDate();
    myDate.getFullYear() + '-' + month
    var dateStr = year + "-" + month + "-" + day + ' 00:00:00';
    return dateStr
}

/**
 * 初始化时间选择控件
 */
function initDataPicker() {
    $('.onebean-data-picker-data').datetimepicker({
        language: 'zh-CN',
        format: 'yyyy-mm-dd hh:ii:00',
        autoclose: true,
        todayBtn: true,
        startDate: getTodayDataStr()
    });
    $('.onebean-data-picker-time').datetimepicker({
        language: 'zh-CN',
        format: 'yyyy-mm-dd hh:ii:00',
        autoclose: true,
        startView: 1,
        minView: 0,
        maxView: 1
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
    } catch (e) {
        //do nothing
    }
}

/**
 * 删除cookies
 * @param name
 */
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString() + ";path=/";
}

/**
 * 读取cookies
 * @param name
 * @returns {*}
 */
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return JSON.parse(decodeURIComponent(arr[2]));
    else
        return null;
}

/**
 * 写cookies
 * @param name
 * @param value
 */
function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + encodeURIComponent(JSON.stringify(value)) + ";expires=" + exp.toGMTString() + ";path=/";
}