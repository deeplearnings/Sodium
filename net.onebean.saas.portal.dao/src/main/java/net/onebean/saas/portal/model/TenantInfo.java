package net.onebean.saas.portal.model;
import com.eakay.core.extend.FiledName;
import com.eakay.core.extend.TableName;
import com.eakay.core.model.BaseModel;
import com.eakay.core.model.InterfaceBaseDeletedModel;


import java.sql.Timestamp;

/**
* @author 0neBean
* @description 租户管理 model
* @date 2019-02-13 15:05:06
*/
@TableName("tenant_info")
public class TenantInfo extends BaseModel implements InterfaceBaseDeletedModel {


        /**
        * 租ID
        */
        private Integer tenantId;
        @FiledName("tenantId")
        public Integer getTenantId(){
            return this.tenantId;
        }
        public void setTenantId(Integer tenantId){
            this.tenantId = tenantId;
        }


        /**
        * 租户名
        */
        private String tenantName;
        @FiledName("tenantName")
        public String getTenantName(){
            return this.tenantName;
        }
        public void setTenantName(String tenantName){
            this.tenantName = tenantName;
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
        * 逻辑删除 0否  1是
        */
        private String isDeleted;
        @FiledName("isDeleted")
        public String getIsDeleted(){
            return this.isDeleted;
        }
        public void setIsDeleted(String isDeleted){
            this.isDeleted = isDeleted;
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
        * 操作人用户名
        */
        private String operatorName;
        @FiledName("operatorName")
        public String getOperatorName(){
            return this.operatorName;
        }
        public void setOperatorName(String operatorName){
            this.operatorName = operatorName;
        }



}