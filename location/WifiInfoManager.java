package com.tudaidai.tuantrip.location;

/*WifiInfoManager.java 可获取wifi的信息，目前我只取了当前连接的wifi，没有获取所有能扫描到的wifi信息。 */
import java.util.ArrayList;
import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiInfoManager {

	WifiManager wm;

	public WifiInfoManager() {
	}

	public ArrayList getWifiInfo(WifiManager wm, List<ScanResult> wifiList) {
		ArrayList<WifiInfo> wifi = new ArrayList();
		// wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiList == null || wifiList.size() == 0) {
			WifiInfo info = new WifiInfo();
			info.mac = wm.getConnectionInfo().getBSSID();
			if (info.mac != null)
				wifi.add(info);
		} else {
			for (ScanResult scanResult : wifiList) {
				WifiInfo info = new WifiInfo();
				info.mac = scanResult.BSSID;
				wifi.add(info);
			}
		}
		return wifi;
	}

	/* WifiInfo.java 封装了wifi的信息 */
	public class WifiInfo {

		public String mac;

		public WifiInfo() {
		}
	}
}
