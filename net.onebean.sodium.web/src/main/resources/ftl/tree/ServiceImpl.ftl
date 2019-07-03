package ${service_package_name}.impl;
import org.springframework.stereotype.Service;
import net.onebean.util.CollectionUtil;
import net.onebean.core.BaseBiz;
import net.onebean.core.Condition;
import net.onebean.core.Pagination;
import net.onebean.core.form.Parse;
import ${model_package_name}.${model_name};
import ${service_package_name}.${model_name}Service;
import ${dao_package_name}.${model_name}Dao;
import net.onebean.util.StringUtils;
import ${vo_package_name}.${model_name}Tree;

import java.util.ArrayList;
import java.util.List;

/**
* @author ${author}
* @description ${description} serviceImpl
* @date ${create_time}
*/
@Service
public class ${model_name}ServiceImpl extends BaseBiz<${model_name}, ${model_name}Dao> implements ${model_name}Service{

    /**
     * 查找所有子节点
     * @author 0neBean
     * @param parentId
     * @return
     */
    public List<${model_name}> findChildSync(Long parentId) {
        List<${model_name}> list;
                if(null == parentId){
                list = new ArrayList<>();
                Condition condition = Condition.parseModelCondition("isRoot@string@eq$");
                condition.setValue("1");
                ${model_name} sysOrganization = this.find(new Pagination(),condition).get(0);
                sysOrganization.setChildList(baseDao.findChildSync(sysOrganization.getId()));
                list.add(sysOrganization);
            }else{
                list = baseDao.findChildSync(parentId);
            }
        return list;
    }

        /**
        * 异步查找子节点,每次查找一级
        * @author 0neBean
        * @param parentId
        * @return
        */
        @Override
        public List<${model_name}> findChildAsync(Long parentId,Long selfId) {
            List<${model_name}> res = new ArrayList<>();
            List<${model_name}> list = baseDao.findChildAsync(parentId);
            for (${model_name} o : list) {//某些业务场景 节点不能选择自己作为父级节点,故过滤掉所有自己及以下节点
                if (null == selfId || (null != selfId && Parse.toInt(o.getId()) != selfId) || selfId == 1) {
                    res.add(o);
                }
            }
            return res;
        }



        @Override
        public void save(${model_name} entity) {
            super.save(entity);
            entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
            super.save(entity);
        }

        @Override
        public void saveBatch(List<${model_name}> entities) {
            super.saveBatch(entities);
            for (${model_name} entity : entities) {
                entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
            }
            super.saveBatch(entities);
        }

        @Override
        public void update(${model_name} entity) {
            entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
            super.update(entity);
        }

        @Override
        public void updateBatch(${model_name} entity, List<Long> ids) {
            entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
            super.updateBatch(entity, ids);
        }

        @Override
        public void updateBatch(List<${model_name}> entities) {
            for (${model_name} entity : entities) {
                entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
            }
            super.updateBatch(entities);
        }

        protected String getParentOrgIdsNotEmpty(Long id){
            String res = baseDao.getParentIds(id);
            if (StringUtils.isEmpty(res)){
                return  null;
            }
            return  res;
        }


        @Override
        public void deleteSelfAndChildById(Long id) {
            baseDao.deleteSelfAndChildById(id);
        }

        @Override
            public Integer findChildOrderNextNum(Long parentId) {
            Integer res = baseDao.findChildOrderNextNum(parentId);
            return (null == res)?0:res;
        }

    @Override
    public List<${model_name}Tree> model2Tree(List<${model_name}> before,Long selfId){
        List<${model_name}Tree> treeList = new ArrayList<>();
        ${model_name}Tree temp;
        if(CollectionUtil.isNotEmpty(before)){
            for (${model_name} o: before) {
                if (null == selfId || (Parse.toInt(o.getId()) != selfId) || selfId == 1) {
                    temp = new ${model_name}Tree();
                    temp.setTitle(o.getName());
                    temp.setId(o.getId());
                    temp.setSort(o.getSort());
                if (CollectionUtil.isNotEmpty(o.getChildList())) {
                    temp.setType(${model_name}Tree.TYPE_FOLDER);
                    temp.setChildList(model2Tree(o.getChildList(),selfId));
                } else {
                    temp.setType(${model_name}Tree.TYPE_ITEM);
                }
                    treeList.add(temp);
                }
            }
        }
        return  treeList;
    }
}