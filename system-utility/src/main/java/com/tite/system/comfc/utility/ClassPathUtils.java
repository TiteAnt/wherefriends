package com.tite.system.comfc.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 获取路径的工具类
 * @author jiaoyk 2012-04-20
 * @version 6.2.0
 * */
public class ClassPathUtils {
	
	public static String WEBROOTPATH = "";
	
	/**
	 * 获取当前类的ClassPath路径
	 * @return 返回当前类的ClassPath路径
	 * */
	public static String getClassPath(){
		return ClassPathUtils.class.getClassLoader().getResource(File.separator).getPath();
	}
	/**
	 * 获取当前站点的WebInf路径
	 * @return 返回当前站点的WebInf路径
	 * */
	public static String getWebInfPath(){
		String classpath = ClassPathUtils.getClassPath();
		String path = "";
		String[] pathArry = classpath.split("/");
		for(int i=0;i<pathArry.length-1;i++){
			path =path+pathArry[i].concat(File.separator);
		}
		return path;
	}
	/***
	 * 获取当前站点的contextpath路径
	 * @return
	 */
	public static String getWebContextPath(){
		String classpath = ClassPathUtils.getClassPath();
		String path = "";
		String[] pathArry = classpath.split("/");
		for(int i=0;i<pathArry.length-2;i++){
			path =path+pathArry[i].concat(File.separator);
		}
		return path;
	}
	
	
	
	/**
	 * 获取相对于WebRoot文件夹下的指定文件流
	 * @param filePath 相对于WebRoot目录的文件路径, 如"WEB-INF/dfc-config/log.properties"
	 * @return 文件流对象
	 * */
	@SuppressWarnings("resource")
	public static InputStream getWebrootInputStream(String filePath){
		if(!filePath.startsWith(File.separator)){
			filePath = File.separator + filePath;
		}
		//获取classes文件夹的绝对路径
		String strClassLoaderPath =  Thread.currentThread().getContextClassLoader().getResource("").getPath();
		//去掉WEB-INF/classes字符串, 为了避免操作系统的区别, 需要不同方向的斜线同时移除
		strClassLoaderPath = strClassLoaderPath.replace("WEB-INF/classes/", "");
		strClassLoaderPath = strClassLoaderPath.replace("WEB-INF"+ File.separator +"classes" + File.separator, "");
		InputStream ins = null;
		try {
			ins = new FileInputStream(strClassLoaderPath + filePath);
		} catch (FileNotFoundException e) {
			ins = null;
		}
		//如果该方式无法获取到资源文件, 则采用相对路径的方式获取
		if(ins == null){
			ins = ClassPathUtils.class.getClassLoader().getResourceAsStream(".." + File.separator + ".." + filePath);
			//若相对路径无法获取, 则在classloader目录下获取
			if(ins == null){
				ins = ClassPathUtils.class.getClassLoader().getResourceAsStream(filePath);
			}
		}
		return ins;
	}
}
