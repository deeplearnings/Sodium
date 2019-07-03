package net.onebean.sodium.model;
import net.onebean.core.extend.FiledName;
import net.onebean.core.extend.IgnoreColumn;
import net.onebean.core.extend.TableName;
import net.onebean.core.model.BaseModel;
import net.onebean.util.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TableName("code_database_field")
public class CodeDatabaseField extends BaseModel{

	private Long tableId;
	private String columnName;
	private String databaseType;
	private String annotation;
	private Integer sort;
	private String pageType;
	private String pageValidate;
	private Timestamp createTime;
	private String methodName;
	private String isQueryParam;
	private String queryOperator;
	private String isDicQueryParam;
	private String[] validateArr;

	@FiledName("tableId")
	public Long getTableId() {
		return tableId;
	}
	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	@FiledName("columnName")
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@FiledName("databaseType")
	public String getDatabaseType() {
		return databaseType;
	}
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	@FiledName("annotation")
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	@FiledName("sort")
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@FiledName("pageType")
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	@FiledName("pageValidate")
	public String getPageValidate() {
		return pageValidate;
	}
	public void setPageValidate(String pageValidate) {
		this.pageValidate = pageValidate;
	}

	@FiledName("createTime")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@IgnoreColumn
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@FiledName("isQueryParam")
	public String getIsQueryParam() {
		return isQueryParam;
	}
	public void setIsQueryParam(String isQueryParam) {
		this.isQueryParam = isQueryParam;
	}

	@FiledName("queryOperator")
	public String getQueryOperator() {
		return queryOperator;
	}
	public void setQueryOperator(String queryOperator) {
		this.queryOperator = queryOperator;
	}

	@IgnoreColumn
	public String getIsDicQueryParam() {
		return isDicQueryParam;
	}
	public void setIsDicQueryParam(String isDicQueryParam) {
		this.isDicQueryParam = isDicQueryParam;
	}

	@IgnoreColumn
	public String[] getValidateArr() {
		return validateArr;
	}
	public void setValidateArr(String[] validateArr) {
		this.validateArr = validateArr;
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