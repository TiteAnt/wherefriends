package com.tite.system.wherefriends.wherefriends.core.db.interfaces;

import java.util.Date;
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
import com.tite.system.wherefriends.wherefriends.core.db.commontype.LocatePoint;
import com.tite.system.wherefriends.wherefriends.core.db.commontype.User;

public class IUserTest extends TestCase{
	
	private static IUser userImpl = null;
	private static INoSQL nosql = null;
	
	private static final String userId_1 = "b93cd619-f240-4865-80c5-ee080d4ee31b";
	private static final String userId_2 = "de8a1806-3876-41fd-a2bd-7c61f28adbf6";
	private static final String userId_3 = "1c7cd2fe-767c-4ce3-b998-8f7cf47afe29";
	
	public IUserTest(String name){
		super(name);
	}
	
	//自定义测试套件,让测试用例按照指定顺序执行
	public static junit.framework.Test suite(){
		TestSuite suite = new TestSuite();
		suite.addTest(new IUserTest("start"));
		suite.addTest(new IUserTest("testAddUser"));
		suite.addTest(new IUserTest("testUpdateUser"));
		suite.addTest(new IUserTest("testDeleteUser"));
		suite.addTest(new IUserTest("testGetUsersQueryOptions"));
		suite.addTest(new IUserTest("testCountUsers"));
		suite.addTest(new IUserTest("testGetUsersStringQueryOptions"));
		suite.addTest(new IUserTest("testCountUsersString"));
		suite.addTest(new IUserTest("end"));
		return suite;
	}
	
	@Before
	public void start(){
		//准备测试环境
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
		userImpl = (IUser)beanFactory.getBean("userImpl");
		nosql = (INoSQL)beanFactory.getBean("MongoDBImpl");
	}
	
	@After
	public void end(){
		//删除测试数据库
		nosql.deleteDatabase("WHEREFRIENDS");
	}
	
	@Test
	public void testAddUser() {
		User user = new User();
		user.setId(UUID.fromString(userId_1));
		user.setUserName("张三");
		user.setLoginName("zhangsan");
		user.setLoginPassword("123456");
		user.setEmail("zhangsan@hotmail.com");
		user.setPhone("18420214566");
		user.setSex(true);
		user.setDate(new Date());
		user.setUpDate(new Date());
		user.setCoord(new LocatePoint(116.273, 31.31));
		user.setOnLine(true);
		user.setOpenCtrip(true);
		user.setOpenLBS(true);
		
		User user1 = new User();
		user1.setId(UUID.fromString(userId_2));
		user1.setUserName("李四");
		user1.setLoginName("李四");
		user1.setLoginPassword("123456");
		user1.setEmail("lisi@gmail.com");
		user1.setPhone("18345228763");
		user1.setSex(true);
		user1.setDate(new Date());
		user1.setUpDate(new Date());
		user1.setCoord(new LocatePoint(116.273, 31.31));
		user1.setOnLine(true);
		user1.setOpenCtrip(true);
		user1.setOpenLBS(true);
		
		User user2 = new User();
		user2.setId(UUID.fromString(userId_3));
		user2.setUserName("肖燕");
		user2.setLoginName("xiaoyan");
		user2.setLoginPassword("123456");
		user2.setEmail("xiaoyan@gmail.com");
		user2.setPhone("13027490894");
		user2.setSex(false);
		user2.setDate(new Date());
		user2.setUpDate(new Date());
		user2.setCoord(new LocatePoint(116.273, 31.31));
		user2.setOnLine(true);
		user2.setOpenCtrip(true);
		user2.setOpenLBS(true);
		
		assertEquals(userId_1, userImpl.addUser(user).toString());
		assertEquals(userId_2, userImpl.addUser(user1).toString());
		assertEquals(userId_3, userImpl.addUser(user2).toString());
	}

	@Test
	public void testUpdateUser() {
		List<User> users = userImpl.getUsers("张三", new QueryOptions());
		assertEquals(1, users.size());
		User user = users.get(0);
		assertEquals(userId_1, user.getId().toString());
		
		user.setUserName("王五");
		user.setEmail("wangwu@gmail.com");
		user.setPhone("18320278946");
		user.setUpDate(new Date());
		assertTrue(userImpl.updateUser(user));
		
		users = userImpl.getUsers("张三", new QueryOptions());
		assertEquals(0, users.size());
		
		users = userImpl.getUsers("王五", new QueryOptions());
		assertEquals(1, users.size());
		assertEquals("王五", users.get(0).getUserName());
		assertEquals("wangwu@gmail.com", users.get(0).getEmail());
		assertEquals("18320278946", users.get(0).getPhone());
	}

	@Test
	public void testDeleteUser() {
		boolean blnDelete = userImpl.deleteUser(UUID.fromString(userId_1));
		assertTrue(blnDelete);
		
		List<User> users = userImpl.getUsers("王五", new QueryOptions());
		assertEquals(0, users.size());
	}

	@Test
	public void testGetUsersQueryOptions() {
		QueryOptions options = new QueryOptions();
		options.setPageIndex(1);
		options.setPageSize(1);
		List<User> users = userImpl.getUsers(options);
		assertEquals(1, users.size());
		options.setPageIndex(2);
		options.setPageSize(1);
		users = userImpl.getUsers(options);
		assertEquals(1, users.size());
	}

	@Test
	public void testCountUsers() {
		assertEquals(2, userImpl.countUsers());
	}

	@Test
	public void testGetUsersStringQueryOptions() {
		QueryOptions options = new QueryOptions();
		options.setPageIndex(1);
		options.setPageSize(5);
		List<User> users = userImpl.getUsers("李四",options);
		assertEquals(1, users.size());
		assertEquals("李四", users.get(0).getUserName());
		assertEquals(userId_2, users.get(0).getId().toString());
		options.setPageIndex(2);
		options.setPageSize(5);
		users = userImpl.getUsers("李四",options);
		assertEquals(0, users.size());
	}

	@Test
	public void testCountUsersString() {
		assertEquals(1, userImpl.countUsers("李四"));
		assertEquals(1, userImpl.countUsers("肖"));
	}
}
