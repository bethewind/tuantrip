package com.tudaidai.tuantrip.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.tudaidai.tuantrip.util.ParcelUtils;

public class Ticket implements TuanTripType, Parcelable {
	private String tid;
	private String title;
	private String date;
	private String ticketStatus;
	private String code;
	private String pwd;
	private String travelDate;
	public static final Parcelable.Creator<Ticket> CREATOR = new Parcelable.Creator<Ticket>() {
		public Ticket createFromParcel(Parcel in) {
			return new Ticket(in);
		}

		@Override
		public Ticket[] newArray(int size) {
			return new Ticket[size];
		}
	};

	public Ticket() {

	}

	private Ticket(Parcel in) {
		tid = ParcelUtils.readStringFromParcel(in);
		title = ParcelUtils.readStringFromParcel(in);
		date = ParcelUtils.readStringFromParcel(in);
		ticketStatus = ParcelUtils.readStringFromParcel(in);
		code = ParcelUtils.readStringFromParcel(in);
		pwd = ParcelUtils.readStringFromParcel(in);
		travelDate = ParcelUtils.readStringFromParcel(in);
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
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
		ParcelUtils.writeStringToParcel(out, tid);
		ParcelUtils.writeStringToParcel(out, title);
		ParcelUtils.writeStringToParcel(out, date);
		ParcelUtils.writeStringToParcel(out, ticketStatus);
		ParcelUtils.writeStringToParcel(out, code);
		ParcelUtils.writeStringToParcel(out, pwd);
		ParcelUtils.writeStringToParcel(out, travelDate);

	}

}
