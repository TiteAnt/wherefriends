package com.tite.system.comfc.nosql.interfaces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tite.system.comfc.nosql.commontypes.DBCollectionInfo;
import com.tite.system.comfc.nosql.commontypes.QueryOptions;
import com.tite.system.comfc.nosql.interfaces.INoSQL;
/**
 * 测试NoSQL数据库操作接口的MongoDB默认实现
 * @author jiaoyk 2012-04-06
 * @version 6.2.0
 * */
public class INoSQLTest {
	
	private BeanFactory beanFactory = null;
	private INoSQL nosqlDB = null;
	private DBCollectionInfo collectioninfo = new DBCollectionInfo("TestcaseDB", "TestcaseCollection");
	private DBCollectionInfo collectioninfo_Lucene = new DBCollectionInfo("TestcaseDB", "LuceneCol");
	private DBCollectionInfo collectioninfo_geo = new DBCollectionInfo("TestcaseDB", "GeoCollection");
	
	@Before
	public void start(){
		beanFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
		nosqlDB = (INoSQL)beanFactory.getBean("MongoDBImpl");
	}
	
	/**
	 * 测试NoSQL数据库的添加方法,由于需要对数据进行验证,因此,也测试了数据查询与删除方法
	 * */
	@Test
	public void testInsert() {
		//调用插入文档方法
		insertOneTestDocument();
		
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("id", 1);
		//将插入的记录删除,确保数据库中没有冗余数据(根据整型属性删除)
		boolean blnDelete = nosqlDB.delete(collectioninfo, whereClause);
		assertTrue(blnDelete);
		//验证数据删除是否成功
		List<Map<String, Object>> result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(0, result.size());
	}
	
	/**
	 * 测试NoSQL数据库错误的添加方法,如传入了Java对象
	 * */
	@Test
	public void testInsertError(){
		Map<String, Object> document = new HashMap<String, Object>();
		//验证NoSQL数据库插入整型,字符串,子对象(Map类型)的属性
		document.put("id", 1);
		document.put("name", "Tite");
		document.put("company", "SuperMap");
		document.put("collectioninfo", collectioninfo);
		boolean blnInsert = nosqlDB.insert(collectioninfo, document);
		//验证插入记录完成后返回结果是否正确
		assertFalse(blnInsert);
	}
	
	/**
	 * 测试不允许自动创建文档集合对象方法
	 * */
	@Test
	public void testInsertWithOptions(){
		DBCollectionInfo tempinfo = new DBCollectionInfo("tempTestcaseDB", "tempTestcaseCollection");
		Map<String, Object> document = new HashMap<String, Object>();
		//验证NoSQL数据库插入整型,字符串,子对象(Map类型)的属性
		document.put("id", 1);
		document.put("name", "Tite");
		document.put("company", "SuperMap");
		boolean blnInsert = nosqlDB.insert(tempinfo, document, false);
		//验证插入记录完成后返回结果是否正确
		assertFalse(blnInsert);
	}
	
	@Test
	public void testUpdate() {
		//调用插入文档方法
		insertOneTestDocument();
		//对新插入的数据进行更新(先查询到该文档,然后对文档进行更新)
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("id", 1);
		//查询待更新文档
		List<Map<String, Object>> updateResult = nosqlDB.query(collectioninfo, whereClause);
		Iterator<Map<String, Object>> iterResult = updateResult.iterator();
		Map<String, Object> queryDocument = new HashMap<String, Object>();
		Map<String, Object> updateDocument = null;
		while(iterResult.hasNext()){
			updateDocument = iterResult.next();
			updateDocument.put("name", "SuperMap");
			//设置查询条件(_id为文档的唯一标识)
			queryDocument.put("_id", updateDocument.get("_id"));
			boolean blnUpdate = nosqlDB.update(collectioninfo, updateDocument, queryDocument);
			assertTrue(blnUpdate);
		}
		
		//根据更新后的数据进行查询, 验证更新是否成功
		whereClause.remove("id");
		whereClause.put("name", "SuperMap");
		List<Map<String, Object>> result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(1, result.size());
		int id = Integer.parseInt(result.get(0).get("id").toString());
		String name = result.get(0).get("name").toString();
		String company = result.get(0).get("company").toString();
		assertEquals(1, id);
		assertEquals("SuperMap", name);
		assertEquals("SuperMap", company);
		
		//将插入的记录删除,确保数据库中没有冗余数据(根据字符串类型属性删除)
		boolean blnDelete = nosqlDB.delete(collectioninfo, whereClause);
		assertTrue(blnDelete);
		//验证数据删除是否成功
		result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(0, result.size());
	}
	
