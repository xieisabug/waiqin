package hn.join.fieldwork.web;

import hn.join.fieldwork.web.command.LoginCommand;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 系统安全管理控制器
 * @author chenjinlong
 *
 */
@Controller
public class WebSecurityController {
	/**
	 * 通过根路径进入主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String index(Model model) {
		if (SecurityUtils.getSubject().isAuthenticated())
			return "redirect:/web/entrance";
		else
			return showLoginForm(model, null);
	}
	/**
	 * 进入登录页面
	 * @param model
	 * @param command
	 * @return
	 */
	@RequestMapping(value = { "/web/login" }, method = RequestMethod.GET)
	public String showLoginForm(Model model,
			@ModelAttribute LoginCommand command) {
		return "login";
	}
	/**
	 * 退出系统
	 * @return
	 */
	@RequestMapping("/web/logout")
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "redirect:/";
	}
	/**
	 * 用户登录
	 * @param model
	 * @param command
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/web/login", method = RequestMethod.POST)
	public String login(Model model, @ModelAttribute LoginCommand command,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		UsernamePasswordToken token = new UsernamePasswordToken(
				command.getUsername(), command.getPassword(),
				command.getRememberMe());
		try {
			SecurityUtils.getSubject().login(token);

			//WebUtils.redirectToSavedRequest(request, response, "/web/fieldworker/manage");
			WebUtils.redirectToSavedRequest(request, response, "/web/entrance");
			return null;
		} catch (AuthenticationException e) {
			model.addAttribute("error_login_generic", "无效的用户名或密码，请重试.");
			return showLoginForm(model, command);
		}

	}
	/**
	 * 进入主页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/web/entrance", method = RequestMethod.GET)
	public String entrance(Model model) {
		return "home";
	}

}
