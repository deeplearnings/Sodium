package net.onebean.sodium.model;
import net.onebean.core.extend.FiledName;
import net.onebean.core.extend.TableName;
import net.onebean.core.model.BaseModel;
import net.onebean.core.model.InterfaceBaseDeletedModel;

import java.math.BigDecimal;

import java.sql.Timestamp;

/**
* @author 0neBean
* @description 阿斯顿撒多 model
* @date 2019-08-01 13:39:40
*/
@TableName("test_code")
public class TestCode extends BaseModel implements InterfaceBaseDeletedModel {


        /**
        * 金钱
        */
        private BigDecimal money;
        @FiledName("money")
        public BigDecimal getMoney(){
            return this.money;
        }
        public void setMoney(BigDecimal money){
            this.money = money;
        }


        /**
        * 真名
        */
        private String realName;
        @FiledName("realName")
        public String getRealName(){
            return this.realName;
        }
        public void setRealName(String realName){
            this.realName = realName;
        }


        /**
        * 结束时间
        */
        private Timestamp endTime;
        @FiledName("endTime")
        public Timestamp getEndTime(){
            return this.endTime;
        }
        public void setEndTime(Timestamp endTime){
            this.endTime = endTime;
        }


        /**
        * 组织ID
        */
        private Integer orgId;
        @FiledName("orgId")
        public Integer getOrgId(){
            return this.orgId;
        }
        public void setOrgId(Integer orgId){
            this.orgId = orgId;
        }


        /**
        * 上级ID
        */
        private Integer pid;
        @FiledName("pid")
        public Integer getPid(){
            return this.pid;
        }
        public void setPid(Integer pid){
            this.pid = pid;
        }


        /**
        * 是否锁定
        */
        private String isLock;
        @FiledName("isLock")
        public String getIsLock(){
            return this.isLock;
        }
        public void setIsLock(String isLock){
            this.isLock = isLock;
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



}