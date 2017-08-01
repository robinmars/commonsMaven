package com.commons.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class RedisDcAdapter implements DC_Adapter {
	private static Log log_info = LogFactory.getLog(RedisDcAdapter.class);

	private JedisPool pool = null;
	private String poolType = null;
	private String redisServerIp = "";
	private String sentinels = "";
	private String pwd = null;
	private int redisServerPort = 6379;
	private String masterName = "mymaster";
	private int maxTotal = 2000;
	private int maxIdle = 100;
	private int maxWaitMillis = 10000;
	private int tmp_count = 0;
	private boolean testOnBorrow = true;
	private final int COUNT = 5000;
	private JedisSentinelPool sentinelPool;
	private Map<Integer, String> poolInfoMap = new HashMap();
	private int dbIndex = 0;

	private boolean flag = false;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getDbIndex() {
		return dbIndex;
	}

	public void setDbIndex(int dbIndex) {
		this.dbIndex = dbIndex;
	}

	public String getPoolType() {
		return this.poolType;
	}

	public void setPoolType(String poolType) {
		this.poolType = poolType;
	}

	public String getSentinels() {
		return this.sentinels;
	}

	public void setSentinels(String sentinels) {
		this.sentinels = sentinels;
	}

	public String getMasterName() {
		return this.masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public JedisSentinelPool getsentinelPool() {
		return this.sentinelPool;
	}

	public void setsentinelPool(JedisSentinelPool sentinelPool) {
		this.sentinelPool = sentinelPool;
	}

	public RedisDcAdapter() {
	}

	public RedisDcAdapter(String redisServerIp, int redisServerPort) {
		this.redisServerIp = redisServerIp;
		this.redisServerPort = redisServerPort;
	}

	public RedisDcAdapter(String redisServerIp, int redisServerPort, int maxTotal, int maxIdle, int maxWaitMillis,
			boolean testOnBorrow) {
		this.redisServerIp = redisServerIp;
		this.redisServerPort = redisServerPort;
		this.maxTotal = maxTotal;
		this.maxIdle = maxIdle;
		this.maxWaitMillis = maxWaitMillis;
		this.testOnBorrow = testOnBorrow;
	}

	public RedisDcAdapter(String redisServerIp, int redisServerPort, int maxTotal, int maxIdle, int maxWaitMillis,
			String pwd, boolean testOnBorrow) {
		this.redisServerIp = redisServerIp;
		this.redisServerPort = redisServerPort;
		this.maxTotal = maxTotal;
		this.maxIdle = maxIdle;
		this.maxWaitMillis = maxWaitMillis;
		this.pwd = pwd;
		this.testOnBorrow = testOnBorrow;
	}

	public RedisDcAdapter(String sentinels, String poolType, String masterName, int maxTotal, int maxIdle,
			int maxWaitMillis, boolean testOnBorrow) {
		this.sentinels = sentinels;
		this.poolType = poolType;
		this.masterName = masterName;
		this.maxTotal = maxTotal;
		this.maxIdle = maxIdle;
		this.maxWaitMillis = maxWaitMillis;
		this.testOnBorrow = testOnBorrow;
	}

	public RedisDcAdapter(String sentinels, String poolType, String masterName, int maxTotal, int maxIdle,
			int maxWaitMillis, String pwd, boolean testOnBorrow) {
		this.sentinels = sentinels;
		this.poolType = poolType;
		this.masterName = masterName;
		this.maxTotal = maxTotal;
		this.maxIdle = maxIdle;
		this.maxWaitMillis = maxWaitMillis;
		this.pwd = pwd;
		this.testOnBorrow = testOnBorrow;
	}

	public synchronized void initPool() {
		if ((this.poolType == null) || ("".equals(this.poolType))) {
			log_info.error("poolType:[" + this.poolType + "],配置错误！！！！");
		} else if ("sentinel".equals(this.poolType.trim())) {
			if (this.sentinelPool == null)
				initSentinelPool();
		} else if ("single".equals(this.poolType)) {
			if (this.pool == null)
				JedisPool();
		} else
			log_info.error("poolType:[" + this.poolType + "],配置错误！！！！");
	}

	public synchronized void JedisPool() {
		this.pool = getPool();
		log_info.info("initPool [" + this.poolType + "] a pool hashCode:" + this.pool.hashCode() + ", redisServerIp: "
				+ this.redisServerIp + ":" + this.redisServerPort);
	}

	public synchronized void initSentinelPool() {
		this.sentinelPool = getSentinelPool();
		log_info.info("initSentinelPool [" + this.poolType + "] a pool hashCode:" + this.sentinelPool.hashCode()
				+ ", sentinels: " + this.sentinels);
	}

	private synchronized JedisSentinelPool getSentinelPool() {
		if (this.sentinelPool == null) {
			Set set = new HashSet();

			String[] sentinel = this.sentinels.split(";");
			for (String ip : sentinel) {
				String[] address = ip.split(":");
				set.add(new HostAndPort(address[0], Integer.valueOf(address[1]).intValue()).toString());
			}
			JedisPoolConfig config = new JedisPoolConfig();

			config.setMaxTotal(this.maxTotal);

			config.setMaxIdle(this.maxIdle);

			config.setMaxWaitMillis(this.maxWaitMillis);

			config.setMinIdle(this.maxIdle);

			config.setTestOnBorrow(this.testOnBorrow);

			if (this.pwd != null && !"".equals(this.pwd))
				this.sentinelPool = new JedisSentinelPool(this.masterName, set, config, this.maxWaitMillis, this.pwd);
			else {
				this.sentinelPool = new JedisSentinelPool(this.masterName, set, config, this.maxWaitMillis);
			}
			this.poolInfoMap.put(Integer.valueOf(this.sentinelPool.hashCode()), String.valueOf(this.sentinels));
		}
		return this.sentinelPool;
	}

	public synchronized JedisPool getPool() {
		if (this.pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();

			config.setMaxTotal(this.maxTotal);

			config.setMaxIdle(this.maxIdle);

			config.setMinIdle(this.maxIdle);

			config.setMaxWaitMillis(this.maxWaitMillis);

			config.setTestOnBorrow(this.testOnBorrow);

			if (this.pwd != null && !"".equals(this.pwd))
				this.pool = new JedisPool(config, this.redisServerIp, this.redisServerPort, this.maxWaitMillis,
						this.pwd);
			else {
				this.pool = new JedisPool(config, this.redisServerIp, this.redisServerPort, this.maxWaitMillis);
			}
			this.poolInfoMap.put(Integer.valueOf(this.pool.hashCode()),
					String.valueOf(this.redisServerIp + ":" + this.redisServerPort));
		}
		return this.pool;
	}

	public Jedis getJedis() {
		Jedis jedis = null;
		int code = 0;
		try {
			if ((this.poolType == null) || ("".equals(this.poolType))) {
				log_info.error("poolType:[" + this.poolType + "],配置错误！！！！");
				return jedis;
			}
			if ("sentinel".equals(this.poolType.trim())) {
				if (this.sentinelPool == null) {
					initSentinelPool();
				}
				code = this.sentinelPool.hashCode();
				jedis = this.sentinelPool.getResource();
			} else if ("single".equals(this.poolType)) {
				if (this.pool == null) {
					JedisPool();
				}
				code = this.pool.hashCode();
				jedis = this.pool.getResource();
			} else {
				log_info.error("poolType:[" + this.poolType + "],配置错误！！！！");
				return jedis;
			}
		} catch (Exception e) {
			returnJedis(jedis);
			this.tmp_count += 1;
			StringBuffer sb = new StringBuffer();
			sb.append("poolType:").append(this.poolType).append(",");
			sb.append("tmp_count:").append(this.tmp_count);
			if (this.tmp_count > 10)
				try {
					if ("sentinel".equals(this.poolType.trim()))
						try {
							code = this.sentinelPool.hashCode();
							this.sentinelPool.close();
							this.sentinelPool.destroy();
							this.sentinelPool = null;
							this.poolInfoMap.remove(Integer.valueOf(code));
							initSentinelPool();
							this.tmp_count = 0;
						} catch (Exception e2) {
						}
					else if ("single".equals(this.poolType))
						try {
							code = this.pool.hashCode();
							this.pool.close();
							this.pool.destroy();
							this.pool = null;
							this.poolInfoMap.remove(Integer.valueOf(code));
							JedisPool();
							this.tmp_count = 0;
						} catch (Exception e2) {
						}
				} catch (Exception e2) {
				}
			log_info.error(
					"get jedis from [" + (String) this.poolInfoMap.get(Integer.valueOf(code)) + "]; " + sb.toString(),
					e);
			try {
				Thread.sleep(2000L);
				if (jedis == null)
					jedis = getJedis();
			} catch (Exception e1) {
				this.tmp_count += 1;
				sb = new StringBuffer();
				sb.append("poolType:").append(this.poolType).append(",");
				sb.append("tmp_count:").append(this.tmp_count);
				if (this.tmp_count > 10)
					try {
						if ("sentinel".equals(this.poolType.trim()))
							try {
								code = this.sentinelPool.hashCode();
								this.sentinelPool.close();
								this.sentinelPool.destroy();
								this.sentinelPool = null;
								this.poolInfoMap.remove(Integer.valueOf(code));
								initSentinelPool();
								this.tmp_count = 0;
							} catch (Exception e2) {
							}
						else if ("single".equals(this.poolType))
							try {
								code = this.pool.hashCode();
								this.pool.close();
								this.pool.destroy();
								this.pool = null;
								this.poolInfoMap.remove(Integer.valueOf(code));
								JedisPool();
								this.tmp_count = 0;
							} catch (Exception e2) {
							}
					} catch (Exception e2) {
					}
			}
		}
		// 设置统计接口使用的数据库index
		if (flag) {
			jedis.select(dbIndex);
		}
		return jedis;
	}

	public void returnJedis(Jedis jedis) {
		if (jedis != null)
			try {
				jedis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public String getRedisServerIp() {
		return this.redisServerIp;
	}

	public void setRedisServerIp(String redisServerIp) {
		this.redisServerIp = redisServerIp;
	}

	public int getRedisServerPort() {
		return this.redisServerPort;
	}

	public void setRedisServerPort(int redisServerPort) {
		this.redisServerPort = redisServerPort;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getMaxTotal() {
		return this.maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdle() {
		return this.maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWaitMillis() {
		return this.maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isTestOnBorrow() {
		return this.testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getQueueElement(String queueName) throws NoSuchQueueException {
		T result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			byte[] tmp = jedis.rpop(queueName.getBytes());
			if (tmp != null) {
				try {
					result = (T) new ObjectInputStream(new ByteArrayInputStream(tmp)).readObject();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			log_info.error("getQueueElement,key=[" + queueName + "] ", e);
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	@Override
	public List<Object> getQueueElements(String queueName, int number) throws NoSuchQueueException {
		List<Object> result = new ArrayList<Object>();
		List<Object> tmp_list = new ArrayList<Object>();

		Jedis jedis = null;
		try {
			jedis = getJedis();
			long length = jedis.llen(queueName.getBytes());
			if (length > 0) {
				long getNumber = (length > number ? number : length);
				Pipeline pipeline = jedis.pipelined();
				for (int i = 0; i < getNumber; i++) {
					pipeline.rpop(queueName.getBytes());
				}
				tmp_list = pipeline.syncAndReturnAll();

			}

			for (int i = 0; tmp_list != null && i < tmp_list.size(); i++) {
				if (tmp_list.get(i) != null) {
					byte[] tmp = (byte[]) tmp_list.get(i);
					Object obj = Utils.toObject(tmp);
					if (obj != null) {
						result.add(obj);
					}
				}

			}
		} catch (Exception e) {
			log_info.error("getQueueElements,key=[" + queueName + "]", e);
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	@Override
	public <T> boolean addQueueElement(String queueName, T element) {
		boolean result = false;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			new ObjectOutputStream(bos).writeObject(element);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Jedis jedis = null;
		try {
			jedis = getJedis();
			long r = jedis.lpush(queueName.getBytes(), bos.toByteArray());
			if (r > 0) {
				result = true;
			}
		} catch (Exception e) {
			log_info.error("addQueueElement,key=[" + queueName + "]" + jedis.isConnected(), e);
		} finally {
			returnJedis(jedis);
		}

		return result;
	}

	@Override
	public <T> boolean addQueueElements(String queueName, List<T> elements) {
		boolean result = false;
		if (elements != null && elements.size() > 0) {
			byte[][] total = new byte[elements.size()][];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			for (int i = 0; i < elements.size(); i++) {
				Object obj = elements.get(i);
				try {
					new ObjectOutputStream(bos).writeObject(obj);
					byte[] t = bos.toByteArray();
					bos.flush();
					bos.reset();
					total[i] = t;
				} catch (IOException e) {
					log_info.error("addQueueElements,key=[" + queueName + "]", e);
				}
			}
			Jedis jedis = null;
			try {
				jedis = getJedis();
				long r = jedis.lpush(queueName.getBytes(), total);
				if (r > 0) {
					result = true;
				}
			} catch (Exception e) {
				log_info.error("addQueueElements,key=[" + queueName + "]", e);
			} finally {
				returnJedis(jedis);
			}
		}
		return result;
	}

	@Override
	public <T> boolean addQueueElements(String queueName, T element) {
		boolean result = false;
		if (element != null) {
			byte[][] total = new byte[1][];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Object obj = element;
			try {
				new ObjectOutputStream(bos).writeObject(obj);
				byte[] t = bos.toByteArray();
				bos.flush();
				bos.reset();
				total[0] = t;
			} catch (IOException e) {
				log_info.error("addQueueElements,key=[" + queueName + "]", e);
			}
			Jedis jedis = null;
			try {
				jedis = getJedis();
				long r = jedis.lpush(queueName.getBytes(), total);
				if (r > 0) {
					result = true;
				}
			} catch (Exception e) {
				log_info.error("addQueueElements,key=[" + queueName + "]", e);
			} finally {
				returnJedis(jedis);
			}
		}
		return result;
	}

	@Override
	public <T> boolean addHashQueueElements(String queueName, Map<String, T> hashValue) {
		boolean result = false;
		if (queueName != null && hashValue != null && hashValue.size() > 0) {
			Map<byte[], byte[]> tmp = new HashMap<byte[], byte[]>();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			for (String key : hashValue.keySet()) {
				try {
					new ObjectOutputStream(bos).writeObject(hashValue.get(key));
					byte[] t = bos.toByteArray();
					bos.flush();
					bos.reset();
					tmp.put(key.getBytes(), t);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Jedis jedis = null;
			try {
				jedis = getJedis();
				String r = jedis.hmset(queueName.getBytes(), tmp);
				if (r != null && r.equalsIgnoreCase("OK")) {
					result = true;
				}
			} catch (Exception e) {
				log_info.error("addHashQueueElements,key=[" + queueName + "]", e);
			} finally {
				returnJedis(jedis);
			}
		}
		return result;
	}

	@Override
	public boolean delHashQueueElements(String queueName, Set<String> fields) {
		boolean result = false;
		if (queueName != null && fields != null && fields.size() > 0) {
			byte[][] tmp = new byte[fields.size()][];
			int i = 0;
			for (String each : fields) {
				tmp[i] = each.getBytes();
				i++;
			}
			Jedis jedis = null;
			try {
				jedis = getJedis();
				long r = jedis.hdel(queueName.getBytes(), tmp);
				if (r == fields.size()) {
					result = true;
				}
			} catch (Exception e) {
				log_info.error("delHashQueueElements,key=[" + queueName + "]", e);
			} finally {
				returnJedis(jedis);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getHashQueueValues(String queueName, Set<String> fields) {
		List<T> result = new ArrayList<T>();
		if (queueName != null && fields != null && fields.size() > 0) {
			byte[][] tmp = new byte[fields.size()][];
			int i = 0;
			for (String each : fields) {
				tmp[i] = each.getBytes();
				i++;
			}
			Jedis jedis = null;
			try {
				jedis = getJedis();
				List<byte[]> r = jedis.hmget(queueName.getBytes(), tmp);
				if (r != null) {
					for (byte[] each : r) {
						if (null == each)
							continue;
						result.add((T) Utils.toObject(each));
					}
				}
			} catch (Exception e) {
				log_info.error("getHashQueueValues,key=[" + queueName + "]", e);
			} finally {
				returnJedis(jedis);
			}
		}
		return result;
	}

	@Override
	public long getQueueSize(String queueName) throws NoSuchQueueException {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.llen(queueName);
		} catch (Exception e) {
			log_info.error("getQueueSize,key=[" + queueName + "]", e);
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	@Override
	public Object getCacheData(String key) {
		Object result = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			byte[] tmp = jedis.get(key.getBytes());
			if (tmp != null) {
				try {
					result = (Object) new ObjectInputStream(new ByteArrayInputStream(tmp)).readObject();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			log_info.error("getCacheData,key=[" + key + "]", e);
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	@Override
	public List<Object> getCacheData(List<String> keys) {
		List<Object> result = new ArrayList<Object>();
		Jedis jedis = null;

		byte[][] total = new byte[keys.size()][];
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			total[i] = key.getBytes();
		}
		try {
			jedis = getJedis();
			List<byte[]> tmp = jedis.mget(total);
			for (byte[] each : tmp) {
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(each));
				Object tmpObj = ois.readObject();
				result.add(tmpObj);
				ois.close();
			}
		} catch (Exception e) {
			log_info.error("getCacheData,key=[" + keys + "]", e);
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	@Override
	public boolean putCacheData(Map<String, ?> cacheMap) {
		boolean result = false;
		Jedis jedis = null;

		byte[][] total = new byte[cacheMap.size() * 2][];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int i = 0;
		for (String eachKey : cacheMap.keySet()) {
			Object cache = cacheMap.get(eachKey);
			try {
				new ObjectOutputStream(bos).writeObject(cache);
				byte[] t = bos.toByteArray();
				bos.flush();
				bos.reset();
				total[i++] = eachKey.getBytes();
				total[i++] = t;
			} catch (IOException e) {
				log_info.error("putCacheData", e);
			}
		}
		try {
			jedis = getJedis();
			String resp = jedis.mset(total);
			if ("OK".equals(resp)) {
				result = true;
			}
		} catch (Exception e) {
			log_info.error("putCacheData,jedis_info", e);
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	@Override
	public void putRWCacheDataInStringMode(Map<String, ?> cacheMap) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			for (String eachKey : cacheMap.keySet()) {
				Object cache = cacheMap.get(eachKey);
				Field[] fields = cache.getClass().getDeclaredFields();
				Map<String, String> hash = new HashMap<String, String>();

				for (Field eachField : fields) {
					String strValue;
					try {
						strValue = cache.getClass().getMethod("get" + Utils.FirstUpperCase(eachField.getName()), null)
								.invoke(cache, null).toString();
						hash.put(eachField.getName(), strValue);
					} catch (Exception e) {
						log_info.error("putRWCacheDataInStringMode", e);
					}
				}
				jedis.hmset(eachKey, hash);
				jedis.sadd("HashCacheKeysSet", eachKey);
			}
		} catch (Exception e) {
			log_info.error("putRWCacheDataInStringMode", e);
		} finally {
			returnJedis(jedis);
		}
	}

	@Override
	public void putRWCacheData(Map<String, ?> cacheMap) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			for (String eachKey : cacheMap.keySet()) {
				Object cache = cacheMap.get(eachKey);
				Field[] fields = cache.getClass().getDeclaredFields();
				Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();

				for (Field eachField : fields) {
					Object objValue;
					byte[] value;
					try {
						objValue = cache.getClass().getMethod("get" + Utils.FirstUpperCase(eachField.getName()), null)
								.invoke(cache, null);
						value = Utils.toByteArray(objValue);
						hash.put(eachField.getName().getBytes(), value);
					} catch (Exception e) {
						log_info.error("putRWCacheData", e);
					}
				}
				jedis.hmset(eachKey.getBytes(), hash);
				jedis.sadd("HashCacheKeysSet", eachKey);
			}
		} catch (Exception e) {
			log_info.error("putRWCacheData", e);
		} finally {
			returnJedis(jedis);
		}
	}

	@Override
	public <T> List<T> getRWCacheInStringMode(String keyPattern, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			ScanParams sp = new ScanParams();
			sp.match(keyPattern);
			sp.count(COUNT);
			ScanResult<String> rs = jedis.sscan("HashCacheKeysSet", "0", sp);
			List<String> keySet = rs.getResult();

			Pipeline pipeline = jedis.pipelined();

			for (String each : keySet) {
				pipeline.hgetAll(each);
			}
			List<Object> tmpList = pipeline.syncAndReturnAll();
			for (Object each : tmpList) {
				@SuppressWarnings("unchecked")
				Map<String, String> tmp = (Map<String, String>) each;
				T o = clazz.newInstance();
				for (String field : tmp.keySet()) {
					String str = tmp.get(field);
					Method m = clazz.getMethod("set" + Utils.FirstUpperCase(new String(field)), String.class);
					m.invoke(o, str);
				}
				result.add(o);
			}
		} catch (Exception e) {
			log_info.error("getRWCacheInStringMode,key=[" + keyPattern + "]", e);
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	@Override
	public <T> List<T> getRWCacheInStringModeByKey(String keyPattern, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Set<String> keySet = jedis.keys(keyPattern);

			Pipeline pipeline = jedis.pipelined();

			for (String each : keySet) {
				pipeline.hgetAll(each);
			}
			List<Object> tmpList = pipeline.syncAndReturnAll();
			for (Object each : tmpList) {
				@SuppressWarnings("unchecked")
				Map<String, String> tmp = (Map<String, String>) each;
				T o = clazz.newInstance();
				for (String field : tmp.keySet()) {
					String str = tmp.get(field);
					Method m = clazz.getMethod("set" + Utils.FirstUpperCase(new String(field)), String.class);
					m.invoke(o, str);
				}
				result.add(o);
			}
		} catch (Exception e) {
			log_info.error("getRWCacheInStringModeByKey,key=[" + keyPattern + "]", e);
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	@Override
	public <T> List<T> getRWCache(String keyPattern, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		Jedis jedis = null;
		try {
			jedis = getJedis();

			ScanParams sp = new ScanParams();
			sp.match(keyPattern);
			sp.count(COUNT);
			ScanResult<String> rs = jedis.sscan("HashCacheKeysSet", "0", sp);
			List<String> keySet = rs.getResult();

			Pipeline pipeline = jedis.pipelined();

			for (String each : keySet) {
				pipeline.hgetAll(each.getBytes());
			}
			List<Object> tmpList = pipeline.syncAndReturnAll();
			for (Object each : tmpList) {
				@SuppressWarnings("unchecked")
				Map<byte[], byte[]> tmp = (Map<byte[], byte[]>) each;
				T o = clazz.newInstance();
				Method[] ms = clazz.getMethods();
				for (byte[] field : tmp.keySet()) {

					Object obj = Utils.toObject(tmp.get(field));
					for (Method m : ms) {
						if (m.getName().equals("set" + Utils.FirstUpperCase(new String(field)))) {
							m.invoke(o, obj);
						}
					}
				}
				result.add(o);
			}
		} catch (Exception e) {
			log_info.error("getRWCache,key=[" + keyPattern + "]", e);
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	@Override
	public <T> List<T> getRWCacheByKey(String keyPattern, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Set<String> keySet = jedis.keys(keyPattern);
			Pipeline pipeline = jedis.pipelined();

			for (String each : keySet) {
				pipeline.hgetAll(each.getBytes());
			}
			List<Object> tmpList = pipeline.syncAndReturnAll();
			for (Object each : tmpList) {
				@SuppressWarnings("unchecked")
				Map<byte[], byte[]> tmp = (Map<byte[], byte[]>) each;
				T o = clazz.newInstance();
				Method[] ms = clazz.getMethods();
				for (byte[] field : tmp.keySet()) {

					Object obj = Utils.toObject(tmp.get(field));
					for (Method m : ms) {
						if (m.getName().equals("set" + Utils.FirstUpperCase(new String(field)))) {
							m.invoke(o, obj);
						}
					}
				}
				result.add(o);
			}
		} catch (Exception e) {
			log_info.error("getRWCacheByKey,key=[" + keyPattern + "]", e);
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	@Override
	public void updateCachesValue(String[] changeFields, Map<String, ?> currentMap) {
		Jedis jedis = null;
		if (changeFields != null && currentMap != null) {
			try {
				jedis = getJedis();
				Pipeline pipeline = jedis.pipelined();

				for (String key : currentMap.keySet()) {
					Object obj = currentMap.get(key);
					for (String field : changeFields) {
						Object changedValue = obj.getClass().getMethod("get" + Utils.FirstUpperCase(field), null)
								.invoke(obj, null);
						pipeline.hset(key.getBytes(), field.getBytes(), Utils.toByteArray(changedValue));
					}
				}
				pipeline.sync();
			} catch (Exception e) {
				log_info.error("updateCachesValue", e);
			} finally {
				returnJedis(jedis);
			}
		}
	}

	@Override
	public void updateCachesValueInStringMode(String[] changeFields, Map<String, ?> currentMap) {
		Jedis jedis = null;
		if (changeFields != null && currentMap != null) {
			try {
				jedis = getJedis();
				Pipeline pipeline = jedis.pipelined();
				for (String key : currentMap.keySet()) {
					Object obj = currentMap.get(key);
					for (String field : changeFields) {
						Object changedValue = obj.getClass().getMethod("get" + Utils.FirstUpperCase(field), null)
								.invoke(obj, null);
						pipeline.hset(key, field, changedValue.toString());
					}
				}
				pipeline.sync();
			} catch (Exception e) {
				log_info.error("updateCachesValueInStringMode", e);
			} finally {
				returnJedis(jedis);
			}
		}

	}

	@Override
	public void removeCacheKey(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.srem("HashCacheKeysSet", key);
			jedis.del(key);
		} catch (Exception e) {
			log_info.error("removeCacheKey,key=[" + key + "]", e);
		} finally {
			returnJedis(jedis);
		}
	}

	@Override
	public <T> boolean addStringValue(String prefix, Map<String, T> keyValues) {
		boolean result = false;
		if (keyValues != null && keyValues.size() > 0) {
			Jedis jedis = null;
			try {
				jedis = getJedis();
				List<byte[]> tmpList = new ArrayList<byte[]>();
				for (String each : keyValues.keySet()) {
					T t = keyValues.get(each);
					tmpList.add((prefix + each).getBytes());
					tmpList.add(Utils.toByteArray(t));
				}
				byte[][] bytes = tmpList.toArray(new byte[tmpList.size()][]);
				String response = jedis.mset(bytes);
				if ("OK".equals(response)) {
					result = true;
				}
			} catch (Exception e) {
				log_info.error("addStringValue,key=[" + prefix + "]", e);
			} finally {
				returnJedis(jedis);
			}
		}
		return result;
	}

	@Override
	public List<Object> getStringTypeValues(List<String> keys) {
		List<Object> result = new ArrayList<Object>();
		Jedis jedis = null;
		if (keys != null && keys.size() > 0) {
			try {
				byte[][] byteKeys = new byte[keys.size()][];
				jedis = getJedis();
				for (int i = 0; i < keys.size(); i++) {
					String key = keys.get(i);
					byteKeys[i] = key.getBytes();
				}

				List<byte[]> byteValues = jedis.mget(byteKeys);
				for (byte[] eachValue : byteValues) {
					Object obj = Utils.toObject(eachValue);
					result.add(obj);
				}
			} catch (Exception e) {
				log_info.error("getStringTypeValues,key=[" + keys + "]", e);
			} finally {
				returnJedis(jedis);
			}
		}
		return result;
	}

	/**
	 * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。 若域 field 已经存在，该操作无效。 如果
	 * key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
	 * 
	 * @param arg0
	 * @param field
	 * @param value
	 * @return 设置成功，返回 1 。 如果给定域已经存在且没有操作被执行，返回 0 。
	 */
	@Override
	public Long hsetnx(String arg0, String field, String value) {
		Long l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.hsetnx(arg0, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}

	@Override
	public Long expire(byte[] key, int value) {
		Long l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.expire(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}

	/**
	 * 设置一个key 的过期时间(单位:秒)， 返回 1 成功 ，0 表示 key已经设置过过期时间或者不存在
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long expire(String key, int value) {
		Long l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.expire(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。 如果 key 已经存在， SETEX
	 * 命令将覆写旧值。
	 * 
	 * @param key
	 * @param seconds
	 * @param value
	 * @return 设置成功时返回 OK 。 当 seconds 参数不合法时，返回一个错误。
	 */
	public String setex(String key, int seconds, String value) {
		String l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}
	
	public String setex(byte[] key, int seconds, byte[] value) {
		String l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}

	/**
	 * 将 key 的值设为 value ，当且仅当 key 不存在。 若给定的 key 已经存在，则 SETNX 不做任何动作。
	 * 
	 * @param key
	 * @param value
	 * @return 设置成功，返回 1 。 设置失败，返回 0 。
	 */
	public Long setnx(String key, String value) {
		Long l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.setnx(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}

	/**
	 * 将字符串值 value 关联到 key 。 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
	 * 
	 * @param key
	 * @param value
	 * @return 总是返回 OK ，因为 SET 不可能失败。
	 */
	public String set(String key, String value) {
		String l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}
	
	
	/**
	 * 将字符串值 value 关联到 key 。 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
	 * 
	 * @param key
	 * @param value
	 * @return 总是返回 OK ，因为 SET 不可能失败。
	 */
	public String set(byte[] key, byte[] value) {
		String l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}

	/**
	 * 判断指定键是否存在
	 * 
	 * @param key
	 * @return
	 */
	public Boolean exists(String key) {
		Boolean l = false;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}
	
	public Boolean exists(byte[] key) {
		Boolean l = false;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}

	/**
	 * 获取key 对应的string值,如果key 不存在返回 nil。
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}
	
	public byte[] get(byte[] key) {
		byte[] l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}

	public Long del(String arg0) {
		Long l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.del(arg0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}
	
	public Long del(byte[] arg0) {
		Long l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.del(arg0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}
	
	//
	public Set<String> keys(String arg0) {
		Set<String> l = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			l = jedis.keys(arg0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				try {
					returnJedis(jedis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}
	
}
