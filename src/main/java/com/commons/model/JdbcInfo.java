package com.commons.model;

/**
 * 项目名称：SmsMonitorPlate
 * 类名称：JdbcInfo
 * 类描述：数据库连接信息
 * 创建人：chenxiaoyi
 * 创建时间：2017-04-27 11:39:32
 * @version V1.0.0.T.1
 * ----------------------------------------- 
 * 修改记录(迭代更新)：chenxiaoyi- 2017-04-27 11:39:32---(新建)
 *
 */ 
public class JdbcInfo {
	
	private static final long serialVersionUID = 1L;
	
	private String driverName;
	
	private String url;
	
	private String userName;
	
	private String passWord;

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	
}
