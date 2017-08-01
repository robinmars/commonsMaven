package com.commons.util;

import com.commons.mybatis.PageUtil;
import com.github.pagehelper.PageInfo;

public final class PageResultBuilder {
	

	public static PageUtil buildPageBar(PageInfo<Object> result){
		PageUtil page = new PageUtil();
		page.setPageSize(result.getPageSize());
		page.setTotalRow(result.getTotal());
		page.setCurPage(result.getPageNum());
		return page;
	}
	
	public static PageUtil buildPageBar(PageInfo<Object> result,PageUtil page){
		if(page==null){
			page = new PageUtil();
		}
		page.setPageSize(result.getPageSize());
		page.setTotalRow(result.getTotal());
		page.setCurPage(result.getPageNum());
		return page;
	}
	
}
