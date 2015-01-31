package com.tudaidai.tuantrip.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.tudaidai.tuantrip.util.ParcelUtils;

public class HotelOrder implements TuanTripType, Parcelable {
	private String oid;
	private String name;
	private String cometime;
	private String detail;
	public static final Parcelable.Creator<HotelOrder> CREATOR = new Parcelable.Creator<HotelOrder>() {
		public HotelOrder createFromParcel(Parcel in) {
			return new HotelOrder(in);
		}

		@Override
		public HotelOrder[] newArray(int size) {
			return new HotelOrder[size];
		}
	};

	private HotelOrder(Parcel in) {
		oid = ParcelUtils.readStringFromParcel(in);
		name = ParcelUtils.readStringFromParcel(in);
		cometime = ParcelUtils.readStringFromParcel(in);
		detail = ParcelUtils.readStringFromParcel(in);
	}

	public HotelOrder() {
		// TODO Auto-generated constructor stub
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCometime() {
		return cometime;
	}

	public void setCometime(String cometime) {
		this.cometime = cometime;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		ParcelUtils.writeStringToParcel(out, oid);
		ParcelUtils.writeStringToParcel(out, name);
		ParcelUtils.writeStringToParcel(out, cometime);
		ParcelUtils.writeStringToParcel(out, detail);

	}

}
