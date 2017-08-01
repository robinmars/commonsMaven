package com.commons.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.commons.model.ShiroAdmin;
import com.commons.redis.DC_Adapter;
import com.commons.util.Constants;
import com.commons.util.SessionUtil;
import com.mysql.jdbc.StringUtils;

/**
 * 项目名称：SmsMonitorPlate 
 * 类名称：SingleUserInvalidInterceptor 
 * 类描述：单用户登录实现
 * 创建人：chenxiaoyi 
 * 创建时间：2017-07-14 12:21:59
 * @version V1.0.0.T.1 -----------------------------------------
 * 修改记录(迭代更新)：chenxiaoyi- 2017-07-14 12:21:59---(新建)
 */
public class SingleUserInvalidInterceptor extends HandlerInterceptorAdapter {

	@Resource(name = "redisCache")
	protected DC_Adapter redisCache;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			Subject subject = SecurityUtils.getSubject();
			ShiroAdmin admin = (ShiroAdmin) subject.getPrincipal();
			if (admin != null) {
				String redis_key = Constants.SMS_ADMIN_SESSION+"_"+admin.getAdminSn();
				if (!StringUtils.isNullOrEmpty(redis_key)) {
					// 判断用户名是否存在redis
					if (redisCache.exists(redis_key)) {
						String session_id = redisCache.get(redis_key);
						if (!StringUtils.isNullOrEmpty(session_id)) {
							if (!session_id.equals(request.getSession().getId())) {
								SessionUtil.remove(request, Constants.SMS_ADMIN_SESSION);
								subject.logout();
								response.sendRedirect(request.getContextPath()+"/index.jsp?kick=1");
								return false;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
		return super.preHandle(request, response, handler);
	}

	// 获取request对象，不用在每个方法上加request参数
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

}
