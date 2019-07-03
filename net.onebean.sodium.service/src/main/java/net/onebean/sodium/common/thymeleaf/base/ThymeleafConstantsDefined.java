package net.onebean.sodium.common.thymeleaf.base;

import net.onebean.util.PropUtil;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * thymeleaf 常量定义
 * @author 0neBean
 */
@Component
public class ThymeleafConstantsDefined {


    @Resource
    private void configureThymeleafStaticVars(ThymeleafViewResolver viewResolver) {
        if(viewResolver != null) {
            Map<String, Object> vars = new HashMap<>();
            vars.put("oss_ctx", PropUtil.getInstance().getConfig("server.oss.context-path",PropUtil.PUBLIC_CONF_ALIYUN));
            vars.put("ctx", PropUtil.getInstance().getConfig("server.context-path",PropUtil.DEFLAULT_NAME_SPACE));
            viewResolver.setStaticVariables(vars);
        }
    }
}
