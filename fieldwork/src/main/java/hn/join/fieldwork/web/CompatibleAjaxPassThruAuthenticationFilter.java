package hn.join.fieldwork.web;

import hn.join.fieldwork.utils.SystemConstants;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.AuthenticationFilter;

public class CompatibleAjaxPassThruAuthenticationFilter extends
		AuthenticationFilter {

	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			return true;
		} else {

			if (!isAjaxRequest((HttpServletRequest) request))
				saveRequestAndRedirectToLogin(request, response);
			else {
				response.getWriter().write("{\"resultCode\":"+SystemConstants.ajax_timeout+"}");
			}
			return false;
		}
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWith = request.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith)
				: false;
	}

}
