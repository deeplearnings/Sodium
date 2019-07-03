package net.onebean.sodium.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;


/**
 * web页面权限方法拦截器
 * 对应sec:hasPermission标签功能
 * @author 0neBean
 */
@Service
public class OneBeanPermissionEvaluator implements PermissionEvaluator {

    /**
     * @remark 校验权限方法
     * @author 0neBean
     * @param authentication
     * @param permission
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object target, Object permission) {

        if (target.equals("$everyone")) {//如果target符合表达式所有人,只校验权限
            return this.hasPermission(authentication, permission);
        }else {
            User u = (User) authentication.getPrincipal();
            return this.hasPermission(u, target);
        }
    }

    /**
     * @remark 校验权限默认方法(暂时没用上)
     * @author 0neBean
     * @param authentication
     * @param taegetId
     * @param taegetType
     * @param permission
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable taegetId, String taegetType, Object permission) {
        return false;
    }

    /**
     * 判断用户是否与传入的用户名相等
     * @param u
     * @param target
     * @return
     */
    protected boolean hasPermission(User u, Object target) {
        if (u.getUsername().equals(target)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * @remark 校验权限方法
     * @author 0neBean
     * 简单的字符串比较，相同则认为有权限
     */
    protected boolean hasPermission(Authentication authentication, Object permission) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(permission)) {
                return true;
            }
        }
        return false;
    }

}
