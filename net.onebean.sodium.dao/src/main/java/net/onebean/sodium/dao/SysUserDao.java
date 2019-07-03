package net.onebean.sodium.dao;

import net.onebean.core.BaseSplitDao;
import net.onebean.sodium.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserDao extends BaseSplitDao <SysUser> {
    /**
     *根据用户名查询出用户及其拥有的所有的角色
     * @param username 用户名
     * @return SysUser
     */
    SysUser findByUsername(@Param("username")String username,@Param("tableSuffix")String tableSuffix);
    /**
     * 查询ids影响多少条数据
     * @param orgIds ids
     * @param tableSuffix 租户id
     * @return int
     */
    Integer countUserByIds(@Param("orgIds")List<String> orgIds,@Param("tableSuffix")String tableSuffix);
}