package com.commons.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;


public interface DC_Adapter {
	
	public Jedis getJedis();
	
	public void returnJedis(Jedis jedis);
	
	/**
	 * 非阻塞的获取数据中心指定队列中单个元素
	 * @param String queueName （队列名 ）
	 * @return Object 如果队列空返回null
	 * throws NoSuchQueueExcepiton 如果指定队列不存在，抛出NoSuchQueueException
	 */
	public <T> T getQueueElement(String queueName) throws NoSuchQueueException;

	/**
	 * 非阻塞的获取数据中心指定队列中多个元素
	 * @param String queueName （队列名 ） int number（获取数量）  
	 * @return List<Object> 如果队列空返回空队列
	 * throws NoSuchQueueExcepiton 如果指定队列不存在，抛出NoSuchQueueException
	 */
	public List<Object> getQueueElements(String queueName, int number) throws NoSuchQueueException;
	
	/**
	 * 向HashQueue中添加元素，需要指明在hash表中的field名。
	 * @param queueName
	 * @param field
	 * @param obj
	 * return true 如果一切正常，异常情况下返回false
	 */
	public <T> boolean addHashQueueElements(String queueName, Map<String, T> hashValue);
	
	/**
	 * 删除hash表中的元素
	 * @param queueName
	 * @param field
	 * @return
	 */
	public boolean delHashQueueElements(String queueName, Set<String> fields);
	
	/**
	 * 获取hash表中对应键的值
	 * @param queueName
	 * @param fields
	 * @return
	 */
	public <T> List<T> getHashQueueValues(String queueName, Set<String> fields);
		
	/**
	 * 向数据中心添加string类型的缓存数据
	 * @param String prefix(前缀名), Map<String, T> keyValues (实际key-value) 
	 * @return boolean 添加成功返回true，失败返回false；
	 */
	public <T> boolean addStringValue(String prefix, Map<String, T> keyValues);
	
	/**
	 * 获取数据中心String类型的多个元素
	 * @param keys Nosql 数据库中key的集合 
	 * @return List<Object> 如果队列空返回null
	 */
	public List<Object> getStringTypeValues(List<String> keys);
	
	/**
	 * 向指定队列中添加单个元素
	 * @param queueName 目标队列名称
	 * @param element 要添加的元素
	 * @return true = 添加成功；false = 添加失败，可能是队列过大或数据中心暂挂导致
	 */
	public <T> boolean addQueueElement(String queueName, T element);

	/**
	 * 向指定队列中添加多个元素
	 * @param queueName 目标队列名称
	 * @param elements 要添加的集合，以list存放
	 * @return true = 添加成功；false = 添加失败，可能是队列过大或数据中心暂挂导致
	 */
	public <T> boolean addQueueElements(String queueName, List<T> elements);
	
	/**
	 * 向指定队列中添加多个元素
	 * @param queueName 目标队列名称
	 * @param element 要添加单个对象，以list存放
	 * @return true = 添加成功；false = 添加失败，可能是队列过大或数据中心暂挂导致
	 */
	public <T> boolean addQueueElements(String queueName, T element);
	
	/**
	 * 
	 * @param queueName
	 * @return 队列中的元素数
	 * @throws NoSuchQueueExcepiton
	 */
	public long getQueueSize(String queueName) throws NoSuchQueueException;
	

	/**
	 * 到数据中心取数据库对应的缓存信息
	 * @param key
	 * @return 缓存对象，可能是map，list，set等
	 */
	public Object getCacheData(String key);
	
	/**
	 * 批量到数据中心取数据库对应的缓存信息
	 * @param keys
	 * @return 与keys排序相同的缓存对象
	 */
	public List<Object> getCacheData(List<String> keys);
	
	/**
	 * 将缓存数据放入数据中心
	 * @param cacheMap
	 * @return true:添加缓存成功，false:添加缓存失败
	 */
	public boolean putCacheData(Map<String, ?> cacheMap);
	
	/**
	 * 将可读写的缓存写入数据中心以byte数组的形式
	 * @param cacheMap
	 */
	public void putRWCacheData(Map<String, ?> cacheMap);
	
	/**
	 * 将可读写的缓存写入数据中心以String的形式
	 * @param cacheMap
	 */
	public void putRWCacheDataInStringMode(Map<String, ?> cacheMap);
	/**
	 * 从数据中获取多个可读写的缓存对象，以list形式返回
	 * @param <T>
	 * @param keyPattern 数据中心的key 的pattern
	 * @param clazz 该缓存对应对象的类
	 * @return 与keyPattern匹配的key对应的缓存组装成的list
	 */
	public <T> List<T> getRWCache(String keyPattern, Class<T> clazz);
	
	public <T> List<T> getRWCacheByKey(String keyPattern, Class<T> clazz);
	
	/**
	 * 从数据中获取多个可读写的缓存对象，以list形式返回
	 * @param <T>
	 * @param keyPattern 数据中心的key 的pattern
	 * @param clazz 该缓存对应对象的类
	 * @return 与keyPattern匹配的key对应的缓存组装成的list
	 */
	public <T> List<T> getRWCacheInStringMode(String keyPattern, Class<T> clazz);
	
	public <T> List<T> getRWCacheInStringModeByKey(String keyPattern, Class<T> clazz);
	/**
	 * 更新可写缓存的字段值
	 * @param changeFields 要更新的字段组
	 * @param currentMap 要更新的缓存的key和对应的对象
	 */
	public void updateCachesValue(String[] changeFields, Map<String, ?> currentMap);
	
	/**
	 * 更新可写缓存的字段值, 以str方式
	 *  * @param changeFields 要更新的字段组
	 * @param currentMap 要更新的缓存的key和对应的对象
	 */
	public void updateCachesValueInStringMode(String[] changeFields, Map<String, ?> currentMap);
	
	/**
	 * 移除数据中心中该key和值，以及所有对该key的引用
	 * @param key
	 */
	public void removeCacheKey(String key);
	
	/***
	 * 设置hash field为指定值，如果 key 不存在，则先创建。如果 field已经存在，返回0，nx是
		not exist的意思。
	 */
	public Long hsetnx(String arg0, String arg1, String arg2);
	
	/***
	 * 设置一个key 的过期时间(单位:秒) 
	 */
	public Long expire(String arg0, int arg1) ;
	public Long expire(byte[] arg0, int arg1);
	
	/***
	 * 设置key 对应的值为 string类型的value，并指定此键值对应的有效期
	 */
	public String setex(String arg0, int arg1, String arg2) ;
	public String setex(byte[] arg0, int arg1, byte[] arg2) ;
	/***
	 * 设置key 对应的值为 string类型的value。 如果 key 已经存在，返回 0， nx是 not exist的意思。
	 */
	
	public Long setnx(String arg0, String arg1) ;
	
	/***
	 * 设置key 对应的值为 string类型的value。
	 */
	public String set(String arg0, String arg1);
	
	public String set(byte[] arg0, byte[] arg1);
	
	/***
	 * 判断指定键是否存在
	 */
	public Boolean exists(String arg0) ;
	
	public Boolean exists(byte[] arg0) ;
	
	/***
	 * 获取key 对应的string值,如果key 不存在返回 nil。
	 */
	public String get(String arg0);
	
	public byte[] get(byte[] arg0);
	
	/**
	 * 删除指定key
	 */
	public Long del(String arg0);
	
	public Long del(byte[] arg0);
	
	/**
	 * 查找给指定key的字符串开头，返回。
	 */
	public  Set<String> keys(String arg0);
	
}
