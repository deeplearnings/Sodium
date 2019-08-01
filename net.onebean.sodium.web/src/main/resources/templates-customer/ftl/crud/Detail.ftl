<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<!--通用head 其中加载了css-->
<head th:replace="public/head :: onLoadHead(${description}管理)"></head>

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
                    <div class="row">
                        <div class="am-u-sm-12 am-u-md-12 am-u-lg-12">
                            <div class="widget am-cf">
                                <div class="widget-head am-cf">
                                    <th:block th:include="public/breadCrumbs :: breadCrumbsTempl"/>
                                </div>
                                <div class="widget-body am-fr">

                                    <form class="am-form tpl-form-border-form tpl-form-border-br" id="DataFrom">
                                                <input type="hidden" name="id" th:value="${r"${entity.id}"}">

                                    <#if field_arr?exists>
                                        <#list field_arr as item>
                                            <#if item.columnName != 'id' && item.columnName != 'createTime' && item.columnName != 'updateTime' && item.columnName != 'isDeleted' && item.columnName != 'operatorId'  && item.columnName != 'operatorName'>
                                            <#if item.pageType == 'input_text'>
                                                <div class="am-form-group">
                                                    <label for="${item.columnName}" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">Text</span></label>
                                                    <div class="am-u-sm-9">
                                                        <input type="text" class="tpl-form-input" name="${item.columnName}" id="${item.columnName}" placeholder="请输入${item.annotation}" th:disabled="${r"${view}"}" th:value="${'$'}{entity.${item.columnName}}">
                                                        <small th:unless="${r"${view}"}"><#if (item.page_description??)>${item.page_description}<#else>${item.annotation}</#if></small>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_password'>
                                                <div class="am-form-group">
                                                    <label for="${item.columnName}" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">Password</span></label>
                                                    <div class="am-u-sm-9">
                                                        <input type="password" class="tpl-form-input" name="${item.columnName}" id="${item.columnName}" placeholder="请输入${item.annotation}" th:disabled="${r"${view}"}" th:value="${'$'}{entity.${item.columnName}}">
                                                        <small th:unless="${r"${view}"}"><#if (item.page_description??)>${item.page_description}<#else>${item.annotation}</#if></small>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_number'>
                                                <div class="am-form-group">
                                                    <label for="${item.columnName}" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">Number</span></label>
                                                    <div class="am-u-sm-9">
                                                        <input type="number" class="tpl-form-input" name="${item.columnName}" id="${item.columnName}" placeholder="请输入${item.annotation}" th:disabled="${r"${view}"}" th:value="${'$'}{entity.${item.columnName}}">
                                                        <small th:unless="${r"${view}"}"><#if (item.page_description??)>${item.page_description}<#else>${item.annotation}</#if></small>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_email'>
                                                <div class="am-form-group">
                                                    <label for="${item.columnName}" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">Email</span></label>
                                                    <div class="am-u-sm-9">
                                                        <input type="email" class="tpl-form-input" name="${item.columnName}" id="${item.columnName}" placeholder="请输入${item.annotation}" th:disabled="${r"${view}"}" th:value="${'$'}{entity.${item.columnName}}">
                                                        <small th:unless="${r"${view}"}"><#if (item.page_description??)>${item.page_description}<#else>${item.annotation}</#if></small>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_switch'>
                                                <div class="am-form-group">
                                                    <label for="isLock" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">Switch</span></label>
                                                    <div class="am-u-sm-9">
                                                        <input type="checkbox" data-am-switch data-size="xs" data-on-color="success" data-off-color="default" th:checked="${'$'}{entity.${item.columnName} eq '1'}" th:disabled="${r"${view}"}" name="${item.columnName}" id="${item.columnName}"/>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_dic'>
                                                <div class="am-form-group">
                                                    <label for="${item.columnName}" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">SelectBox</span></label>
                                                    <div class="am-u-sm-9">
                                                        <dic:code name="${item.columnName}" id="${item.columnName}" code="SF" th:attr="value=${'$'}{entity.${item.columnName}},disabled=${r"${view}"}"/>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_textarea'>
                                                <div class="am-form-group">
                                                    <label for="${item.columnName}" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">Text</span></label>
                                                    <div class="am-u-sm-9">
                                                        <textarea class="" rows="10" id="${item.columnName}" name="${item.columnName}" placeholder="请输入${item.annotation}"  th:disabled="${r"${view}"}" th:text="${'$'}{entity.${item.columnName}}"></textarea>
                                                        <small th:unless="${r"${view}"}"><#if (item.page_description??)>${item.page_description}<#else>${item.annotation}</#if></small>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_menu_tree'>
                                                <div class="am-form-group">
                                                    <label for="${item.columnName}" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">Tree</span></label>
                                                    <div class="am-u-sm-9">
                                                        <input type="hidden" name="${item.columnName}" id="${item.columnName}">
                                                        <tree:menu th:attr="disabled=${r"${view}"},businessInPutId='${item.columnName}',pid=${r"${pid}"}"  th:unless="${r"${add}"}"/>
                                                        <tree:menu th:attr="pid=${r"${pid}"},businessInPutId='${item.columnName}'" th:if="${r"${add}"}"/>
                                                        <small th:unless="${r"${view}"}"><#if (item.page_description??)>${item.page_description}<#else>${item.annotation}</#if></small>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_org_tree'>
                                                <div class="am-form-group">
                                                    <label for="orgId" class="am-u-sm-3 am-form-label">所属机构 <span class="tpl-form-line-small-title">Tree</span></label>
                                                    <div class="am-u-sm-9"  th:with="pid=(${'$'}{entity.${item.columnName}} != null)?${'$'}{entity.${item.columnName}}:1">
                                                        <input type="hidden" class="treeValue" name="${item.columnName}" id="${item.columnName}" th:value="${r"${pid}"}">
                                                        <tree:org th:attr="disabled=${r"${view}"},businessInPutId='${item.columnName}',pid=${r"${pid}"}"  th:unless="${r"${add}"}"/>
                                                        <tree:org th:attr="pid=${r"${pid}"},businessInPutId='${item.columnName}'" th:if="${r"${add}"}"/>
                                                        <small th:unless="${r"${view}"}">从机构树上选择一个机构</small>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_org_user_tree'>
                                                <div class="am-form-group">
                                                    <label for="${item.columnName}" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">Tree</span></label>
                                                    <div class="am-u-sm-9">
                                                        <input type="hidden" name="${item.columnName}" id="${item.columnName}">
                                                        <tree:org-user th:attr="disabled=${r"${view}"},businessInPutId='${item.columnName}',value=${'$'}{entity.${item.columnName}}"  th:unless="${r"${add}"}"/>
                                                        <tree:org-user th:attr="businessInPutId='${item.columnName}'" th:if="${r"${add}"}"/>
                                                        <small th:unless="${r"${view}"}"><#if (item.page_description??)>${item.page_description}<#else>${item.annotation}</#if></small>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_date_picker'>
                                                <div class="am-form-group">
                                                    <label for="${item.columnName}" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">Data</span></label>
                                                    <div class="am-u-sm-9">
                                                        <input type="text" class="onebean-data-picker-data" th:value="${'$'}{entity.${item.columnName}}" id="${item.columnName}" name="${item.columnName}" placeholder="请选择${item.annotation}">
                                                        <small th:unless="${r"${view}"}"><#if (item.page_description??)>${item.page_description}<#else>${item.annotation}</#if></small>
                                                    </div>
                                                </div>
                                            </#if>
                                            <#if item.pageType == 'input_time_picker'>
                                                <div class="am-form-group">
                                                    <label for="${item.columnName}" class="am-u-sm-3 am-form-label">${item.annotation} <span class="tpl-form-line-small-title">Time</span></label>
                                                    <div class="am-u-sm-9">
                                                        <input type="text" class="onebean-data-picker-time" th:value="${'$'}{entity.${item.columnName}}" id="${item.columnName}" name="${item.columnName}" placeholder="请选择${item.annotation}">
                                                        <small th:unless="${r"${view}"}"><#if (item.page_description??)>${item.page_description}<#else>${item.annotation}</#if></small>
                                                    </div>
                                                </div>
                                            </#if>
                                            </#if>
                                        </#list>
                                    </#if>
                                        <div class="am-form-group">
                                            <div class="am-u-sm-9 am-u-sm-push-3">
                                                <th:block sec:authorize="hasPermission('$everyone','${premName}_SAVE')">
                                                    <button type="submit" class="am-btn am-btn-primary tpl-btn-bg-color-success" th:unless="${r"${view}"}">提交</button>
                                                </th:block>

                                                <th:block sec:authorize="hasPermission('$everyone','${premName}_SAVE')">
                                                    <button type="button" class="am-btn am-btn-warning" th:onclick="'routingPage(\'/sysuser/edit/'+${r"${entity.id}"}+'\',\''+编辑+'\')'" th:if="${r"${view}"}">编辑</button>
                                                </th:block>
                                                <button type="button" class="am-btn am-btn-danger" onClick="routingPage('/${mapping}/preview/')">返回</button>
                                            </div>
                                        </div>
                                    </form>
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
<script th:inline="javascript">
    $(function () {
        validateFrom();
    });

    /**
     * 校验登录表单
     * @returns void
     */
    function validateFrom(){
        $("#DataFrom").validate({
            rules: {
        <#if field_arr?exists>
        <#list field_arr as item>
        <#switch item.pageType>
            <#case "input_org_user_tree">
            orgUserTree:{
                <#break>
            <#case "input_org_tree">
            orgTree:{
                <#break>
            <#case "input_menu_tree">
            menuTree:{
                <#break>
            <#default>
            ${item.columnName}:{
        </#switch>
            <#if item.validateArr?exists>
            <#list item.validateArr as v>
                ${v}:<#switch v><#case "minlength">5<#break><#case "maxlength">5<#break><#default>true</#switch><#if (v_index!=item.validateArr?size-1)>,</#if>
            </#list>
            </#if>
            }<#if (item_index!=field_arr?size-1)>,</#if>
        </#list>
        </#if>

            },
            submitHandler: function(form) { //验证成功时调用
                var param = $('#DataFrom').serializeJson();
                var url = "/${mapping}/save";
                var completeHandler = function (data) {
                    routingPage('/${mapping}/preview/','${description}管理');
                };
                doPost(url,param,completeHandler);
            }
        });
    }

</script>
</body>
</html>