	@Test
	public void testAccurateUpdate() {
		//调用插入文档方法
		insertMoreTestDocuments();
		
		//对指定ID的记录进行精确查找后,更新该记录的值
		//对新插入的数据进行更新(先查询到该文档,然后对文档进行更新)
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("id", 1);
		//查询待更新文档
		Map<String, Object> updateResult = nosqlDB.accurateQuery(collectioninfo, whereClause);
		updateResult.put("name", "SuperMap");
		boolean blnUpdate = nosqlDB.accurateUpdate(collectioninfo, updateResult, whereClause);
		assertTrue(blnUpdate);
		
		//根据更新后的数据进行查询, 验证更新是否成功
		Map<String, Object> result = nosqlDB.accurateQuery(collectioninfo, whereClause);
		assertNotNull(result.size());
		int id = Integer.parseInt(result.get("id").toString());
		String name = result.get("name").toString();
		String postcode = result.get("postcode").toString();
		assertEquals(1, id);
		assertEquals("SuperMap", name);
		assertEquals("100015", postcode);
		
		//验证对于能够查出来多条记录的查询条件,只更新一条记录的精确更新功能
		whereClause.remove("id");
		whereClause.put("company", "SuperMap");
		
		Map<String, Object> update = new HashMap<String, Object>();
		update.put("id", 11);
		update.put("postcode", "100011");
		update.put("company", "SuperMap");
		blnUpdate = nosqlDB.accurateUpdate(collectioninfo, update, whereClause);
		assertTrue(blnUpdate);
		
		//查询被更新的数据,默认应该只有一条记录被更新
		List<Map<String, Object>> listResult = nosqlDB.query(collectioninfo);
		int updateIDCount = 0;
		for(int i=0; i<listResult.size(); i++){
			int tempID = Integer.parseInt(listResult.get(i).get("id").toString());
			if(tempID == 11){
				//验证结果是否符合预先设置
				assertNull(listResult.get(i).get("address"));
				assertEquals("SuperMap", listResult.get(i).get("company").toString());
				assertEquals("100011", listResult.get(i).get("postcode").toString());
				updateIDCount++;
			}
		}
		//遍历所有记录后,ID为11的记录应该只有一条
		assertEquals(1, updateIDCount);
		
		//将插入的记录删除,确保数据库中没有冗余数据(根据字符串类型属性删除)
		boolean blnDelete = nosqlDB.delete(collectioninfo, whereClause);
		assertTrue(blnDelete);
		//验证数据删除是否成功
		List<Map<String, Object>> clearResult = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(0, clearResult.size());
	}
	
	@Test
	public void testDelete() {
		//调用插入文档方法
		insertOneTestDocument();
		
		//将插入的记录删除,确保数据库中没有冗余数据(根据对象类型属性进行删除)
		Map<String, Object> collection = new HashMap<String, Object>();
		collection.put("DBName", "TestcaseDB");
		
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("collectioninfo", collection);
		boolean blnDelete = nosqlDB.delete(collectioninfo, whereClause);
		assertTrue(blnDelete);
		//验证数据删除是否成功
		List<Map<String, Object>> result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(0, result.size());
	}

	@Test
	public void testQuery() {
		//插入多条测试数据
		insertMoreTestDocuments();
		//验证查询是否正确
		List<Map<String, Object>> result = nosqlDB.query(collectioninfo);
		assertEquals(3, result.size());
		
		//删除测试数据
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("company", "SuperMap");
		boolean blnDelete = nosqlDB.delete(collectioninfo, whereClause);
		assertTrue(blnDelete);
		//验证数据删除是否成功
		result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(0, result.size());
	}
	
	@Test
	public void testAccurateQuery(){
		//插入多条测试数据
		insertMoreTestDocuments();
		
		//验证查询是否正确
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("id", 1);
		
		Map<String, Object> result = nosqlDB.accurateQuery(collectioninfo, whereClause);
		
		assertNotNull(result);
		assertEquals(1, Integer.parseInt(result.get("id").toString()));
		assertEquals("北京市朝阳区酒仙桥北路甲10号电子城IT产业园201楼E门3层", result.get("address").toString());
		assertEquals("100015", result.get("postcode").toString());
		assertEquals("SuperMap", result.get("company").toString());
		
		//删除测试数据
		whereClause.remove("id");
		whereClause.put("company", "SuperMap");
		boolean blnDelete = nosqlDB.delete(collectioninfo, whereClause);
		assertTrue(blnDelete);
		//验证数据删除是否成功
		List<Map<String, Object>> resultList = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(0, resultList.size());
	}
	
