package net.onebean.sodium.VO;

import net.onebean.sodium.model.SysOrganization;
import net.onebean.core.BaseTree;

import java.util.List;

/**
 * 组织树模型
 */
public class OrgTree extends BaseTree {

    public final static String TYPE_ITEM = "item";
    public final static String TYPE_FOLDER = "folder";

    /**
     * ID
     */
    private Long id;
    private List<OrgTree> childList;
    private List<SysOrganization> dataList;

    public List<SysOrganization> getDataList() {
        return dataList;
    }

    public void setDataList(List<SysOrganization> dataList) {
        this.dataList = dataList;
    }

    public List<OrgTree> getChildList() {
        return childList;
    }

    public void setChildList(List<OrgTree> childList) {
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
