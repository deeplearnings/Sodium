package ${vo_package_name};

import net.onebean.core.base.BaseTree;
import ${model_package_name}.${model_name};

import java.util.List;


/**
* @author ${author}
* @description ${description} Tree VO
* @date ${create_time}
*/
public class ${model_name}Tree extends BaseTree {

    public final static String TYPE_ITEM = "item";
    public final static String TYPE_FOLDER = "folder";

    /**
    * ID
    */
    private Long id;
    private List<${model_name}Tree> childList;
    private List<${model_name}> dataList;

    public List<${model_name}> getDataList() {
        return dataList;
    }

    public void setDataList(List<${model_name}> dataList) {
        this.dataList = dataList;
    }

    public List<${model_name}Tree> getChildList() {
        return childList;
    }

    public void setChildList(List<${model_name}Tree> childList) {
        this.childList = childList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}