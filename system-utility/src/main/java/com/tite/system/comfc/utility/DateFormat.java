package com.tite.system.comfc.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * 日期格式化的工具类
 * @author yangh 2012-04-20
 * @version 6.2.0
 * */
public class DateFormat {
	/**
	 * 获取指定日期的字符串表示形式，字符串格式为"yyyy-MM-dd HH:mm:ss"
	 * @param date 要获取字符串形式的日期对象
	 * @return 指定日期的字符串
	 * */
	public static String getDateString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 将指定的字符串转换为日期对象,字符串格式为"yyyy-MM-dd HH:mm:ss"
	 * @param strDate 日期的字符串形式
	 * @return 返回转换后的日期对象
	 * */
	public static Date stringToDate(String strDate){
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return date;
	}
	/**
	 * 将指定的字符串转换为日期对象,字符串格式为"EEE MMM dd HH:mm:ss z yyyy"
	 * @param strDate 日期的字符串形式
	 * @return 返回转换后的日期对象
	 * */
	public static Date stringToDate1(String strDate){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return date;
	}
}
