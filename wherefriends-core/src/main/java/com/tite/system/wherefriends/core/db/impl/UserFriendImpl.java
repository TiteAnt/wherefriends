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
import com.tite.system.wherefriends.core.db.commontype.UserFriend;
import com.tite.system.wherefriends.core.db.interfaces.IUserFriend;

public class UserFriendImpl implements IUserFriend {
	private INoSQL noSQLClient;
	private DBCollectionInfo dbCollection = null;
	//该范围用于创建空间索引范围,需要外部设置
	private double minCoord = 0;
	private double maxCoord = 0;
	
	public UserFriendImpl(DBCollectionInfo dbCollection, double minCoord, double maxCoord) {
		if(dbCollection != null){
			this.dbCollection = dbCollection;
		}else{
			dbCollection = new DBCollectionInfo();
			dbCollection.setDBName("WHEREFRIENDS");
			dbCollection.setCollectionName("WF_USERFRIEND");
		}
		this.minCoord = minCoord;
		this.maxCoord = maxCoord;
	}
	
	public void setNoSQLClient(INoSQL noSQLClient) {
		this.noSQLClient = noSQLClient;
		//自动为用户数据集创建空间索引
		this.noSQLClient.createGeospatialIndex(this.dbCollection, UserFriend.COORD, this.minCoord, this.maxCoord);
	}
	
	@Override
	public UUID addUserFriend(UserFriend friend) {
		Map<String, Object> map = friend.toMap();
		boolean blnInsert = this.noSQLClient.insert(this.dbCollection, map);
		UUID ufId = null;
		if(blnInsert){
			ufId = UUID.fromString(map.get(UserFriend.ID).toString());
		}
		return ufId;
	}

	@Override
	public boolean updateUserFriend(UserFriend friend) {
		Map<String, Object> map = friend.toMap();
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(UserFriend.ID, map.get(UserFriend.ID).toString());
		boolean blnUpdate = this.noSQLClient.update(this.dbCollection, map, whereClause);
		return blnUpdate;
	}

	@Override
	public boolean deleteUserFriend(UUID friendId) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(UserFriend.ID, friendId.toString());
		boolean blnDelete = this.noSQLClient.delete(this.dbCollection, whereClause);
		return blnDelete;
	}

	@Override
	public List<UserFriend> getUserFriends(UUID userId,
			QueryOptions queryOptions) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(UserFriend.USERID, userId.toString());
		List<Map<String, Object>> dbMaps = this.noSQLClient.query(this.dbCollection, whereClause, queryOptions);
		return this.convertDBResult(dbMaps);
	}

	@Override
	public long countUserFriends(UUID userId) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(UserFriend.USERID, userId.toString());
		return this.noSQLClient.count(this.dbCollection, whereClause);
	}

	@Override
	public List<UserFriend> getUserFriends(UUID userId, String alias,
			QueryOptions queryOptions) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(UserFriend.USERID, userId.toString());
		whereClause.put(UserFriend.FRIENDALIAS, Pattern.compile(alias, Pattern.CASE_INSENSITIVE));
		List<Map<String, Object>> dbMaps = this.noSQLClient.query(this.dbCollection, whereClause);
		return this.convertDBResult(dbMaps);
	}

	@Override
	public long countUserFriends(UUID userId, String alias) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(UserFriend.USERID, userId.toString());
		whereClause.put(UserFriend.FRIENDALIAS, Pattern.compile(alias, Pattern.CASE_INSENSITIVE));
		return this.noSQLClient.count(this.dbCollection, whereClause);
	}

	@Override
	public boolean deleteUserFriend(UUID userId, UUID friendId) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(UserFriend.USERID, userId.toString());
		whereClause.put(UserFriend.FRIENDID, friendId.toString());
		return this.noSQLClient.delete(this.dbCollection, whereClause);
	}
	
	/**
	 * 实现好友关系对象从数据库模型到定义对象的转换
	 * */
	private List<UserFriend> convertDBResult(List<Map<String, Object>> dbMaps){
		List<UserFriend> uFriends = null;
		if(dbMaps != null){
			uFriends = new ArrayList<UserFriend>();
			Iterator<Map<String, Object>> iterDBMaps = dbMaps.iterator();
			UserFriend tmpUFriend = null;
			while(iterDBMaps.hasNext()){
				tmpUFriend = new UserFriend();
				tmpUFriend.fromMap(iterDBMaps.next());
				uFriends.add(tmpUFriend);
			}
		}
		return uFriends;
	}
}
