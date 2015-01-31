package com.tudaidai.tuantrip.types;

public class BaseLbsDetail implements TuanTripType {

	private String lbsid;
	private String title;
	private String address;
	private String phone;
	private double jingdu = 0;
	private double weidu = 0;
	private String detail;
	Group<Comment> comments;
	private String image;
	private String city;
	int total = 0;

	public String getLbsid() {
		return lbsid;
	}

	public void setLbsid(String lbsid) {
		this.lbsid = lbsid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getJingdu() {
		return jingdu;
	}

	public void setJingdu(double jingdu) {
		this.jingdu = jingdu;
	}

	public double getWeidu() {
		return weidu;
	}

	public void setWeidu(double weidu) {
		this.weidu = weidu;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Group<Comment> getComments() {
		return comments;
	}

	public void setComments(Group<Comment> comments) {
		this.comments = comments;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}
}
