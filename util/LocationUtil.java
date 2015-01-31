package com.tudaidai.tuantrip.util;

public class LocationUtil {
	public static double distanceByLnglat(double _Longitude1,
			double _Latidute1, double _Longitude2, double _Latidute2) {

		double radLat1 = _Latidute1 * Math.PI / 180;
		double radLat2 = _Latidute2 * Math.PI / 180;
		double a = radLat1 - radLat2;
		double b = _Longitude1 * Math.PI / 180 - _Longitude2 * Math.PI / 180;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
		s = Math.round(s * 10000) / 10000;
		return s;
	}

}
