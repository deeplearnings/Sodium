package net.onebean.saas.portal.service;

import net.onebean.saas.portal.model.SysRole;
import net.onebean.core.IBaseSplitBiz;

import java.util.List;

public interface SysRoleService extends IBaseSplitBiz <SysRole> {

    /**
     * 根据用户登录名查询用户所有角色
     * @author 0neBean
     * @param userId
     * @return List<SysRole>
     */
    public List<SysRole> findRolesByUserId(Long userId);
}