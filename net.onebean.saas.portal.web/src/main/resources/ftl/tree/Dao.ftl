package ${dao_package_name};

import net.onebean.core.BaseDao;
import ${model_package_name}.${model_name};
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* @author ${author}
* @description ${description} Dao
* @date ${create_time}
*/
public interface ${model_name}Dao extends BaseDao<${model_name}> {

    /**
     * 异步查找子节点,每次查找一级
     * @param parentId 父级id
     * @return
     */
    List<${model_name}> findChildAsync(@Param("parentId") Long parentId);

    /**
    * 查找所有子节点
    * @param parentId 父级id
    * @return List<${model_name}>
    */
    List<${model_name}> findChildSync(@Param("parentId") Long parentId);

    /**
    * 获取所有父ID
    * @param childId
    * @return
    */
    String getParentIds(@Param("childId")Long childId);

    /**
    * 根据id删除自身以及自项
    * @param id
    */
    void deleteSelfAndChildById(@Param("id")Long id);

    /**
    * 根据父ID查找下一个排序值
    * @param parentId
    * @return
    */
    Integer findChildOrderNextNum(Long parentId);
}