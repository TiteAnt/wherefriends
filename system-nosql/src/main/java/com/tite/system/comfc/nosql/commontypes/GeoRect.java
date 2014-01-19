package com.tite.system.comfc.nosql.commontypes;
/**
 * 定义表达矩形类型数据空间位置的对象
 * @author jiaoyk 2013-08-19
 * @version DFC 7.0
 * */
public class GeoRect extends Geometry{
	private GeoPoint leftBottom;
	private GeoPoint rightTop;
	public GeoRect() {
		super();
		this.leftBottom=new GeoPoint();
		this.rightTop=new GeoPoint();
	}
	public GeoRect(GeoPoint leftBottom, GeoPoint rightTop) {
		super();
		this.leftBottom = leftBottom;
		this.rightTop = rightTop;
	}
	public GeoPoint getLeftBottom() {
		return leftBottom;
	}
	public void setLeftBottom(GeoPoint leftBottom) {
		this.leftBottom = leftBottom;
	}
	public GeoPoint getRightTop() {
		return rightTop;
	}
	public void setRightTop(GeoPoint rightTop) {
		this.rightTop = rightTop;
	}
}
