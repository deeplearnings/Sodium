package net.onebean.saas.portal.web.action.center;

import net.onebean.saas.portal.security.SpringSecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CenterController {
	
	@RequestMapping({"/center",""})
	public String view(Model model) {
		model.addAttribute("current_sys_user",SpringSecurityUtil.getCurrentLoginUser());
		return "center/bone";
	}

	@RequestMapping({"/index"})
	public String index() {
		return "center/view";
	}

}


