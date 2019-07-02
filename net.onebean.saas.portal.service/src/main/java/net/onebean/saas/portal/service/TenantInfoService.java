package net.onebean.saas.portal.service;

import net.onebean.saas.portal.model.TenantInfo;
import com.eakay.core.IBaseBiz;

import java.util.List;


/**
* @author 0neBean
* @description 租户管理 service
* @date 2019-02-13 15:05:06
*/
public interface TenantInfoService extends IBaseBiz<TenantInfo> {
    /**
     * 初始化租户表和mysql函数
     * @param tenantId 租户ID
     */
    void initTenantInfoTableAndFunctionBySql(String tenantId);

    /**
     * 根据租户ID删除数据
     * @param deletedTenantIds
     */
    void deleteByTenantIds(List<Long> deletedTenantIds);
}