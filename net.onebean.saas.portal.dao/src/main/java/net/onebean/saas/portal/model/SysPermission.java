package net.onebean.saas.portal.model;

import net.onebean.core.extend.FiledName;
import net.onebean.core.extend.IgnoreColumn;
import net.onebean.core.extend.TableName;
import net.onebean.core.model.BaseModel;
import net.onebean.core.model.InterfaceBaseSplitModel;

import java.sql.Timestamp;
import java.util.List;

@TableName("sys_permission_")
public class SysPermission extends BaseModel implements InterfaceBaseSplitModel {

	private String name;
	@FiledName("name")
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		 this.name = name;
	}

	private String descritpion;
	@FiledName("descritpion")
	public String getDescritpion(){
		return this.descritpion;
	}
	public void setDescritpion(String descritpion){
		 this.descritpion = descritpion;
	}

	private String url;
	@FiledName("url")
	public String getUrl(){
		return this.url;
	}
	public void setUrl(String url){
		 this.url = url;
	}

	private Long parentId;
	@FiledName("parentId")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	private Integer sort;
	private String remark;
	private String menuType;
	@FiledName("sort")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	@FiledName("remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@FiledName("menuType")
	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	private String isRoot;
	@FiledName("isRoot")
	public String getIsRoot() {
		isRoot = (null == isRoot)?"0":isRoot;
		return isRoot;
	}
	public void setIsRoot(String isRoot) {
		this.isRoot = isRoot;
	}

	private Boolean hasChild;
	@IgnoreColumn
	public Boolean getHasChild() {
		return hasChild;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}

	private List<SysPermission> childList;
	@IgnoreColumn
	public List<SysPermission> getChildList() {
		return childList;
	}

	public void setChildList(List<SysPermission> childList) {
		this.childList = childList;
	}

	private String icon;
	@FiledName("icon")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	private String parentIds;
	@FiledName("parentIds")
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	@FiledName("createTime")
	public Timestamp getCreateTime(){
		return this.createTime;
	}
	public void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}


	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	@FiledName("updateTime")
	public Timestamp getUpdateTime(){
		return this.updateTime;
	}
	public void setUpdateTime(Timestamp updateTime){
		this.updateTime = updateTime;
	}


	/**
	 * 操作人ID
	 */
	private Integer operatorId;
	@FiledName("operatorId")
	public Integer getOperatorId(){
		return this.operatorId;
	}
	public void setOperatorId(Integer operatorId){
		this.operatorId = operatorId;
	}


	/**
	 * 操作人姓名
	 */
	private String operatorName;
	@FiledName("operatorName")
	public String getOperatorName(){
		return this.operatorName;
	}
	public void setOperatorName(String operatorName){
		this.operatorName = operatorName;
	}


	/**
	 * 逻辑删除,0否1是
	 */
	private String isDeleted;
	@FiledName("isDeleted")
	public String getIsDeleted(){
		return this.isDeleted;
	}
	public void setIsDeleted(String isDeleted){
		this.isDeleted = isDeleted;
	}
}