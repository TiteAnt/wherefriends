package com.tite.system.wherefriends.core.db.commontype;
/**
 * 位置点模型
 * */
public class LocatePoint {
	private double x;
	private double y;
	
	public LocatePoint(double x, double y) {
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
