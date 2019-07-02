package net.onebean.saas.portal.model;

import jdk.nashorn.internal.ir.annotations.Ignore;
import com.eakay.core.extend.FiledName;
import com.eakay.core.extend.IgnoreColumn;
import com.eakay.core.extend.TableName;
import com.eakay.core.model.BaseModel;
import com.eakay.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;

@TableName("code_database_table")
public class CodeDatabaseTable extends BaseModel{

	private Long parentMenuId;
	private String menuIcon;
	private String premName;
	private String mapping;
	private String tableNameGenerate;
	private String tableName;
	private String author;
	private String description;
	private Timestamp createTime;
	private String createTimeStr;
	private String generateScope;
	private String generateType;
	private String logicallyDelete;
	private String isSplitTable;
	private String tablePrefix;
	private List<CodeDatabaseField> childList;

	@FiledName("tablePrefix")
	public String getTablePrefix() {
		return tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	@FiledName("logicallyDelete")
	public String getLogicallyDelete() {
		return logicallyDelete;
	}
	public void setLogicallyDelete(String logicallyDelete) {
		this.logicallyDelete = logicallyDelete;
	}

	@FiledName("isSplitTable")
	public String getIsSplitTable() {
		return isSplitTable;
	}
	public void setIsSplitTable(String isSplitTable) {
		this.isSplitTable = isSplitTable;
	}

	@FiledName("tableName")
	public String getTableName(){
		return this.tableName;
	}
	public void setTableName(String tableName){
		 this.tableName = tableName;
	}

	@FiledName("author")
	public String getAuthor(){
		return this.author;
	}
	public void setAuthor(String author){
		 this.author = author;
	}

	@FiledName("description")
	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		 this.description = description;
	}

	@IgnoreColumn
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	@FiledName("generateScope")
	public String getGenerateScope() {
		return generateScope;
	}
	public void setGenerateScope(String generateScope) {
		this.generateScope = generateScope;
	}

	@FiledName("generateType")
	public String getGenerateType() {
		return generateType;
	}
	public void setGenerateType(String generateType) {
		this.generateType = generateType;
	}

	@Ignore
	public List<CodeDatabaseField> getChildList() {
		return childList;
	}
	public void setChildList(List<CodeDatabaseField> childList) {
		this.childList = childList;
	}

	@FiledName("parentMenuId")
	public Long getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(Long parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	@FiledName("menuIcon")
	public String getMenuIcon() {
		return menuIcon;
	}
	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	@FiledName("premName")
	public String getPremName() {
		return premName;
	}
	public void setPremName(String premName) {
		this.premName = premName;
	}

	@IgnoreColumn
	public String getMapping() {
		return mapping;
	}
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	@IgnoreColumn
	public String getTableNameGenerate() {
		return tableNameGenerate;
	}
	public void setTableNameGenerate(String tableNameGenerate) {
		this.tableNameGenerate = tableNameGenerate;
	}

	/**
	 * 创建时间
	 */
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