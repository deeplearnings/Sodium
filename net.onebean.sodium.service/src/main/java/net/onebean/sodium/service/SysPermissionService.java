package net.onebean.sodium.service;

import com.alibaba.fastjson.JSONArray;
import net.onebean.sodium.model.CodeDatabaseTable;
import net.onebean.sodium.model.SysPermission;
import net.onebean.sodium.model.SysUser;
import net.onebean.core.IBaseSplitBiz;
import net.onebean.sodium.VO.MenuTree;

import java.util.List;

public interface SysPermissionService extends IBaseSplitBiz <SysPermission> {
	/**
	 * 生成权限
	 * @param table 数据库表模型
	 */
	void generatePermission(CodeDatabaseTable table);
	/**
	 * springSecurity 用于根据ID查找用户的方法
	 * @param userId 用户ID
	 * @return List<SysPermission>
	 */
 	List<SysPermission> springSecurityFindByAdminUserId(Integer userId);
	/**
	 * 查找所有子节点
	 * @param currentUser 当前登录用户
	 * @return List<SysPermission>
	 */
	List<SysPermission> findChildSync(SysUser currentUser);
	/**
	 * 查找所有子节点菜单
	 * @return List<SysPermission>
	 */
	List<SysPermission> findChildSyncForMenu();
	/**
	 * 异步查找子节点,每次查找一级
	 * @param parentId 父ID
	 * @param selfId 数据自己的ID
	 * @return List<MenuTree>
	 */
	List<MenuTree> findChildAsync(Long parentId,Long selfId);
	/**
	 * 包装方法,将机构包装成treeList
	 * @param before 加工前的 List<SysPermission>
	 * @param selfId 数据自己的ID
	 * @return List<MenuTree>
	 */
	List<MenuTree> permissionToMenuTree(List<SysPermission> before, Long selfId);
	/**
	 * 包装方法,将菜单包装成treeList
	 * @param before 加工前的 List<SysPermission>
	 * @param selfId 数据自己的ID
	 * @param roleId 角色ID
	 * @return List<MenuTree>
	 */
	List<MenuTree> permissionToMenuTreeForRole(List<SysPermission> before, Long selfId,Long roleId);

	/**
	 * 匹配当前登录用户拥有的菜单权限
	 * @param list 传入匹配前的菜单列表
	 * @param currentPer 当前用户的权限列表
	 * @return List<SysPermission>
	 */
	List<SysPermission> getCurrentLoginUserHasPermission(List<SysPermission> list, JSONArray currentPer);

	/**
	 * 根据id删除自身以及自项
	 * @param id 主键
	 */
	void deleteSelfAndChildById(Long id);

	/**
	 * 根据父ID查找下一个排序值
	 * @param parentId 父ID
	 * @return Integer
	 */
	Integer findChildOrderNextNum(Long parentId);

}