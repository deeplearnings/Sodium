package net.onebean.saas.portal.common.filter;

import com.alibaba.fastjson.JSON;
import com.eakay.core.BreadCrumbs;
import com.eakay.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 过滤器
 * @author 0neBean
 */
@Service
@WebFilter(filterName="onebeanFilter",urlPatterns="/*")
public class DoOnceFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        if (StringUtils.isNotEmpty(request.getParameter("breadCrumbsStr"))){//处理面包屑
            breadCrumbsHandler(request);
        }
        //执行操作后必须doFilter
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    /**
     * 处理面包屑
     * @param request
     */
    private void breadCrumbsHandler(HttpServletRequest request){
        /*面包屑*/
        String breadCrumbsStr = request.getParameter("breadCrumbsStr");
        String uri = (request.getRequestURI().endsWith("/"))?request.getRequestURI():request.getRequestURI()+"/";
        Map<String,Object> isRepeat = new HashMap<>();
        List<BreadCrumbs> breadCrumbsList = JSON.parseArray(breadCrumbsStr.replace("\\",""),BreadCrumbs.class);
        List<BreadCrumbs> breadCrumbsList_result = new ArrayList<>();
        for (int i = 0; i < breadCrumbsList.size(); i++) {
            //selectedCut 为手动点击面包屑 截断点击后的面包屑
            boolean selectedCut = uri.equals(breadCrumbsList.get(i).getBreadCrumbsUrl()) && i != breadCrumbsList.size()-1;
            if(isRepeat.get(breadCrumbsList.get(i).getBreadCrumbsUrl()) != null){
                for (BreadCrumbs bb : breadCrumbsList_result) {
                    boolean repeat = bb.getBreadCrumbsUrl().equals(breadCrumbsList.get(i).getBreadCrumbsUrl());
                    if (!repeat){//如果发现重复 遍历面包屑 在重复的地方截断
                        breadCrumbsList_result = new ArrayList<>();
                        breadCrumbsList_result.add(bb);
                    }
                }
                break;
            }else{
                breadCrumbsList_result.add(breadCrumbsList.get(i));
                isRepeat.put(breadCrumbsList.get(i).getBreadCrumbsUrl(),new byte[0]);
                if (selectedCut){
                    break;
                }
            }
        }
        request.setAttribute("breadCrumbs",breadCrumbsList_result);
    }



}
