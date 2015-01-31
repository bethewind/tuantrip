package com.tudaidai.tuantrip.parser.xml;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.error.TuanTripException;
import com.tudaidai.tuantrip.parser.AbstractParser;
import com.tudaidai.tuantrip.types.CurrentWeather;
import com.tudaidai.tuantrip.types.ForecastWeather;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.WeatherResult;

//http://www.google.com/ig/api?hl=zh-cn&weather=%2C%2C%2C40075391%2C116592135

public class WeatherParser extends AbstractParser<WeatherResult> {

	private static final String TAG = "WeatherParser";
	private static final String GOOGLEURL = "http://www.google.com";
	private static final String FORECAST_INFORMATION = "forecast_information";
	private static final String CURRENT_CONDITIONS = "current_conditions";
	private static final String FORECAST_CONDITIONS = "forecast_conditions";
	private static final String PROBLEM_CAUSE = "problem_cause";

	private static final String CITY = "city";
	private static final String FORECAST_DATE = "forecast_date";
	private static final String CONDITION = "condition";
	private static final String TEMP_F = "temp_f";
	private static final String TEMP_C = "temp_c";
	private static final String HUMIDITY = "humidity";
	private static final String ICON = "icon";
	private static final String WIND_CONDITION = "wind_condition";

	private static final String DAY_OF_WEEK = "day_of_week";
	private static final String LOW = "low";
	private static final String HIGH = "high";

