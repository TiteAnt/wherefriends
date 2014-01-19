package com.tite.system.comfc.utility;
/***
 * 请求请求消息头，获取用户请求的浏览器名称
 * @author dongwenfeng
 *
 */
public class WebBrowseUtil {

	public static String getBrowserVerion(String agent){
		String browserVersion = "其它"; 
		//得到用户的浏览器名 
		String userbrowser = agent; 
		if( agent == null ){
			return browserVersion;
		}
		if (userbrowser.indexOf("MSIE 6.0") > 0) 
			browserVersion = "IE6"; 
		else if (userbrowser.indexOf("MSIE 7.0") > 0) 
			browserVersion = "IE7"; 
		else if (userbrowser.indexOf("MSIE 8.0") > 0) 
			browserVersion = "IE8"; 
		else if(userbrowser.indexOf("MSIE 9.0") > 0)
			browserVersion = "IE9";
		else if(userbrowser.indexOf("MSIE 10.0") > 0)
			browserVersion = "IE10";
		else if (userbrowser.indexOf("Firefox") > 0) 
			browserVersion = "Firefox"; 
		else if (userbrowser.indexOf("Chrome") > 0) 
			browserVersion = "Chrome"; 
		else if (userbrowser.indexOf("Safari") > 0) 
			browserVersion = "Safari"; 
		else if (userbrowser.indexOf("Opera") > 0) 
			browserVersion = "Opera"; 
		return browserVersion;
	}
}
