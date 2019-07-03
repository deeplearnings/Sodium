package net.onebean.sodium.web.action.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

	public static String ERROR_SESSION_MSG_KEY = "SPRING_SECURITY_LAST_EXCEPTION";

		@RequestMapping("/login")
		public String view(HttpServletRequest request,Model model) {
			HttpSession session = request.getSession();
			if (null != session.getAttribute(ERROR_SESSION_MSG_KEY)) {
				String errorMsg = session.getAttribute(ERROR_SESSION_MSG_KEY).toString();
				if (null != errorMsg)
					model.addAttribute("errorMsg", errorMsg);
					session.removeAttribute(ERROR_SESSION_MSG_KEY);
			}
			return "login/view";
		}

		@RequestMapping("/home")
		public String index(Model model){
			Map<String, Object> msg = new HashMap<String, Object>();
			msg.put("title", "测试标题");
			msg.put("content", "测试内容");
			msg.put("etraInfo", "欢迎来到HOME页面,您拥有 ROLE_HOME 权限");
			model.addAttribute("msg", msg);
			return "home";
		}

}
	