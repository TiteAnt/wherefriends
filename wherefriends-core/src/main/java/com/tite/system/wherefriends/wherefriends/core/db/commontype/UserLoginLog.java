package com.tite.system.wherefriends.wherefriends.core.db.commontype;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tite.system.comfc.nosql.interfaces.IDBModel;
/**
 * 记录用户上下线时间日志
 * */
public class UserLoginLog implements IDBModel {
	private UUID id;
	private UUID userId;
	private Date loginTime;
	private Date logoutTime;
	private long onlineTime;
	
	private static final String ID = "_id";
	private static final String USERID = "uid";
	private static final String LOGINTIME = "ltime";
	private static final String LOGOUTTIME = "lttime";
	private static final String ONLINETIME = "oltime";
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if(this.id != null){
			map.put(ID, this.id.toString());
		}else{
			map.put(ID, UUID.randomUUID().toString());
		}
		if(this.userId != null){
			map.put(USERID, this.userId.toString());
		}
		if(this.loginTime != null){
			map.put(LOGINTIME, this.loginTime);
		}
		if(this.logoutTime != null){
			map.put(LOGOUTTIME, this.logoutTime);
		}
		map.put(ONLINETIME, this.onlineTime);

		return map;
	}

	@Override
	public void fromMap(Map<String, Object> map) {
		if(map.get(ID) != null){
			this.id = UUID.fromString(map.get(ID).toString());
		}
		if(map.get(USERID) != null){
			this.userId = UUID.fromString(map.get(USERID).toString());
		}
		if(map.get(LOGINTIME) != null){
			this.loginTime = (Date)map.get(LOGINTIME);
		}
		if(map.get(LOGOUTTIME) != null){
			this.logoutTime = (Date)map.get(LOGOUTTIME);
		}
		if(map.get(ONLINETIME) != null){
			this.onlineTime = Long.parseLong(map.get(ONLINETIME).toString());
		}
	}

}
