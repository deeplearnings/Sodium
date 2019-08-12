package net.onebean.sodium.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

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


    private static final PathMatcher PATH_MATCHER = new AntPathMatcher(System.getProperty("file.separator"));
    private static final Logger LOGGER = LoggerFactory.getLogger(OneBeanLoginSuccessDoOnceFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();
        for (String unSecuredUrl : OneBeanAccessWhiteList.unSecuredUrls) {
            if (PATH_MATCHER.match(unSecuredUrl,uri)){
                //执行操作后必须doFilter
                chain.doFilter(request, response);
                return;
            }
        }
        LOGGER.debug("current uri is = "+uri+" set current_sys_user in session");
        session.setAttribute("current_sys_user", SpringSecurityUtil.getCurrentLoginUser());
        request.setAttribute("current_sys_user", SpringSecurityUtil.getCurrentLoginUser());
        //执行操作后必须doFilter
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
