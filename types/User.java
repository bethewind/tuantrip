package com.tudaidai.tuantrip.types;

public class User implements TuanTripType {
	private int uid;
	private String uName;
	private String email;
	private String phone;
	private String realName;
	private String mPwd;
	private int idType; // 证件类型
	private String idNumber; // 证件号码

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uName;
	}

	public void setUname(String uName) {
		this.uName = uName;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPwd() {
		return this.mPwd;
	}

	public void setPwd(String paramString) {
		this.mPwd = paramString;
	}

	public int getIdType() {
		return idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

}
