package com.commons.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.commons.model.JdbcInfo;


/**
 * 数据库工具类
 * @author Administrator
 *
 */
public class DBUtil {
	/**
	 * 取得数据库连接
	 * @return
	 */	
	public static Connection getConnection(JdbcInfo jdbcInfo) {
		Connection conn = null;
		try {
			//取得jdbc配置信息
			Class.forName(jdbcInfo.getDriverName());
			conn = DriverManager.getConnection(jdbcInfo.getUrl(), jdbcInfo.getUserName(), jdbcInfo.getPassWord());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
		
	}

	/**
	 * 取得数据库连接-----设置连接超时
	 *
	 * 这里使用Future接口的实现类：FutureTask
	 * 原理：  我有一个任务，交个Future给我执行，期间，我可以做其他的任何事。
	 *      在设置的时间内，如果返回我需要的数据类型（这里我是Connection），则返回；
	 *      时间一到，还没给我返回数据，那抱歉，直接给你终结了。
	 * @return
	 */
	public static Connection getConnectionTimeOUt(final JdbcInfo jdbcInfo) throws ClassNotFoundException, TimeoutException {
		Connection conn = null;
		//取得jdbc配置信息
		Class.forName(jdbcInfo.getDriverName());

		ExecutorService executor = Executors.newSingleThreadExecutor();
		FutureTask<Connection> future = new FutureTask<Connection>(new Callable<Connection>() {
			@Override
			public Connection call() throws Exception {
				return DriverManager.getConnection(jdbcInfo.getUrl(), jdbcInfo.getUserName(), jdbcInfo.getPassWord());
			}
		});
		executor.execute(future);

		try {
			conn = future.get(Constants.DB_TIME_OUT, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			future.cancel(true);
		} catch (ExecutionException e) {
			future.cancel(true);
		} finally {
			executor.shutdown();
		}

		return conn;
	}
	
	/**
	 * 关闭     PreparedStatement（预处理执行语句） 目的：可以防止SQL注入、在特定的驱动数据库下相对效率要高(不绝对)、不需要频繁编译.因为已经预加载了
	 * @param pstmt
	 */
	public static void close(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 关闭连接
	 * @param conn
	 */
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
	}
	/**
	 * 关闭数据库结果集的数据表
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 提交事务
	 * @param conn
	 */
	public static void commit(Connection conn) {
		if (conn != null) {
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 回滚事务
	 * @param conn
	 */
	public static void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 自动提交事务
	 * @param conn
	 * @param autoCommit
	 */
	public static void setAutoCommit(Connection conn, boolean autoCommit) {
		if (conn != null) {
			try {
				conn.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 关闭执行语句
	 * @param stmt
	 */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
}
