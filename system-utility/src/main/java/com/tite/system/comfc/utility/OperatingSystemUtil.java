package com.tite.system.comfc.utility;
/***
 * 根据请求消息头，获取用户请求的操作系统名称
 * 
 * @author dongwenfeng
 *
 */
public class OperatingSystemUtil {
	
	/***
	 * 从请求头中获取请求操作系统的信息
	 * @param agent
	 * @return
	 */
	public static String getSystem(String agent){
		String  strSystem="其他";
		if(agent.indexOf("Windows NT 5.0")>-1){
			strSystem="Windows 2000";
		}else if(agent.indexOf("Windows NT 5.1")>-1){
			strSystem="Windows XP";
		}if(agent.indexOf("Windows NT 5.2")>-1){
			strSystem="Windows Server 2003";
		}else if(agent.indexOf("Windows NT 6.0")>-1){
			strSystem="Windows Server 2008";
		}if(agent.indexOf("Windows NT 6.1")>-1){
			strSystem="Windows 7";
		}else if(agent.indexOf("Windows NT 6.2")>-1){
			strSystem="Windows 8";
		}
		return strSystem;
	}

}
