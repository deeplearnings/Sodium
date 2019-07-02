package net.onebean.saas.portal.model;
import net.onebean.core.extend.FiledName;
import net.onebean.core.extend.TableName;
import net.onebean.core.model.BaseModel;
import net.onebean.core.model.InterfaceBaseSplitModel;

import java.sql.Timestamp;

@TableName("sys_permission_role_")
public class SysPermissionRole extends BaseModel implements InterfaceBaseSplitModel {

	private Long roleId;
	@FiledName("roleId")
	public Long getRoleId(){
		return this.roleId;
	}
	public void setRoleId(Long roleId){
		 this.roleId = roleId;
	}

	private Long permissionId;
	@FiledName("permissionId")
	public Long getPermissionId(){
		return this.permissionId;
	}
	public void setPermissionId(Long permissionId){
		 this.permissionId = permissionId;
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