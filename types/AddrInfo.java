package com.tudaidai.tuantrip.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.tudaidai.tuantrip.util.ParcelUtils;

public class AddrInfo implements TuanTripType, Parcelable {
	private int aid;
	private String province;
	private String city;
	private String area;
	private String house;
	private String consiName;
	private String phone;
	private String tel;
	private String zip;
	public static final Parcelable.Creator<AddrInfo> CREATOR = new Parcelable.Creator<AddrInfo>() {
		public AddrInfo createFromParcel(Parcel in) {
			return new AddrInfo(in);
		}

		@Override
		public AddrInfo[] newArray(int size) {
			return new AddrInfo[size];
		}
	};

	public AddrInfo() {

	}

	private AddrInfo(Parcel in) {
		aid = in.readInt();
		province = ParcelUtils.readStringFromParcel(in);
		city = ParcelUtils.readStringFromParcel(in);
		area = ParcelUtils.readStringFromParcel(in);
		house = ParcelUtils.readStringFromParcel(in);
		consiName = ParcelUtils.readStringFromParcel(in);
		phone = ParcelUtils.readStringFromParcel(in);
		tel = ParcelUtils.readStringFromParcel(in);
		zip = ParcelUtils.readStringFromParcel(in);
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getConsiName() {
		return consiName;
	}

	public void setConsiName(String consiName) {
		this.consiName = consiName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(aid);
		ParcelUtils.writeStringToParcel(out, province);
		ParcelUtils.writeStringToParcel(out, city);
		ParcelUtils.writeStringToParcel(out, area);
		ParcelUtils.writeStringToParcel(out, house);
		ParcelUtils.writeStringToParcel(out, consiName);
		ParcelUtils.writeStringToParcel(out, phone);
		ParcelUtils.writeStringToParcel(out, tel);
		ParcelUtils.writeStringToParcel(out, zip);
	}

}
