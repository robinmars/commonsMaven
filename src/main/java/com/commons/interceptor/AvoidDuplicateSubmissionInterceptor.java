package com.commons.interceptor;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.commons.util.Constants;

import common.Logger;

/*
 * 项目名称：SmsMonitorPlate
 * 类名称：AvoidDuplicateSubmissionInterceptor
 * 类描述：防止重复提交拦截器
 * 创建人：chenxiaoyi
 * 创建时间：2017-3-7 下午01:30:40
 * @version V1.0.0.T.1
 * ----------------------------------------- 
 * 修改记录(迭代更新)：chenxiaoyi - 2017-3-7 下午01:30:40---(创建)
 */

public class AvoidDuplicateSubmissionInterceptor extends
		HandlerInterceptorAdapter {
	private static final Logger log = Logger.getLogger(AvoidDuplicateSubmissionInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();

			AvoidDuplicateSubmission annotation = method.getAnnotation(AvoidDuplicateSubmission.class);
			if (annotation != null) {
				boolean needSaveSession = annotation.needSaveToken();
				if (needSaveSession) {
					request.getSession(false).setAttribute(Constants.AVOID_TOKEN,UUID.randomUUID().toString());
				}
				boolean needRemoveSession = annotation.needRemoveToken();
				if (needRemoveSession) {
					if (isRepeatSubmit(request)) {
						return false;
					}
					request.getSession(false).removeAttribute(Constants.AVOID_TOKEN);
				}
			}
			return true;
		}else{
			return super.preHandle(request, response, handler);
		}
	}

	private boolean isRepeatSubmit(HttpServletRequest request) {
		String serverToken = (String) request.getSession(false).getAttribute(Constants.AVOID_TOKEN);
		if (serverToken == null) {
			return true;
		}
		String clinetToken = request.getParameter(Constants.AVOID_TOKEN);
		if (clinetToken == null) {
			return true;
		}
		if (!serverToken.equals(clinetToken)) {
			return true;
		}
		return false;
	}

}
