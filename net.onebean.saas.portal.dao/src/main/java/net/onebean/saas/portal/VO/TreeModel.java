package net.onebean.saas.portal.VO;

import com.eakay.core.BaseTree;

import java.util.List;

/**
 * 组织树模型
 */
public class TreeModel extends BaseTree {

    public final static String TYPE_ITEM = "item";
    public final static String TYPE_FOLDER = "folder";

    /**
     * ID
     */
    private Long id;
    private List<Object> childList;
    private List<Object> dataList;

    public List<Object> getDataList() {
        return dataList;
    }

    public void setDataList(List<Object> dataList) {
        this.dataList = dataList;
    }

    public List<Object> getChildList() {
        return childList;
    }

    public void setChildList(List<Object> childList) {
        this.childList = childList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
