package com.commons.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 
 * 项目名称：SmsMonitorPlate
 * 类名称：AvoidDuplicateSubmission
 * 类描述：<p>
 * 防止重复提交注解，用于方法上<br/>
 * 在新建页面方法上，设置@AvoidDuplicateSubmission(needSaveToken = true)，此时拦截器会在Session中保存一个token，
 * 同时需要在新建的页面中添加
 * <input type="hidden" id="avoid_token" name="avoid_token" value="${avoid_token}">
 * <br/>
 * 保存方法需要验证重复提交的，设置@AvoidDuplicateSubmission(needRemoveToken = true)
 * 此时会在拦截器中验证是否重复提交
 * </p>
 * 创建人：chenxiaoyi
 * 创建时间：2017-3-7 下午01:31:58
 * @version V1.0.0.T.1
 * ----------------------------------------- 
 * 修改记录(迭代更新)：chenxiaoyi- 2017-3-7 下午01:31:58---(创建)
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidDuplicateSubmission {
	boolean needSaveToken() default false;

	boolean needRemoveToken() default false;
}
