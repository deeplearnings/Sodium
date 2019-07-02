package net.onebean.saas.portal.common.dataPerm;

import net.onebean.saas.portal.model.SysOrganization;
import net.onebean.saas.portal.model.SysRole;
import net.onebean.saas.portal.model.SysUser;
import net.onebean.saas.portal.service.SysOrganizationService;
import net.onebean.saas.portal.service.SysRoleService;
import net.onebean.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据权限 工具类 用于生成数据权限sql
 * 该类设计参考了 jeeplus 框架数据权限 感谢jeeplus作者
 * @author 0neBean
 */
@Service
public class DataPermUtils {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysOrganizationService sysOrganizationService;


    private static final String DATA_PERM_LEVEL_ALL = "0";
    private static final String DATA_PERM_LEVEL_CUSTOM = "1";
    private static final String DATA_PERM_LEVEL_ORG = "2";
    private static final String DATA_PERM_LEVEL_ORG_AND_CHILD = "3";
    private static final String USER_TYPE_ADMIN_ROOT = "admin,root";
    private Map<String,Object> dataPermMap;

    /**
     * 生成数据权限sql
     * @param user 传入当前登录用户 用于获取用户所属机构及用户关联角色
     * @param orgAlias 自定义sql中 机构表的别名 可以有多个别名 用','分割
     * @param userAlias 自定义sql中 用户表的别名 可以有多个别名 用','分割
     * @param tenantId 租户ID
     * @return 返回的结果中 hasDatePerm 标识sql是否为空,sql字段为数据权限拼接的sql join是join语句
     */
    public Map<String,Object> dataPermFilter(SysUser user, String orgAlias, String userAlias,String tenantId,String join){
        Map<String,Object> dp = dataPermFilter(user,orgAlias,userAlias);
        dp.put("join", join);
        return dp;
    }

    /**
     * 生成数据权限sql
     * @param user 传入当前登录用户 用于获取用户所属机构及用户关联角色
     * @param orgAlias 自定义sql中 机构表的别名 可以有多个别名 用','分割
     * @param userAlias 自定义sql中 用户表的别名 可以有多个别名 用','分割
     * @return 返回的结果中 hasDatePerm 标识sql是否为空,sql字段为数据权限拼接的sql
     */
    public Map<String,Object> dataPermFilter(SysUser user, String orgAlias, String userAlias){
        StringBuffer sqlString = new StringBuffer();
        dataPermMap = new HashMap<>();
        dataPermMap.put("orgId",user.getOrgId());
        List<SysRole> list = sysRoleService.findRolesByUserId(user.getId());
        SysOrganization sysOrganization = sysOrganizationService.findByUserId(user.getId()).get(0);//目前程序设计一个用户归属一个组织
        boolean allDataPerm = false;
        if (USER_TYPE_ADMIN_ROOT.contains(user.getUserType())){
            dataPermMap.put("hasDatePerm",false);
            return dataPermMap;
        }
        for (SysRole sysRole : list) {
            for (String org_a : orgAlias.split(",")) {
                switch(sysRole.getDataPermissionLevel())
                {
                    case DATA_PERM_LEVEL_ALL:
                        //所有数据
                        allDataPerm = true;
                        break;
                    case DATA_PERM_LEVEL_CUSTOM:
                        //个人数据
                        if (StringUtils.isNotEmpty(userAlias)){
                            for (String user_a : userAlias.split(",")){
                                sqlString.append(" OR " + user_a + ".id = '" + user.getId() + "'");
                            }
                        }
                        break;
                    case DATA_PERM_LEVEL_ORG:
                        //组织数据
                        sqlString.append(" OR " + org_a + ".id = '" + sysOrganization.getId() + "'");
                        break;
                    case DATA_PERM_LEVEL_ORG_AND_CHILD:
                        //组织及组织下级
                        sqlString.append(" OR " + org_a + ".id = '" + sysOrganization.getId() + "'");
                        sqlString.append(" OR " + org_a + ".parent_ids LIKE '" + sysOrganization.getParentIds()+",%'");
                        break;
                    default:
                        break;
                }
            }
        }

        dataPermMap.put("hasDatePerm",!allDataPerm);
        if ((Boolean) dataPermMap.get("hasDatePerm")){
            dataPermMap.put("sql"," AND (" + sqlString.substring(4) + ")");
        }
        return  dataPermMap;
    }

}
