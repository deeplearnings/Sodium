package net.onebean.sodium.service.impl;

import net.onebean.core.base.BaseSplitBiz;
import net.onebean.core.error.BusinessException;
import net.onebean.core.form.Parse;
import net.onebean.sodium.vo.OrgTree;
import net.onebean.sodium.common.dataPerm.DataPermUtils;
import net.onebean.sodium.common.error.ErrorCodesEnum;
import net.onebean.sodium.dao.SysOrganizationDao;
import net.onebean.sodium.model.SysOrganization;
import net.onebean.sodium.model.SysUser;
import net.onebean.sodium.service.SysOrganizationService;
import net.onebean.sodium.service.SysUserService;
import net.onebean.util.CollectionUtil;
import net.onebean.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysOrganizationServiceImpl extends BaseSplitBiz<SysOrganization, SysOrganizationDao> implements SysOrganizationService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private DataPermUtils dataPermUtils;

    /**
     * 查找所有子节点
     * @author 0neBean
     * @return list
     */
    public List<SysOrganization> findChildSync(SysUser currentUser) {
        String tenantId = getTenantId();
        String join = MessageFormat.format(" LEFT JOIN sys_user_{0} u on u.org_id = t.id",tenantId);
        List<SysOrganization> queryList = this.findAll(dataPermUtils.dataPermFilter(currentUser,"t","u",tenantId,join));
        if (CollectionUtil.isNotEmpty(queryList)){
            queryList.get(0).setIsRoot("1");
        }
        return tree(queryList);
    }


    /**
     * 包装数据列表成树结构
     * @param original 数据列表
     * @return List<SysOrganization>
     */
    private List<SysOrganization> tree(List<SysOrganization> original){
        List<SysOrganization> _final = new ArrayList<>();
        for (SysOrganization p0:original){
            //如果是根节点,放入最终的结果中剩余数量减一
            if (p0.getIsRoot().equals("1")){
                _final.add(p0);
            }
        }
        treeChild(_final,original);
        return _final;
    }

    /**
     * 包装方法递归子方法
     * @param list 数据列表
     * @param original 元数据
     */
    private void treeChild(List<SysOrganization> list,List<SysOrganization> original){
        List<SysOrganization> _final;
        for (SysOrganization s : list) {
            if (CollectionUtil.isEmpty(s.getChildList())){
                _final = new ArrayList<>();
                for (SysOrganization s1 : original) {
                    if (null != s1.getParentId() && null != s.getId() && s1.getParentId().equals(s.getId())){
                        _final.add(s1);
                    }
                }
                //递归算法
                treeChild(_final,original);
                s.setChildList(_final);
            }
        }
    }

    /**
     * 异步查找子节点,每次查找一级
     * @author 0neBean
     * @param parentId
     * @return
     */
    @Override
    public List<OrgTree> findChildAsync(Long parentId,Long selfId,SysUser currentUser) {
        List<OrgTree> res = new ArrayList<>();
        String tenantId = getTenantId();
        String join = MessageFormat.format("LEFT JOIN sys_organization_{0} o ON o.`id` = t.org_id",tenantId);
        List<OrgTree> list = baseDao.findChildAsync(parentId,getTenantId(),dataPermUtils.dataPermFilter(currentUser,"o","t",tenantId,join));
        for (OrgTree o : list) {//某些业务场景 节点不能选择自己作为父级节点,故过滤掉所有自己及以下节点
            if (null == selfId || (Parse.toInt(o.getId()) != selfId) || selfId == 1) {
                res.add(o);
            }
        }
        return res;
    }


    @Override
    public List<OrgTree> organizationToOrgTree(List<SysOrganization> before,Long selfId){
        List<OrgTree> treeList = new ArrayList<>();
        OrgTree temp;
        if(CollectionUtil.isNotEmpty(before)){
            for (SysOrganization sysOrganization: before) {
                if (null == selfId || (Parse.toInt(sysOrganization.getId()) != selfId) || selfId == 1) {
                    temp = new OrgTree();
                    temp.setTitle(sysOrganization.getOrgName());
                    temp.setId(sysOrganization.getId());
                    temp.setSort(sysOrganization.getSort());
                    if (CollectionUtil.isNotEmpty(sysOrganization.getChildList())) {
                        temp.setType(OrgTree.TYPE_FOLDER);
                        temp.setChildList(organizationToOrgTree(sysOrganization.getChildList(),selfId));
                    } else {
                        temp.setType(OrgTree.TYPE_ITEM);
                    }
                    treeList.add(temp);
                }
            }
        }
        return  treeList;
    }

    @Override
    public void save(SysOrganization entity) {
        super.save(entity);
        entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
        super.save(entity);
    }

    @Override
    public void saveBatch(List<SysOrganization> entities) {
        super.saveBatch(entities);
        for (SysOrganization entity : entities) {
            entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
        }
        super.saveBatch(entities);
    }

    @Override
    public Integer update(SysOrganization entity) {
        entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
        return super.update(entity);
    }

    @Override
    public Integer updateBatch(SysOrganization entity, List<Long> ids) {
        entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
        return super.updateBatch(entity, ids);
    }

    @Override
    public void updateBatch(List<SysOrganization> entities) {
        for (SysOrganization entity : entities) {
            entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
        }
        super.updateBatch(entities);
    }

    protected String getParentOrgIdsNotEmpty(Long id){
        String res = baseDao.getParentOrgIds(id,getTenantId());
        if (StringUtils.isEmpty(res)){
            return  null;
        }
        return  res;
    }

    @Override
    public List<SysOrganization> findByUserId(Long userId) {
        return baseDao.findByUserId(userId,getTenantId());
    }

    @Override
    public Boolean deleteSelfAndChildById(Long id) {
        List<String> ids = baseDao.findDeleteId(id,getTenantId());
        if (CollectionUtil.isEmpty(ids)){
            throw new BusinessException(ErrorCodesEnum.GET_DATE_ERR.code(),ErrorCodesEnum.GET_DATE_ERR.msg());
        }
        Integer count = sysUserService.countUserByIds(ids);
        if (null == count){
            throw new BusinessException(ErrorCodesEnum.GET_DATE_ERR.code(),ErrorCodesEnum.GET_DATE_ERR.msg());
        }
        if (count > 0){
            return false;
        }
        baseDao.deleteSelfAndChildById(id,getTenantId());
        return true;
    }

    @Override
    public Integer findChildOrderNextNum(Long parentId) {
        Integer res = baseDao.findChildOrderNextNum(parentId,getTenantId());
        return (null == res)?0:res;
    }

    @Override
    public Boolean deleteOrg(Object id) {
        if (CollectionUtil.isNotEmpty(sysUserService.findUserByOrgID(id))) {
            throw new BusinessException(ErrorCodesEnum.ASSOCIATED_DATA_CANNOT_BE_DELETED.code(),"该机构关联了用户不能删除!");
        }
        if (!this.deleteSelfAndChildById(Parse.toLong(id))) {
            throw new BusinessException(ErrorCodesEnum.ASSOCIATED_DATA_CANNOT_BE_DELETED.code(),"该机构下级机构关联了用户不能删除!");
        }
        return true;
    }
}