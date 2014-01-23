package com.tite.system.wherefriends.core.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import com.tite.system.comfc.nosql.commontypes.DBCollectionInfo;
import com.tite.system.comfc.nosql.commontypes.QueryOptions;
import com.tite.system.comfc.nosql.interfaces.INoSQL;
import com.tite.system.wherefriends.core.db.commontype.User;
import com.tite.system.wherefriends.core.db.interfaces.IUser;

public class UserImpl implements IUser {
	
	private INoSQL noSQLClient;
	private DBCollectionInfo dbCollection = null;
	//该范围用于创建空间索引范围,需要外部设置
	private double minCoord = 0;
	private double maxCoord = 0;
	
	public UserImpl(DBCollectionInfo dbCollection, double minCoord, double maxCoord) {
		if(dbCollection != null){
			this.dbCollection = dbCollection;
		}else{
			dbCollection = new DBCollectionInfo();
			dbCollection.setDBName("WHEREFRIENDS");
			dbCollection.setCollectionName("WF_USER");
		}
		this.minCoord = minCoord;
		this.maxCoord = maxCoord;
	}
	
	public void setNoSQLClient(INoSQL noSQLClient) {
		this.noSQLClient = noSQLClient;
		//自动为用户数据集创建空间索引
		this.noSQLClient.createGeospatialIndex(this.dbCollection, User.COORD, this.minCoord, this.maxCoord);
	}

	@Override
	public UUID addUser(User user) {
		Map<String, Object> map = user.toMap();
		boolean blnInsert = this.noSQLClient.insert(this.dbCollection, map);
		UUID userId = null;
		if(blnInsert){
			userId = UUID.fromString(map.get(User.USERID).toString());
		}
		return userId;
	}

	@Override
	public boolean updateUser(User user) {
		Map<String, Object> map = user.toMap();
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(User.USERID, map.get(User.USERID).toString());
		boolean blnUpdate = this.noSQLClient.update(this.dbCollection, map, whereClause);
		return blnUpdate;
	}

	@Override
	public boolean deleteUser(UUID userId) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(User.USERID, userId.toString());
		boolean blnDelete = this.noSQLClient.delete(this.dbCollection, whereClause);
		return blnDelete;
	}

	@Override
	public List<User> getUsers(QueryOptions queryOptions) {
		List<Map<String, Object>> maps = this.noSQLClient.query(this.dbCollection, null, queryOptions);
		return this.convertDBResult(maps);
	}

	@Override
	public long countUsers() {
		return this.noSQLClient.count(this.dbCollection, null);
	}

	@Override
	public List<User> getUsers(String userName, QueryOptions queryOptions) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(User.USERNAME, Pattern.compile(userName, Pattern.CASE_INSENSITIVE));
		List<Map<String, Object>> maps = this.noSQLClient.query(this.dbCollection, whereClause, queryOptions);
		return this.convertDBResult(maps);
	}

	@Override
	public long countUsers(String userName) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(User.USERNAME, Pattern.compile(userName, Pattern.CASE_INSENSITIVE));
		return this.noSQLClient.count(this.dbCollection, whereClause);
	}
	
	/**
	 * 实现将集合类型的数据据查询对象转换为User对象集合
	 * */
	private List<User> convertDBResult(List<Map<String, Object>> dbMaps){
		List<User> users = null;
		if(dbMaps != null){
			users = new ArrayList<User>();
			Iterator<Map<String, Object>> iterDBMaps = dbMaps.iterator();
			User tmpUser = null;
			while(iterDBMaps.hasNext()){
				tmpUser = new User();
				tmpUser.fromMap(iterDBMaps.next());
				users.add(tmpUser);
			}
		}
		return users;
	}

}
