package net.onebean.sodium.dao;

import net.onebean.core.base.BaseDao;
import net.onebean.sodium.model.TenantInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 0neBean
* @description 租户管理 Dao
* @date 2019-02-13 15:05:06
*/
public interface TenantInfoDao extends BaseDao<TenantInfo> {
    String isExistTenantInfo(@Param("tenantId") String tenantId);

    Integer initTenantDataBaseInfo(@Param("sqlStr") String sqlStr);

    void deleteByTenantIds(@Param("tenantIds") List<Long> deletedTenantIds);
}