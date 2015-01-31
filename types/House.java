package com.tudaidai.tuantrip.types;

public class House implements TuanTripType {
	private String typeId;
	private String typeName;
	private Group<DatePrice> datePrice;

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setDatePrice(Group<DatePrice> datePrice) {
		this.datePrice = datePrice;
	}

	public Group<DatePrice> getDatePrice() {
		return datePrice;
	}
}
