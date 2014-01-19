package com.tite.system.comfc.nosql.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.store.Directory;
import org.lumongo.storage.lucene.DistributedDirectory;
import org.lumongo.storage.lucene.MongoDirectory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.tite.system.comfc.nosql.commontypes.DBCollectionInfo;
import com.tite.system.comfc.nosql.commontypes.DBConnection;
import com.tite.system.comfc.nosql.commontypes.DBOptions;
import com.tite.system.comfc.nosql.commontypes.QueryOptions;
import com.tite.system.comfc.nosql.interfaces.INoSQL;
/**
 * NoSQL数据库操作接口定义的MongoDB数据库实现,由于MongoDB会自己维护一个连接池,因此,该MongoDB的NoSQL实现类
 * 在多数情况下,应该使用单例模式,一个JVM中只保留一个MongodbImpl实例
 * @author jiaoyk 2012-04-06
 * @version 7.0
 * */
public class MongodbImpl implements INoSQL {
	
	//允许子类调用该对象
	protected MongoClient mongoDB = null;
	private Logger log4j = Logger.getLogger(this.getClass());
	private boolean backgroundIndex = false;
	
	/**
	 * 升级了MongoDB驱动版本为2.11.1版本,根据该版本新的SDK,调整mongoDB对象的初始化方法
	 * @author jiaoyk 2013-06-19
	 * */
	public MongodbImpl(List<DBConnection> connections, DBOptions options) throws UnknownHostException {
		if(mongoDB == null){
			try {
				//判断辅助参数设置是否存在
				MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
				if(options == null){
					options = new DBOptions();
				}
				builder.autoConnectRetry(options.isAutoConnectRetry());
				builder.connectionsPerHost(options.getConnectionsPerHost());
				builder.connectTimeout(options.getConnectTimeout());
				builder.maxWaitTime(options.getMaxWaitTime());
				builder.socketTimeout(options.getSocketTimeout());
				builder.socketKeepAlive(options.isSocketKeepAlive());
				builder.threadsAllowedToBlockForConnectionMultiplier(options.getThreadsPerConnection());
				//区分单机部署与集群部署
				if(connections.size() == 1){
					//单机部署时使用
					mongoDB = new MongoClient(new ServerAddress(connections.get(0).getDBAddress(), connections.get(0).getDBPort()), builder.build());
				}else{
					//将数据库连接对象转换为MongoDB需要的数据库连接对象
					List<ServerAddress> addrs = new ArrayList<ServerAddress>();
					Iterator<DBConnection> iterConnections = connections.iterator();
					DBConnection tempConnection = null;
					while(iterConnections.hasNext()){
						tempConnection = iterConnections.next();
						if(tempConnection.getDBAddress() != null 
								&& !"".equalsIgnoreCase(tempConnection.getDBAddress())
								&& tempConnection.getDBPort() >= 0){
							addrs.add(new ServerAddress(tempConnection.getDBAddress(), tempConnection.getDBPort()));
						}
					}
					mongoDB = new MongoClient(addrs, builder.build());
				}
			} catch (MongoException e) {
				this.log4j.error(e.getMessage());
				throw e;
			} catch (java.net.UnknownHostException e) {
				this.log4j.error(e.getMessage());
				throw e;
			}
		}
	}
	
	public void setBackgroundIndex(boolean backgroundIndex) {
		this.backgroundIndex = backgroundIndex;
	}

	@Override
	public boolean insert(DBCollectionInfo collectioninfo,
			Map<String, Object> document) {
		
		return this.insert(collectioninfo, document, true);
	}

	@Override
	public boolean insert(DBCollectionInfo collectioninfo,
			Map<String, Object> document, boolean autocreatecol) {
		
		boolean blnInsert = false;
		
		//打开文档集合
		DBCollection dbcol = null;
		if(autocreatecol){
			//如果数据库或文档集合不存在,则直接创建(MongoDB默认实现)
			dbcol = mongoDB.getDB(collectioninfo.getDBName()).getCollection(collectioninfo.getCollectionName());
		}else{
			//判断数据库是否存在,存在则获取,不存在,则返回插入失败
			if(mongoDB.getDB(collectioninfo.getDBName()).collectionExists(collectioninfo.getCollectionName())){
				dbcol = mongoDB.getDB(collectioninfo.getDBName()).getCollection(collectioninfo.getCollectionName());
			}else{
				log4j.warn("数据插入失败,传入文档集合不存在,且不允许自动创建");
				return false;
			}
		}
		
		BasicDBObject bObject = new BasicDBObject();
		bObject.putAll(document);
		
		try{
			WriteResult result = dbcol.save(bObject);
			if(result.getError() == null){
				blnInsert = true;
			}else{
				log4j.warn("数据插入失败: " + result.getError());
			}
		}catch(RuntimeException e){
			log4j.error("数据插入失败: " + e.getMessage());
		}
		
		return blnInsert;
	}
	
