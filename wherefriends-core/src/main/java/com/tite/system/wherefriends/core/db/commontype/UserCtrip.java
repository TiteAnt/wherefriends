package com.tite.system.wherefriends.core.db.commontype;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tite.system.comfc.nosql.interfaces.IDBModel;
/**
 * 用户行程模型
 * */
public class UserCtrip implements IDBModel {
	private UUID id;
	private UUID userId;
	private UUID outPlaceId;
	private UUID inPlaceId;
	private Date outDate;
	private Date inDate;
	private String desc;
	private Date date;
	
	public static final String ID = "_id";
	public static final String USERID = "uid";
	public static final String OUTPLACEID = "opid";
	public static final String INPLACEID = "ipid";
	public static final String OUTDATE = "odate";
	public static final String INDATE = "idate";
	public static final String DESC = "desc";
	public static final String DATE = "date";
	
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

	public UUID getOutPlaceId() {
		return outPlaceId;
	}

	public void setOutPlaceId(UUID outPlaceId) {
		this.outPlaceId = outPlaceId;
	}

	public UUID getInPlaceId() {
		return inPlaceId;
	}

	public void setInPlaceId(UUID inPlaceId) {
		this.inPlaceId = inPlaceId;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
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
		if(this.outPlaceId != null){
			map.put(OUTPLACEID, this.outPlaceId.toString());
		}
		if(this.inPlaceId != null){
			map.put(INPLACEID, this.inPlaceId.toString());
		}
		if(this.outDate != null){
			map.put(OUTDATE, this.outDate);
		}
		if(this.inDate != null){
			map.put(INDATE, this.inDate);
		}
		map.put(DESC, this.desc);
		map.put(DATE, this.date);
		
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
		if(map.get(OUTPLACEID) != null){
			this.outPlaceId = UUID.fromString(map.get(OUTPLACEID).toString());
		}
		if(map.get(INPLACEID) != null){
			this.inPlaceId = UUID.fromString(map.get(INPLACEID).toString());
		}
		if(map.get(OUTDATE) != null){
			this.outDate = (Date)map.get(OUTDATE);
		}
		if(map.get(INDATE) != null){
			this.inDate = (Date)map.get(INDATE);
		}
		if(map.get(DESC) != null){
			this.desc = map.get(DESC).toString();
		}
		if(map.get(DATE) != null){
			this.date = (Date)map.get(DATE);
		}
	}

}
