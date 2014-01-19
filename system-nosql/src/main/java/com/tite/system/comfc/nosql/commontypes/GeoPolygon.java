package com.tite.system.comfc.nosql.commontypes;

import java.util.List;

/***
 *  地理多边形对象(对应的在monogodb中起始点不需要存储两次，认为起始点和终点是进行连接的)
 * @author dongwenfeng
 *
 */
public class GeoPolygon extends Geometry{
	private List<GeoPoint> points=null;

	public GeoPolygon(List<GeoPoint> points){
		this.points=points;
	}
	public List<GeoPoint> getPoints() {
		return points;
	}

	public void setPoints(List<GeoPoint> points) {
		this.points = points;
	}
}
