package com.tite.system.comfc.nosql.commontypes;
/**
 * 定义NoSQL数据库中数据集合连接的信息
 * @author jiaoyk 2012-04-05
 * @version 6.2.0
 * */
public class DBCollectionInfo {
	private String DBName = "";
	private String CollectionName = "";
	public DBCollectionInfo() {
		super();
	}
	/**
	 * @param dBName 数据库名称
	 * @param collectionName 数据集合名称
	 * */
	public DBCollectionInfo(String dBName, String collectionName) {
		super();
		DBName = dBName;
		CollectionName = collectionName;
	}
	/**
	 * 获取数据库名称
	 * @return 数据库名称
	 * */
	public String getDBName() {
		return DBName;
	}
	/**
	 * 设置数据库名称
	 * @param dBName 数据库名称
	 * */
	public void setDBName(String dBName) {
		DBName = dBName;
	}
	/**
	 * 获取数据集合名称
	 * @return 数据集合名称
	 * */
	public String getCollectionName() {
		return CollectionName;
	}
	/**
	 * 设置数据集合名称
	 * @param collectionName 数据集合名称
	 * */
	public void setCollectionName(String collectionName) {
		CollectionName = collectionName;
	}
}
