package com.tudaidai.tuantrip.app;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParserException;

import com.tudaidai.tuantrip.error.TuanTripError;
import com.tudaidai.tuantrip.error.TuanTripException;
import com.tudaidai.tuantrip.http.AbstractHttpApi;
import com.tudaidai.tuantrip.http.HttpApi;
import com.tudaidai.tuantrip.http.MyHttpApi;
import com.tudaidai.tuantrip.parser.AddAddressParser;
import com.tudaidai.tuantrip.parser.AddressListParser;
import com.tudaidai.tuantrip.parser.AirportDetailParser;
import com.tudaidai.tuantrip.parser.AirportResultParser;
import com.tudaidai.tuantrip.parser.BaseParser;
import com.tudaidai.tuantrip.parser.CarParser;
import com.tudaidai.tuantrip.parser.CityParser;
import com.tudaidai.tuantrip.parser.CommentListResultParser;
import com.tudaidai.tuantrip.parser.DingCaiResultParser;
import com.tudaidai.tuantrip.parser.HotelCityParser;
import com.tudaidai.tuantrip.parser.HotelDetailParser;
import com.tudaidai.tuantrip.parser.HotelListParser;
import com.tudaidai.tuantrip.parser.HotelOrderInfoParser;
import com.tudaidai.tuantrip.parser.HotelOrderParser;
import com.tudaidai.tuantrip.parser.HotelOrderResultParser;
import com.tudaidai.tuantrip.parser.HttpResultParser;
import com.tudaidai.tuantrip.parser.LogOutParser;
import com.tudaidai.tuantrip.parser.NearbyResultParser;
import com.tudaidai.tuantrip.parser.OrderInfoParser;
import com.tudaidai.tuantrip.parser.OrderParser;
import com.tudaidai.tuantrip.parser.ProductListParser;
import com.tudaidai.tuantrip.parser.ProductParser;
import com.tudaidai.tuantrip.parser.TicketParser;
import com.tudaidai.tuantrip.parser.UserParser;
import com.tudaidai.tuantrip.parser.VersionParser;
import com.tudaidai.tuantrip.parser.ViewPointDetailParser;
import com.tudaidai.tuantrip.parser.XianluOrderInfoParser;
import com.tudaidai.tuantrip.parser.xml.HBDTParser;
import com.tudaidai.tuantrip.parser.xml.HBSKParser;
import com.tudaidai.tuantrip.parser.xml.WeatherParser;
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

public class TuanTripHttpApi {
	private HttpApi mHttpApi;
	private final DefaultHttpClient mHttpClient;
	private static final String urlHost = "im.tuantrip.com";
	private static final String urlHost1 = "192.168.0.104";

	public TuanTripHttpApi(String paramString1, String paramString2) {
		mHttpClient = AbstractHttpApi.createHttpClient();
		mHttpApi = new MyHttpApi(mHttpClient, paramString2);
	}

	public ProductResult getCurProduct(String s, int i) throws IOException,
			ParseException, TuanTripException, XmlPullParserException {
		HttpGet httpget = null;
		HttpApi httpapi1 = null;
		ProductParser productparser = null;
		String s1 = "http://" + urlHost + "/product.php";

		if (i == 0) {
			HttpApi httpapi = mHttpApi;
			// String s1 = "http://"+urlHost+"/androidphp/1.html";
			NameValuePair anamevaluepair[] = new NameValuePair[1];
			BasicNameValuePair basicnamevaluepair1 = new BasicNameValuePair(
					"city", s);
			anamevaluepair[0] = basicnamevaluepair1;
			httpget = httpapi.createHttpGet(s1, anamevaluepair);
		} else {
			HttpApi httpapi2 = mHttpApi;
			// String s1 = "http://"+urlHost+"/androidphp/2.html";
			NameValuePair anamevaluepair1[] = new NameValuePair[1];
			BasicNameValuePair basicnamevaluepair3 = new BasicNameValuePair(
					"pid", s);
			anamevaluepair1[0] = basicnamevaluepair3;
			httpget = httpapi2.createHttpGet(s1, anamevaluepair1);
		}

		// HttpApi httpapi2 = mHttpApi;
		// //String s2 = "http://"+urlHost+"/androidphp/2.html";
		// String s2 = "http://im.dev.tuantrip.com/product.php";
		// NameValuePair anamevaluepair1[] = new NameValuePair[1];
		// BasicNameValuePair basicnamevaluepair3 = new
		// BasicNameValuePair("pid","26");
		// anamevaluepair1[0] = basicnamevaluepair3;
		// httpget = httpapi2.createHttpGet(s2, anamevaluepair1);

		httpapi1 = mHttpApi;
		productparser = new ProductParser();
		return (ProductResult) httpapi1.doHttpRequest(httpget, productparser);
	}

