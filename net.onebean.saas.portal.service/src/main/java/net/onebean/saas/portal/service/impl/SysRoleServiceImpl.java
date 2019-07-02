package net.onebean.saas.portal.service.impl;
import net.onebean.saas.portal.dao.SysRoleDao;
import net.onebean.saas.portal.model.SysRole;
import net.onebean.saas.portal.service.SysRoleService;
import com.eakay.core.BaseSplitBiz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends BaseSplitBiz <SysRole, SysRoleDao> implements SysRoleService {
    @Override
    public List<SysRole> findRolesByUserId(Long userId) {
        return baseDao.findRolesByUserId(userId,getTenantId());
    }
}