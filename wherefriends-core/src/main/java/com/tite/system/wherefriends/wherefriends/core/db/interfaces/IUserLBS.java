package com.tite.system.wherefriends.wherefriends.core.db.interfaces;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.tite.system.wherefriends.wherefriends.core.db.commontype.UserLBS;

/**
 * 定义用户位置信息操作接口
 * */
public interface IUserLBS {
	/**
	 * 为用户添加位置信息
	 * @param userlbs 用户位置信息对象
	 * @return UUID
	 * */
	public UUID addUserLBS(UserLBS userlbs);
	
	/**
	 * 查询用户最近一次位置信息
	 * @param userId 用户唯一标识
	 * @return UserLBS
	 * */
	public UserLBS getRecentlyUserLBS(UUID userId);
	
	/**
	 * 获取指定时间范围内的用户位置信息
	 * @param userId 用户唯一标识
	 * @param startDate 起始时间
	 * @param endDate 截止时间
	 * @return List<UserLBS> 
	 * */
	public List<UserLBS> getUserLBSs(UUID userId, Date startDate, Date endDate);
}
