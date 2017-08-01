package com.commons.util;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;

import com.alibaba.druid.pool.DruidDataSource;


/**
 * 
 * 类名称：SqlSessionFactoryBeanExtend    
 * 类描述：扩展org.mybatis.spring.SqlSessionFactoryBean类，增加多类型配置文件，根据数据库类型选择对应的配置文件
 * 创建人：yanzhiying
 * 创建时间：2016-6-13 上午11:53:33
 *
 */
public class SqlSessionFactoryBeanExtend extends SqlSessionFactoryBean {
	private final Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 多个mybatis Mapper配置文件路径<br />
	 * key为数据库类型，value为该方言对应Mapper配置文件路径
	 */
	private Map<String, Resource[]> mapperLocationsMap;
	
	/**
	 * 
	 * 创建一个新的实例 SqlSessionFactoryBeanExtend.    
	 * 根据使用的数据库类型选择mybatis Mapper配置文件
	 * @param dataSource
	 * @param useDialect
	 * @param mapperLocationsMap
	 * @throws Exception
	 *
	 */
	public SqlSessionFactoryBeanExtend(DataSource dataSource,Map<String, Resource[]> mapperLocationsMap) throws Exception {
		if(mapperLocationsMap==null){
			throw new Exception("mybatis mapperLocations未指定！");
		}
		
		//此处数据源写死，换数据源时应注意此处
		DruidDataSource druidDataSource = (DruidDataSource)dataSource;
		String dbType = JdbcUtil.getDbTypeByUrl(druidDataSource.getUrl());
		
		if(log.isDebugEnabled()){
			log.debug("SqlSessionFactoryBeanExtend...");
			log.debug("dbType:"+dbType+" -- mappers:");
		}
		
		/*
		 * 根据数据库类型选择mapperLocations，不识别类型默认使用"mysql"
		 */
		if(mapperLocationsMap.containsKey(dbType)){
			if(log.isDebugEnabled()){
				for (int i = 0; i < mapperLocationsMap.get(dbType).length; i++) {
					log.debug(mapperLocationsMap.get(dbType)[i].toString());
				}
			}
			super.setMapperLocations(mapperLocationsMap.get(dbType));
		}else{
			if(log.isDebugEnabled()){
				for (int i = 0; i < mapperLocationsMap.get("mysql").length; i++) {
					log.debug(mapperLocationsMap.get("mysql")[i].toString());
				}
			}
			super.setMapperLocations(mapperLocationsMap.get("mysql"));
			
			/*TODO 添加数据库类型时维护此处 */
			log.warn("不支持的数据库类型，已使用默认mysql类型的Mapper文件！当前支持的数据库类型："+JdbcUtil.DBTYPE_MYSQL+"、"+JdbcUtil.DBTYPE_SQLSERVER+"、" + JdbcUtil.DBTYPE_ORACLE);
		}
		super.setDataSource(dataSource);
	}
	
	public Map<String, Resource[]> getMapperLocationsMap() {
		return mapperLocationsMap;
	}

	public void setMapperLocationsMap(Map<String, Resource[]> mapperLocationsMap) {
		this.mapperLocationsMap = mapperLocationsMap;
	}
}
