package com.tite.system.comfc.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

import com.tite.system.comfc.utility.DateFormat;

public class DateFormatTest {

	@Test
	/**
	 * java.util.Date对象转换后的字符串格式必须为"year-month-day hours:minutes:seconds"
	 * */
	public void testGetDateString() {
		Date date = new Date();
		String strDate = DateFormat.getDateString(date);
		String[] str1 = strDate.split("\\ ");
		assertEquals(2, str1.length);
		String[] str2 = str1[0].split("\\-");
		assertEquals(3, str2.length);
		String[] str3 = str1[1].split("\\:");
		assertEquals(3, str3.length);
	}
	@Test
	/**
	 * java.util.Date对象转换后的字符串格式必须为"year-month-day hours:minutes:seconds"
	 * */
	public void testStringToDate() {
		Date date = DateFormat.stringToDate("2012-05-07 11:09:20");
		assertNotNull(date);
	}

}
