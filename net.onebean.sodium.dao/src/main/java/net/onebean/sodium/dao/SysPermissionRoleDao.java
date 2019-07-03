package net.onebean.sodium.dao;

import net.onebean.core.BaseSplitDao;
import net.onebean.sodium.model.SysPermissionRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysPermissionRoleDao extends BaseSplitDao <SysPermissionRole> {
    /**
     * 获取角色菜单
     * @param roleId
     * @return
     */
    List<SysPermissionRole> getRolePremissionByRoleId(@Param("roleId") Long roleId,@Param("tableSuffix")String tableSuffix);

    /**
     * 根据RoleId删除数据
     * @param roleId
     */
    void deteleByRoleId(@Param("roleId") Long roleId,@Param("tableSuffix")String tableSuffix);

    /**
     * 根据permissionId删除数据
     * @param permissionId
     */
    void deteleByPermissionId(@Param("permissionId") Long permissionId,@Param("tableSuffix")String tableSuffix);

    /**
     * 批量插入
     * @param list
     */
    void insertBatch(@Param("list") List<Map<String,Object>> list,@Param("tableSuffix")String tableSuffix);
}