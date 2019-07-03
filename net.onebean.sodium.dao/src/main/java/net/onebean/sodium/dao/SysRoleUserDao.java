package net.onebean.sodium.dao;

import net.onebean.core.BaseSplitDao;
import net.onebean.sodium.model.SysRoleUser;
import org.apache.ibatis.annotations.Param;

public interface SysRoleUserDao extends BaseSplitDao <SysRoleUser> {
    /**
     * 根据用户id删除关联数据
     * @param userId
     */
    void deleteByUserId(@Param("userId")Long userId,@Param("tableSuffix")String tableSuffix);
    /**
     * 根据角色id删除关联数据
     * @param roleId
     */
    void deleteByRoleId(@Param("roleId")Long roleId,@Param("tableSuffix")String tableSuffix);
}