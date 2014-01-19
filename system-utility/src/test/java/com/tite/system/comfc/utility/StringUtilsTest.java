package com.tite.system.comfc.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.tite.system.comfc.utility.StringUtils;

public class StringUtilsTest {

	@Test
	/**
	 * 测试字符串解码功能,必须将UTF-8编码的字符串正常解码
	 * */
	public void testDecodeString() {
		String str = "%E6%88%91%E6%98%AF%E6%B1%89%E5%AD%97";
		try {
			String strDecode = StringUtils.decodeString(str);
			assertEquals("我是汉字", strDecode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	/**
	 * 测试字符串按照UTF-8编码
	 * */
	public void testEncodeString() {
		String str = "%E6%88%91%E6%98%AF%E6%B1%89%E5%AD%97";
		try {
			assertEquals(str, StringUtils.encodeString("我是汉字"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIsUInteger() {
		String str = "2";
		assertTrue(StringUtils.isUInteger(str, false));
		str = "0";
		assertFalse(StringUtils.isUInteger(str, false));
		assertTrue(StringUtils.isUInteger(str, true));
		str = "ew";
		assertFalse(StringUtils.isUInteger(str, false));
		str = "-1";
		assertFalse(StringUtils.isUInteger(str, true));
		str = "-0";
		assertFalse(StringUtils.isUInteger(str, true));
	}
	
	@Test
	public void testIsFloat(){
		String str = "2";
		assertFalse(StringUtils.isFloat(str));
		str = "2.21";
		assertTrue(StringUtils.isFloat(str));
		str = "jfa";
		assertFalse(StringUtils.isFloat(str));
		str = "汉字";
		assertFalse(StringUtils.isFloat(str));
		str = "-21";
		assertFalse(StringUtils.isFloat(str));
		str = "-22.00";
		assertTrue(StringUtils.isFloat(str));
	}

	@Test
	public void testIsInteger(){
		String str = "2";
		assertTrue(StringUtils.isInteger(str));
		str = "2.21";
		assertFalse(StringUtils.isInteger(str));
		str = "jfa";
		assertFalse(StringUtils.isInteger(str));
		str = "汉字";
		assertFalse(StringUtils.isInteger(str));
		str = "-21";
		assertTrue(StringUtils.isInteger(str));
		str = "-22.00";
		assertFalse(StringUtils.isInteger(str));
	}
}
