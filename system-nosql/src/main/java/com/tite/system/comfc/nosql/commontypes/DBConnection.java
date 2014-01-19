package com.tite.system.comfc.nosql.commontypes;
/**
 * NoSQL数据库连接对象
 * @author jiaoyk 2012-04-17
 * @version 6.2.0
 * */
public class DBConnection {
	//数据库连接地址
	private String DBAddress = "";
	private int DBPort = 0;
	
	public DBConnection() {
		super();
	}
	
	/**
	 * @param dBAddress 数据库连接地址
	 * @param dBPort 数据库连接端口
	 * */
	public DBConnection(String dBAddress, int dBPort) {
		super();
		DBAddress = dBAddress;
		DBPort = dBPort;
	}
	/**
	 * 获取数据库连接地址
	 * @return 数据库连接地址
	 * */
	public String getDBAddress() {
		return DBAddress;
	}
	/**
	 * 设置数据库连接地址
	 * @param dBAddress 数据库连接地址
	 * */
	public void setDBAddress(String dBAddress) {
		DBAddress = dBAddress;
	}
	/**
	 * 获取数据库连接端口
	 * @return 数据库连接端口
	 * */
	public int getDBPort() {
		return DBPort;
	}
	/**
	 * 设置数据库连接端口
	 * @param dBPort 数据库连接端口
	 * */
	public void setDBPort(int dBPort) {
		DBPort = dBPort;
	}
}
