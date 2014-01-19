package com.tite.system.comfc.nosql.commontypes;
/**
 * 数据库可选项配置
 * @author jiaoyk 2012-04-17
 * @version 6.2.0
 * */
public class DBOptions {
	
	//数据库连接错误时是否自动重连数据库
	private boolean AutoConnectRetry = true;
	//数据库连接数配置
	private int ConnectionsPerHost = 10;
	//连接超时时间,单位毫秒
	private int ConnectTimeout = 10000;
	//线程等待连接时间,单位毫秒
	private int MaxWaitTime = 120000;
	//是否始终保存Socket存活
	private boolean SocketKeepAlive = false;
	//套接字上面的发送和接收操作的超时时间,单位毫秒
	private int SocketTimeout = 0;
	//数据库每一个连接可以同时启用的线程数
	private int ThreadsPerConnection = 50;
	
	/**
	 * 获取是否数据库连接错误时是否自动重连数据库
	 * */
	public boolean isAutoConnectRetry() {
		return AutoConnectRetry;
	}
	/**
	 * 设置是否数据库连接错误时是否自动重连数据库
	 * */
	public void setAutoConnectRetry(boolean autoConnectRetry) {
		AutoConnectRetry = autoConnectRetry;
	}
	/**
	 * 获取数据库连接数配置
	 * */
	public int getConnectionsPerHost() {
		return ConnectionsPerHost;
	}
	/**
	 * 设置数据库连接数配置
	 * @param connectionsPerHost 数据库连接数
	 * */
	public void setConnectionsPerHost(int connectionsPerHost) {
		ConnectionsPerHost = connectionsPerHost;
	}
	/**
	 * 获取连接超时时间,单位毫秒
	 * */
	public int getConnectTimeout() {
		return ConnectTimeout;
	}
	/**
	 * 设置连接超时时间,单位毫秒
	 * */
	public void setConnectTimeout(int connectTimeout) {
		ConnectTimeout = connectTimeout;
	}
	/**
	 * 获取线程等待连接时间,单位毫秒
	 * */
	public int getMaxWaitTime() {
		return MaxWaitTime;
	}
	/**
	 * 设置线程等待连接时间,单位毫秒
	 * */
	public void setMaxWaitTime(int maxWaitTime) {
		MaxWaitTime = maxWaitTime;
	}
	/**
	 * 获取是否始终保存Socket存活
	 * */
	public boolean isSocketKeepAlive() {
		return SocketKeepAlive;
	}
	/**
	 * 设置是否始终保存Socket存活
	 * */
	public void setSocketKeepAlive(boolean socketKeepAlive) {
		SocketKeepAlive = socketKeepAlive;
	}
	/**
	 * 获取套接字上面的发送和接收操作的超时时间
	 * */
	public int getSocketTimeout() {
		return SocketTimeout;
	}
	/**
	 * 设置套接字上面的发送和接收操作的超时时间
	 * */
	public void setSocketTimeout(int socketTimeout) {
		SocketTimeout = socketTimeout;
	}
	/**
	 * 获取数据库每一个连接可以同时启用的线程数
	 * @return threadsPerConnection
	 */
	public int getThreadsPerConnection() {
		return ThreadsPerConnection;
	}
	/**
	 * 设置数据库每一个连接可以同时启用的线程数
	 * @param threadsPerConnection 
	 */
	public void setThreadsPerConnection(int threadsPerConnection) {
		ThreadsPerConnection = threadsPerConnection;
	}
}
