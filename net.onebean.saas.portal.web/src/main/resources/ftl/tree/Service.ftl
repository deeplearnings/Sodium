package ${service_package_name};

import com.eakay.core.IBaseBiz;
import ${model_package_name}.${model_name};
import ${vo_package_name}.${model_name}Tree;

import java.util.List;

/**
* @author ${author}
* @description ${description} service
* @date ${create_time}
*/
public interface ${model_name}Service extends IBaseBiz<${model_name}> {

    /**
     * 查找所有子节点
     * @author 0neBean
     * @param parentId
     * @return
     */
    List<${model_name}> findChildSync(Long parentId);

    /**
    * 异步查找子节点,每次查找一级
    * @author 0neBean
    * @param parentId
    * @return
    */
    List<${model_name}> findChildAsync(Long parentId,Long selfId);

    /**
    * 根据id删除自身以及自项
    * @param id
    */
    void deleteSelfAndChildById(Long id);

    /**
    * 根据父ID查找下一个排序值
    * @param parentId
    * @return
    */
    Integer findChildOrderNextNum(Long parentId);

    /**
     * 包装方法,将机构包装成treeList
     * @param before
     * @param selfId
     * @return
     */
    List<${model_name}Tree> model2Tree(List<${model_name}> before,Long selfId);
}