	@Test
	public void testAccurateQueryByResultOptions(){
		//插入多条测试数据
		insertMoreTestDocuments();
		
		//验证查询是否正确
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("id", 1);
		
		Map<String, Object> resultOptions = new HashMap<String, Object>();
		resultOptions.put("id", 1);
		resultOptions.put("postcode", 1);
		
		Map<String, Object> result = nosqlDB.accurateQuery(collectioninfo, whereClause, resultOptions);
		
		assertNotNull(result);
		assertEquals(1, Integer.parseInt(result.get("id").toString()));
		assertNull(result.get("address"));
		assertEquals("100015", result.get("postcode").toString());
		assertNull(result.get("company"));
		
		//删除测试数据
		whereClause.remove("id");
		whereClause.put("company", "SuperMap");
		boolean blnDelete = nosqlDB.delete(collectioninfo, whereClause);
		assertTrue(blnDelete);
		//验证数据删除是否成功
		List<Map<String, Object>> resultList = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(0, resultList.size());
	}
	
	@Test
	public void testQueryByWhereClause() {
		//插入多条测试数据
		insertMoreTestDocuments();
		//验证查询是否正确
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("id", 1);
		List<Map<String, Object>> result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(1, result.size());
		assertEquals(1, Integer.parseInt(result.get(0).get("id").toString()));
		assertEquals("北京市朝阳区酒仙桥北路甲10号电子城IT产业园201楼E门3层", result.get(0).get("address").toString());
		assertEquals("100015", result.get(0).get("postcode").toString());
		assertEquals("SuperMap", result.get(0).get("company").toString());
		
		//移除上一个查询条件
		whereClause.remove("id");
		whereClause.put("company", "SuperMap");
		result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(3, result.size());
		
		//删除测试数据
		boolean blnDelete = nosqlDB.delete(collectioninfo, whereClause);
		assertTrue(blnDelete);
		//验证数据删除是否成功
		result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(0, result.size());
	}

	@Test
	public void testQueryByNoWhereClause(){
		//插入多条测试数据
		insertMoreTestDocuments();
		
		//测试只分页的查询方式,第一页查询结果
		QueryOptions options = new QueryOptions();
		options.setPageSize(2);
		options.setPageIndex(1);
		options.setReturnFields(new String[]{"_id"});
		List<Map<String, Object>> result = nosqlDB.query(collectioninfo, null, options);
		assertEquals(2, result.size());
	}
	
	@Test
	public void testQueryByOptions() {
		//插入多条测试数据
		insertMoreTestDocuments();
		
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("company", "SuperMap");
		
		//测试只分页的查询方式,第一页查询结果
		QueryOptions options = new QueryOptions();
		options.setPageSize(2);
		options.setPageIndex(1);
		List<Map<String, Object>> result = nosqlDB.query(collectioninfo, whereClause, options);
		assertEquals(2, result.size());
		
		//第二页查询结果
		options.setPageIndex(2);
		result = nosqlDB.query(collectioninfo, whereClause, options);
		assertEquals(1, result.size());
		
		//测试分页并排序的查询结果
		options.setPageIndex(1);
		options.setSortField("id");
		result = nosqlDB.query(collectioninfo, whereClause, options);
		assertEquals(2, result.size());
		assertEquals(1, Integer.parseInt(result.get(0).get("id").toString()));
		assertEquals("100015", result.get(0).get("postcode").toString());
		assertEquals(2, Integer.parseInt(result.get(1).get("id").toString()));
		assertEquals("100019", result.get(1).get("postcode").toString());
		
		options.setPageIndex(1);
		options.setSortField("address");
		result = nosqlDB.query(collectioninfo, whereClause, options);
		assertEquals(2, result.size());
		assertEquals(1, Integer.parseInt(result.get(0).get("id").toString()));
		assertEquals("100015", result.get(0).get("postcode").toString());
		assertEquals(3, Integer.parseInt(result.get(1).get("id").toString()));
		assertEquals("100019", result.get(1).get("postcode").toString());
		options.setPageIndex(2);
		options.setSortField("address");
		result = nosqlDB.query(collectioninfo, whereClause, options);
		assertEquals(1, result.size());
		assertEquals(2, Integer.parseInt(result.get(0).get("id").toString()));
		assertEquals("100019", result.get(0).get("postcode").toString());
		
		
		//测试返回结果中只包含指定字段
		options.setPageIndex(1);
		options.setSortField("id");
		options.setReturnFields(new String[]{"id","postcode"});
		result = nosqlDB.query(collectioninfo, whereClause, options);
		assertEquals(2, result.size());
		assertEquals(1, Integer.parseInt(result.get(0).get("id").toString()));
		assertEquals("100015", result.get(0).get("postcode").toString());
		assertNull(result.get(0).get("address"));
		assertNull(result.get(0).get("company"));
		
		//删除测试数据
		boolean blnDelete = nosqlDB.delete(collectioninfo, whereClause);
		assertTrue(blnDelete);
		//验证数据删除是否成功
		result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(0, result.size());
	}

