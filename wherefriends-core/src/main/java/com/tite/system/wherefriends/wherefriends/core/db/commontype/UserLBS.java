package com.tite.system.wherefriends.wherefriends.core.db.commontype;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tite.system.comfc.nosql.interfaces.IDBModel;

public class UserLBS implements IDBModel {
	private UUID id;
	private UUID userId;
	//朋友上次出现的位置
	private LocatePoint coord;
	private UUID xzqhId;
	private Date date;
	
	public static final String ID = "_id";
	public static final String USERID = "uid";
	public static final String COORD = "coord";
	public static final String XZHQID = "xzqhid";
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

	public LocatePoint getCoord() {
		return coord;
	}

	public void setCoord(LocatePoint coord) {
		this.coord = coord;
	}

	public UUID getXzqhId() {
		return xzqhId;
	}

	public void setXzqhId(UUID xzqhId) {
		this.xzqhId = xzqhId;
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
		if(this.coord != null){
			map.put(COORD, new double[]{this.coord.getX(), this.coord.getY()});
		}
		if(this.xzqhId != null){
			map.put(XZHQID, this.xzqhId.toString());
		}
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
		if(map.get(COORD) != null){
			@SuppressWarnings("unchecked")
			List<Object> point = (List<Object>)map.get(COORD);
			this.coord = new LocatePoint(Double.parseDouble(point.get(0).toString()), 
					Double.parseDouble(point.get(1).toString()));
		}
		if(map.get(XZHQID) != null){
			this.xzqhId = UUID.fromString(map.get(XZHQID).toString());
		}
		if(map.get(DATE) != null){
			this.date = (Date)map.get(DATE);
		}
	}
}
