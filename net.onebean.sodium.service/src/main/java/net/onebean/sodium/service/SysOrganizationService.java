package net.onebean.sodium.service;

import net.onebean.core.IBaseSplitBiz;
import net.onebean.sodium.VO.OrgTree;
import net.onebean.sodium.model.SysOrganization;
import net.onebean.sodium.model.SysUser;

import java.util.List;

public interface SysOrganizationService extends IBaseSplitBiz <SysOrganization> {
    /**
     * 查找所有子节点
     * @author 0neBean
     * @return
     */
    List<SysOrganization> findChildSync(SysUser currentUser);

    /**a
     * 异步查找子节点,每次查找一级
     * @author 0neBean
     * @param parentId
     * @return
     */
    List<OrgTree> findChildAsync(Long parentId,Long selfId,SysUser currentUser);

    /**
     * 包装方法,将机构包装成treeList
     * @param before
     * @param selfId
     * @return
     */
    List<OrgTree> organizationToOrgTree(List<SysOrganization> before,Long selfId);

    /**
     * 根据userid 查找所有机构
     * @param userId
     * @return
     */
    List<SysOrganization> findByUserId(Long userId);

    /**
     * 根据id删除自身以及自项
     * @param id
     */
    Boolean deleteSelfAndChildById(Long id);
    /**
     * 根据父ID查找下一个排序值
     * @param parentId
     * @return
     */
    Integer findChildOrderNextNum(Long parentId);
}