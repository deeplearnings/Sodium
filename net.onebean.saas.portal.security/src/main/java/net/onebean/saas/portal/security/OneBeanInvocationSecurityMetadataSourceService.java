package net.onebean.saas.portal.security;

import ch.qos.logback.classic.Logger;
import com.eakay.core.error.GetTenantInfoException;
import net.onebean.saas.portal.model.SysPermission;
import net.onebean.saas.portal.service.SysPermissionService;
import com.eakay.util.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 资源初始化拦截器,
 * 所有用到的静态权限资源在这里加载
 * 0neBean
 */
@Service
public class OneBeanInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private SysPermissionService sysPermissionService;

    private HashMap<String, Collection<ConfigAttribute>> map =null;

    private final static Logger logger = (Logger) LoggerFactory.getLogger(OneBeanInvocationSecurityMetadataSourceService.class);

    /**
     * 加载资源，初始化资源变量
     */
    private void loadResourceDefine(){
        map = new HashMap<>();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        List<SysPermission> list;
        try {
            list = sysPermissionService.findAll();
        } catch (GetTenantInfoException e) {
            logger.info("eureka heartbeat sending now");
            return;
        }
        for(SysPermission permission : list) {
            array = new ArrayList<>();
            cfg = new SecurityConfig(permission.getName());
            array.add(cfg);
            map.put(permission.getUrl(), array);
        }
    }



    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if(map ==null) loadResourceDefine();
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        AntPathRequestMatcher matcher;
        String resUrl;
        for(Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
            resUrl = iter.next();
            if (StringUtils.isEmpty(resUrl)){
                //如果url为空 不加载该url资源
                return null;
            }
            matcher = new AntPathRequestMatcher(resUrl);
            if(matcher.matches(request)) {
                return map.get(resUrl);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
