package com.tudaidai.tuantrip.types;

public class Product implements TuanTripType {
	private int pid = -1;
	private String title;
	private String url;
	private String imageUrl;
	private String cityName;
	private double priceOriginal;
	private double price;
	private double reBate; // 折扣
	private int startTime;
	private int endTime;
	private int timeLeft;
	private int bought;
	private int minBought;
	private int boughtLimit;
	private String state;
	private int teamType;
	private int sucessTime;
	private String tip;
	private String description;
	private String customSay;
	private String weSay;
	private Group<Shop> shops;
	public static final String NONE = "none";
	public static final String SUCCESS = "success";
	public static final String SOLDOUT = "soldout";
	public static final String FAILURE = "failure";
	public static final String END = "end";

	public Product() {
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public double getPriceOriginal() {
		return priceOriginal;
	}

	public void setPriceOriginal(double priceOriginal) {
		this.priceOriginal = priceOriginal;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getReBate() {
		return reBate;
	}

	public void setReBate(double reBate) {
		this.reBate = reBate;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getBought() {
		return bought;
	}

	public void setBought(int bought) {
		this.bought = bought;
	}

	public int getMinBought() {
		return minBought;
	}

	public void setMinBought(int minBought) {
		this.minBought = minBought;
	}

	public int getBoughtLimit() {
		return boughtLimit;
	}

	public void setBoughtLimit(int boughtLimit) {
		this.boughtLimit = boughtLimit;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getTeamType() {
		return teamType;
	}

	public void setTeamType(int teamType) {
		this.teamType = teamType;
	}

	public int getSucessTime() {
		return sucessTime;
	}

	public void setSucessTime(int sucessTime) {
		this.sucessTime = sucessTime;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomSay() {
		return customSay;
	}

	public void setCustomSay(String customSay) {
		this.customSay = customSay;
	}

	public String getWeSay() {
		return weSay;
	}

	public void setWeSay(String weSay) {
		this.weSay = weSay;
	}

	public Group<Shop> getShops() {
		return shops;
	}

	public void setShops(Group<Shop> shops) {
		this.shops = shops;
	}
}