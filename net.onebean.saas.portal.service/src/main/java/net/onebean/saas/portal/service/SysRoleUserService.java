package net.onebean.saas.portal.service;

import net.onebean.saas.portal.model.SysRoleUser;
import net.onebean.core.IBaseSplitBiz;

import java.util.List;

public interface SysRoleUserService extends IBaseSplitBiz <SysRoleUser> {
    /**
     * 根据用户id删除关联数据
     * @param userId
     */
    void deleteByUserId(Long userId);
    /**
     * 根据角色id删除关联数据
     * @param roleId
     */
    void deleteByRoleId(Long roleId);
    /**
     * 根据角色ID查找关联信息
     * @param roleId 角色ID
     * @return SysRoleUser
     */
    List<SysRoleUser> findbyRoleId(Object roleId);
}