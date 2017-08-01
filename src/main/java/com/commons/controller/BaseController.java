package com.commons.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.commons.mybatis.PageUtil;
import com.commons.util.PageResultBuilder;
import com.github.pagehelper.PageInfo;

//所有controller父类
@Controller
public class BaseController {
	
	//获取项目根地址，如http://localhost:8080/SmsMonitorPlate
	public String basePath(){
		HttpServletRequest request = getRequest();
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	}
	
	//获取request对象，不用在每个方法上加request参数
	public HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	//分页数据对象放入model
	public void pageOper(ModelMap model,PageInfo pageResult){
		model.addAttribute("pageResult", pageResult);
		model.addAttribute("pageBar", PageResultBuilder.buildPageBar(pageResult).getNumberPageBar());
	}
	//分页数据对象放入model
	public void pageOper(ModelMap model,PageInfo pageResult,PageUtil page){
		model.addAttribute("pageResult", pageResult);
		model.addAttribute("pageBar", PageResultBuilder.buildPageBar(pageResult,page).getNumberPageBar());
	}
	
	//操作提示到前端页面
	protected void operatePrompt(RedirectAttributes redirectAttributes,String msg){
		redirectAttributes.addFlashAttribute("promptMessage", msg);
	}
	
	//XSS验证，包括html sql script
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		//binder.registerCustomEditor(String.class, new StringEscapeEditor(true, true, true));
	}

	
}
