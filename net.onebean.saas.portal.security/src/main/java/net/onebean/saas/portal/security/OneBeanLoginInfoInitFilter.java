package net.onebean.saas.portal.security;

import net.onebean.core.error.GetTenantInfoException;
import net.onebean.util.PropUtil;
import net.onebean.util.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OneBeanLoginInfoInitFilter extends OncePerRequestFilter {

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isProtectedUrl(request)) {
            String tenantId = request.getParameter("tenantId");
            if(StringUtils.isEmpty(tenantId)) {
                // 转发到错误Url
                request.getRequestDispatcher("/error/401").forward(request,response);
            } else {
                String tenantIdHeaderKey = PropUtil.getInstance().getConfig("uag.tenant.id.header.key", "application");
                //手动设置异常
                request.getSession().setAttribute(tenantIdHeaderKey,tenantId);
                filterChain.doFilter(request,response);
            }
        } else {
            try {
                filterChain.doFilter(request,response);
            } catch (GetTenantInfoException  e) {
                request.getRequestDispatcher("/login").forward(request,response);
            }
        }
    }


    // 拦截 /login的POST请求
    private boolean isProtectedUrl(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && pathMatcher.match("/login", request.getServletPath());
    }
}