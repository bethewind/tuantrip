package com.tudaidai.tuantrip.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.tudaidai.tuantrip.util.ParcelUtils;

public class Nearby implements TuanTripType, Parcelable {
	private int lid;
	private int fid;
	private String name = "";
	private String imageUrl = "";
	private String type = "";
	private String address = "";
	private double jingdu = 0;
	private double weidu = 0;
	public static final Parcelable.Creator<Nearby> CREATOR = new Parcelable.Creator<Nearby>() {
		public Nearby createFromParcel(Parcel in) {
			return new Nearby(in);
		}

		@Override
		public Nearby[] newArray(int size) {
			return new Nearby[size];
		}
	};

	public Nearby() {

	}

	private Nearby(Parcel in) {
		lid = in.readInt();
		fid = in.readInt();
		name = ParcelUtils.readStringFromParcel(in);
		imageUrl = ParcelUtils.readStringFromParcel(in);
		type = ParcelUtils.readStringFromParcel(in);
		address = ParcelUtils.readStringFromParcel(in);
		jingdu = in.readDouble();
		weidu = in.readDouble();
	}

	public int getLid() {
		return lid;
	}

	public void setLid(int lid) {
		this.lid = lid;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(lid);
		out.writeInt(fid);
		ParcelUtils.writeStringToParcel(out, name);
		ParcelUtils.writeStringToParcel(out, imageUrl);
		ParcelUtils.writeStringToParcel(out, type);
		ParcelUtils.writeStringToParcel(out, address);
		out.writeDouble(jingdu);
		out.writeDouble(weidu);

	}
}
