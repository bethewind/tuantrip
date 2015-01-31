package com.tudaidai.tuantrip.app;

import java.io.IOException;

import org.apache.http.ParseException;
import org.xmlpull.v1.XmlPullParserException;

import com.tudaidai.tuantrip.error.TuanTripError;
import com.tudaidai.tuantrip.error.TuanTripException;
import com.tudaidai.tuantrip.types.AddAddressResult;
import com.tudaidai.tuantrip.types.AddrListResult;
import com.tudaidai.tuantrip.types.AirportDetailResult;
import com.tudaidai.tuantrip.types.AirportResult;
import com.tudaidai.tuantrip.types.BaseResult;
import com.tudaidai.tuantrip.types.CarResult;
import com.tudaidai.tuantrip.types.Cities;
import com.tudaidai.tuantrip.types.CommentListResult;
import com.tudaidai.tuantrip.types.DingCaiResult;
import com.tudaidai.tuantrip.types.HBSKResult;
import com.tudaidai.tuantrip.types.HotelDetailResult;
import com.tudaidai.tuantrip.types.HotelListResult;
import com.tudaidai.tuantrip.types.HotelOrderInfoResult;
import com.tudaidai.tuantrip.types.HotelOrderListResult;
import com.tudaidai.tuantrip.types.HotelOrderResult;
import com.tudaidai.tuantrip.types.HttpResult;
import com.tudaidai.tuantrip.types.LogOutResult;
import com.tudaidai.tuantrip.types.NearbyResult;
import com.tudaidai.tuantrip.types.OrderInfoResult;
import com.tudaidai.tuantrip.types.OrderResult;
import com.tudaidai.tuantrip.types.ProductListResult;
import com.tudaidai.tuantrip.types.ProductResult;
import com.tudaidai.tuantrip.types.TicketResult;
import com.tudaidai.tuantrip.types.UserResult;
import com.tudaidai.tuantrip.types.Version;
import com.tudaidai.tuantrip.types.ViewpointDetailResult;
import com.tudaidai.tuantrip.types.WeatherResult;
import com.tudaidai.tuantrip.types.XianluOrderInfoResult;

public class TuanTripApp {
	private TuanTripHttpApi mHttpApi;

	public TuanTripApp(TuanTripHttpApi paramAbtuanHttpApi) {
		this.mHttpApi = paramAbtuanHttpApi;

	}

	public ProductResult getCurProduct(String city, int type)
			throws IOException, ParseException, TuanTripException,
			XmlPullParserException {
		return this.mHttpApi.getCurProduct(city, type);
	}

	public UserResult login(String paramString1, String paramString2,
			String paramString3) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.login(paramString1, paramString2, paramString3);
	}

	public AddrListResult getAddress() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getAddress();
	}

	public OrderInfoResult getOrderInfo(String pid) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getOrderInfo(pid);
	}

	public HotelOrderInfoResult getHotelOrderInfo(String pid)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getHotelOrderInfo(pid);
	}

	public XianluOrderInfoResult getXianluOrderInfo(String pid)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getXianluOrderInfo(pid);
	}

	public LogOutResult logOut() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.logOut();
	}

	public AddAddressResult addAddress(String[] params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.addAddress(params);
	}

	public AddAddressResult editAddress(String aid, String[] params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.editAddress(aid, params);
	}

	public BaseResult submitOrder(String[] params) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.submitOrder(params);
	}

	public BaseResult submitXianluOrder(String[] params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.submitXianluOrder(params);
	}

	public BaseResult submitHotelOrder(String[] params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.submitHotelOrder(params);
	}

	public OrderResult getOrders(String option, String curpage, String pagenum)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getOrders(option, curpage, pagenum);
	}

	public BaseResult submitOrder(String param) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.submitOrder(param);
	}

	public Cities getCities() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getCities();
	}

	public Version getVersion() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getVersion();
	}

	public ProductListResult getProductList(String city)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getProductList(city);
	}

	public BaseResult updateUser(String[] params) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.updateUser(params);
	}

	public TicketResult getTickets(String option, String curpage, String pagenum)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getTickets(option, curpage, pagenum);
	}

	// //////////////hotels////////
	public HotelListResult getNearbyHotel(String[] option, String curpage,
			String pagenum) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getNearbyHotel(option, curpage, pagenum);
	}

	public HotelListResult getSearchHotel(String[] option, String curpage,
			String pagenum) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getSearchHotel(option, curpage, pagenum);
	}

	public ProductListResult getHotelTuanProductList()
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getHotelTuanProductList();
	}

	public HotelDetailResult getHotelDetailResult(String... params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getHotelDetailResult(params);
	}

	public HttpResult submitComment(Object... objects)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {

		return this.mHttpApi.submitComment(objects);
	}

	public CommentListResult getCommentList(String... parms)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getCommentList(parms);
	}

	public DingCaiResult getDingCaiResult(String... parms)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getDingCaiResult(parms);
	}

	public HotelOrderResult getSHotelOrderInfo(String... parms)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getSHotelOrderInfo(parms);
	}

	public BaseResult submitSHotelOrder(String[] params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.submitSHotelOrder(params);
	}

	public NearbyResult getNearby(String[] option, String curpage,
			String pagenum) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getNearby(option, curpage, pagenum);
	}

	public NearbyResult getNearbySearchInfo(String[] option, String curpage,
			String pagenum) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getNearbySearchInfo(option, curpage, pagenum);
	}

	public ViewpointDetailResult getViewPointDetailResult(String... params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getViewPointDetailResult(params);
	}

	public AirportResult getAirportList(String param) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getAirportList(param);
	}

	public AirportDetailResult getAirportDetailResult(String... params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getAirportDetailResult(params);
	}

	public WeatherResult getWeatherResult(String... params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getWeatherResult(params);
	}

	public HBSKResult getHBSKResult(String... params) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getHBSKResult(params);
	}

	public HBSKResult getHBDTResult(String... params) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getHBDTResult(params);
	}

	public HBSKResult getHBDTResult1(String... params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		return this.mHttpApi.getHBDTResult1(params);
	}

	public HotelOrderListResult getHotelOrders(String option, String curpage,
			String pagenum) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getHotelOrders(option, curpage, pagenum);
	}

	public CarResult getCarList(String param) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getCarList(param);
	}

	public Cities getHotelCities() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getHotelCities();
	}

	public Cities getAirportCities() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getAirportCities();
	}

	public Cities getCarCities() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		return this.mHttpApi.getCarCities();
	}

}