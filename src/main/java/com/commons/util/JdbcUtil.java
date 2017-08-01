package com.commons.util;

/**
 * 
 * 类名称：JdbcUtil    
 * 类描述：JDBC工具类
 * 创建人：yanzhiying
 * 创建时间：2016-6-13 下午6:54:55
 *
 */
public abstract class JdbcUtil {
	
	/**
	 * 数据库类型 - mysql
	 */
	public static final String DBTYPE_MYSQL = "mysql";
	
	/**
	 * 数据库类型 - sqlserver(2005版本及以上)
	 */
	public static final String DBTYPE_SQLSERVER = "sqlserver";
	
	/**
	 * 数据库类型 - oracle
	 */
	public static final String DBTYPE_ORACLE = "oracle";
	
	/**
	 * 数据库类型 - 其他
	 */
	public static final String DBTYPE_OTHER = "other";
	
	//当前数据库类型
	private static String curDBType=null;
	
	/**
	 * 
	 * getDbTypeByUrl(根据jdbcUrl获得数据库类型，当前支持mysql、sqlserver、oracle类型)
	 * @param jdbcUrl
	 * @return
	 *
	 */
	public static String getDbTypeByUrl(String jdbcUrl){
		if(jdbcUrl.startsWith("jdbc:mysql:")){
			setCurDBType(DBTYPE_MYSQL);
			return DBTYPE_MYSQL;
		}else if(jdbcUrl.startsWith("jdbc:sqlserver:")){
			setCurDBType(DBTYPE_SQLSERVER);
			return DBTYPE_SQLSERVER;
		}else if(jdbcUrl.startsWith("jdbc:oracle:")){
			setCurDBType(DBTYPE_ORACLE);
			return DBTYPE_ORACLE;
		}else{
			setCurDBType(DBTYPE_OTHER);
			return DBTYPE_OTHER;
		}
	}

	/**
	 * 
	 * setCurDBType(设置当前使用数据库类型)
	 * @param dbType
	 *
	 */
	private static void setCurDBType(String dbType){
		if(curDBType==null || curDBType.equals("")){
			curDBType = dbType;
		}
	}
	
	/**
	 * 
	 * getCurDBType(获得当前使用数据库类型) 
	 * @return
	 *
	 */
	public static String getCurDBType() {
		return curDBType;
	}
}
