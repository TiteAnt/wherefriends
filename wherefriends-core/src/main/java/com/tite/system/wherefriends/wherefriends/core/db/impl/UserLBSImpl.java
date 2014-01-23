package com.tite.system.wherefriends.wherefriends.core.db.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tite.system.comfc.nosql.commontypes.DBCollectionInfo;
import com.tite.system.comfc.nosql.commontypes.QueryOptions;
import com.tite.system.comfc.nosql.interfaces.INoSQL;
import com.tite.system.wherefriends.wherefriends.core.db.commontype.UserLBS;
import com.tite.system.wherefriends.wherefriends.core.db.interfaces.IUserLBS;

public class UserLBSImpl implements IUserLBS {
	private INoSQL noSQLClient;
	private DBCollectionInfo dbCollection = null;
	//该范围用于创建空间索引范围,需要外部设置
	private double minCoord = 0;
	private double maxCoord = 0;
	
	public UserLBSImpl(DBCollectionInfo dbCollection, double minCoord, double maxCoord) {
		if(dbCollection != null){
			this.dbCollection = dbCollection;
		}else{
			dbCollection = new DBCollectionInfo();
			dbCollection.setDBName("WHEREFRIENDS");
			dbCollection.setCollectionName("WF_USER_LBS");
		}
		this.minCoord = minCoord;
		this.maxCoord = maxCoord;
	}
	
	public void setNoSQLClient(INoSQL noSQLClient) {
		this.noSQLClient = noSQLClient;
		//自动为用户数据集创建空间索引
		this.noSQLClient.createGeospatialIndex(this.dbCollection, UserLBS.COORD, this.minCoord, this.maxCoord);
	}
	
	@Override
	public UUID addUserLBS(UserLBS userlbs) {
		Map<String, Object> map = userlbs.toMap();
		boolean blnInsert = this.noSQLClient.insert(this.dbCollection, map);
		UUID lbsId = null;
		if(blnInsert){
			lbsId = UUID.fromString(map.get(UserLBS.ID).toString());
		}
		return lbsId;
	}

	@Override
	public UserLBS getRecentlyUserLBS(UUID userId) {
		QueryOptions options = new QueryOptions();
		options.setPageIndex(1);
		options.setPageSize(1);
		options.setSortField(UserLBS.DATE);
		options.setASC(false);
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put(UserLBS.USERID, userId.toString());
		List<Map<String, Object>> dbMaps = this.noSQLClient.query(this.dbCollection, whereClause, options);
		UserLBS userlbs = null;
		if(dbMaps != null
				&& dbMaps.size() > 0){
			userlbs = new UserLBS();
			userlbs.fromMap(dbMaps.get(0));
		}
		return userlbs;
	}

	@Override
	public List<UserLBS> getUserLBSs(UUID userId, Date startDate, Date endDate) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		if(startDate != null){
			map.put("$gt", startDate);
		}
		if(endDate != null){	
			map.put("$lt", endDate);
		}
		whereClause.put(UserLBS.DATE, map);
		QueryOptions options = new QueryOptions();
		options.setPageIndex(1);
		//最大只返回5000条记录
		options.setPageSize(5000);
		options.setSortField(UserLBS.DATE);
		options.setASC(false);
		List<Map<String, Object>> dbMaps = this.noSQLClient.query(this.dbCollection, whereClause, options);
		return this.convertDBResult(dbMaps);
	}
	
	/**
	 * 实现用户位置对象从数据库模型到定义对象的转换
	 * */
	private List<UserLBS> convertDBResult(List<Map<String, Object>> dbMaps){
		List<UserLBS> userlbses = null;
		if(dbMaps != null){
			userlbses = new ArrayList<UserLBS>();
			Iterator<Map<String, Object>> iterDBMaps = dbMaps.iterator();
			UserLBS tmpUserLBS = null;
			while(iterDBMaps.hasNext()){
				tmpUserLBS = new UserLBS();
				tmpUserLBS.fromMap(iterDBMaps.next());
				userlbses.add(tmpUserLBS);
			}
		}
		return userlbses;
	}
}
