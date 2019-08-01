package ${service_package_name};

<#if is_split_table == true>
import net.onebean.core.IBaseSplitBiz;
<#else>
import net.onebean.core.base.IBaseBiz;
</#if>
import ${model_package_name}.${model_name};


/**
* @author ${author}
* @description ${description} service
* @date ${create_time}
*/
<#if is_split_table == true>
public interface ${model_name}Service extends IBaseSplitBiz<${model_name}> {
<#else>
public interface ${model_name}Service extends IBaseBiz<${model_name}> {
</#if>

}