package net.onebean.saas.portal.model;
import com.eakay.core.extend.FiledName;
import com.eakay.core.extend.TableName;
import com.eakay.core.model.BaseModel;
import java.io.Serializable;
import java.sql.Timestamp;

@TableName("dic_dictionary")
public class DicDictionary extends BaseModel{

	private String val;
	@FiledName("val")
	public String getVal(){
		return this.val;
	}
	public void setVal(String val){
		 this.val = val;
	}

	private String dic;
	@FiledName("dic")
	public String getDic(){
		return this.dic;
	}
	public void setDic(String dic){
		 this.dic = dic;
	}

	private String code;
	@FiledName("code")
	public String getCode(){
		return this.code;
	}
	public void setCode(String code){
		 this.code = code;
	}

	private String groupDic;
	@FiledName("groupDic")
	public String getGroupDic(){
		return this.groupDic;
	}
	public void setGroupDic(String groupDic){
		 this.groupDic = groupDic;
	}

	private Integer sort;
	@FiledName("sort")
	public Integer getSort(){
		return this.sort;
	}
	public void setSort(Integer sort){
		 this.sort = sort;
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