	// jdom
	// @Override
	// public WeatherResult parse(String xml) throws Exception
	// {
	//
	// WeatherResult aListResult = new WeatherResult();
	//
	// SAXBuilder builder = new SAXBuilder();
	// InputSource source = new InputSource(new StringReader(xml));
	// //source.setEncoding("utf-8");
	// Document doc = builder.build(source);
	// Element root = doc.getRootElement();
	// if(root.getChild("weather")!=null&&root.getChild("weather").getChild("current_conditions")!=null)
	// {
	// aListResult.mHttpResult.setStat(TuanTripSettings.POSTSUCCESS);
	//
	// Element currentWether =
	// root.getChild("weather").getChild("current_conditions");
	// if(currentWether.getChild("condition")!=null&&currentWether.getChild("condition").getAttributeValue("data")!=null)
	// {
	// aListResult.currentWeather.setCondition(currentWether.getChild("condition").getAttributeValue("data"));
	// }
	// if(currentWether.getChild("temp_c")!=null&&currentWether.getChild("temp_c").getAttributeValue("data")!=null)
	// {
	// aListResult.currentWeather.setTemp_c(currentWether.getChild("temp_c").getAttributeValue("data")+"℃");
	// }
	// if(currentWether.getChild("humidity")!=null&&currentWether.getChild("humidity").getAttributeValue("data")!=null)
	// {
	// aListResult.currentWeather.setHumidity(currentWether.getChild("humidity").getAttributeValue("data"));
	// }
	// if(currentWether.getChild("icon")!=null&&currentWether.getChild("icon").getAttributeValue("data")!=null)
	// {
	// aListResult.currentWeather.setIcon("http://www.google.com"+currentWether.getChild("icon").getAttributeValue("data"));
	// }
	// if(currentWether.getChild("wind_condition")!=null&&currentWether.getChild("wind_condition").getAttributeValue("data")!=null)
	// {
	// aListResult.currentWeather.setWind_condition(currentWether.getChild("wind_condition").getAttributeValue("data"));
	// }
	//
	// List allChildren = root.getChild("weather").getChildren();
	// for(int i=2;i<allChildren.size();i++)
	// {
	// ForecastWeather fWeather = new ForecastWeather();
	// Element urlElement = (Element)allChildren.get(i);
	// if(urlElement.getChild("day_of_week")!=null&&urlElement.getChild("day_of_week").getAttributeValue("data")!=null)
	// {
	// fWeather.setDay_of_week(urlElement.getChild("day_of_week").getAttributeValue("data"));
	// }
	// if(urlElement.getChild("low")!=null&&urlElement.getChild("low").getAttributeValue("data")!=null)
	// {
	// fWeather.setLow(urlElement.getChild("low").getAttributeValue("data")+"℃");
	// }
	// if(urlElement.getChild("high")!=null&&urlElement.getChild("high").getAttributeValue("data")!=null)
	// {
	// fWeather.setHigh(urlElement.getChild("high").getAttributeValue("data")+"℃");
	// }
	// if(urlElement.getChild("icon")!=null&&urlElement.getChild("icon").getAttributeValue("data")!=null)
	// {
	// fWeather.setIcon("http://www.google.com"+urlElement.getChild("icon").getAttributeValue("data"));
	// }
	// if(urlElement.getChild("condition")!=null&&urlElement.getChild("condition").getAttributeValue("data")!=null)
	// {
	// fWeather.setCondition(urlElement.getChild("condition").getAttributeValue("data"));
	// }
	//
	// aListResult.weathers.add(fWeather);
	// }
	// }
	// else
	// {
	// aListResult.mHttpResult.setStat(TuanTripSettings.POSTFAILED);
	// aListResult.mHttpResult.setMessage("赞无相关天气信息");
	// }
	//
	// return aListResult;
	// }
	@Override
	public WeatherResult parse(String xml) throws TuanTripException {
		WeatherResult aListResult = new WeatherResult();
		try {

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();

			String tagName = null;
			xpp.setInput(new StringReader(xml));
			int eventType = xpp.getEventType();

			/**
			 * SAX方式循环解析xml文件
			 */
			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_TAG) {

					/**
					 * 获取xml元素名
					 */
					tagName = xpp.getName();
					/**
					 * 城市代码错误，抛出异常
					 */
					if (PROBLEM_CAUSE.equals(tagName)) {
						throw new Exception("the city is non correct!");
					} else if (FORECAST_INFORMATION.equals(tagName)) {

						aListResult.mHttpResult
								.setStat(TuanTripSettings.POSTSUCCESS);
						aListResult.mHttpResult.setMessage("成功");
						/**
						 * forecast_information
						 */
						dealWithInfomation(tagName, xpp);
					} else if (CURRENT_CONDITIONS.equals(tagName)) {
						/**
						 * current_conditions
						 */
						dealWithCurrentConditions(tagName,
								aListResult.currentWeather, xpp);
					} else if (FORECAST_CONDITIONS.equals(tagName)) {
						/**
						 * forecast_conditions
						 */
						dealWithForecastConditions(tagName,
								aListResult.weathers, xpp);
					}
				}
				eventType = xpp.next();
			}

		} catch (Exception e) {
			throw new TuanTripException("解析天气出错");
		}
		return aListResult;
	}

	private static void dealWithInfomation(String name, XmlPullParser xpp)
			throws IOException, XmlPullParserException {

		/**
		 * 指向下一个元素
		 */
		xpp.next();
		/**
		 * 获取标签类型：开始，结束
		 */
		int eventType = xpp.getEventType();
		/**
		 * 获取元素标签名
		 */
		String tagName = xpp.getName();

		while ((!name.equals(tagName) || eventType != XmlPullParser.END_TAG)
				&& eventType != XmlPullParser.END_DOCUMENT) {
			/**
			 * 如果元素标签为开始标签
			 */
			if (eventType == XmlPullParser.START_TAG) {

				if (tagName.equals(CITY)) {
					/**
					 * city信息
					 */

				} else if (tagName.equals(FORECAST_DATE)) {
					/**
					 * 日期信息
					 */

				}
			}
			/**
			 * 指向下一个元素
			 */
			xpp.next();

			eventType = xpp.getEventType();
			tagName = xpp.getName();
		}
	}

	private static void dealWithForecastConditions(String name,
			Group<ForecastWeather> weathers, XmlPullParser xpp)
			throws IOException, XmlPullParserException {

		Log.d(TAG, "dealWithForecastConditions");
		ForecastWeather forecast = new ForecastWeather();
		/**
		 * 指向下一个元素
		 */
		xpp.next();

		/**
		 * 获取标签类型：开始，结束
		 */
		int eventType = xpp.getEventType();
		/**
		 * 获取元素标签名
		 */
		String tagName = xpp.getName();

		while ((!name.equals(tagName) || eventType != XmlPullParser.END_TAG)
				&& eventType != XmlPullParser.END_DOCUMENT) {
			/**
			 * 如果元素标签为开始标签
			 */
			if (eventType == XmlPullParser.START_TAG) {
				if (tagName.equals(CONDITION)) {
					/**
					 * 天气状态
					 */
					forecast.setCondition(xpp.getAttributeValue(null, "data"));
				} else if (tagName.equals(DAY_OF_WEEK)) {
					/**
					 * 周几
					 */
					forecast.setDay_of_week(xpp.getAttributeValue(null, "data"));
				} else if (tagName.equals(HIGH)) {
					/**
					 * 最高温度
					 */
					forecast.setHigh(xpp.getAttributeValue(null, "data") + "℃");
				} else if (tagName.equals(LOW)) {
					/**
					 * 最低温度
					 */
					forecast.setLow(xpp.getAttributeValue(null, "data") + "℃");
				} else if (tagName.equals(ICON)) {
					/**
					 * 对天气状态的图标的描述
					 */
					forecast.setIcon(GOOGLEURL
							+ xpp.getAttributeValue(null, "data"));
				}
			}
			xpp.next();

			eventType = xpp.getEventType();
			tagName = xpp.getName();
		}
		/**
		 * 加入天气列表（通常显示后4天的天气）
		 */
		weathers.add(forecast);
	}

	private static void dealWithCurrentConditions(String name,
			CurrentWeather currentWeather, XmlPullParser xpp)
			throws IOException, XmlPullParserException {

		Log.d(TAG, "dealWithCurrentConditions");
		/**
		 * 指向下一个元素
		 */
		xpp.next();

		/**
		 * 获取标签类型：开始，结束
		 */
		int eventType = xpp.getEventType();
		/**
		 * 获取元素标签名
		 */
		String tagName = xpp.getName();

		while ((!name.equals(tagName) || eventType != XmlPullParser.END_TAG)
				&& eventType != XmlPullParser.END_DOCUMENT) {
			/**
			 * 如果元素标签为开始标签
			 */
			if (eventType == XmlPullParser.START_TAG) {
				if (tagName.equals(CONDITION)) {
					/**
					 * 天气状态
					 */
					currentWeather.setCondition(xpp.getAttributeValue(null,
							"data"));
					Log.d(TAG, "condition" + currentWeather.getCondition());
				} else if (tagName.equals(TEMP_F)) {
					/**
					 * ？？
					 */
					// currentWeather.setTempF(Integer.parseInt(xpp
					// .getAttributeValue(null, "data")));
					// Log.d(TAG, "TEMP_F" + currentWeather.getTempF());
				} else if (tagName.equals(TEMP_C)) {
					/**
					 * 当前温度
					 */
					currentWeather.setTemp_c(xpp
							.getAttributeValue(null, "data") + "℃");
					Log.d(TAG, "TEMP_C" + currentWeather.getTemp_c());
				} else if (tagName.equals(HUMIDITY)) {
					/**
					 * 湿度信息
					 */
					currentWeather.setHumidity(xpp.getAttributeValue(null,
							"data"));
				} else if (tagName.equals(ICON)) {
					/**
					 * 对当前天气状态的图标描述
					 */
					currentWeather.setIcon(GOOGLEURL
							+ xpp.getAttributeValue(null, "data"));
				} else if (tagName.equals(WIND_CONDITION)) {
					/**
					 * 风向信息
					 */
					currentWeather.setWind_condition(xpp.getAttributeValue(
							null, "data"));
				}
			}
			xpp.next();

			eventType = xpp.getEventType();
			tagName = xpp.getName();
		}
	}
}