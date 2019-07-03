package net.onebean.sodium.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.DefaultWebInvocationPrivilegeEvaluator;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * web页面url拦截器
 * 对应sec:authorize-url标签功能
 * @author 0neBean
 */
@Service
public class OneBeanWebInvocationPrivilegeEvaluator implements WebInvocationPrivilegeEvaluator {

    protected static final Log logger = LogFactory.getLog(DefaultWebInvocationPrivilegeEvaluator.class);

    //权限拦截器
    private final OneBeanFilterSecurityInterceptor securityInterceptor;
    //可以指定权限拦截器的构造器
    public OneBeanWebInvocationPrivilegeEvaluator(OneBeanFilterSecurityInterceptor securityInterceptor) {
        this.securityInterceptor = securityInterceptor;
    }


    @Override
    public boolean isAllowed(String uri, Authentication authentication) {
        return this.isAllowed((String)null, uri, (String)null, authentication);
    }

    @Override
    public boolean isAllowed(String contextPath, String uri, String method, Authentication authentication) {
        Assert.notNull(uri, "uri parameter is required");
        FilterInvocation fi = new FilterInvocation(contextPath, uri, method);
        Collection<ConfigAttribute> attrs = this.securityInterceptor.obtainSecurityMetadataSource().getAttributes(fi);
        if (attrs == null) {
            return !this.securityInterceptor.isRejectPublicInvocations();
        } else if (authentication == null) {
            return false;
        } else {
            try {
                //权限拦截器中的决策器来判断是否通过
                this.securityInterceptor.getAccessDecisionManager().decide(authentication, fi, attrs);
                return true;
            } catch (AccessDeniedException var8) {
                if (logger.isDebugEnabled()) {
                    logger.debug(fi.toString() + " denied for " + authentication.toString(), var8);
                }

                return false;
            }
        }
    }
}
