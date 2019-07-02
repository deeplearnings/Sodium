package net.onebean.saas.portal.dao;

import com.eakay.core.BaseSplitDao;
import net.onebean.saas.portal.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleDao extends BaseSplitDao <SysRole> {
    /**
     * 根据用户登录名查询用户所有角色
     * @author 0neBean
     * @param userId
     * @return List<SysRole>
     */
    public List<SysRole> findRolesByUserId(@Param("userId")Long userId,@Param("tableSuffix")String tableSuffix);
}