	@Test
	public void testCount() {
		//插入多条测试数据
		insertMoreTestDocuments();
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("postcode", "100015");
		long count = nosqlDB.count(collectioninfo, whereClause);
		assertEquals(1, count);
		
		whereClause.put("postcode", "100019");
		count = nosqlDB.count(collectioninfo, whereClause);
		assertEquals(2, count);
		
		//移除上一个条件
		whereClause.remove("postcode");
		whereClause.put("company", "SuperMap");
		count = nosqlDB.count(collectioninfo, whereClause);
		assertEquals(3, count);
		
		//删除测试数据
		boolean blnDelete = nosqlDB.delete(collectioninfo, whereClause);
		assertTrue(blnDelete);
		//验证数据删除是否成功
		List<Map<String, Object>> result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(0, result.size());
	}
	
	/**
	 * 向测试数据库中增加测试数据,用于测试用例使用
	 * */
	@SuppressWarnings("unchecked")
	private void insertOneTestDocument(){
		
		Map<String, Object> collection = new HashMap<String, Object>();
		collection.put("DBName", "TestcaseDB");
		collection.put("CollectionName", "TestcaseCollection");
		
		Map<String, Object> document = new HashMap<String, Object>();
		//验证NoSQL数据库插入整型,字符串,子对象(Map类型)的属性
		document.put("id", 1);
		document.put("name", "Tite");
		document.put("company", "SuperMap");
		document.put("collectioninfo", collection);
		boolean blnInsert = nosqlDB.insert(collectioninfo, document);
		//验证插入记录完成后返回结果是否正确
		assertTrue(blnInsert);
		
		//验证记录是否正确被插入数据库中
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("id", 1);
		List<Map<String, Object>> result = nosqlDB.query(collectioninfo, whereClause);
		assertEquals(1, result.size());
		int id = Integer.parseInt(result.get(0).get("id").toString());
		String name = result.get(0).get("name").toString();
		String company = result.get(0).get("company").toString();
		Map<String, Object> colinfo = (Map<String, Object>)result.get(0).get("collectioninfo");
		assertEquals(1, id);
		assertEquals("Tite", name);
		assertEquals("SuperMap", company);
		assertEquals("TestcaseDB", colinfo.get("DBName").toString());
		assertEquals("TestcaseCollection", colinfo.get("CollectionName").toString());
	}
	
