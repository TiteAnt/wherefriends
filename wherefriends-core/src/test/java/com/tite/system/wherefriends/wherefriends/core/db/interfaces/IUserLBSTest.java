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

import com.tite.system.comfc.nosql.interfaces.INoSQL;
import com.tite.system.comfc.utility.DateFormat;
import com.tite.system.wherefriends.core.db.commontype.LocatePoint;
import com.tite.system.wherefriends.core.db.commontype.UserLBS;
import com.tite.system.wherefriends.core.db.interfaces.IUserLBS;

public class IUserLBSTest extends TestCase{
	
	private static IUserLBS userLBSImpl;
	private static INoSQL nosql;
	
	private static final String userId = "b93cd619-f240-4865-80c5-ee080d4ee31b";
	
	public IUserLBSTest(String name){
		super(name);
	}
	
	//自定义测试套件,让测试用例按照指定顺序执行
	public static junit.framework.Test suite(){
		TestSuite suite = new TestSuite();
		suite.addTest(new IUserLBSTest("start"));
		suite.addTest(new IUserLBSTest("testAddUserLBS"));
		suite.addTest(new IUserLBSTest("testGetRecentlyUserLBS"));
		suite.addTest(new IUserLBSTest("testGetUserLBSs"));
		suite.addTest(new IUserLBSTest("end"));
		return suite;
	}
	
	@Before
	public void start(){
		//准备测试环境
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
		userLBSImpl = (IUserLBS)beanFactory.getBean("UserLBSImpl");
		nosql = (INoSQL)beanFactory.getBean("MongoDBImpl");
	}
	
	@After
	public void end(){
		//删除测试数据库
		nosql.deleteDatabase("WHEREFRIENDS");
	}
	
	@Test
	public void testAddUserLBS() {
		UserLBS lbs1 = new UserLBS();
		lbs1.setUserId(UUID.fromString(userId));
		lbs1.setCoord(new LocatePoint(110,28));
		lbs1.setXzqhId(UUID.randomUUID());
		lbs1.setDate(DateFormat.stringToDate("2014-01-15 12:00:00"));
		
		UserLBS lbs2 = new UserLBS();
		lbs2.setUserId(UUID.fromString(userId));
		lbs2.setCoord(new LocatePoint(104.52,28.98));
		lbs2.setXzqhId(UUID.randomUUID());
		lbs2.setDate(DateFormat.stringToDate("2014-01-16 13:33:00"));
		
		UserLBS lbs3 = new UserLBS();
		lbs3.setUserId(UUID.fromString(userId));
		lbs3.setCoord(new LocatePoint(101.85,11.12));
		lbs3.setXzqhId(UUID.randomUUID());
		lbs3.setDate(DateFormat.stringToDate("2014-01-18 06:11:28"));
		
		UserLBS lbs4 = new UserLBS();
		lbs4.setUserId(UUID.fromString(userId));
		lbs4.setCoord(new LocatePoint(85.556,66.42));
		lbs4.setXzqhId(UUID.randomUUID());
		lbs4.setDate(DateFormat.stringToDate("2014-01-23 11:45:11"));
		
		UserLBS lbs5 = new UserLBS();
		lbs5.setUserId(UUID.fromString(userId));
		lbs5.setCoord(new LocatePoint(120.56,22.11));
		lbs5.setXzqhId(UUID.randomUUID());
		lbs5.setDate(DateFormat.stringToDate("2014-01-23 12:00:00"));
		
		assertTrue(userLBSImpl.addUserLBS(lbs1) != null);
		assertTrue(userLBSImpl.addUserLBS(lbs2) != null);
		assertTrue(userLBSImpl.addUserLBS(lbs3) != null);
		assertTrue(userLBSImpl.addUserLBS(lbs4) != null);
		assertTrue(userLBSImpl.addUserLBS(lbs5) != null);
	}

	@Test
	public void testGetRecentlyUserLBS() {
		UserLBS userlbs = userLBSImpl.getRecentlyUserLBS(UUID.fromString(userId));
		assertNotNull(userlbs);
		assertEquals(userId, userlbs.getUserId().toString());
		assertEquals("2014-01-23 12:00:00", DateFormat.getDateString(userlbs.getDate()));
		assertEquals(120.56, userlbs.getCoord().getX());
		assertEquals(22.11, userlbs.getCoord().getY());
	}

	@Test
	public void testGetUserLBSs() {
		Date startDate = DateFormat.stringToDate("2014-01-17 12:00:00");
		Date endDate = DateFormat.stringToDate("2014-01-23 24:00:00");
		List<UserLBS> userlbses = userLBSImpl.getUserLBSs(UUID.fromString(userId), startDate, endDate);
		assertNotNull(userlbses);
		assertEquals(3, userlbses.size());
		//默认应该按照时间由近及远排序
		assertEquals("2014-01-23 12:00:00", DateFormat.getDateString(userlbses.get(0).getDate()));
		assertEquals("2014-01-23 11:45:11", DateFormat.getDateString(userlbses.get(1).getDate()));
		assertEquals("2014-01-18 06:11:28", DateFormat.getDateString(userlbses.get(2).getDate()));
	}

}
