package com.commons.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.commons.mapper.GenericMapper;
import com.commons.mybatis.BasePage;

@Service
public class GenericService<T,Pk>{
	
	private GenericMapper<T,Long> genericMapper;
	
	public GenericService() {
		super();
	}

	public GenericService(GenericMapper<T, Long> genericMapper) {
        this.genericMapper = genericMapper;
    }
	
	public void insert(T t){
		genericMapper.insert(t);
	}
	
	public int insertGetPk(T t){
		return genericMapper.insertGetPk(t);
	}
	
	public void update(T t){
		genericMapper.update(t);
	}
	
	public void delete(String ids){
		genericMapper.delete(ids);
	}
		
	public T getByPk(Long id){
		return (T)genericMapper.getByPk(id);
	}
	
	public List<T> getByPkIds(String ids){
		return (List<T>)genericMapper.getByPkIds(ids);
	}
	
	public List<Object> getListByPage(BasePage basePage){
		return genericMapper.getListByPage(basePage);
	}
	
	public List<Object> getList(Map<String ,Object> map){
		return genericMapper.getList(map);
	}

	
}
