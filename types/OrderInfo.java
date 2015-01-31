package com.tudaidai.tuantrip.types;

public class OrderInfo implements TuanTripType {
	private String shortTitle;
	private int haveAddress; // 是否要写地址信息
	private int isFreight; // 是否有运费
	private int freightNum; // 超过几件包邮
	private double freightFee; // 运费
	private String travelDate;
	private int isTravelDate; // 是否要填写出发日期
	private double balance; // 余额
	private String uname;
	private String phone;
	private int quantity; // 购买的数量
	private String remaik; // 留言
	private String dateBind; // 绑定的日期
	private int idType; // 证件类型
	private String idNumber; // 证件号码
	private int aId = 0;
	private String aName;

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public int getHaveAddress() {
		return haveAddress;
	}

	public void setHaveAddress(int haveAddress) {
		this.haveAddress = haveAddress;
	}

	public int getIsFreight() {
		return isFreight;
	}

	public void setIsFreight(int isFreight) {
		this.isFreight = isFreight;
	}

	public int getFreightNum() {
		return freightNum;
	}

	public void setFreightNum(int freightNum) {
		this.freightNum = freightNum;
	}

	public double getFreightFee() {
		return freightFee;
	}

	public void setFreightFee(double freightFee) {
		this.freightFee = freightFee;
	}

	public String getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(String travelDate) {
		this.travelDate = travelDate;
	}

	public int getIsTravelDate() {
		return isTravelDate;
	}

	public void setIsTravelDate(int isTravelDate) {
		this.isTravelDate = isTravelDate;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getRemaik() {
		return remaik;
	}

	public void setRemaik(String remaik) {
		this.remaik = remaik;
	}

	public String getDateBind() {
		return dateBind;
	}

	public void setDateBind(String dateBind) {
		this.dateBind = dateBind;
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

	public int getAId() {
		return aId;
	}

	public void setAId(int aId) {
		this.aId = aId;
	}

	public String getAName() {
		return aName;
	}

	public void setAName(String aName) {
		this.aName = aName;
	}

}
