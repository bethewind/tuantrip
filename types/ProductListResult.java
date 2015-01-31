package com.tudaidai.tuantrip.types;

public class ProductListResult implements TuanTripType {
	public HttpResult mHttpResult = new HttpResult();
	public Group<ProductShort> mProductList = new Group<ProductShort>();

	public ProductListResult() {
	}
}