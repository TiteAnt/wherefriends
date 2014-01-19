package com.tite.system.comfc.nosql.commontypes;
/**
 * 定义表达点类型数据空间位置的坐标对象
 * @author jiaoyk 2013-08-19
 * @version DFC 7.0
 * */
public class GeoPoint extends Geometry {
	private double x;
	private double y;
	
	public GeoPoint() {
		super();
	}
	public GeoPoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
}
