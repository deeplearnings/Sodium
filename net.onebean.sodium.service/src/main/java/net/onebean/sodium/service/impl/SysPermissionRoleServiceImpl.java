package net.onebean.sodium.service.impl;
import net.onebean.sodium.dao.SysPermissionRoleDao;
import net.onebean.sodium.model.SysPermissionRole;
import net.onebean.sodium.service.SysPermissionRoleService;
import net.onebean.core.base.BaseSplitBiz;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysPermissionRoleServiceImpl extends BaseSplitBiz <SysPermissionRole, SysPermissionRoleDao> implements SysPermissionRoleService {
    @Override
    public List<SysPermissionRole> getRolePremissionByRoleId(Long roleId) {
        return baseDao.getRolePremissionByRoleId(roleId,getTenantId());
    }

    @Override
    public void deteleByRoleId(Long roleId) {
        baseDao.deteleByRoleId(roleId,getTenantId());
    }

    @Override
    public void deteleByPermissionId(Long permissionId) {
        baseDao.deteleByPermissionId(permissionId,getTenantId());
    }

    @Override
    public void insertBatch(String pids,String rid) {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map;
        String[] arr = pids.split(",");
        for (String s : arr) {
            map = new HashMap<>();
            map.put("permissionId",s);
            map.put("roleId",rid);
            list.add(map);
        }
        baseDao.insertBatch(list,getTenantId());
    }
}