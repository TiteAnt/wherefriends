package com.tite.system.wherefriends.core.db.commontype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tite.system.comfc.nosql.interfaces.IDBModel;
/**
 * 行政区划数据模型
 * */
public class XZQH implements IDBModel {
	private UUID id;
	private int type;
	private String name;
	private UUID parentId;
	private LocatePoint[] coords;
	private LocatePoint coord;
	
	public static final String ID = "_id";
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String PARENTID = "pid";
	public static final String COORDS = "coords";
	public static final String COORD = "coord";
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getParentId() {
		return parentId;
	}

	public void setParentId(UUID parentId) {
		this.parentId = parentId;
	}

	public LocatePoint[] getCoords() {
		return coords;
	}

	public void setCoords(LocatePoint[] coords) {
		this.coords = coords;
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
		map.put(TYPE, this.type);
		if(this.parentId != null){
			map.put(PARENTID, this.parentId.toString());
		}
		map.put(NAME, this.name);
		if(this.coords != null){
			double[][] coords = new double[this.coords.length][2];
			for(int i=0; i<this.coords.length; i++){
				coords[i][0] = this.coords[i].getX();
				coords[i][1] = this.coords[i].getY();
			}
			map.put(COORDS, coords);
		}
		if(this.coord != null){
			map.put(COORD, new double[]{this.coord.getX(), this.coord.getY()});
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromMap(Map<String, Object> map) {
		if(map.get(ID) != null){
			this.id = UUID.fromString(map.get(ID).toString());
		}
		if(map.get(TYPE) != null){
			this.type = Integer.parseInt(map.get(TYPE).toString());
		}
		if(map.get(NAME) != null){
			this.name = map.get(NAME).toString();
		}
		if(map.get(PARENTID) != null){
			this.parentId = UUID.fromString(map.get(PARENTID).toString());
		}
		if(map.get(COORDS) != null){
			List<Object> points = (List<Object>)map.get(COORDS);
			this.coords = new LocatePoint[points.size()];
			List<Object> point = null;
			for(int i=0; i<points.size(); i++){
				point = (List<Object>)points.get(i);
				this.coords[i] = new LocatePoint(Double.parseDouble(point.get(0).toString()), 
						Double.parseDouble(point.get(1).toString()));
			}
		}
		if(map.get(COORD) != null){
			List<Object> point = (List<Object>)map.get(COORD);
			this.coord = new LocatePoint(Double.parseDouble(point.get(0).toString()), 
					Double.parseDouble(point.get(1).toString()));
		}
	}

}
