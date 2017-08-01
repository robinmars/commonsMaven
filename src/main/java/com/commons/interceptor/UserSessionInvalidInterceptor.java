/*package com.hskj.common.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hskj.common.util.Constants;
import com.hskj.common.util.SessionUtil;

//用户session失效权限控制
public class UserSessionInvalidInterceptor extends HandlerInterceptorAdapter {

	private static final String LOGINURL = "/login.do";
	private static final String INDEXURL = "/index.jsp";

	// 被允许的url
	private List<String> uncheckedUrls;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("进入拦截器");

		Object adminUser = (Object) SessionUtil.getString(request,Constants.SMS_ADMIN_SESSION);
		
		String servletPath = request.getServletPath();
		String url = null;
		if (servletPath != null && servletPath.length() > 1) {
			url = servletPath.substring(1);
		}
		
		if ( (adminUser == null || "".equals(adminUser)) && !uncheckedUrls.contains(url) ) {
			response.sendRedirect(request.getContextPath() + INDEXURL);
			return false;
		}
		
		if ( uncheckedUrls!=null && !uncheckedUrls.isEmpty() && uncheckedUrls.contains(url) ) {
			return true;
		}

		return super.preHandle(request, response, handler);
	}

	public void setUncheckedUrls(List<String> uncheckedUrls) {
		this.uncheckedUrls = uncheckedUrls;
	}

	// 获取request对象，不用在每个方法上加request参数
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
	}

}
*/