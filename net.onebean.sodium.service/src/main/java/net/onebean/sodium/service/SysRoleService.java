package net.onebean.sodium.service;

import net.onebean.sodium.model.SysRole;
import net.onebean.core.base.IBaseSplitBiz;

import java.util.List;

public interface SysRoleService extends IBaseSplitBiz <SysRole> {

    /**
     * 根据用户登录名查询用户所有角色
     * @author 0neBean
     * @param userId
     * @return List<SysRole>
     */
    List<SysRole> findRolesByUserId(Long userId);

    /**
     * 删除角色
     * @param id 主键
     * @return bool
     */
    Boolean deleteRole(Object id);

    /**
     * 根据角色名查找角色
     * @param name 角色名
     * @return list
     */
    List<SysRole> findByName(String name);

    /**
     * 添加用户角色关联
     * @param userId 用户id
     * @param roleIds 角色IDs
     * @return bool
     */
    Boolean addRoleUser(Long userId, String roleIds);

    /**
     * 移除户角色关联
     * @param urIds ids
     * @return bool
     */
    Boolean removeRoleUser(String urIds);
}