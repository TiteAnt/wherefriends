package com.tite.system.comfc.nosql.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.lucene.store.Directory;

import com.tite.system.comfc.nosql.commontypes.DBCollectionInfo;
import com.tite.system.comfc.nosql.commontypes.QueryOptions;

/**
 * 统一定义NoSQL数据库的操作接口,对外屏蔽不同NoSQL数据库的实现
 * @author jiaoyk 2012-04-05
 * @version 6.2.0
 * */
public interface INoSQL {
	
	/**
	 * 定义文档入库方法,若传入的数据库或文档集合不存在,则自动创建
	 * @param collectioninfo 文档集合连接信息
	 * @param document 被添加的文档
	 * @return true表示添加成功,false表示添加失败
	 * */
	public boolean insert(DBCollectionInfo collectioninfo, Map<String, Object> document);
	/**
	 * 定义文件入库方法,若传入的数据库或文档集合不存在,则自动创建,若传入的文件元数据包含_id字段且库中已存在_id相同的数据则直接覆盖
	 * @param collectioninfo 文件集合连接信息
	 * @param file 被添加的文件
	 * @param document 文件的元数据
	 * @return true表示添加成功,false表示添加失败
	 * */
	public boolean insertFile(DBCollectionInfo collectioninfo, InputStream file, Map<String, Object> document);
	/**
	 * 定义文档入库方法,支持自定义文档集合是否自动创建
	 * @param collectioninfo 文档集合连接信息
	 * @param document 被添加的文档
	 * @param autocreatecol 是否自动创建文档集合
	 * @return true表示添加成功,false表示添加失败
	 * */
	public boolean insert(DBCollectionInfo collectioninfo, Map<String, Object> document, boolean autocreatecol);
	/**
	 * 定义文档更新方法,将符合条件的文档对象替换为指定的新文档对象
	 * @param collectioninfo 文档集合连接信息
	 * @param document 用于替换的文档
	 * @param whereClause 查询条件,将符合条件的文档进行替换
	 * @return true表示更新成功,false表示更新失败
	 * */
	public boolean update(DBCollectionInfo collectioninfo, Map<String, Object> document, Map<String,Object> whereClause);
	/**
	 * 定义精确更新方法,根据查询条件查询到唯一一条记录并进行更新,该方法性能较高
	 * 当查询条件查出多条记录时,只更新第一条记录
	 * @param collectioninfo 文档集合连接信息
	 * @param document 用于替换的文档
	 * @param whereClause 查询条件,将符合条件的文档进行替换
	 * @return true表示更新成功,false表示更新失败
	 * */
	public boolean accurateUpdate(DBCollectionInfo collectioninfo, Map<String, Object> document, Map<String,Object> whereClause);
	/**
	 * 定义文档删除方法
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据删除
	 * @return true表示删除成功,false表示删除失败
	 * */
	public boolean delete(DBCollectionInfo collectioninfo, Map<String,Object> whereClause);
	/**
	 * 定义文件删除方法
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据删除
	 * @return true表示删除成功,false表示删除失败
	 * */
	public boolean deleteFile(DBCollectionInfo collectioninfo, Map<String,Object> whereClause);
	/**
	 * 定义数据精确查询方法,查询只返回第一条结果,该方法在使用唯一标识查询时,具有高性能优势
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据返回
	 * @return Map<String, Object> 文档对象
	 * */
	public Map<String, Object> accurateQuery(DBCollectionInfo collectioninfo, Map<String,Object> whereClause);
	/**
	 * 定义数据精确查询方法,查询只返回第一条结果,该方法在使用唯一标识查询时,具有高性能优势
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据返回
	 * @return Map<String, Object> 文件对象，包含文件的属性及文件流，获取文件流对象方法：Map.get("file")
	 * */
	public Map<String, Object> accurateQueryFile(DBCollectionInfo collectioninfo, Map<String,Object> whereClause);
	
	/**
	 * 定义数据精确查询方法,查询只返回第一条结果,该方法在使用唯一标识查询时,具有高性能优势
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据返回
	 * @param resultOptions 结果的过滤条件,对符合条件的结果进行定制
	 * @return Map<String, Object> 文档对象
	 * */
	public Map<String, Object> accurateQuery(DBCollectionInfo collectioninfo, Map<String,Object> whereClause, Map<String,Object> resultOptions);
	
