package com.tite.system.wherefriends.wherefriends.core.db.interfaces;

import java.util.List;
import java.util.UUID;

import com.tite.system.comfc.nosql.commontypes.QueryOptions;
import com.tite.system.wherefriends.wherefriends.core.db.commontype.User;

/**
 * 用户管理接口
 * */
public interface IUser {
	/**
	 * 用户添加接口
	 * @param user 用户对象
	 * @return UUID
	 * */
	public UUID addUser(User user);
	
	/**
	 * 更新用户信息
	 * @param user 用户对象, 用户ID必须指定
	 * @return boolean
	 * */
	public boolean updateUser(User user);
	
	/**
	 * 删除指定用户
	 * @param userId 用户唯一标识
	 * @return boolean
	 * */
	public boolean deleteUser(UUID userId);
	
	/**
	 * 查询所有用户
	 * @param queryOptions 查询参数
	 * @return List<User>
	 * */
	public List<User> getUsers(QueryOptions queryOptions);
	
	/**
	 * 统计用户总数
	 * @return long
	 * */
	public long countUsers();
	
	/**
	 * 根据用户名称模糊查询用户信息
	 * @param userName 用户名称
	 * @param queryOptions 查询参数
	 * @return List<User>
	 * */
	public List<User> getUsers(String userName, QueryOptions queryOptions);
	
	/**
	 * 根据用户名称统计用户总数
	 * @param userName 用户名称
	 * @return long
	 * */
	public long countUsers(String userName);
}
