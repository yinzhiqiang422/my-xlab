package com.mock;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.jfinal.kit.PropKit;
import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * MDBManager.java
 * 功能：使用C3P0连接池获取数据库连接对象
 * @author boonya
 * @version 1.0 2013-03-07
 */
public class MDBManager {
	
	private static final MDBManager instance=new MDBManager();
	private static ComboPooledDataSource cpds=new ComboPooledDataSource(true); 
	
	/**
	 * 此处可以不配置，采用默认也行
	 */
	static{
		PropKit.use("c3p0.properties");
		cpds.setDataSourceName("mydatasource");
		cpds.setJdbcUrl(PropKit.get("c3p0.jdbcUrl").toString());
		try {
			cpds.setDriverClass(PropKit.get("c3p0.driverClass").toString());
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		cpds.setUser(PropKit.get("c3p0.user").toString());
		cpds.setPassword(PropKit.get("c3p0.password").toString());
		cpds.setMaxPoolSize(Integer.valueOf(PropKit.get("c3p0.maxPoolSize")));
		cpds.setMinPoolSize(Integer.valueOf(PropKit.get("c3p0.minPoolSize").toString()));
		cpds.setAcquireIncrement(Integer.valueOf(PropKit.get("c3p0.acquireIncrement").toString()));
		cpds.setInitialPoolSize(Integer.valueOf(PropKit.get("c3p0.initialPoolSize").toString()));
		cpds.setMaxIdleTime(Integer.valueOf(PropKit.get("c3p0.maxIdleTime").toString()));
	}
	
	private MDBManager(){}
	
	public static MDBManager getInstance(){
		return instance;
	}
	
	public static Connection  getConnection(){
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static void closeConnection(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