	@Override
	public boolean update(DBCollectionInfo collectioninfo,
			Map<String, Object> document, Map<String, Object> whereClause) {
		
		boolean blnUpdate = false;
		//打开文档集合, 如果数据库或文档集合不存在,则直接创建(MongoDB默认实现)
		DBCollection dbcol = mongoDB.getDB(collectioninfo.getDBName()).getCollection(collectioninfo.getCollectionName());
		//构造更新对象
		BasicDBObject update = new BasicDBObject();
		update.putAll(document);
		//构造查询对象
		BasicDBObject query = new BasicDBObject();
		query.putAll(whereClause);
		
		try{
			WriteResult result = dbcol.update(query, update);
			if(result != null){
				blnUpdate = true;
			}else{
				log4j.warn("根据查询条件无法获取要更新的文档对象");
			}
		}catch(RuntimeException e){
			log4j.error("数据插入失败: " + e.getMessage());
		}
			
		return blnUpdate;
	}
	
	@Override
	public boolean accurateUpdate(DBCollectionInfo collectioninfo,
			Map<String, Object> document, Map<String, Object> whereClause) {
		//修改为通用更新实现, 虽然降低了一定的效率, 但适应数据库分布式部署模式
		//原方法使用的findAndModify方法, 该方法在数据库分片部署时无法正常使用(对使用条件要求较高)
		return this.update(collectioninfo, document, whereClause);
	}	
	
	@Override
	public boolean delete(DBCollectionInfo collectioninfo,
			Map<String, Object> whereClause) {
		boolean blnDelete = false;
		
		//打开文档集合, 如果数据库或文档集合不存在,则直接创建(MongoDB默认实现)
		DBCollection dbcol = mongoDB.getDB(collectioninfo.getDBName()).getCollection(collectioninfo.getCollectionName());
		//构造更新对象
		BasicDBObject query = new BasicDBObject();
		query.putAll(whereClause);
		
		try{
			WriteResult result = dbcol.remove(query);
			if(result.getError() == null){
				blnDelete = true;
			}else{
				log4j.warn("数据删除失败: " + result.getError());
			}
		}catch(RuntimeException e){
			log4j.error("数据插入失败: " + e.getMessage());
		}
		
		return blnDelete;
	}
	