	/**
	 * 定义数据查询方法,获取集合所有数据
	 * @param collectioninfo 文档集合连接信息
	 * @return List<Map<String, Object>> 文档集合
	 * */
	public List<Map<String, Object>> query(DBCollectionInfo collectioninfo);
	/**
	 * 定义数据查询方法,获取集合所有数据
	 * @param collectioninfo 文档集合连接信息
	 * @return List<Map<String, Object>> 文档集合,包含文件的属性及文件流，获取文件流对象方法：Map.get("file")
	 * */
	public List<Map<String, Object>> queryFiles(DBCollectionInfo collectioninfo);
	/**
	 * 定义数据查询方法,使用默认查询结果选项对象
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据返回
	 * @return List<Map<String, Object>> 文档集合
	 * */
	public List<Map<String, Object>> query(DBCollectionInfo collectioninfo, Map<String,Object> whereClause);
	/**
	 * 定义数据查询方法,使用默认查询结果选项对象
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据返回
	 * @return List<Map<String, Object>> 文档集合,包含文件的属性及文件流，获取文件流对象方法：Map.get("file")
	 * */
	public List<Map<String, Object>> queryFiles(DBCollectionInfo collectioninfo, Map<String,Object> whereClause);
	/**
	 * 定义支持分页等查询方式的查询方法
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据返回
	 * @param options 查询结果选项
	 * @return List<Map<String, Object>> 文档集合
	 * */
	public List<Map<String, Object>> query(DBCollectionInfo collectioninfo, Map<String,Object> whereClause, QueryOptions options);
	
	/**
	 * 定义获取查询结果总数方法
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据返回
	 * @return int 返回符合条件的结果总数
	 * */
	public long count(DBCollectionInfo collectioninfo, Map<String,Object> whereClause);
	/**
	 * 定义获取查询结果总数方法
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据返回
	 * @return int 返回符合条件的结果总数
	 * */
	public long countFile(DBCollectionInfo collectioninfo, Map<String,Object> whereClause);
	/**
	 * 定义数据库文档集合删除方法
	 * @param collectioninfo 文档集合连接信息
	 * */
	public void deleteCollection(DBCollectionInfo collectioninfo);
	/**
	 * 定义数据库删除方法
	 * @param dbName 数据库名称
	 * */
	public void deleteDatabase(String dbName);
	/**
	 * 支持为指定文档集合建立索引
	 * @param collectioninfo 文档集合连接信息
	 * @param fields 被建立索引的字段集合, Key为字段名称, Value为1时表示升序, Value为-1时表示降序
	 * */
	public void createIndex(DBCollectionInfo collectioninfo, Map<String, Integer> fields);
	
	/**
	 * 支持为指定文档集合建立索引,并指定索引的创建条件,如唯一索引等
	 * @param collectioninfo 文档集合连接信息
	 * @param fields 被建立索引的字段集合, Key为字段名称, Value为1时表示升序, Value为-1时表示降序
	 * @param whereClause 索引的条件,如是否唯一等
	 * */
	public void createIndex(DBCollectionInfo collectioninfo, Map<String, Integer> fields, Map<String, Object> whereClause);
	
	/**
	 * 支持关闭数据库连接
	 * */
	public void closeDB();
	
	/**
	 * 为指定字段创建空间索引<br />
	 * 该字段必须存储了空间坐标,且坐标格式为double数组<br />
	 * 超出坐标范围之外的坐标不会被加入到索引中
	 * @param collectioninfo 文档集合连接信息
	 * @param fieldName 字段名称
	 * @param minCoord 最小的坐标值(横轴与纵轴中最小坐标值,如WGS84坐标系中经纬度坐标为-180)
	 * @param maxCoord 最大的坐标值(横轴与纵轴中最大坐标值,如WGS84坐标系中经纬度坐标为180)
	 * */
	public void createGeospatialIndex(DBCollectionInfo collectioninfo, String fieldName,
			double minCoord, double maxCoord);
	
	/**
	 * 支持将符合条件的数据指定的属性数据批量更新
	 * @param collectioninfo 文档集合连接信息
	 * @param whereClause 查询条件,将符合条件的数据返回
	 * @param updateObject 需要更新的数据信息
	 * @return boolean true表示更新成功,false表示更新失败
	 * */
	public boolean updateFieldValues(DBCollectionInfo collectioninfo, Map<String,Object> whereClause, Map<String,Object> updateObject);
	
	/**
	 * 判断指定的字段是否创建了索引
	 * @param collectioninfo 文档集合连接信息
	 * @param fieldName 字段名称
	 * @param type 索引类型， 1时表示升序, -1时表示降序
	 * @return boolean true表示已创建索引, false表示未创建索引
	 * */
	public boolean isIndex(DBCollectionInfo collectioninfo, String fieldName);
	
	/**
	 * 删除指定字段的索引
	 * @param collectioninfo 文档集合连接信息
	 * @param fieldName 字段名称
	 * @param type 索引类型， 1时表示升序, -1时表示降序
	 * @return boolean true表示删除成功, false表示删除失败
	 * */
	public boolean dropIndex(DBCollectionInfo collectioninfo, String fieldName);
	
	/**
	 * 基于指定的数据库集合对象获取一个Lucene索引存储对象 <br />
	 * 若不存在则自动创建
	 * @param collectioninfo 文档集合连接信息
	 * @return org.apache.lucene.store.Directory
	 * */
	public Directory getLuceneIndexDir(DBCollectionInfo collectioninfo) throws IOException ;
}