	/**
	 * 插入多条测试文档数据,用于测试数据查询
	 * */
	private void insertMoreTestDocuments(){
		Map<String, Object> document1 = new HashMap<String, Object>();
		document1.put("id", 1);
		document1.put("address", "北京市朝阳区酒仙桥北路甲10号电子城IT产业园201楼E门3层");
		document1.put("postcode", "100015");
		document1.put("company", "SuperMap");
		
		Map<String, Object> document2 = new HashMap<String, Object>();
		document2.put("id", 2);
		document2.put("address", "北京市海淀区西三旗建材城西路");
		document2.put("postcode", "100019");
		document2.put("company", "SuperMap");
		
		Map<String, Object> document3 = new HashMap<String, Object>();
		document3.put("id", 3);
		document3.put("address", "北京市海淀区回龙观");
		document3.put("postcode", "100019");
		document3.put("company", "SuperMap");
		
		nosqlDB.insert(collectioninfo, document1);
		nosqlDB.insert(collectioninfo, document2);
		nosqlDB.insert(collectioninfo, document3);
	}
	@Test
	public void testInsertFile(){
		this.insertFile();
	}
	@Test
	public void testAccurateQueryFile(){
		this.insertFile();
		final Map<String, Object> document = new HashMap<String, Object>();
		document.put("_id", 3);
		Map<String,Object> map = nosqlDB.accurateQueryFile(collectioninfo, document);
		assertEquals(3, map.get("_id"));
		assertNotNull(map.get("$file"));
	}
	@Test
	public void testQueryFiles(){
		this.insertFile();
		Map<String, Object> document = new HashMap<String, Object>();
		document.put("_id", 3);
		List<Map<String,Object>> list = nosqlDB.queryFiles(collectioninfo,document);
		assertEquals(1, list.size());
		assertEquals(3, list.get(0).get("_id"));
		assertNotNull(list.get(0).get("$file"));
	}
	@Test
	public void testQueryFilesAll(){
		this.insertFile();
		List<Map<String,Object>> list = nosqlDB.queryFiles(collectioninfo);
		assertEquals(3, list.size());
	}
	@Test
	public void testCountFile(){
		this.insertFile();
		final Map<String, Object> document = new HashMap<String, Object>();
		document.put("_id", 3);
		long cnt = nosqlDB.countFile(collectioninfo, document);
		long s = new Long("1");
		assertEquals(s, cnt);
		cnt = nosqlDB.countFile(collectioninfo, null);
		s = new Long("3");
		assertEquals(s, cnt);
	}
	@Test
	public void testDeleteFile(){
		Map<String, Object> document = new HashMap<String, Object>();
		document.put("_id", 1);
		nosqlDB.deleteFile(collectioninfo, document);
		document.clear();
		document.put("_id", 2);
		nosqlDB.deleteFile(collectioninfo, document);
		document.clear();
		document.put("_id", 3);
		nosqlDB.deleteFile(collectioninfo, document);
	}
	
	@Test
	public void testCreateGeospatialIndex(){
		//插入带有空间坐标的数据collectioninfo_geo
		Map<String, Object> document1 = new HashMap<String, Object>();
		document1.put("_id", 1);
		document1.put("coords", new double[]{116.3880386353, 39.9061927795});
		document1.put("name", "北京");
		boolean blnInsert = this.nosqlDB.insert(collectioninfo_geo, document1);
		assertTrue(blnInsert);
		
		Map<String, Object> document2 = new HashMap<String, Object>();
		document2.put("_id", 2);
		document2.put("coords", new double[]{125.7575149536, 39.0285148621});
		document2.put("name", "平壤");
		blnInsert = this.nosqlDB.insert(collectioninfo_geo, document2);
		assertTrue(blnInsert);
		
		Map<String, Object> document3 = new HashMap<String, Object>();
		document3.put("_id", 3);
		document3.put("coords", new double[]{126.9352493286, 37.5423507690});
		document3.put("name", "首尔");
		blnInsert = this.nosqlDB.insert(collectioninfo_geo, document3);
		assertTrue(blnInsert);
		
		Map<String, Object> document4 = new HashMap<String, Object>();
		document4.put("_id", 4);
		document4.put("coords", new double[]{139.8091888428, 35.6830558777});
		document4.put("name", "东京");
		blnInsert = this.nosqlDB.insert(collectioninfo_geo, document4);
		assertTrue(blnInsert);
		
		Map<String, Object> document5 = new HashMap<String, Object>();
		document5.put("_id", 5);
		document5.put("coords", new double[]{100.5526657104, 13.7455711365});
		document5.put("name", "曼谷");
		blnInsert = this.nosqlDB.insert(collectioninfo_geo, document5);
		assertTrue(blnInsert);
		//创建索引
		this.nosqlDB.createGeospatialIndex(collectioninfo_geo, "coords", -180, 180);
		
		assertTrue(this.nosqlDB.isIndex(collectioninfo_geo, "coords"));
		
		//索引创建完毕后,测试最近距离查询,确认空间索引是否创建成功
		Map<String, Object> whereClause1 = new HashMap<String, Object>();
		whereClause1.put("$near", new double[]{106.9123535156,47.9285964966});
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("coords", whereClause1);
		
		List<Map<String, Object>> result = this.nosqlDB.query(collectioninfo_geo, whereClause);
		assertNotNull(result);
		//距离该坐标最近的位置应该是北京
		assertEquals("北京", result.get(0).get("name").toString());
		//其次是平壤
		assertEquals("平壤", result.get(1).get("name").toString());
	}
	
