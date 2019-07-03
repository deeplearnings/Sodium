package net.onebean.sodium.model;
import net.onebean.core.extend.FiledName;
import net.onebean.core.extend.TableName;
import net.onebean.core.model.BaseModel;
import net.onebean.core.model.InterfaceBaseSplitModel;

import java.sql.Timestamp;

@TableName("sys_role_user_")
public class SysRoleUser extends BaseModel implements InterfaceBaseSplitModel {

	public SysRoleUser() {
	}

	public SysRoleUser(Long sysUserId, Long sysRoleId) {
		this.sysUserId = sysUserId;
		this.sysRoleId = sysRoleId;
	}

	private Long sysUserId;
	@FiledName("sysUserId")
	public Long getSysUserId(){
		return this.sysUserId;
	}
	public void setSysUserId(Long sysUserId){
		 this.sysUserId = sysUserId;
	}

	private Long sysRoleId;
	@FiledName("sysRoleId")
	public Long getSysRoleId(){
		return this.sysRoleId;
	}
	public void setSysRoleId(Long sysRoleId){
		 this.sysRoleId = sysRoleId;
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