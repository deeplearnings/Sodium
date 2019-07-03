package net.onebean.sodium.service.impl;
import net.onebean.sodium.dao.SysRoleDao;
import net.onebean.sodium.model.SysRole;
import net.onebean.sodium.service.SysRoleService;
import net.onebean.core.BaseSplitBiz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends BaseSplitBiz <SysRole, SysRoleDao> implements SysRoleService {
    @Override
    public List<SysRole> findRolesByUserId(Long userId) {
        return baseDao.findRolesByUserId(userId,getTenantId());
    }
}