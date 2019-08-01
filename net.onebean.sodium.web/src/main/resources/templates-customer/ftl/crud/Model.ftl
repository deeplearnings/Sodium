package ${model_package_name};
import net.onebean.core.extend.FiledName;
import net.onebean.core.extend.TableName;
import net.onebean.core.model.BaseModel;
<#if generateType == 'tree'>
import net.onebean.core.extend.IgnoreColumn;
</#if>
<#if is_split_table == true>
import net.onebean.core.model.InterfaceBaseSplitModel;
<#elseif is_split_table == false && logically_delete == true>
import net.onebean.core.model.InterfaceBaseDeletedModel;
<#else>
import net.onebean.core.model.InterfaceBaseModel;
</#if>

<#if field_arr?exists>
    <#list field_arr as item>
        <#if item.databaseType == 'BigDecimal'>
import java.math.BigDecimal;
            <#break>
        </#if>
    </#list>
</#if>

import java.sql.Timestamp;

/**
* @author ${author}
* @description ${description} model
* @date ${create_time}
*/
<#if is_split_table == true>
@TableName("${table_prefix}")
<#else> 
@TableName("${model_name_original}")
</#if>
<#if is_split_table == true>
public class ${model_name} extends BaseModel implements InterfaceBaseSplitModel {
<#elseif is_split_table == false && logically_delete == true>
public class ${model_name} extends BaseModel implements InterfaceBaseDeletedModel {
<#else>
public class ${model_name} extends BaseModel implements InterfaceBaseModel {
</#if>


<#if field_arr?exists>
    <#list field_arr as item>
        <#if item.columnName != 'id'>
        /**
        * ${item.annotation}
        */
        private ${item.databaseType} ${item.columnName};
        @FiledName("${item.columnName}")
        public ${item.databaseType} get${item.methodName}(){
            return this.${item.columnName};
        }
        public void set${item.methodName}(${item.databaseType} ${item.columnName}){
            this.${item.columnName} = ${item.columnName};
        }


        </#if>
    </#list>
</#if>

<#if generateType == 'tree'>
	private Boolean hasChild;
	@IgnoreColumn
	public Boolean getHasChild() {
		return hasChild;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}

	private List<${model_name}> childList;
    @IgnoreColumn
    public List<${model_name}> getChildList() {
        return childList;
    }

    public void setChildList(List<${model_name}> childList) {
        this.childList = childList;
    }
</#if>
}