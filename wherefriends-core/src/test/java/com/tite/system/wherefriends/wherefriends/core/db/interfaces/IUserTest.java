package com.tite.system.wherefriends.wherefriends.core.db.interfaces;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tite.system.comfc.nosql.interfaces.INoSQL;

public class IUserTest extends TestCase{
	
	private static IUser user = null;
	private static INoSQL nosql = null;
	
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
		user = (IUser)beanFactory.getBean("userImpl");
		nosql = (INoSQL)beanFactory.getBean("MongoDBImpl");
	}
	
	@After
	public void end(){
		//删除测试数据库
		nosql.deleteDatabase("WHEREFRIENDS");
	}
	
	@Test
	public void testAddUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUsersQueryOptions() {
		fail("Not yet implemented");
	}

	@Test
	public void testCountUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUsersStringQueryOptions() {
		fail("Not yet implemented");
	}

	@Test
	public void testCountUsersString() {
		fail("Not yet implemented");
	}

}
