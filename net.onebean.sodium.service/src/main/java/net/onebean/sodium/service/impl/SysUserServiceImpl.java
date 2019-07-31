package net.onebean.sodium.service.impl;

import net.onebean.core.base.BaseSplitBiz;
import net.onebean.core.form.Parse;
import net.onebean.core.query.Condition;
import net.onebean.sodium.dao.SysUserDao;
import net.onebean.sodium.model.SysUser;
import net.onebean.sodium.service.SysRoleUserService;
import net.onebean.sodium.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends BaseSplitBiz <SysUser, SysUserDao> implements SysUserService {

	@Autowired
	private SysRoleUserService sysRoleUserService;

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

	@Override
	public Boolean deleteUser(Object id) {
		this.deleteById(id);
		sysRoleUserService.deleteByUserId(Parse.toLong(id));
		return true;
	}
}