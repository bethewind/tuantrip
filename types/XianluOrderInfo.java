package com.tudaidai.tuantrip.types;

public class XianluOrderInfo implements TuanTripType {
	private String shortTitle;
	private double balance; // 余额
	private String uname;
	private String phone;
	private int quantity; // 购买的数量
	private String remaik; // 留言
	private int idType; // 证件类型
	private String idNumber; // 证件号码
	private Group<Xianlu> xianluGroup;
	private Xianlu userBuyXianlu;

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
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

	public Group<Xianlu> getXianluGroup() {
		return xianluGroup;
	}

	public void setXianluGroup(Group<Xianlu> xianluGroup) {
		this.xianluGroup = xianluGroup;
	}

	public Xianlu getUserBuyXianlu() {
		return userBuyXianlu;
	}

	public void setUserBuyXianlu(Xianlu userBuyXianlu) {
		this.userBuyXianlu = userBuyXianlu;
	}
}
