package net.onebean.saas.portal.model;
import com.eakay.core.extend.FiledName;
import com.eakay.core.extend.IgnoreColumn;
import com.eakay.core.extend.TableName;
import com.eakay.core.model.BaseModel;
import com.eakay.core.model.InterfaceBaseSplitModel;

import java.sql.Timestamp;
import java.util.List;

@TableName("sys_organization_")
public class SysOrganization extends BaseModel implements InterfaceBaseSplitModel {

	private Long parentId;
	@FiledName("parentId")
	public Long getParentId(){
		return this.parentId;
	}
	public void setParentId(Long parentId){
		 this.parentId = parentId;
	}

	private String isDelete;
	@FiledName("isDelete")
	public String getIsDelete() {
		isDelete = (null == isDelete)?"0":isDelete;
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
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

	private String orgName;
	@FiledName("orgName")
	public String getOrgName(){
		return this.orgName;
	}
	public void setOrgName(String orgName){
		 this.orgName = orgName;
	}

	private String remark;
	@FiledName("remark")
	public String getRemark(){
		return this.remark;
	}
	public void setRemark(String remark){
		 this.remark = remark;
	}

	private Timestamp createTime;
	private String createTimeStr;

	@FiledName("createTime")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	private Integer sort;
	@FiledName("sort")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@IgnoreColumn
	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}


	private Boolean hasChild;
	@IgnoreColumn
	public Boolean getHasChild() {
		return hasChild;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}

	private List<SysOrganization> childList;
	@IgnoreColumn
	public List<SysOrganization> getChildList() {
		return childList;
	}

	public void setChildList(List<SysOrganization> childList) {
		this.childList = childList;
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