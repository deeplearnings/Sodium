package ${dao_package_name};

<#if is_split_table == true>
import net.onebean.core.base.BaseSplitDao;
<#else>
import net.onebean.core.base.BaseDao;
</#if>
import ${model_package_name}.${model_name};

/**
* @author ${author}
* @description ${description} Dao
* @date ${create_time}
*/
<#if is_split_table == true>
public interface ${model_name}Dao extends BaseSplitDao<${model_name}> {
<#else>
public interface ${model_name}Dao extends BaseDao<${model_name}> {
</#if>

}