	public UserResult login(String paramString1, String paramString2,
			String paramString3) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/login.html";
		String url = "http://" + urlHost + "/login.php";
		NameValuePair anamevaluepair[] = new NameValuePair[2];
		anamevaluepair[0] = new BasicNameValuePair("lemail", paramString1);
		anamevaluepair[1] = new BasicNameValuePair("lpwd", paramString2);

		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		UserParser userParser = new UserParser();
		return (UserResult) mHttpApi.doHttpRequest(httpPost, userParser);
	}

	public AddrListResult getAddress() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/addresslist.html";
		String url = "http://" + urlHost + "/addresslist.php";
		NameValuePair anamevaluepair[] = new NameValuePair[0];
		// anamevaluepair[0] = new BasicNameValuePair("action","addresslist");

		HttpGet httpGet = mHttpApi.createHttpGet(url, anamevaluepair);
		AddressListParser addressListParser = new AddressListParser();
		return (AddrListResult) mHttpApi.doHttpRequest(httpGet,
				addressListParser);
	}

	public OrderInfoResult getOrderInfo(String pid) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/buy.html";
		String url = "http://" + urlHost + "/buy.php";
		NameValuePair anamevaluepair[] = new NameValuePair[1];
		anamevaluepair[0] = new BasicNameValuePair("pid", pid);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		OrderInfoParser orderInfoParser = new OrderInfoParser();
		return (OrderInfoResult) mHttpApi.doHttpRequest(httpPost,
				orderInfoParser);
	}

	public HotelOrderInfoResult getHotelOrderInfo(String pid)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/buyhotel302.html";
		String url = "http://" + urlHost + "/buyhotel.php";
		NameValuePair anamevaluepair[] = new NameValuePair[1];
		anamevaluepair[0] = new BasicNameValuePair("pid", pid);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		HotelOrderInfoParser orderInfoParser = new HotelOrderInfoParser();
		return (HotelOrderInfoResult) mHttpApi.doHttpRequest(httpPost,
				orderInfoParser);
	}

	public XianluOrderInfoResult getXianluOrderInfo(String pid)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/buyxianlu302.html";
		String url = "http://" + urlHost + "/buyxianlu.php";
		NameValuePair anamevaluepair[] = new NameValuePair[1];
		anamevaluepair[0] = new BasicNameValuePair("pid", pid);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		XianluOrderInfoParser orderInfoParser = new XianluOrderInfoParser();
		return (XianluOrderInfoResult) mHttpApi.doHttpRequest(httpPost,
				orderInfoParser);
	}

	public LogOutResult logOut() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/logout.html";
		String url = "http://" + urlHost + "/logout.php";
		NameValuePair anamevaluepair[] = new NameValuePair[0];
		// anamevaluepair[0] = new BasicNameValuePair("action","logout");

		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		LogOutParser addressListParser = new LogOutParser();
		return (LogOutResult) mHttpApi.doHttpRequest(httpPost,
				addressListParser);
	}

	public AddAddressResult addAddress(String[] params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/addaddress.html";
		String url = "http://" + urlHost + "/addaddress.php";
		NameValuePair anamevaluepair[] = new NameValuePair[8];
		anamevaluepair[0] = new BasicNameValuePair("province", params[0]);
		anamevaluepair[1] = new BasicNameValuePair("city", params[1]);
		anamevaluepair[2] = new BasicNameValuePair("area", params[2]);
		anamevaluepair[3] = new BasicNameValuePair("house", params[3]);
		anamevaluepair[4] = new BasicNameValuePair("zip", params[4]);
		anamevaluepair[5] = new BasicNameValuePair("phone", params[5]);
		anamevaluepair[6] = new BasicNameValuePair("tel", params[6]);
		anamevaluepair[7] = new BasicNameValuePair("consiName", params[7]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		AddAddressParser addressListParser = new AddAddressParser();
		return (AddAddressResult) mHttpApi.doHttpRequest(httpPost,
				addressListParser);
	}

	public AddAddressResult editAddress(String aid, String[] params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/addaddress.html";
		String url = "http://" + urlHost + "/editaddress.php";
		NameValuePair anamevaluepair[] = new NameValuePair[9];
		anamevaluepair[0] = new BasicNameValuePair("province", params[0]);
		anamevaluepair[1] = new BasicNameValuePair("city", params[1]);
		anamevaluepair[2] = new BasicNameValuePair("area", params[2]);
		anamevaluepair[3] = new BasicNameValuePair("house", params[3]);
		anamevaluepair[4] = new BasicNameValuePair("zip", params[4]);
		anamevaluepair[5] = new BasicNameValuePair("phone", params[5]);
		anamevaluepair[6] = new BasicNameValuePair("tel", params[6]);
		anamevaluepair[7] = new BasicNameValuePair("consiName", params[7]);
		anamevaluepair[8] = new BasicNameValuePair("aid", aid);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		AddAddressParser addressListParser = new AddAddressParser();
		return (AddAddressResult) mHttpApi.doHttpRequest(httpPost,
				addressListParser);
	}

	public BaseResult submitOrder(String[] params) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {

		// String url = "http://"+urlHost+"/androidphp/addaddress.html";
		String url = "http://" + urlHost + "/submitbuy.php";
		NameValuePair anamevaluepair[] = new NameValuePair[11];
		anamevaluepair[0] = new BasicNameValuePair("pid", params[0]);
		anamevaluepair[1] = new BasicNameValuePair("adressid", params[1]);
		anamevaluepair[2] = new BasicNameValuePair("mobile", params[2]);
		anamevaluepair[3] = new BasicNameValuePair("remark", params[3]);
		anamevaluepair[4] = new BasicNameValuePair("realname", params[4]);
		anamevaluepair[5] = new BasicNameValuePair("id_type", params[5]);
		anamevaluepair[6] = new BasicNameValuePair("id_number", params[6]);
		anamevaluepair[7] = new BasicNameValuePair("date", params[7]);
		anamevaluepair[8] = new BasicNameValuePair("daiding", params[8]);
		anamevaluepair[9] = new BasicNameValuePair("quantity", params[9]);
		anamevaluepair[10] = new BasicNameValuePair("boughtlimit", params[10]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		BaseParser baseParser = new BaseParser();
		return (BaseResult) mHttpApi.doHttpRequest(httpPost, baseParser);

	}

	public BaseResult submitXianluOrder(String[] params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {

		// String url = "http://"+urlHost+"/androidphp/addaddress.html";
		String url = "http://" + urlHost + "/submitbuyxianlu.php";
		NameValuePair anamevaluepair[] = new NameValuePair[8];
		anamevaluepair[0] = new BasicNameValuePair("pid", params[0]);
		anamevaluepair[1] = new BasicNameValuePair("xianlu_type", params[1]);
		anamevaluepair[2] = new BasicNameValuePair("mobile", params[2]);
		anamevaluepair[3] = new BasicNameValuePair("remark", params[3]);
		anamevaluepair[4] = new BasicNameValuePair("realname", params[4]);
		anamevaluepair[5] = new BasicNameValuePair("id_type", params[5]);
		anamevaluepair[6] = new BasicNameValuePair("id_number", params[6]);
		anamevaluepair[7] = new BasicNameValuePair("quantity", params[7]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		BaseParser baseParser = new BaseParser();
		return (BaseResult) mHttpApi.doHttpRequest(httpPost, baseParser);

	}

	public BaseResult submitHotelOrder(String[] params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {

		// String url = "http://"+urlHost+"/androidphp/addaddress.html";
		String url = "http://" + urlHost + "/submitbuyhotel.php";
		NameValuePair anamevaluepair[] = new NameValuePair[9];
		anamevaluepair[0] = new BasicNameValuePair("pid", params[0]);
		anamevaluepair[1] = new BasicNameValuePair("datejson", params[1]);
		anamevaluepair[2] = new BasicNameValuePair("mobile", params[2]);
		anamevaluepair[3] = new BasicNameValuePair("remark", params[3]);
		anamevaluepair[4] = new BasicNameValuePair("realname", params[4]);
		anamevaluepair[5] = new BasicNameValuePair("id_type", params[5]);
		anamevaluepair[6] = new BasicNameValuePair("id_number", params[6]);
		anamevaluepair[7] = new BasicNameValuePair("quantity", params[7]);
		anamevaluepair[8] = new BasicNameValuePair("hoteType", params[8]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		BaseParser baseParser = new BaseParser();
		return (BaseResult) mHttpApi.doHttpRequest(httpPost, baseParser);

	}

	public OrderResult getOrders(String option, String curpage, String pagenum)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		String url = "http://" + urlHost + "/orderlist.php";

		// if(curpage.equals("1"))
		// url = "http://"+urlHost+"/androidphp/orderlist.html";
		// else if(curpage.equals("11"))
		// url = "http://"+urlHost+"/androidphp/orderlist1.html";
		// else if(curpage.equals("21"))
		// url = "http://"+urlHost+"/androidphp/orderlist2.html";

		NameValuePair anamevaluepair[] = new NameValuePair[3];
		anamevaluepair[0] = new BasicNameValuePair("type", option);
		anamevaluepair[1] = new BasicNameValuePair("curpage", curpage);
		anamevaluepair[2] = new BasicNameValuePair("pagenum", pagenum);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		OrderParser orderParser = new OrderParser();
		return (OrderResult) mHttpApi.doHttpRequest(httpPost, orderParser);

	}

	public BaseResult submitOrder(String param) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {

		// String url = "http://"+urlHost+"/androidphp/addaddress.html";
		String url = "http://" + urlHost + "/payorder.php";
		NameValuePair anamevaluepair[] = new NameValuePair[1];
		anamevaluepair[0] = new BasicNameValuePair("oid", param);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		BaseParser baseParser = new BaseParser();
		return (BaseResult) mHttpApi.doHttpRequest(httpPost, baseParser);

	}

	public Cities getCities() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/citylist.html";
		String url = "http://" + urlHost + "/citylist.php";
		NameValuePair anamevaluepair[] = new NameValuePair[0];
		HttpGet httpGet = mHttpApi.createHttpGet(url, anamevaluepair);
		CityParser baseParser = new CityParser();
		return (Cities) mHttpApi.doHttpRequest(httpGet, baseParser);
	}

	public Version getVersion() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		String url = "http://" + urlHost + "/code1.6.html";
		NameValuePair anamevaluepair[] = new NameValuePair[0];
		HttpGet httpGet = mHttpApi.createHttpGet(url, anamevaluepair);
		VersionParser versionParser = new VersionParser();
		return (Version) mHttpApi.doHttpRequest(httpGet, versionParser);
	}

	public ProductListResult getProductList(String city)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/productlist.html";
		String url = "http://" + urlHost + "/productlist.php";
		NameValuePair anamevaluepair[] = new NameValuePair[1];
		anamevaluepair[0] = new BasicNameValuePair("city", city);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		ProductListParser productParser = new ProductListParser();
		return (ProductListResult) mHttpApi.doHttpRequest(httpPost,
				productParser);
	}

	public BaseResult updateUser(String[] params) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		String url = "http://" + urlHost + "/androidphp/addaddress.html";
		NameValuePair anamevaluepair[] = new NameValuePair[4];
		anamevaluepair[0] = new BasicNameValuePair("phone", params[0]);
		anamevaluepair[1] = new BasicNameValuePair("oldPwd", params[1]);
		anamevaluepair[2] = new BasicNameValuePair("newPwd", params[2]);
		anamevaluepair[3] = new BasicNameValuePair("reNewPwd", params[3]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		BaseParser baseParser = new BaseParser();
		return (BaseResult) mHttpApi.doHttpRequest(httpPost, baseParser);
	}

	public TicketResult getTickets(String option, String curpage, String pagenum)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		String url = "http://" + urlHost + "/ticketlist.php";

		// if(option.equals("0"))
		// {
		// if(curpage.equals("1"))
		// url = "http://"+urlHost+"/androidphp/ticketlist.html";
		// else if(curpage.equals("11"))
		// url = "http://"+urlHost+"/androidphp/ticketlist1.html";
		// else if(curpage.equals("21"))
		// url = "http://"+urlHost+"/androidphp/ticketlist2.html";
		// }
		// else
		// {
		// url = "http://"+urlHost+"/androidphp/noticketlist.html";
		// }
		NameValuePair anamevaluepair[] = new NameValuePair[3];
		anamevaluepair[0] = new BasicNameValuePair("type", option);
		anamevaluepair[1] = new BasicNameValuePair("curpage", curpage);
		anamevaluepair[2] = new BasicNameValuePair("pagenum", pagenum);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		TicketParser orderParser = new TicketParser();
		return (TicketResult) mHttpApi.doHttpRequest(httpPost, orderParser);
	}

	// ////////////////hotel//////////////////////////////////////
	public HotelListResult getNearbyHotel(String[] option, String curpage,
			String pagenum) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		String url = "http://" + urlHost1
				+ "/androidphp/hotel/nearbyhotellist.html";
		if (curpage.equals("9"))
			url = "http://" + urlHost1
					+ "/androidphp/hotel/nearbyhotellist1.html";

		NameValuePair anamevaluepair[] = new NameValuePair[7];
		anamevaluepair[0] = new BasicNameValuePair("jingdu", option[0]);
		anamevaluepair[1] = new BasicNameValuePair("weidu", option[1]);
		anamevaluepair[2] = new BasicNameValuePair("banjing", option[2]);
		anamevaluepair[3] = new BasicNameValuePair("type", option[3]);
		anamevaluepair[4] = new BasicNameValuePair("city", option[4]);
		anamevaluepair[5] = new BasicNameValuePair("curpage", curpage);
		anamevaluepair[6] = new BasicNameValuePair("pagenum", pagenum);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		HotelListParser productParser = new HotelListParser();
		return (HotelListResult) mHttpApi
				.doHttpRequest(httpPost, productParser);
	}

	public HotelListResult getSearchHotel(String[] option, String curpage,
			String pagenum) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		// String url =
		// "http://"+urlHost1+"/androidphp/hotel/searchhotellist.html";
		//
		// if(curpage.equals("9"))
		// url = "http://"+urlHost1+"/androidphp/hotel/searchhotellist1.html";

		String url = "http://" + urlHost + "/searchhotellist.php";

		NameValuePair anamevaluepair[] = new NameValuePair[option.length + 2];
		anamevaluepair[0] = new BasicNameValuePair("city", option[0]);
		anamevaluepair[1] = new BasicNameValuePair("startdate", option[1]);
		anamevaluepair[2] = new BasicNameValuePair("enddate", option[2]);
		anamevaluepair[3] = new BasicNameValuePair("keyword", option[3]);
		anamevaluepair[4] = new BasicNameValuePair("curpage", curpage);
		anamevaluepair[5] = new BasicNameValuePair("pagenum", pagenum);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		HotelListParser productParser = new HotelListParser();
		return (HotelListResult) mHttpApi
				.doHttpRequest(httpPost, productParser);
	}

	public ProductListResult getHotelTuanProductList()
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url = "http://"+urlHost+"/androidphp/productlist.html";
		String url = "http://" + urlHost1
				+ "/androidphp/hotel/tuanhotellist.html";
		NameValuePair anamevaluepair[] = new NameValuePair[0];
		HttpGet httpGet = mHttpApi.createHttpGet(url, anamevaluepair);
		ProductListParser productParser = new ProductListParser();
		return (ProductListResult) mHttpApi.doHttpRequest(httpGet,
				productParser);
	}

	public HotelDetailResult getHotelDetailResult(String... params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url = "http://"+urlHost1+"/androidphp/hotel/hoteldetail.html";
		String url = "http://" + urlHost + "/hoteldetail.php";

		NameValuePair anamevaluepair[] = new NameValuePair[3];
		anamevaluepair[0] = new BasicNameValuePair("hid", params[0]);
		anamevaluepair[1] = new BasicNameValuePair("startdate", params[1]);
		anamevaluepair[2] = new BasicNameValuePair("enddate", params[2]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		HotelDetailParser hotelDetailParser = new HotelDetailParser();
		return (HotelDetailResult) mHttpApi.doHttpRequest(httpPost,
				hotelDetailParser);
	}

	public HttpResult submitComment(Object... objects)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		byte[] b = (byte[]) objects[0];
		// String url =
		// "http://"+urlHost1+"/androidphp/hotel/submitcomment.php";
		String url = "http://" + urlHost + "/submitcomment.php";
		HttpPost httppost = new HttpPost(url);
		if (b != null) {
			ByteArrayEntity byteArrayEntity = new ByteArrayEntity(b);
			httppost.setEntity(byteArrayEntity);
		}
		httppost.addHeader("lbsid", (String) objects[1]);
		httppost.addHeader("userid", (String) objects[2]);
		httppost.addHeader("content",
				URLEncoder.encode((String) objects[3], "utf-8"));

		httppost.addHeader("ishavepic", (String) objects[4]);
		HttpResultParser httpResultParser = new HttpResultParser();
		return (HttpResult) mHttpApi.doHttpRequest(httppost, httpResultParser);
	}

	public CommentListResult getCommentList(String... parms)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url = "http://"+urlHost1+"/androidphp/hotel/commentlist.html";
		String url = "http://" + urlHost + "/commentlist.php";

		NameValuePair anamevaluepair[] = new NameValuePair[parms.length];
		anamevaluepair[0] = new BasicNameValuePair("lbsid", parms[0]);
		anamevaluepair[1] = new BasicNameValuePair("curpage", parms[1]);
		anamevaluepair[2] = new BasicNameValuePair("pagenum", parms[2]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		CommentListResultParser listResultParser = new CommentListResultParser();
		return (CommentListResult) mHttpApi.doHttpRequest(httpPost,
				listResultParser);
	}

	public DingCaiResult getDingCaiResult(String... parms)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url =
		// "http://"+urlHost1+"/androidphp/hotel/dingcairesult.html";
		String url = "http://" + urlHost + "/dingcai.php";

		NameValuePair anamevaluepair[] = new NameValuePair[parms.length];
		anamevaluepair[0] = new BasicNameValuePair("cid", parms[0]);
		anamevaluepair[1] = new BasicNameValuePair("uid", parms[1]);
		anamevaluepair[2] = new BasicNameValuePair("type", parms[2]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		DingCaiResultParser listResultParser = new DingCaiResultParser();
		return (DingCaiResult) mHttpApi.doHttpRequest(httpPost,
				listResultParser);
	}

	public HotelOrderResult getSHotelOrderInfo(String... parms)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url =
		// "http://"+urlHost1+"/androidphp/hotel/hotelorderresult.html";
		String url = "http://" + urlHost + "/hotelorderresult.php";
		NameValuePair anamevaluepair[] = new NameValuePair[parms.length];
		anamevaluepair[0] = new BasicNameValuePair("nightid", parms[0]);
		anamevaluepair[1] = new BasicNameValuePair("startdate", parms[1]);
		anamevaluepair[2] = new BasicNameValuePair("enddate", parms[2]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		HotelOrderResultParser listResultParser = new HotelOrderResultParser();
		return (HotelOrderResult) mHttpApi.doHttpRequest(httpPost,
				listResultParser);
	}

	public BaseResult submitSHotelOrder(String[] params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {

		// String url =
		// "http://"+urlHost1+"/androidphp/hotel/submithotelorder.html";
		String url = "http://" + urlHost + "/submithotelorder.php";
		NameValuePair anamevaluepair[] = new NameValuePair[16];
		anamevaluepair[0] = new BasicNameValuePair("check_city", params[0]);
		anamevaluepair[1] = new BasicNameValuePair("check_name", params[1]);
		anamevaluepair[2] = new BasicNameValuePair("contact", params[2]);
		anamevaluepair[3] = new BasicNameValuePair("email", params[3]);
		anamevaluepair[4] = new BasicNameValuePair("first_time", params[4]);
		anamevaluepair[5] = new BasicNameValuePair("last_time", params[5]);
		anamevaluepair[6] = new BasicNameValuePair("id_type", params[6]);
		anamevaluepair[7] = new BasicNameValuePair("id_num", params[7]);
		anamevaluepair[8] = new BasicNameValuePair("fangxing", params[8]);
		anamevaluepair[9] = new BasicNameValuePair("indate", params[9]);
		anamevaluepair[10] = new BasicNameValuePair("leftdate", params[10]);
		anamevaluepair[11] = new BasicNameValuePair("message", params[11]);
		anamevaluepair[12] = new BasicNameValuePair("mobile", params[12]);
		anamevaluepair[13] = new BasicNameValuePair("night_id", params[13]);
		anamevaluepair[14] = new BasicNameValuePair("num", params[14]);
		anamevaluepair[15] = new BasicNameValuePair("phone", params[15]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		BaseParser baseParser = new BaseParser();
		return (BaseResult) mHttpApi.doHttpRequest(httpPost, baseParser);

	}

	public NearbyResult getNearby(String[] option, String curpage,
			String pagenum) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		// String url = "http://"+urlHost1+"/androidphp/hotel/nearby.html";
		// if(curpage.equals("9"))
		// url = "http://"+urlHost1+"/androidphp/hotel/nearby1.html";

		String url = "http://" + urlHost + "/nearby.php";

		NameValuePair anamevaluepair[] = new NameValuePair[7];
		anamevaluepair[0] = new BasicNameValuePair("jingdu", option[0]);
		anamevaluepair[1] = new BasicNameValuePair("weidu", option[1]);
		anamevaluepair[2] = new BasicNameValuePair("banjing", option[2]);
		anamevaluepair[3] = new BasicNameValuePair("type", option[3]);
		anamevaluepair[4] = new BasicNameValuePair("city", option[4]);
		anamevaluepair[5] = new BasicNameValuePair("curpage", curpage);
		anamevaluepair[6] = new BasicNameValuePair("pagenum", pagenum);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		NearbyResultParser productParser = new NearbyResultParser();
		return (NearbyResult) mHttpApi.doHttpRequest(httpPost, productParser);
	}

	public NearbyResult getNearbySearchInfo(String[] option, String curpage,
			String pagenum) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		// String url = "http://"+urlHost1+"/androidphp/hotel/nearby.html";
		// if(curpage.equals("9"))
		// url = "http://"+urlHost1+"/androidphp/hotel/nearby1.html";

		String url = "http://" + urlHost + "/searchlbs.php";

		NameValuePair anamevaluepair[] = new NameValuePair[5];
		anamevaluepair[0] = new BasicNameValuePair("keyword", option[0]);
		anamevaluepair[1] = new BasicNameValuePair("type", option[1]);
		anamevaluepair[2] = new BasicNameValuePair("city", option[2]);
		anamevaluepair[3] = new BasicNameValuePair("curpage", curpage);
		anamevaluepair[4] = new BasicNameValuePair("pagenum", pagenum);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		NearbyResultParser productParser = new NearbyResultParser();
		return (NearbyResult) mHttpApi.doHttpRequest(httpPost, productParser);
	}

	public ViewpointDetailResult getViewPointDetailResult(String... params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		// String url =
		// "http://"+urlHost1+"/androidphp/hotel/viewpointdetail.html";
		String url = "http://" + urlHost + "/viewpointdetail.php";

		NameValuePair anamevaluepair[] = new NameValuePair[1];
		anamevaluepair[0] = new BasicNameValuePair("vid", params[0]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		ViewPointDetailParser vDetailParser = new ViewPointDetailParser();
		return (ViewpointDetailResult) mHttpApi.doHttpRequest(httpPost,
				vDetailParser);

	}

	public AirportResult getAirportList(String param) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		// String url = "http://"+urlHost1+"/androidphp/hotel/airport.html";
		String url = "http://" + urlHost + "/airportlist.php";

		if (param == null) {

			NameValuePair anamevaluepair[] = new NameValuePair[0];
			HttpGet httpGet = mHttpApi.createHttpGet(url, anamevaluepair);
			AirportResultParser aParser = new AirportResultParser();
			return (AirportResult) mHttpApi.doHttpRequest(httpGet, aParser);
		} else {

			NameValuePair anamevaluepair[] = new NameValuePair[1];
			anamevaluepair[0] = new BasicNameValuePair("city", param);
			HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
			AirportResultParser aParser = new AirportResultParser();
			return (AirportResult) mHttpApi.doHttpRequest(httpPost, aParser);
		}

	}

	public AirportDetailResult getAirportDetailResult(String... params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {

		// String url =
		// "http://"+urlHost1+"/androidphp/hotel/airportdetail.html";
		String url = "http://" + urlHost + "/airportdetail.php";

		NameValuePair anamevaluepair[] = new NameValuePair[1];
		anamevaluepair[0] = new BasicNameValuePair("aid", params[0]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		AirportDetailParser vDetailParser = new AirportDetailParser();
		return (AirportDetailResult) mHttpApi.doHttpRequest(httpPost,
				vDetailParser);

	}

	public WeatherResult getWeatherResult(String... params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		String url = "http://www.google.com/ig/api";
		NameValuePair anamevaluepair[] = new NameValuePair[2];
		anamevaluepair[0] = new BasicNameValuePair("hl", "zh-cn");
		anamevaluepair[1] = new BasicNameValuePair("weather", ",,," + params[1]
				+ "," + params[0] + "");
		HttpGet httpGet = mHttpApi.createHttpGet(url, anamevaluepair);
		WeatherParser wParser = new WeatherParser();
		return (WeatherResult) mHttpApi.doHttpRequest(httpGet, wParser,
				AbstractHttpApi.HTML, "gb2312");
	}

	public HBSKResult getHBSKResult(String... params) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		String url1 = "http://wap.feeyo.com/flight/loading.asp?org="
				+ URLEncoder.encode(params[0], "utf-8") + "&dst="
				+ URLEncoder.encode(params[1], "utf-8") + "";
		NameValuePair anamevaluepair1[] = new NameValuePair[0];
		HttpGet httpGet1 = mHttpApi.createHttpGet(url1, anamevaluepair1);
		HttpResponse response = mHttpApi.executeHttpRequest(httpGet1);
		Header[] urls = response.getHeaders("Location");
		if (urls == null) {
			throw new TuanTripException("无相关信息");
		}

		String url = urls[0].getValue();
		NameValuePair anamevaluepair[] = new NameValuePair[0];
		HttpGet httpGet = mHttpApi.createHttpGet(url, anamevaluepair);
		HBSKParser wParser = new HBSKParser();
		return (HBSKResult) mHttpApi.doHttpRequest(httpGet, wParser,
				AbstractHttpApi.HTML, "utf-8");
	}

	public HBSKResult getHBDTResult(String... params) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		String url = "http://m.veryzhun.com/flightstatus/search.asp";
		NameValuePair anamevaluepair[] = new NameValuePair[2];
		anamevaluepair[0] = new BasicNameValuePair("fafrom", params[0]);
		anamevaluepair[1] = new BasicNameValuePair("fato", params[1]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		HBDTParser wParser = new HBDTParser();
		return (HBSKResult) mHttpApi.doHttpRequest(httpPost, wParser,
				AbstractHttpApi.HTML, "utf-8");
	}

	public HBSKResult getHBDTResult1(String... params)
			throws TuanTripException, TuanTripError, IOException,
			ParseException, XmlPullParserException {
		String url = "http://m.veryzhun.com/flightstatus/searchnum.asp";
		NameValuePair anamevaluepair[] = new NameValuePair[1];
		anamevaluepair[0] = new BasicNameValuePair("flightNum", params[0]);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		HBDTParser wParser = new HBDTParser();
		return (HBSKResult) mHttpApi.doHttpRequest(httpPost, wParser,
				AbstractHttpApi.HTML, "utf-8");
	}

	public HotelOrderListResult getHotelOrders(String option, String curpage,
			String pagenum) throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		// String url =
		// "http://"+urlHost1+"/androidphp/hotel/hotelorderlist.html";
		String url = "http://" + urlHost + "/hotelorderlist.php";

		NameValuePair anamevaluepair[] = new NameValuePair[3];
		anamevaluepair[0] = new BasicNameValuePair("type", option);
		anamevaluepair[1] = new BasicNameValuePair("curpage", curpage);
		anamevaluepair[2] = new BasicNameValuePair("pagenum", pagenum);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		HotelOrderParser orderParser = new HotelOrderParser();
		return (HotelOrderListResult) mHttpApi.doHttpRequest(httpPost,
				orderParser);
	}

	public CarResult getCarList(String param) throws TuanTripException,
			TuanTripError, IOException, ParseException, XmlPullParserException {
		// String url = "http://"+urlHost1+"/androidphp/hotel/carlist.html";
		String url = "http://" + urlHost + "/carlist.php";

		NameValuePair anamevaluepair[] = new NameValuePair[1];
		anamevaluepair[0] = new BasicNameValuePair("city", param);
		HttpPost httpPost = mHttpApi.createHttpPost(url, anamevaluepair);
		CarParser aParser = new CarParser();
		return (CarResult) mHttpApi.doHttpRequest(httpPost, aParser);
	}

	public Cities getHotelCities() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		String url = "http://" + urlHost + "/hotelcitylist.php";
		NameValuePair anamevaluepair[] = new NameValuePair[0];
		HttpGet httpGet = mHttpApi.createHttpGet(url, anamevaluepair);
		HotelCityParser baseParser = new HotelCityParser();
		return (Cities) mHttpApi.doHttpRequest(httpGet, baseParser);
	}

	public Cities getAirportCities() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		String url = "http://" + urlHost + "/airportcitylist.php";
		NameValuePair anamevaluepair[] = new NameValuePair[0];
		HttpGet httpGet = mHttpApi.createHttpGet(url, anamevaluepair);
		HotelCityParser baseParser = new HotelCityParser();
		return (Cities) mHttpApi.doHttpRequest(httpGet, baseParser);
	}

	public Cities getCarCities() throws TuanTripException, TuanTripError,
			IOException, ParseException, XmlPullParserException {
		String url = "http://" + urlHost + "/carcitylist.php";
		NameValuePair anamevaluepair[] = new NameValuePair[0];
		HttpGet httpGet = mHttpApi.createHttpGet(url, anamevaluepair);
		HotelCityParser baseParser = new HotelCityParser();
		return (Cities) mHttpApi.doHttpRequest(httpGet, baseParser);
	}
}