	@Override
	public Map<String, Object> accurateQuery(DBCollectionInfo collectioninfo,
			Map<String, Object> whereClause) {
		
		return this.accurateQuery(collectioninfo, whereClause, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> accurateQuery(DBCollectionInfo collectioninfo,
			Map<String, Object> whereClause, Map<String, Object> resultOptions) {
		Map<String, Object> result = null;
		
		//打开文档集合, 如果数据库或文档集合不存在,则直接创建(MongoDB默认实现)
		DBCollection dbcol = mongoDB.getDB(collectioninfo.getDBName()).getCollection(collectioninfo.getCollectionName());
		
		DBObject dbResult = null;
		if(whereClause != null && whereClause.size() > 0){
			//构造更新对象
			BasicDBObject query = new BasicDBObject();
			query.putAll(whereClause);
			try{
				if(resultOptions != null && resultOptions.size() > 0){
					BasicDBObject fields = new BasicDBObject();
					fields.putAll(resultOptions);
					dbResult = dbcol.findOne(query, fields);
				}else{
					dbResult = dbcol.findOne(query);
				}
				if(dbResult != null){
					result = dbResult.toMap();
				}
			}catch(RuntimeException e){
				log4j.error("数据查询失败: " + e.getMessage());
			}
			
		}else{
			dbResult = dbcol.findOne();
		}
		if(dbResult != null){
			result = dbResult.toMap();
		}
		return result;
	}
	
	@Override
	public List<Map<String, Object>> query(DBCollectionInfo collectioninfo) {
		return this.query(collectioninfo, null);
	}
	
	@Override
	public List<Map<String, Object>> query(DBCollectionInfo collectioninfo,
			Map<String, Object> whereClause) {
		
		QueryOptions options = new QueryOptions();
		options.setPageSize(Integer.MAX_VALUE);
		List<Map<String, Object>> result = this.query(collectioninfo, whereClause, options);
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> query(DBCollectionInfo collectioninfo,
			Map<String, Object> whereClause, QueryOptions options) {
		
		List<Map<String, Object>> mapResult = new ArrayList<Map<String, Object>>();
		
		//打开文档集合, 如果数据库或文档集合不存在,则直接创建(MongoDB默认实现)
		DBCollection dbcol = mongoDB.getDB(collectioninfo.getDBName()).getCollection(collectioninfo.getCollectionName());
		
		BasicDBObject returnFields = new BasicDBObject();
		if(options.getReturnFields() != null && 
				options.getReturnFields().length > 0){
			String[] fields = options.getReturnFields();
			for(int i=0; i<fields.length; i++){
				if(fields[i] != null && !"".equalsIgnoreCase(fields[i])){
					returnFields.put(fields[i], 1);
				}
			}
		}
		
		DBCursor dbCursor = null;
		if(whereClause != null && whereClause.size() > 0){
			//构造更新对象
			BasicDBObject query = new BasicDBObject();
			query.putAll(whereClause);
			
			try{
				dbCursor = dbcol.find(query, returnFields);
			}catch(RuntimeException e){
				log4j.error("数据查询失败: " + e.getMessage());
			}
			
		}else{
			if(returnFields.size() > 0){
				dbCursor = dbcol.find(null, returnFields);
			}else{
				dbCursor = dbcol.find();
			}
		}
		
		if(dbCursor.count() < 1){
			return mapResult;
		} 
		
		//判断是否需要排序
		if(options.getSortField() != null && !"".equalsIgnoreCase(options.getSortField().trim())){
			BasicDBObject sortObject = new BasicDBObject();
			if(options.isASC()){
				sortObject.put(options.getSortField().trim(), 1);
			}else{
				sortObject.put(options.getSortField().trim(), -1);
			}
			dbCursor.sort(sortObject);
		}
		
		//判断是否需要分页
		if(options.getPageSize() > 0 && options.getPageIndex() > 0){
			//计算当前从第几条数据开始(第二页则查询结果跳过第一页记录,第一页则不跳过,因此页数减一)
			int startNumber = options.getPageSize() * (options.getPageIndex()-1);
			dbCursor.skip(startNumber);
			dbCursor.limit(options.getPageSize());
			
		}
		//将查询结果按照设置输出返回
		Map<String, Object> tempRecord = null;
		while(dbCursor.hasNext()){
			tempRecord = dbCursor.next().toMap();
			mapResult.add(tempRecord);
		}
		
		return mapResult;
	}

	@Override
	public long count(DBCollectionInfo collectioninfo,
			Map<String, Object> whereClause) {
		
		long lgCount = 0;

		//打开文档集合, 如果数据库或文档集合不存在,则直接创建(MongoDB默认实现)
		DBCollection dbcol = mongoDB.getDB(collectioninfo.getDBName()).getCollection(collectioninfo.getCollectionName());
		if(whereClause != null){
			BasicDBObject query = new BasicDBObject();
			query.putAll(whereClause);
			lgCount = dbcol.count(query);
		}else{
			lgCount = dbcol.count();
		}
		
		return lgCount;
	}
	
	@Override
	public void deleteCollection(DBCollectionInfo collectioninfo) {
		
		DB db = mongoDB.getDB(collectioninfo.getDBName());
		//判断文档集合是否存在
		boolean blnExist = db.collectionExists(collectioninfo.getCollectionName());
		if(blnExist){
			//首先删除该集合的索引
			DBCollection col = db.getCollection(collectioninfo.getCollectionName());
			col.dropIndexes();
			col.drop();
		}
	}

	@Override
	public void createIndex(DBCollectionInfo collectioninfo, Map<String, Integer> fields) {
		this.createIndex(collectioninfo, fields, null);
	}

	@Override
	public void closeDB() {
		if(mongoDB != null){
			mongoDB.close();
			mongoDB = null;	
		}
	}

	@Override
	public boolean insertFile(DBCollectionInfo collectioninfo, InputStream file, 
			Map<String, Object> document) {
		GridFS gridFS = new GridFS(mongoDB.getDB(collectioninfo.getDBName()), collectioninfo.getCollectionName());
		GridFSInputFile gfs = gridFS.createFile(file);
		for(String key:document.keySet()){
			gfs.put(key, document.get(key));
		}
		gfs.save();
		return true;
	}
	@Override
	public boolean deleteFile(DBCollectionInfo collectioninfo,
			Map<String, Object> whereClause) {
		GridFS gridFS = new GridFS(mongoDB.getDB(collectioninfo.getDBName()), collectioninfo.getCollectionName());
		DBObject query = new BasicDBObject(whereClause);
		gridFS.remove(query);
		return true;
	}

	@Override
	public Map<String, Object> accurateQueryFile(
			DBCollectionInfo collectioninfo, Map<String, Object> whereClause) {
		GridFS gridFS = new GridFS(mongoDB.getDB(collectioninfo.getDBName()), collectioninfo.getCollectionName());
		DBObject query = new BasicDBObject(whereClause);
		GridFSDBFile doc = gridFS.findOne(query);
		Map<String,Object> document = null;
		if(doc != null){
			document = new HashMap<String, Object>();
			for(String key:doc.keySet()){
				document.put(key, doc.get(key));
			}
			document.put("$file", doc.getInputStream());
		}
		return document;
	}

	@Override
	public List<Map<String, Object>> queryFiles(DBCollectionInfo collectioninfo) {
		return this.queryFiles(collectioninfo, null);
	}

	@Override
	public List<Map<String, Object>> queryFiles(DBCollectionInfo collectioninfo,
			Map<String, Object> whereClause) {
		List<Map<String, Object>> result = null;
		GridFS gridFS = new GridFS(mongoDB.getDB(collectioninfo.getDBName()), collectioninfo.getCollectionName());
		List<GridFSDBFile> files = null;
		if(whereClause != null && whereClause.size() > 0){
			DBObject query = new BasicDBObject(whereClause);
			files = gridFS.find(query);
		}else{
			DBObject query = new BasicDBObject();
			files = gridFS.find(query);
		}
		if(files == null || files.isEmpty()){
			return result;
		} 
		result = new ArrayList<Map<String, Object>>();

		//将查询结果按照设置输出返回
		Map<String, Object> tempRecord = new HashMap<String, Object>();
		for(int i=0;i<files.size();i++){
			for(String key:files.get(i).keySet()){
				tempRecord.put(key, files.get(i).get(key));
			}
			tempRecord.put("$file", files.get(i).getInputStream());
			result.add(tempRecord);
		}
		
		return result;
	}

	@Override
	public long countFile(DBCollectionInfo collectioninfo,
			Map<String, Object> whereClause) {
		GridFS gridFS = new GridFS(mongoDB.getDB(collectioninfo.getDBName()), collectioninfo.getCollectionName());
		DBCursor dbCursor = null;
		if(whereClause != null && whereClause.size() > 0){
			DBObject query = new BasicDBObject(whereClause);
			dbCursor = gridFS.getFileList(query);
		}else{
			dbCursor = gridFS.getFileList();
		}
		return dbCursor.count();
	}

	@Override
	public void deleteDatabase(String dbName) {
		mongoDB.dropDatabase(dbName);
	}

	@Override
	public void createGeospatialIndex(DBCollectionInfo collectioninfo,
			String fieldName, double minCoord, double maxCoord) {
		DB db = mongoDB.getDB(collectioninfo.getDBName());
		if(db.getCollection(collectioninfo.getCollectionName()) != null){
			DBCollection dbcol = db.getCollection(collectioninfo.getCollectionName());
			//构建空间索引创建条件
			BasicDBObject dbObject1 = new BasicDBObject();
			dbObject1.put(fieldName, "2d");
			BasicDBObject dbObject2 = new BasicDBObject();
			dbObject2.put("min", minCoord);
			dbObject2.put("max", maxCoord);
			dbcol.ensureIndex(dbObject1, dbObject2);
		}else{
			log4j.warn("创建索引失败,待创建索引的文档集合不存在");
		}
	}

	@Override
	public boolean updateFieldValues(DBCollectionInfo collectioninfo,
			Map<String, Object> whereClause, Map<String, Object> updateObject) {
		boolean blnUpdate = false;
		//打开文档集合, 如果数据库或文档集合不存在,则直接创建(MongoDB默认实现)
		DBCollection dbcol = mongoDB.getDB(collectioninfo.getDBName()).getCollection(collectioninfo.getCollectionName());
		//构造更新对象
		Map<String, Object> updateData = new HashMap<String, Object>();
		updateData.put("$set", updateObject);
		BasicDBObject basicData = new BasicDBObject();
		basicData.putAll(updateData);
		//构造查询对象
		BasicDBObject query = new BasicDBObject();
		query.putAll(whereClause);
		try{
			//更新的数据中包含了操作符号
			WriteResult result = dbcol.update(query, basicData, false, true);
			if(result != null){
				blnUpdate = true;
			}else{
				log4j.warn("根据查询条件无法获取要更新的文档对象");
			}
		}catch(RuntimeException e){
			log4j.error("数据插入失败: " + e.getMessage());
		}
			
		return blnUpdate;
	}

	@Override
	public boolean isIndex(DBCollectionInfo collectioninfo, String fieldName) {
		boolean blnExist = false;
		DBCollection dbcol = mongoDB.getDB(collectioninfo.getDBName())
									.getCollection(collectioninfo.getCollectionName());
		List<DBObject> indexs = dbcol.getIndexInfo();
		if(indexs != null
				&& indexs.size() > 0){
			Iterator<DBObject> iterIndexs = indexs.iterator();
			while(iterIndexs.hasNext()){
				@SuppressWarnings("unchecked")
				//从索引对象中获取到设置的索引集合
				Map<String, Object> map = (Map<String, Object>)iterIndexs.next().get("key");
				if(map.containsKey(fieldName)){
					blnExist = true;
					break;
				}
			}
		}
		return blnExist;
	}

	@Override
	public boolean dropIndex(DBCollectionInfo collectioninfo, String fieldName) {
		boolean blnDrop = false;
		DBCollection dbcol = mongoDB.getDB(collectioninfo.getDBName())
				.getCollection(collectioninfo.getCollectionName());
		//先找到该索引对象
		DBObject index = null;
		List<DBObject> indexs = dbcol.getIndexInfo();
		if(indexs != null
				&& indexs.size() > 0){
			Iterator<DBObject> iterIndexs = indexs.iterator();
			while(iterIndexs.hasNext()){
				@SuppressWarnings("unchecked")
				//从索引对象中获取到设置的索引集合
				Map<String, Object> map = (Map<String, Object>)iterIndexs.next().get("key");
				if(map.containsKey(fieldName)){
					index = new BasicDBObject();
					index.put(fieldName, map.get(fieldName));
					break;
				}else{
					index = null;
				}
			}
		}
		if(index != null){
			dbcol.dropIndex(index);
			blnDrop = true;
		}
		return blnDrop;
	}

	@Override
	public void createIndex(DBCollectionInfo collectioninfo,
			Map<String, Integer> fields, Map<String, Object> whereClause) {
		DB db = mongoDB.getDB(collectioninfo.getDBName());
		if(db.getCollection(collectioninfo.getCollectionName()) != null){
			DBCollection dbcol = db.getCollection(collectioninfo.getCollectionName());
			BasicDBObject dbObject = new BasicDBObject();
			dbObject.putAll(fields);
			BasicDBObject options = new BasicDBObject();
			if(whereClause != null){
				options.putAll(whereClause);
			}
			options.put("background", backgroundIndex);
			dbcol.ensureIndex(dbObject, options);
		}else{
			log4j.warn("创建索引失败,待创建索引的文档集合不存在");
		}
	}

	@Override
	public Directory getLuceneIndexDir(DBCollectionInfo collectioninfo) throws IOException {
		return new DistributedDirectory(new MongoDirectory(mongoDB, 
				collectioninfo.getDBName(), collectioninfo.getCollectionName()));
	}

}
