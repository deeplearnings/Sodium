package net.onebean.sodium.web.action.center;

import net.onebean.sodium.security.SpringSecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CenterController {

	@RequestMapping({"/index","/center",""})
	public String view(Model model) {
		model.addAttribute("current_sys_user", SpringSecurityUtil.getCurrentLoginUser());
		return "center/view";
	}

}
