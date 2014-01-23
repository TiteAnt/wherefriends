package com.tite.system.wherefriends.core.db.commontype;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tite.system.comfc.nosql.interfaces.IDBModel;
/**
 * 用户状态模型
 * */
public class UserStatus implements IDBModel {
	private UUID id;
	private UUID userId;
	private String desc;
	private Date date;
	private int type;
	
	public static final String ID = "_id";
	public static final String USERID = "uid";
	public static final String DESC = "desc";
	public static final String DATE = "date";
	public static final String TYPE = "type";
	
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
		map.put(DESC, this.desc);
		if(this.date != null){
			map.put(DATE, this.date);
		}else{
			map.put(DATE, new Date());
		}
		map.put(TYPE, this.type);
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
		if(map.get(DATE) != null){
			this.date = (Date)map.get(DATE);
		}
		if(map.get(DESC) != null){
			this.desc = map.get(DESC).toString();
		}
		if(map.get(TYPE) != null){
			this.type = Integer.parseInt(map.get(TYPE).toString());
		}
	}

}
