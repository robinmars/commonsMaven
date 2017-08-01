package com.commons.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.commons.mybatis.BasePage;

public interface GenericMapper<T,PK extends Serializable> {
	
	//插入
	public void insert(T t);
	
	//插入后并返回该主键,这里的返回值int并不是该主键，而是更改记录的条数，返回的主键应该"实体.getsn()";
	public int insertGetPk(T t);
	
	//修改
	public void update(T t);
	
	//删除
	public void delete(String ids);
	
	//根据主键获取对象实体
	public T getByPk(PK id);
	
	//根据IDs获取对象列表
	public List<T> getByPkIds(String ids);
	
	//分页查询
	public List<Object> getListByPage(BasePage basePage);
	
	//查询
	public List<Object> getList(Map<String ,Object> map);
	

}
