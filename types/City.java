package com.tudaidai.tuantrip.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.tudaidai.tuantrip.util.ParcelUtils;

public class City implements TuanTripType, Parcelable {
	private String name;
	private String cnName;
	public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
		public City createFromParcel(Parcel in) {
			return new City(in);
		}

		@Override
		public City[] newArray(int size) {
			return new City[size];
		}
	};

	public City() {
	}

	private City(Parcel in) {
		name = ParcelUtils.readStringFromParcel(in);
		cnName = ParcelUtils.readStringFromParcel(in);
	}

	public int describeContents() {
		return 0;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCnName() {
		return this.cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public void writeToParcel(Parcel out, int paramInt) {
		ParcelUtils.writeStringToParcel(out, name);
		ParcelUtils.writeStringToParcel(out, cnName);
	}
}