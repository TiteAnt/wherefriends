package com.tite.system.wherefriends.core.db.interfaces;

import java.util.List;
import java.util.UUID;

import com.tite.system.comfc.nosql.commontypes.QueryOptions;
import com.tite.system.wherefriends.core.db.commontype.UserFriend;

/**
 * 定义用户朋友的操作接口
 * */
public interface IUserFriend {
	/**
	 * 添加朋友
	 * @param friend 朋友信息
	 * @return UUID
	 * */
	public UUID addUserFriend(UserFriend friend);
	
	/**
	 * 更新朋友信息
	 * @param friend 朋友信息,必须包含唯一标识
	 * @return boolean
	 * */
	public boolean updateUserFriend(UserFriend friend);
	
	/**
	 * 删除朋友信息
	 * @param friendId 用户与朋友关系的唯一标识
	 * @return boolean
	 * */
	public boolean deleteUserFriend(UUID friendId);
	
	/**
	 * 删除用户的指定朋友关系
	 * @param userId 用户唯一标识
	 * @param friendId 好友唯一标识
	 * @return boolean
	 * */
	public boolean deleteUserFriend(UUID userId, UUID friendId);
	
	/**
	 * 查询指定用户的朋友列表
	 * @param userId 用户唯一标识
	 * @param queryOptions 查询参数
	 * @return List<UserFriend>
	 * */
	public List<UserFriend> getUserFriends(UUID userId, QueryOptions queryOptions);
	
	/**
	 * 查询指定用户的好友总数
	 * @param userId 用户唯一标识
	 * @return long
	 * */
	public long countUserFriends(UUID userId);
	
	/**
	 * 根据好友别名查询好友信息
	 * @param userId 用户唯一标识
	 * @param alias 好友别名
	 * @param queryOptions 查询参数
	 * @return List<UserFriend>
	 * */
	public List<UserFriend> getUserFriends(UUID userId, String alias, QueryOptions queryOptions);
	
	/**
	 * 查询指定用户符合别名条件的好友总数
	 * @param userId 用户唯一标识
	 * @param alias 好友别名
	 * @return long
	 * */
	public long countUserFriends(UUID userId, String alias);
}