	@Test
	public void testUpdateFieldValues(){
		//插入测试数据
		insertMoreTestDocuments();
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("postcode", "100019");
		List<Map<String,Object>> dbResult = this.nosqlDB.query(collectioninfo, whereClause);
		assertNotNull(dbResult);
		assertEquals(2, dbResult.size());
		//测试数据更新
		Map<String, Object> updateObject = new HashMap<String, Object>();
		updateObject.put("postcode", "100015");
		boolean blnUpdate = this.nosqlDB.updateFieldValues(collectioninfo, whereClause, updateObject);
		assertTrue(blnUpdate);
		//验证更新是否成功
		dbResult = this.nosqlDB.query(collectioninfo, whereClause);
		assertNotNull(dbResult);
		assertEquals(0, dbResult.size());
		whereClause.put("postcode", "100015");
		dbResult = this.nosqlDB.query(collectioninfo, whereClause);
		assertNotNull(dbResult);
		assertEquals(3, dbResult.size());
		//删除测试数据
		this.nosqlDB.delete(collectioninfo, whereClause);
	}
	
	@Test
	public void testIsIndex(){
		//验证属性索引
		assertFalse(this.nosqlDB.isIndex(collectioninfo, "postcode"));
		Map<String, Integer> indexs = new HashMap<String, Integer>();
		indexs.put("postcode", 1);
		this.nosqlDB.createIndex(collectioninfo, indexs);
		assertTrue(this.nosqlDB.isIndex(collectioninfo, "postcode"));
	}
	
	@Test
	public void testDropIndex(){
		assertFalse(this.nosqlDB.isIndex(collectioninfo, "postcode"));
		Map<String, Integer> indexs = new HashMap<String, Integer>();
		indexs.put("postcode", 1);
		this.nosqlDB.createIndex(collectioninfo, indexs);
		assertTrue(this.nosqlDB.isIndex(collectioninfo, "postcode"));
		assertTrue(this.nosqlDB.dropIndex(collectioninfo, "postcode"));
		assertFalse(this.nosqlDB.isIndex(collectioninfo, "postcode"));		
	}
	
	private void insertFile(){
		FileInputStream in1 = null;
		try {
			in1 = new FileInputStream(new File(System.getProperty("user.dir").concat(File.separator).concat("src\\test\\resources\\blank.png")));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		Map<String, Object> document = new HashMap<String, Object>();
		document.put("_id", 1);
		nosqlDB.insertFile(collectioninfo, in1, document);
		FileInputStream in2 = null;
		try {
			in2 = new FileInputStream(new File(System.getProperty("user.dir").concat(File.separator).concat("src\\test\\resources\\blank.png")));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		document = new HashMap<String, Object>();
		document.put("_id", 2);
		nosqlDB.insertFile(collectioninfo, in2, document);
		FileInputStream in3 = null;
		try {
			in3 = new FileInputStream(new File(System.getProperty("user.dir").concat(File.separator).concat("src\\test\\resources\\blank.png")));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		document = new HashMap<String, Object>();
		document.put("_id", 3);
		nosqlDB.insertFile(collectioninfo, in3, document);
	} 
	
	@Test
	public void testCreateIndexDBCollectionInfoMapMap(){
		//验证索引是否存在
		assertFalse(this.nosqlDB.isIndex(collectioninfo_geo, "name"));
				
		Map<String, Integer> indexs = new HashMap<String, Integer>();
		indexs.put("name", 1);
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("unique", true);
		this.nosqlDB.createIndex(collectioninfo_geo, indexs, whereClause);
		
		//验证索引是否存在
		assertTrue(this.nosqlDB.isIndex(collectioninfo_geo, "name"));
	}
	
	@Test
	public void testGetLuceneIndexDir(){
		try {
			Directory dir = this.nosqlDB.getLuceneIndexDir(collectioninfo_Lucene);
			assertNotNull(dir);
		} catch (IOException e) {
			fail();
		}
	}
	
	@After
	public void end(){
		//移除测试文档集合
		this.nosqlDB.deleteDatabase(collectioninfo.getDBName());
		beanFactory = null;
		nosqlDB = null;
		collectioninfo = null;
	}
	
}
