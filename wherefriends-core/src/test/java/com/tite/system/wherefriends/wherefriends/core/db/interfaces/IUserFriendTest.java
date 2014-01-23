package com.tite.system.wherefriends.wherefriends.core.db.interfaces;

import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tite.system.comfc.nosql.commontypes.QueryOptions;
import com.tite.system.comfc.nosql.interfaces.INoSQL;
import com.tite.system.wherefriends.core.db.commontype.LocatePoint;
import com.tite.system.wherefriends.core.db.commontype.UserFriend;
import com.tite.system.wherefriends.core.db.interfaces.IUserFriend;

public class IUserFriendTest extends TestCase{
	
	public static IUserFriend userFriendImpl;
	public static INoSQL nosql;
	
	private static final String id1 = "87a1a355-19e7-41db-a805-e1db05f0190b";
	private static final String id2 = "20c8c992-e21b-4aaa-b06e-cf740f6a042c";
	private static final String id3 = "61ec0e8b-c5b8-4055-9fce-286cf2eee7d5";
	private static final String id4 = "4bdcb678-fb70-41aa-9cca-546e86dcc5b1";
	
	private static final String userId = "9a55169b-1db7-4738-b261-1e2128ceacad";
	private static final String ufriendId_1 = "b93cd619-f240-4865-80c5-ee080d4ee31b";
	private static final String ufriendId_2 = "de8a1806-3876-41fd-a2bd-7c61f28adbf6";
	private static final String ufriendId_3 = "1c7cd2fe-767c-4ce3-b998-8f7cf47afe29";
	
	public IUserFriendTest(String name){
		super(name);
	}
	
	//自定义测试套件,让测试用例按照指定顺序执行
	public static junit.framework.Test suite(){
		TestSuite suite = new TestSuite();
		suite.addTest(new IUserFriendTest("start"));
		suite.addTest(new IUserFriendTest("testAddUserFriend"));
		suite.addTest(new IUserFriendTest("testUpdateUserFriend"));
		suite.addTest(new IUserFriendTest("testDeleteUserFriend"));
		suite.addTest(new IUserFriendTest("testDeleteUserFriendUUIDUUID"));
		suite.addTest(new IUserFriendTest("testGetUserFriendsUUIDQueryOptions"));
		suite.addTest(new IUserFriendTest("testCountUserFriendsUUID"));
		suite.addTest(new IUserFriendTest("testGetUserFriendsUUIDStringQueryOptions"));
		suite.addTest(new IUserFriendTest("testCountUserFriendsUUIDString"));
		suite.addTest(new IUserFriendTest("end"));
		return suite;
	}
	
	@Before
	public void start(){
		//准备测试环境
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
		userFriendImpl = (IUserFriend)beanFactory.getBean("userFriendImpl");
		nosql = (INoSQL)beanFactory.getBean("MongoDBImpl");
	}
	
	@After
	public void end(){
		//删除测试数据库
		nosql.deleteDatabase("WHEREFRIENDS");
	}
	
	@Test
	public void testAddUserFriend() {
		UserFriend userFriend1 = new UserFriend();
		userFriend1.setId(UUID.fromString(id1));
		userFriend1.setUserId(UUID.fromString(userId));
		userFriend1.setFriendId(UUID.fromString(ufriendId_1));
		userFriend1.setFriendAlias("张三");
		userFriend1.setOnline(true);
		userFriend1.setOpenCtrip(true);
		userFriend1.setOpenLBS(true);
		userFriend1.setCoord(new LocatePoint(116.21, 32.66));
		
		UserFriend userFriend2 = new UserFriend();
		userFriend2.setId(UUID.fromString(id2));
		userFriend2.setUserId(UUID.fromString(userId));
		userFriend2.setFriendId(UUID.fromString(ufriendId_2));
		userFriend2.setFriendAlias("李四");
		userFriend2.setOnline(true);
		userFriend2.setOpenCtrip(true);
		userFriend2.setOpenLBS(true);
		userFriend2.setCoord(new LocatePoint(118.56, 29.11));
		
		UserFriend userFriend3 = new UserFriend();
		userFriend3.setId(UUID.fromString(id3));
		userFriend3.setUserId(UUID.fromString(userId));
		userFriend3.setFriendId(UUID.fromString(ufriendId_3));
		userFriend3.setFriendAlias("王五");
		userFriend3.setOnline(true);
		userFriend3.setOpenCtrip(false);
		userFriend3.setOpenLBS(true);
		userFriend3.setCoord(new LocatePoint(63.12, 118.62));
		
		UserFriend userFriend4 = new UserFriend();
		userFriend4.setId(UUID.fromString(id4));
		userFriend4.setUserId(UUID.fromString(ufriendId_1));
		userFriend4.setFriendId(UUID.fromString(userId));
		userFriend4.setFriendAlias("肖燕");
		userFriend4.setOnline(false);
		userFriend4.setOpenCtrip(false);
		userFriend4.setOpenLBS(true);
		userFriend4.setCoord(new LocatePoint(128.61, 43.56));
		
		assertEquals(id1, userFriendImpl.addUserFriend(userFriend1).toString());
		assertEquals(id2, userFriendImpl.addUserFriend(userFriend2).toString());
		assertEquals(id3, userFriendImpl.addUserFriend(userFriend3).toString());
		assertEquals(id4, userFriendImpl.addUserFriend(userFriend4).toString());
	}

