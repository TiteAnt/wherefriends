package com.tite.system.wherefriends.wherefriends.core.db.impl;

import java.util.List;
import java.util.UUID;

import com.tite.system.comfc.nosql.commontypes.DBCollectionInfo;
import com.tite.system.comfc.nosql.commontypes.QueryOptions;
import com.tite.system.comfc.nosql.interfaces.INoSQL;
import com.tite.system.wherefriends.wherefriends.core.db.commontype.User;
import com.tite.system.wherefriends.wherefriends.core.db.interfaces.IUser;

public class UserImpl implements IUser {
	
	private INoSQL noSQLClient;
	private DBCollectionInfo dbCollection = null;
	//该范围用于创建空间索引范围,需要外部设置
	private double minCoord = 0;
	private double maxCoord = 0;
	
	public UserImpl(DBCollectionInfo dbCollection) {
		if(dbCollection != null){
			this.dbCollection = dbCollection;
		}else{
			dbCollection = new DBCollectionInfo();
			dbCollection.setDBName("DFC_GEOCODING");
			dbCollection.setCollectionName("DFC_GEOCODING_ADDRESS");
		}
	}
	
	public void setNoSQLClient(INoSQL noSQLClient) {
		this.noSQLClient = noSQLClient;
	}

	public void setMinCoord(double minCoord) {
		this.minCoord = minCoord;
	}

	public void setMaxCoord(double maxCoord) {
		this.maxCoord = maxCoord;
	}

	@Override
	public UUID addUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(UUID userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<User> getUsers(QueryOptions queryOptions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countUsers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<User> getUsers(String userName, QueryOptions queryOptions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countUsers(String userName) {
		// TODO Auto-generated method stub
		return 0;
	}

}
