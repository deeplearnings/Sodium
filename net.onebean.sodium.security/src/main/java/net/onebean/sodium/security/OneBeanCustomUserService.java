package net.onebean.sodium.security;

import net.onebean.core.form.Parse;
import net.onebean.sodium.model.SysPermission;
import net.onebean.sodium.model.SysUser;
import net.onebean.sodium.service.SysPermissionService;
import net.onebean.sodium.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * spring security 根据用户名关联相应的权限实现
 * @author 0neBean
 */
@Service
public class OneBeanCustomUserService implements UserDetailsService { //自定义UserDetailsService 接口

    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysPermissionService sysPermissionService;

    public UserDetails loadUserByUsername(String username) {
        SysUser user = sysUserService.findByUsername(username);
        if (user != null) {
            List<SysPermission> permissions = sysPermissionService.springSecurityFindByAdminUserId(Parse.toInt(user.getId()));
            List<GrantedAuthority> grantedAuthorities = new ArrayList <>();
            for (SysPermission permission : permissions) {
                if (permission != null && permission.getName()!=null) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                    grantedAuthorities.add(grantedAuthority);
                }
            }
            return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }

}