	@Test
	public void testUpdateUserFriend() {
		List<UserFriend> uFriends = 
				userFriendImpl.getUserFriends(UUID.fromString(ufriendId_1), new QueryOptions());
		assertNotNull(uFriends);
		assertTrue(uFriends.size() == 1);
		UserFriend uFriend = uFriends.get(0);
		assertEquals(id4, uFriend.getId().toString());
		assertEquals("肖燕", uFriend.getFriendAlias());
		assertEquals(userId, uFriend.getFriendId().toString());
		//更新查询对象
		uFriend.setFriendId(UUID.fromString(ufriendId_2));
		uFriend.setFriendAlias("李四");
		assertTrue(userFriendImpl.updateUserFriend(uFriend));
		//验证更新结果
		uFriends = userFriendImpl.getUserFriends(UUID.fromString(ufriendId_1), new QueryOptions());
		assertNotNull(uFriends);
		assertTrue(uFriends.size() == 1);
		uFriend = uFriends.get(0);
		assertEquals(id4, uFriend.getId().toString());
		assertEquals("李四", uFriend.getFriendAlias());
		assertEquals(ufriendId_2, uFriend.getFriendId().toString());
	}

	@Test
	public void testDeleteUserFriend() {
		boolean blnDelete = userFriendImpl.deleteUserFriend(UUID.fromString(id4));
		assertTrue(blnDelete);
		//检查删除是否生效
		List<UserFriend> uFriends = 
				userFriendImpl.getUserFriends(UUID.fromString(ufriendId_1), new QueryOptions());
		assertNotNull(uFriends);
		assertTrue(uFriends.size() == 0);
	}
	
	@Test
	public void testDeleteUserFriendUUIDUUID() {
		boolean blnDelete = 
				userFriendImpl.deleteUserFriend(UUID.fromString(userId), UUID.fromString(ufriendId_1));
		assertTrue(blnDelete);
		//检查删除是否生效
		List<UserFriend> uFriends = 
				userFriendImpl.getUserFriends(UUID.fromString(userId), new QueryOptions());
		assertNotNull(uFriends);
		assertTrue(uFriends.size() == 2);
		//剩下两条数据中的好友ID都不会等于被删除的好友ID
		for(int i=0; i<uFriends.size(); i++){
			assertFalse(uFriends.get(i).getFriendId().toString().equalsIgnoreCase(ufriendId_1));
		}
	}

	@Test
	public void testGetUserFriendsUUIDQueryOptions() {
		QueryOptions options = new QueryOptions();
		options.setPageIndex(1);
		options.setPageSize(1);
		List<UserFriend> uFriends = userFriendImpl.getUserFriends(UUID.fromString(userId), 
				options);
		assertNotNull(uFriends);
		assertEquals(1, uFriends.size());
		assertEquals(userId, uFriends.get(0).getUserId().toString());
		
		options.setPageIndex(2);
		options.setPageSize(1);
		uFriends = userFriendImpl.getUserFriends(UUID.fromString(userId), 
				options);
		assertNotNull(uFriends);
		assertEquals(1, uFriends.size());
		assertEquals(userId, uFriends.get(0).getUserId().toString());
		
		options.setPageIndex(3);
		options.setPageSize(1);
		uFriends = userFriendImpl.getUserFriends(UUID.fromString(userId), 
				options);
		assertNotNull(uFriends);
		assertEquals(0, uFriends.size());		
	}

	@Test
	public void testCountUserFriendsUUID() {
		assertEquals(2, userFriendImpl.countUserFriends(UUID.fromString(userId)));
		assertEquals(0, userFriendImpl.countUserFriends(UUID.fromString(ufriendId_1)));
	}

	@Test
	public void testGetUserFriendsUUIDStringQueryOptions() {
		QueryOptions options = new QueryOptions();
		options.setPageIndex(1);
		options.setPageSize(2);
		
		List<UserFriend> uFriends = userFriendImpl.getUserFriends(UUID.fromString(userId), 
				"李", options);
		assertEquals(1, uFriends.size());
		assertEquals("李四", uFriends.get(0).getFriendAlias());
		assertEquals(ufriendId_2, uFriends.get(0).getFriendId().toString());
		
		uFriends = userFriendImpl.getUserFriends(UUID.fromString(userId), 
				"赵", options);
		assertEquals(0, uFriends.size());
	}

	@Test
	public void testCountUserFriendsUUIDString() {
		assertEquals(1, userFriendImpl.countUserFriends(UUID.fromString(userId), "李"));
		assertEquals(1, userFriendImpl.countUserFriends(UUID.fromString(userId), "王"));
		assertEquals(0, userFriendImpl.countUserFriends(UUID.fromString(userId), "赵"));
	}
}
