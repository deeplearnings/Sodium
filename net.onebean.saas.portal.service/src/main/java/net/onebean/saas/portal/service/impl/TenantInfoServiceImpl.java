package net.onebean.saas.portal.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.onebean.common.exception.BusinessException;
import net.onebean.core.BaseBiz;
import net.onebean.saas.portal.common.error.ErrorCodesEnum;
import net.onebean.saas.portal.dao.TenantInfoDao;
import net.onebean.saas.portal.model.TenantInfo;
import net.onebean.saas.portal.service.TenantInfoService;
import net.onebean.util.PropUtil;
import net.onebean.util.StringUtils;
import net.onebean.util.VelocityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 0neBean
* @description 租户管理 serviceImpl
* @date 2019-02-13 15:05:06
*/
@Service
public class TenantInfoServiceImpl extends BaseBiz<TenantInfo, TenantInfoDao> implements TenantInfoService {

    private static final Logger logger = LoggerFactory.getLogger(TenantInfoServiceImpl.class);
    private static final String templateFileKey = "inti.tenant.sql.vm.file.path";



    @Override
    public void initTenantInfoTableAndFunctionBySql(String tenantId) {
        if (StringUtils.isEmpty(baseDao.isExistTenantInfo(tenantId))){
            //根据生成模板生成sql语句
            String sqlStr = mergeTemplate(tenantId);
            Integer res = baseDao.initTenantDataBaseInfo(sqlStr);
            if (null == res){
                throw new BusinessException(ErrorCodesEnum.INIT_TENANT_INFO_ERROR.code(),ErrorCodesEnum.INIT_TENANT_INFO_ERROR.msg());
            }
        }
    }

    /**
     * 根据生成模板生成sql语句
     * @param tenantId 租户ID
     * @return sql
     */
    private String mergeTemplate(String tenantId) {
        String templateFile = PropUtil.getInstance().getConfig(templateFileKey, PropUtil.DEFLAULT_NAME_SPACE);
        JSONObject param = new JSONObject();
        param.put("tenantId",tenantId);
        return VelocityUtils.generateStringFromVelocity(param,templateFile);
    }

    @Override
    public void deleteByTenantIds(List<Long> deletedTenantIds) {
        baseDao.deleteByTenantIds(deletedTenantIds);
    }
}