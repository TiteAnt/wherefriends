package com.tite.system.comfc.nosql.interfaces;

import java.util.Map;
/**
 * 所有与数据库操作有关的模型必须实现此接口
 * @author yangh 2012-04-09
 * @version 6.2.0
 * */
public interface IDBModel {
	/**
	 * 返回包含对象的所有属性的Map
	 * @return Map
	 * */
	public Map<String , Object> toMap();
	/**
	 * 通过Map给对象属性赋值
	 * @param map 包含对象属性的Map
	 * */
	public void fromMap(Map<String, Object> map);
}
