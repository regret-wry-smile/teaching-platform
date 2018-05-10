package com.zkxltech.jdbc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ejet.core.util.constant.Constant;
import com.ejet.core.util.io.IOUtils;
import com.zkxltech.domain.Result;

public class DBHelper<T> {
	private static String dbNameStr = "answer.db";

	/**
	 * 查询
	 * 
	 * @param dbNameStr
	 *            数据库名
	 * @param sql
	 *            查询的SQL语句
	 * @param key
	 *            查询结果对应的key
	 * @return
	 * @throws SQLException
	 */
	public synchronized Result onQuery(String sql, T clazz) {
		Result result = new Result();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbNameStr.trim());
			conn.setAutoCommit(false);

			stmt = conn.prepareStatement(sql);
			String[] property = getFiledName(clazz); // 实体类字段

			if (clazz == null) {
				rs = stmt.executeQuery();
			} else {
				int index = 0;
				for (int i = 0; i < getFields(clazz).length; i++) {
					Object obj = getFiledValues(getFields(clazz)[i], clazz);
					if (obj != null) {
						index++;
						stmt.setObject(index, obj);
					}
				}
				rs = stmt.executeQuery();
			}

			List<T> retList = new ArrayList<T>();
			while (rs.next()) {
				T t = (T) clazz.getClass().newInstance();
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < property.length; i++) {
					Object object = rs.getObject(HumpToUnderline(property[i]));
					setMethodValue(property[i], object, t);
				}
				retList.add(t);
			}
			rs.close();
			stmt.close();
			conn.close();
			result.setRet(Constant.SUCCESS);
			result.setItem(retList);
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setDetail(IOUtils.getError(e));
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e2) {
			}
		}
		return result;
	}

	/**
	 * 增加 删除 修改
	 * 
	 * @param dbNameStr
	 *            数据库名
	 * @param sql
	 *            sql语句
	 * @param param
	 *            插入的参数
	 * @throws SQLException
	 */
	public synchronized Result onUpdate(String sql, T clazz) {
		Result result = new Result();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbNameStr.trim());
			conn.setAutoCommit(false);

			stmt = conn.prepareStatement(sql);
			
			if (clazz != null) {
				int index = 0;
				for (int i = 0; i < getFields(clazz).length; i++) {
					Object obj = getFiledValues(getFields(clazz)[i], clazz);
					if (obj != null) {
						index++;
						stmt.setObject(index, obj);
					}
				}
			}
			stmt.executeUpdate();
			stmt.close();
			conn.commit();
			conn.close();
			result.setRet(Constant.SUCCESS);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setDetail(IOUtils.getError(e));
			return result;
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * 批量增加 删除 修改
	 * 
	 * @param dbNameStr
	 *            数据库名
	 * @param sql
	 *            sql语句
	 * @param param
	 *            插入的参数
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public synchronized Result onUpdateByGroup(List<String> sqls) {
		Result result = new Result();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbNameStr.trim());
			conn.setAutoCommit(false);

			for (int i = 0; i < sqls.size(); i++) {
				stmt = conn.prepareStatement(sqls.get(i));
				stmt.executeUpdate();
			}
			stmt.close();
			conn.commit();
			conn.close();
			result.setRet(Constant.SUCCESS);
			return result;
		} catch (Exception e) {
			result.setRet(Constant.ERROR);
			result.setDetail(IOUtils.getError(e));
			return result;
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e2) {
			}

		}

	}

	public synchronized Connection getConnection(String dbNameStr) {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbNameStr.trim());
			return conn;
		} catch (Exception e) {
		}
		return null;
		// DBCP2Config config = new DBCP2Config();
		// String driverClassName="org.sqlite.JDBC";
		// String url="jdbc:sqlite:answers.db";
		// String username="hxtech";
		// String password="hxtech";
		// Integer initialSize=10;
		// Integer minIdle=5;
		// Integer maxIdle=10;
		// Integer maxActive=20;
		// config.setDriverClassName(driverClassName);
		// config.setUrl(url);
		// config.setUsername(username);
		// config.setPassword(password);
		// config.setInitialSize(initialSize);
		// config.setMaxActive(maxActive);
		// config.setMinIdle(minIdle);
		// config.setMaxIdle(maxIdle);
		//
		// DBCP2PoolManager.getInstance().build(config);
		//
		// Connection con = DBCP2PoolManager.getInstance().getConnection();
		// return con;
	}

	// 获取实体类所有属性的值
	public Object getFiledValues(Field field, T clazz) throws IllegalArgumentException, IllegalAccessException {
		Object obj = null;
		field.setAccessible(true);
		obj = field.get(clazz);
		return obj;
	};

	// 获取实体类所有属性
	private String[] getFiledName(T clazz) {
		Field[] fieds = getFields(clazz);
		String[] strings = new String[fieds.length];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = fieds[i].getName();
		}
		return strings;
	};

	// 驼峰命名转为下划线命名
	public String HumpToUnderline(String para) {
		StringBuilder sb = new StringBuilder(para);
		int temp = 0;// 定位
		for (int i = 0; i < para.length(); i++) {
			if (Character.isUpperCase(para.charAt(i))) {
				sb.insert(i + temp, "_");
				temp += 1;
			}
		}
		return sb.toString().toLowerCase();
	}

	// 实体类方法赋值
	public T setMethodValue(String methodName, Object newValue, T clazz) throws NoSuchMethodException,
			SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = clazz.getClass().getDeclaredField(methodName);
		field.setAccessible(true);
		field.set(clazz, newValue);
		return clazz;
	}

	public Field[] getFields(T clazz) {
		return clazz.getClass().getDeclaredFields();
	}
}
