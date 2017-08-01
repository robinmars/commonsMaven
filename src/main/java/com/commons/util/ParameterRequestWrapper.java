package com.commons.util;

import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/*
 * 
 * 项目名称：SmsMonitorPlate
 * 类名称：ParameterRequestWrapper
 * 类描述：参数格式化
 * 创建人：chenxiaoyi
 * 创建时间：2017-2-7 下午06:58:26
 * @version V1.0.0.T.1
 * ----------------------------------------- 
 * 修改记录(迭代更新)：Administrator- 2017-2-7 下午06:58:26---(新建)
 *
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

	private final Map<String, Object> params;

	public ParameterRequestWrapper(HttpServletRequest request, Map<String, Object> newParams) {
		super(request);
		this.params = newParams;
	}

	@Override
	public Map<String, Object> getParameterMap() {
		return params;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Vector<String> l = new Vector<String>(params.keySet());
		return l.elements();
	}

	@Override
	public String[] getParameterValues(String name) {
		Object v = params.get(name);
		if (v == null) {
			return null;
		} else if (v instanceof String[]) {
			return (String[]) v;
		} else if (v instanceof String) {
			return new String[] { (String) v };
		} else {
			return new String[] { v.toString() };
		}
	}

	@Override
	public String getParameter(String name) {
		Object v = params.get(name);
		if (v == null) {
			return null;
		} else if (v instanceof String[]) {
			String[] strArr = (String[]) v;
			if (strArr.length > 0) {
				return strArr[0];
			} else {
				return null;
			}
		} else if (v instanceof String) {
			return (String) v;
		} else {
			return v.toString();
		}
	}

}