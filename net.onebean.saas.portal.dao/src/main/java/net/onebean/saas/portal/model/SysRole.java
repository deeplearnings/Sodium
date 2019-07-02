package net.onebean.saas.portal.model;
import com.eakay.core.extend.FiledName;
import com.eakay.core.extend.IgnoreColumn;
import com.eakay.core.extend.TableName;
import com.eakay.core.model.BaseModel;
import com.eakay.core.model.InterfaceBaseSplitModel;

import java.sql.Timestamp;

@TableName("sys_role_")
public class SysRole extends BaseModel implements InterfaceBaseSplitModel {

	private String name;
	private String chName;
	private String isDelete;
	private String isLock;
	private String remark;
	private Timestamp createTime;
	private String createTimeStr;
	private Long ruid;
	private String dataPermissionLevel;

	@IgnoreColumn
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	@FiledName("name")
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}

	@FiledName("chName")
	public String getChName() {
		return chName;
	}
	public void setChName(String chName) {
		this.chName = chName;
	}

	@FiledName("isDelete")
	public String getIsDelete() {
		isDelete = (null == isDelete)?"0":isDelete;
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@FiledName("isLock")
	public String getIsLock() {
		isLock = (null == isLock)?"0":isLock;
		return isLock;
	}
	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	@FiledName("remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@FiledName("createTime")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@IgnoreColumn
	public Long getRuid() {
		return ruid;
	}
	public void setRuid(Long ruid) {
		this.ruid = ruid;
	}

	@FiledName("dataPermissionLevel")
	public String getDataPermissionLevel() {
		return dataPermissionLevel;
	}
	public void setDataPermissionLevel(String dataPermissionLevel) {
		this.dataPermissionLevel = dataPermissionLevel;
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