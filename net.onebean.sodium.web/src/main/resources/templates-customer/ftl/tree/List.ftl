<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<head th:replace="public/head :: onLoadHead(首页)">

</head>
<body data-type="widgets">
<div class="am-g tpl-g">
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
                                <div class="am-u-sm-12 am-u-md-6 am-u-lg-2">
                                    <div class="am-form-group" style="float: left">
                                        <div class="am-btn-toolbar">
                                            <div class="am-btn-group am-btn-group-xs">
                                                <th:block sec:authorize="hasPermission('$everyone','PERM_ORG_ADD')">
                                                    <button type="button" class="am-btn am-btn-default am-btn-success" onclick="routingPage('/${mapping}/add')"><span class="am-icon-plus"></span> 新增</button>
                                                </th:block>
                                                <button type="button" class="am-btn am-btn-default am-btn-secondary query-button"><span class="am-icon-search"></span> 刷新</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>

                            <div class="am-u-sm-12">
                                <table width="100%" class="am-table  am-table-bordered am-table-radius am-table-striped tpl-table-black " id="example-r">
                                    <thead>
                                    <tr>
                                        <th>标题</th>
                                        <th>排序</th>
                                        <th></th>
                                        <th>操作</th>
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

<css th:replace="public/css :: onLoadCSS"></css>
<js th:replace="public/js :: onLoadJS"></js>
<script type="text/javascript" >
    $(function () {
        initDataTable()
    })

    function initDataTable(){
        doGet("/${mapping}/aSyncTree",{},function (res) {
            $('#dataTable').html(template('tpl-${mapping}', res.data));
        })
    }
</script>



<!--主体模板-->
<script id="tpl-${mapping}" type="text/html">
    {{each $data as data}}
    <tr  class={{if $index%2==0}} "asadeX" {{else}} "even gradeC" {{/if}} data-id="{{data.id}}" isHide="false" hideChild="false">
    {{set blank = '&nbsp&nbsp&nbsp&nbsp'}}
    {{blank = blank+blank}}
    <td>
        {{if data.childList!=null}}
        <a onclick="foldingChildTree(this)">
            <i class="am-icon-folder-open"></i>
        </a>
        {{else}}
        <a href="javascript:;">
            <i class="am-icon-file"></i>
        </a>
        {{/if}}
        <a href="javascript:;" class="tree-span-title" onclick="routingPage('/${mapping}/view/{{data.id}}')">{{data.title}}</a>
    </td>
    <td>{{data.sort}}</td>
    <td>
        <div class="tpl-table-black-operation">
            <th:block sec:authorize="hasPermission('$everyone','PERM_ORG_ADD_CHILD')">
                <a href="javascript:;" onclick="routingPage('/${mapping}/addchild?parentId={{data.id}}')">
                    <i class="am-icon-pencil"></i> 添加下级机构
                </a>
            </th:block>
        </div>
    </td>
    <td>
        <div class="tpl-table-black-operation">
            <th:block sec:authorize="hasPermission('$everyone','PERM_ORG_EDIT')">
                <a href="javascript:;" onclick="routingPage('/${mapping}/edit/{{data.id}}')">
                    <i class="am-icon-pencil"></i> 编辑
                </a>
            </th:block>
            {{if data.id!=1 }}
            <th:block sec:authorize="hasPermission('$everyone','PERM_ORG_DELETE')">
                <a href="javascript:;" data-url="/${mapping}/delete/{{data.id}}" class="tpl-table-black-operation-del list-del-button">
                    <i class="am-icon-trash" ></i> 删除
                </a>
            </th:block>
            {{/if}}
        </div>
    </td>

    </tr>
    {{if data.childList!=null}}
    {{each data.childList as childList}}
    {{childList.pid = data.id}}
    {{childList.blank = blank}}
    {{include 'tpl-${mapping}-child' childList}}
    {{/each}}
    {{/if}}
    {{/each}}
</script>

<!--递归子模板-->
<script id="tpl-${mapping}-child" type="text/html">
    <tr  class={{if $index%2==0}} "gradeX" {{else}} "even gradeC" {{/if}} data-id="{{id}}" data-pid="{{pid}}" isHide="false" hideChild="false">
    <td>
        {{#blank}}
        {{if childList!=null}}
        <a onclick="foldingChildTree(this)">
            <i class="am-icon-folder-open"></i>
        </a>
        {{else}}
        <a href="javascript:;">
            <i class="am-icon-file"></i>
        </a>
        {{/if}}
        <a href="javascript:;" class="tree-span-title" onclick="routingPage('/${mapping}/view/{{id}}')">{{title}}</a>
    </td>
    <td>{{sort}}</td>
    <td>
        <div class="tpl-table-black-operation">
            <th:block sec:authorize="hasPermission('$everyone','PERM_ORG_ADD_CHILD')">
                <a href="javascript:;" onclick="routingPage('/${mapping}/addchild?parentId={{id}}')">
                    <i class="am-icon-pencil"></i> 添加下级机构
                </a>
            </th:block>
        </div>
    </td>
    <td>
        <div class="tpl-table-black-operation">
            <th:block sec:authorize="hasPermission('$everyone','PERM_ORG_EDIT')">
                <a href="javascript:;" onclick="routingPage('/${mapping}/edit/{{id}}')">
                    <i class="am-icon-pencil"></i> 编辑
                </a>
            </th:block>
            {{if id!=1 }}
            <th:block sec:authorize="hasPermission('$everyone','PERM_ORG_DELETE')">
                <a href="javascript:;" data-url="/${mapping}/delete/{{id}}" class="tpl-table-black-operation-del list-del-button">
                    <i class="am-icon-trash" ></i> 删除
                </a>
            </th:block>
            {{/if}}
        </div>
    </td>
    {{if childList!=null}}
    {{blank = blank+'&nbsp&nbsp&nbsp&nbsp'}}
    {{each childList as data}}
    {{data.pid = id}}
    {{data.blank = blank}}
    {{include 'tpl-${mapping}-child' data}}
    {{/each}}
    {{/if}}
    </tr>
</script>


</body>
</html>