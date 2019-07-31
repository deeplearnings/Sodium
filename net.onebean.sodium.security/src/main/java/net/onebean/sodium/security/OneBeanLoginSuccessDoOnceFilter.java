package net.onebean.sodium.security;

import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 过滤器
 *
 * @author 0neBean
 */
@Service
@WebFilter(filterName = "onebeanFilter", urlPatterns = "/*")
public class OneBeanLoginSuccessDoOnceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        session.setAttribute("current_sys_user", SpringSecurityUtil.getCurrentLoginUser());
        request.setAttribute("current_sys_user", SpringSecurityUtil.getCurrentLoginUser());
        //执行操作后必须doFilter
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
