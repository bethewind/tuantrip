package com.tudaidai.tuantrip.types;

public class WeatherResult implements TuanTripType {
	public HttpResult mHttpResult = new HttpResult();
	public CurrentWeather currentWeather = new CurrentWeather();
	public Group<ForecastWeather> weathers = new Group<ForecastWeather>();

}
