<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<!--通用head 其中加载了css-->
<head th:replace="public/head :: onLoadHead(${description}列表)"></head>

<body data-type="index">
<div class="am-g tpl-g">
    <!-- 通用头部 -->
    <header th:include="public/topBar :: topBar"></header>
    <!-- 主题选择组件 -->
    <div th:include="public/skiner :: skiner" class="tpl-skiner"></div>
    <!-- 公用左侧栏 -->
    <div th:include="public/leftMenu :: leftMenu" class="left-sidebar"></div>
    <!-- 模态提示组件 -->
    <div th:include="public/tips :: Tips"></div>
    <!-- 内容区域 -->
    <div class="tpl-content-wrapper">
        <div class="row-content am-cf">
            <div class="row">
                <div class="am-u-sm-12 am-u-md-12 am-u-lg-12">
                    <div class="widget am-cf">
                        <div class="widget-head am-cf">
                            <th:block th:include="public/breadCrumbs :: breadCrumbsTempl"/>
                        </div>
                        <div class="widget-body am-fr">
                            <form class="am-form tpl-form-border-form tpl-form-border-br paramFrom" onsubmit="return false;">
                                <div class="am-form-group">
                                <#if field_arr?exists>
                                    <#list field_arr as item>
                                        <#if item.isQueryParam == '1'>

                                        <label class="am-u-sm-1 am-form-label line-hight-10">${item.annotation} :</label>
                                        <div class="am-u-sm-3 am-u-end line-hight-10">
                                            <#if (item.isDicQueryParam?? && item.isDicQueryParam == '1')>
                                            <dic:code  code="YHLX" param-pattern="${item.columnName}@string@eq$"/>
                                            <#else>
                                            <input type="text" class="tpl-form-input paramInput" param-pattern="${item.columnName}@${item.queryOperator}$" placeholder="请输入${item.annotation}">
                                            </#if>
                                        </div>

                                        </#if>
                                    </#list>
                                </#if>


                                </div>

                                <label class="am-u-sm-12 am-u-md-6 am-u-lg-2 am-u-lg-offset-2 am-form-label">排序规则 :</label>
                                <div class="am-u-sm-12 am-u-md-6 am-u-lg-2">
                                    <div class="am-form-group tpl-table-list-select">
                                        <select data-am-selected="{btnSize: 'sm'}" id="orderBySelector">
                                            <option value="id-asc" selected>ID升序</option>
                                            <option value="id-desc">ID降序</option>
                                        </select>
                                    </div>
                                </div>


                                <label class="am-u-sm-12 am-u-md-6 am-u-lg-2 am-form-label">每页数量/条 :</label>
                                <div class="am-u-sm-12 am-u-md-6 am-u-lg-2">
                                    <div class="am-form-group tpl-table-list-select">
                                        <select data-am-selected="{btnSize: 'sm'}" id="limitSelector">
                                            <option value="5">5</option>
                                            <option value="10" selected>10</option>
                                            <option value="30">30</option>
                                            <option value="50">50</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="am-u-sm-12 am-u-md-6 am-u-lg-2">
                                    <div class="am-form-group" style="float: right">
                                        <div class="am-btn-toolbar">
                                            <div class="am-btn-group am-btn-group-xs">
                                                <th:block sec:authorize="hasPermission('$everyone','${premName}_ADD')">
                                                    <button type="button" class="am-btn am-btn-default am-btn-success" onclick="routingPage('/${mapping}/add','新增${description}')"><span class="am-icon-plus"></span> 新增</button>
                                                </th:block>
                                                <button type="button" class="am-btn am-btn-default am-btn-secondary query-button"><span class="am-icon-search"></span> 查询</button>
                                                <button type="button"  class="am-btn am-btn-default am-btn-warning reset-button"><span class="am-icon-archive"></span> 重置</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>

                            <div class="am-u-sm-12">
                                <table width="100%" class="am-table am-table-compact am-table-bordered am-table-radius am-table-striped tpl-table-black "
                                       id="example-r">
                                    <thead>
                                    <tr>
                            <#if field_arr?exists>
                                <#list field_arr as item>
                                    <#if item.columnName != 'id' && item.columnName != 'createTime' && item.columnName != 'updateTime' && item.columnName != 'isDeleted' && item.columnName != 'operatorId'  && item.columnName != 'operatorName'>
                                        <th>${item.annotation}</th>
                                    </#if>
                                </#list>
                            </#if>
                                    </tr>
                                    </thead>
                                    <tbody id="dataTable">

                                    </tbody>
                                </table>
                            </div>
                            <div class="am-u-lg-12 am-cf">
                                <div class="am-fr" id="pagination">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--加载JS-->
<js th:replace="public/js :: onLoadJS"></js>
<script type="text/javascript" >
    $(function () {
        initDataTable()
    });

    function initDataTable(){
        var pageSize = $("#limitSelector").val();
        var currentPage = $("#tpl-pagination").attr("currentPage");
        var orderBy = $("#orderBySelector").val().split('-')[0];
        var sort = $("#orderBySelector").val().split('-')[1];
        var param = {sort: {orderBy: orderBy, sort: sort}, page: {currentPage: currentPage,pageSize: pageSize}, data: formatQueryFromParam()};
        doPost("/${mapping}/list",param,function (res) {
            $('#dataTable').html(template('tpl-${mapping}', res.datas));
            $('#pagination').html(template('tpl-pagination', res.page))
        })
    }

</script>
<script id="tpl-${mapping}" type="text/html">
    {{each $data as data}}
    <tr  class={{if $index%2==0}} "gradeX" {{else}} "even gradeC" {{/if}} >
<#if field_arr?exists>
    <#list field_arr as item>
        <#if item.columnName != 'id' && item.columnName != 'createTime' && item.columnName != 'updateTime' && item.columnName != 'isDeleted' && item.columnName != 'operatorId'  && item.columnName != 'operatorName'>
            <td><#if (item_index == 1)><a href="javascript:;" onclick="routingPage('/${mapping}/view/{{data.id}}','查看${description}')"></#if>{{data.${item.columnName}}}<#if (item_index == 1)></a></#if></td>
        </#if>
    </#list>
</#if>
    <td>
        <div class="tpl-table-black-operation">

            <th:block sec:authorize="hasPermission('$everyone','${premName}_EDIT')">
                <a href="javascript:;" onclick="routingPage('/${mapping}/edit/{{data.id}}','编辑${description}')">
                    <i class="am-icon-pencil"></i> 编辑
                </a>
            </th:block>

            <th:block sec:authorize="hasPermission('$everyone','${premName}_DELETE')">
                <a href="javascript:;" data-url="/${mapping}/delete/{{data.id}}" class="tpl-table-black-operation-del list-del-button">
                    <i class="am-icon-trash" ></i> 删除
                </a>
            </th:block>

        </div>
    </td>
    </tr>
    {{/each}}
</script>
<script id="tpl-pagination" type="text/html" th:replace="public/pagination :: paginationTemplate"></script>
</body>
</html>