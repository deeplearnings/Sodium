package net.onebean.saas.portal.service.impl;

import net.onebean.saas.portal.dao.SysUserDao;
import net.onebean.saas.portal.model.SysUser;
import net.onebean.saas.portal.service.SysUserService;
import net.onebean.core.BaseSplitBiz;
import net.onebean.core.Condition;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends BaseSplitBiz <SysUser, SysUserDao> implements SysUserService {

	@Override
	public SysUser findByUsername(String username) {
		return baseDao.findByUsername(username,getTenantId());
	}

	@Override
	public List<SysUser> findUserByOrgID(Object ordId) {
		Condition condition =  Condition.parseModelCondition("orgId@int@eq$");
		condition.setValue(ordId);
		return this.find(null,condition);
	}

	@Override
	public Integer countUserByIds(List<String> orgIds) {
		return baseDao.countUserByIds(orgIds,getTenantId());
	}
}