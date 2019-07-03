package net.onebean.sodium.dao;

import net.onebean.core.BaseSplitDao;
import net.onebean.sodium.VO.OrgTree;
import net.onebean.sodium.model.SysOrganization;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysOrganizationDao extends BaseSplitDao <SysOrganization> {

    /**
     * 异步查找子节点,每次查找一级
     * @param parentId 父级id
     * @param tableSuffix 租户ID
     * @param dp 数据权限sql
     * @return
     */
    List<OrgTree> findChildAsync(@Param("parentId") Long parentId,@Param("tableSuffix")String tableSuffix,@Param("dp") Map<String,Object> dp);

    /**
     * 获取所有父ID
     * @param childId
     * @return
     */
    String getParentOrgIds(@Param("childId")Long childId,@Param("tableSuffix")String tableSuffix);

    /**
     * 根据userid 查找所有机构
     * @param userId
     * @return
     */
    List<SysOrganization> findByUserId(@Param("userId")Long userId,@Param("tableSuffix")String tableSuffix);

    /**
     * 查询待删除行数ID
     * @param id
     * @param tableSuffix
     * @return
     */
    List<String> findDeleteId(@Param("id")Long id,@Param("tableSuffix")String tableSuffix);

    /**
     * 根据id删除自身以及自项
     * @param id
     */
    void deleteSelfAndChildById(@Param("id")Long id,@Param("tableSuffix")String tableSuffix);
    /**
     * 根据父ID查找下一个排序值
     * @param parentId
     * @return
     */
    Integer findChildOrderNextNum(@Param("parentId")Long parentId,@Param("tableSuffix")String tableSuffix);
}