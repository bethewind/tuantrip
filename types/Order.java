package com.tudaidai.tuantrip.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.tudaidai.tuantrip.util.ParcelUtils;

public class Order implements TuanTripType, Parcelable {
	private String pid;
	private String oid;
	private String shortTitle;
	private String code;
	private String cityName;
	private String quantity;
	private String price;
	private String orderTime;
	private String orderStatus;
	private String travelDate;

	public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
		public Order createFromParcel(Parcel in) {
			return new Order(in);
		}

		@Override
		public Order[] newArray(int size) {
			return new Order[size];
		}
	};

	public Order() {

	}

	private Order(Parcel in) {
		pid = ParcelUtils.readStringFromParcel(in);
		oid = ParcelUtils.readStringFromParcel(in);
		shortTitle = ParcelUtils.readStringFromParcel(in);
		code = ParcelUtils.readStringFromParcel(in);
		cityName = ParcelUtils.readStringFromParcel(in);
		quantity = ParcelUtils.readStringFromParcel(in);
		price = ParcelUtils.readStringFromParcel(in);
		orderTime = ParcelUtils.readStringFromParcel(in);
		orderStatus = ParcelUtils.readStringFromParcel(in);
		travelDate = ParcelUtils.readStringFromParcel(in);
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(String travelDate) {
		this.travelDate = travelDate;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		ParcelUtils.writeStringToParcel(out, pid);
		ParcelUtils.writeStringToParcel(out, oid);
		ParcelUtils.writeStringToParcel(out, shortTitle);
		ParcelUtils.writeStringToParcel(out, code);
		ParcelUtils.writeStringToParcel(out, cityName);
		ParcelUtils.writeStringToParcel(out, quantity);
		ParcelUtils.writeStringToParcel(out, price);
		ParcelUtils.writeStringToParcel(out, orderTime);
		ParcelUtils.writeStringToParcel(out, orderStatus);
		ParcelUtils.writeStringToParcel(out, travelDate);

	}

}
