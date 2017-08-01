package com.commons.mybatis;

import java.io.Serializable;


public class BasePage implements Serializable{
	private static final long serialVersionUID = -6352961532639922121L;
	private int curPage = 1 ; // 当前页
	private int pgSize = 20 ;	// 每页多少行
	
	
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getPgSize() {
		return pgSize;
	}
	public void setPgSize(int pgSize) {
		this.pgSize = pgSize;
	}
	
	

	
}
