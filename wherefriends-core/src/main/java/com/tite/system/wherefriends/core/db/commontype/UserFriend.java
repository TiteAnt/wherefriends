package com.tite.system.wherefriends.core.db.commontype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tite.system.comfc.nosql.interfaces.IDBModel;
/**
 * 用户朋友关系表
 * */
public class UserFriend implements IDBModel {
	private UUID id;
	private UUID userId;
	private UUID friendId;
	private String friendAlias;
	private boolean openLBS;
	private boolean openCtrip;
	private boolean online;
	private LocatePoint coord;
	
	public static final String ID = "_id";
	public static final String USERID = "uid";
	public static final String FRIENDID = "fid";
	public static final String FRIENDALIAS = "falias";
	public static final String OPENLBS = "olbs";
	public static final String OPENCTRIP = "ocp";
	public static final String ONLINE = "oline";
	public static final String COORD = "coord";
	
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

	public UUID getFriendId() {
		return friendId;
	}

	public void setFriendId(UUID friendId) {
		this.friendId = friendId;
	}

	public String getFriendAlias() {
		return friendAlias;
	}

	public void setFriendAlias(String friendAlias) {
		this.friendAlias = friendAlias;
	}

	public boolean isOpenLBS() {
		return openLBS;
	}

	public void setOpenLBS(boolean openLBS) {
		this.openLBS = openLBS;
	}

	public boolean isOpenCtrip() {
		return openCtrip;
	}

	public void setOpenCtrip(boolean openCtrip) {
		this.openCtrip = openCtrip;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public LocatePoint getCoord() {
		return coord;
	}

	public void setCoord(LocatePoint coord) {
		this.coord = coord;
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
		if(this.friendId != null){
			map.put(FRIENDID, this.friendId.toString());
		}
		map.put(FRIENDALIAS, this.friendAlias);
		if(this.openLBS){
			map.put(OPENLBS, 1);
		}else{
			map.put(OPENLBS, 0);
		}
		if(this.openCtrip){
			map.put(OPENCTRIP, 1);
		}else{
			map.put(OPENCTRIP, 0);
		}
		if(this.online){
			map.put(ONLINE, 1);
		}else{
			map.put(ONLINE, 0);
		}
		if(this.coord != null){
			map.put(COORD, new double[]{this.coord.getX(), this.coord.getY()});
		}
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
		if(map.get(FRIENDID) != null){
			this.friendId = UUID.fromString(map.get(FRIENDID).toString());
		}
		if(map.get(FRIENDALIAS) != null){
			this.friendAlias = map.get(FRIENDALIAS).toString();
		}
		if(map.get(ONLINE) != null){
			if("1".equalsIgnoreCase(map.get(ONLINE).toString())){
				this.online = true;
			}else{
				this.online = false;
			}
		}
		if(map.get(OPENLBS) != null){
			if("1".equalsIgnoreCase(map.get(OPENLBS).toString())){
				this.openLBS = true;
			}else{
				this.openLBS = false;
			}
		}
		if(map.get(OPENCTRIP) != null){
			if("1".equalsIgnoreCase(map.get(OPENCTRIP).toString())){
				this.openCtrip = true;
			}else{
				this.openCtrip = false;
			}
		}
		if(map.get(COORD) != null){
			@SuppressWarnings("unchecked")
			List<Object> point = (List<Object>)map.get(COORD);
			this.coord = new LocatePoint(Double.parseDouble(point.get(0).toString()), 
					Double.parseDouble(point.get(1).toString()));
		}
	}
}
