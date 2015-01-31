package com.tudaidai.tuantrip.types;

public class Comment implements TuanTripType {
	private String cid;
	private String uid;
	private String uname;
	private String uImage;
	private String content;
	private String image;
	private String ding;
	private String cai;
	private String comtime;

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCid() {
		return cid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUname() {
		return uname;
	}

	public void setUImage(String uImage) {
		this.uImage = uImage;
	}

	public String getUImage() {
		return uImage;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setDing(String ding) {
		this.ding = ding;
	}

	public String getDing() {
		return ding;
	}

	public void setCai(String cai) {
		this.cai = cai;
	}

	public String getCai() {
		return cai;
	}

	public void setComtime(String comtime) {
		this.comtime = comtime;
	}

	public String getComtime() {
		return comtime;
	}
}
