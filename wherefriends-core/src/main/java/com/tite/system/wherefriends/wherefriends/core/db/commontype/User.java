package com.tite.system.wherefriends.wherefriends.core.db.commontype;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tite.system.comfc.nosql.interfaces.IDBModel;

/**
 * 用户信息模型
 * */
public class User implements IDBModel {
	private UUID id;
	private String userName;
	private String loginName;
	private String loginPassword;
	private String email;
	private String phone;
	//true为男, false为女
	private boolean sex;
	private byte[] headImg;
	private Date date;
	private Date upDate;
	private LocatePoint coord;
	private boolean onLine;
	private boolean openLBS;
	private boolean openCtrip;
	
	public static final String USERID = "_id";
	public static final String USERNAME = "uname";
	public static final String LOGINNAME = "lname";
	public static final String LOGINPWD = "lpwd";
	public static final String EMAIL = "email";
	public static final String PHONE = "phone";
	public static final String SEX = "sex";
	public static final String HEADIMG = "himg";
	public static final String DATE = "date";
	public static final String UPDATE = "udate";
	public static final String COORD = "coord";
	public static final String ONLINE = "oline";
	public static final String OPENLBS = "olbs";
	public static final String OPENCTRIP = "ocp";
	
	public User() {
		super();
	}


	public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public byte[] getHeadImg() {
		return headImg;
	}

	public void setHeadImg(byte[] headImg) {
		this.headImg = headImg;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getUpDate() {
		return upDate;
	}

	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}

	public LocatePoint getCoord() {
		return coord;
	}

	public void setCoord(LocatePoint coord) {
		this.coord = coord;
	}

	public boolean isOnLine() {
		return onLine;
	}

	public void setOnLine(boolean onLine) {
		this.onLine = onLine;
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

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(this.id != null){
			map.put(USERID, this.id.toString());
		}else{
			map.put(USERID, UUID.randomUUID().toString());
		}
		map.put(USERNAME, this.userName);
		map.put(LOGINNAME, this.loginName);
		map.put(LOGINPWD, this.loginPassword);
		map.put(EMAIL, this.email);
		map.put(PHONE, this.phone);
		if(this.sex){
			map.put(SEX, 1);
		}else{
			map.put(SEX, 0);
		}
		map.put(HEADIMG, this.headImg);
		map.put(DATE, this.date);
		map.put(UPDATE, this.upDate);
		if(this.coord != null){
			map.put(COORD, new double[]{this.coord.getX(), this.coord.getY()});
		}
		if(this.onLine){
			map.put(ONLINE, 1);
		}else{
			map.put(ONLINE, 0);
		}
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
		return map;
	}

	@Override
	public void fromMap(Map<String, Object> map) {
		if(map.get(USERID) != null){
			this.id = UUID.fromString(map.get(USERID).toString());
		}
		if(map.get(USERNAME) != null){
			this.userName = map.get(USERNAME).toString();
		}
		if(map.get(LOGINNAME) != null){
			this.loginName = map.get(LOGINNAME).toString();
		}
		if(map.get(LOGINPWD) != null){
			this.loginPassword = map.get(LOGINPWD).toString();
		}
		if(map.get(EMAIL) != null){
			this.email = map.get(EMAIL).toString();
		}
		if(map.get(PHONE) != null){
			this.phone = map.get(PHONE).toString();
		}
		if(map.get(SEX) != null){
			if("1".equalsIgnoreCase(map.get(SEX).toString())){
				this.sex = true;
			}else{
				this.sex = false;
			}
		}
		if(map.get(HEADIMG) != null){
			this.headImg = (byte[])map.get(HEADIMG);
		}
		if(map.get(DATE) != null){
			this.date = (Date)map.get(DATE);
		}
		if(map.get(UPDATE) != null){
			this.upDate = (Date)map.get(UPDATE);
		}
		if(map.get(COORD) != null){
			@SuppressWarnings("unchecked")
			List<Object> point = (List<Object>)map.get(COORD);
			this.coord = new LocatePoint(Double.parseDouble(point.get(0).toString()), 
					Double.parseDouble(point.get(1).toString()));
		}
		if(map.get(ONLINE) != null){
			if("1".equalsIgnoreCase(map.get(ONLINE).toString())){
				this.onLine = true;
			}else{
				this.onLine = false;
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
	}
}
