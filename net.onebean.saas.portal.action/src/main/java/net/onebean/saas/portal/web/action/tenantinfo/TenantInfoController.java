package net.onebean.saas.portal.web.action.tenantinfo;

import net.onebean.saas.portal.core.BaseController;
import net.onebean.saas.portal.model.TenantInfo;
import net.onebean.saas.portal.service.TenantInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
* @author 0neBean
* @description 租户管理 controller
* @date 2019-02-13 15:05:06
*/
@Controller
@RequestMapping("tenantinfo")
public class TenantInfoController extends BaseController<TenantInfo,TenantInfoService> {

}
