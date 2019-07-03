$(function() {

    autoLeftNav();
    $(window).resize(function() {
        autoLeftNav();
    });

});

// 风格切换
$('.tpl-skiner-toggle').on('click', function() {
    $('.tpl-skiner').toggleClass('active');
});

$('.tpl-skiner-content-bar').find('span').on('click', function() {
    var $chooseColor = $(this).attr('data-color');
    $('body').attr('class', $chooseColor);
    saveSelectColor.Color = $chooseColor;
    var $tab = $('#onebean-frame-container');
    var $bd = $tab.find('.am-tabs-bd');
    var $onebeanframes = $bd.find('iframe');
    $.each( $onebeanframes, function(index, item) {
        $(item).contents().find('body').attr('class', $chooseColor);
    });
    // 保存选择项
    storageSave(saveSelectColor);
});

// 侧边菜单开关
function autoLeftNav() {
    var $leftSidebar = $('.left-sidebar');
    $('.tpl-header-switch-button').on('click', function() {
        if ($leftSidebar.is('.active')) {
            if ($(window).width() > 1024) {
                $('.tpl-content-wrapper').removeClass('active');
            }
            $leftSidebar.removeClass('active');
        } else {

            $leftSidebar.addClass('active');
            if ($(window).width() > 1024) {
                $('.tpl-content-wrapper').addClass('active');
            }
        }
    });

    if ($(window).width() < 1024) {
        $leftSidebar.addClass('active');
    } else {
        $leftSidebar.removeClass('active');
    }
}


// 侧边菜单
$('body').on('click', '.sidebar-nav-sub-title',function() {
    $(this).siblings('.sidebar-nav-sub').slideToggle(80).end().find('.sidebar-nav-sub-ico').toggleClass('sidebar-nav-sub-ico-rotate');

});