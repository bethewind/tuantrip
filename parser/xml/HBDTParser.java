package com.tudaidai.tuantrip.parser.xml;

import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.error.TuanTripException;
import com.tudaidai.tuantrip.parser.AbstractParser;
import com.tudaidai.tuantrip.types.HBSKResult;

public class HBDTParser extends AbstractParser<HBSKResult> {

	@Override
	public HBSKResult parse(String html) throws TuanTripException {
		int start = html.indexOf("<div class=\"homeNavigation\">");
		int end = html.lastIndexOf("<li>按航段查询</li>");
		if (start == -1 || end == -1 || (end - start) < 50) {
			throw new TuanTripException("无航班信息");
		}

		String hString = "<div>" + html.substring(start, end) + "</div>";
		hString = hString.replaceAll("<li.*?>|<ol>", "").replaceAll("</li>",
				"<br/>");
		hString = hString.replaceAll("<a.*?>.*?</a>", "");
		hString = hString.replaceAll("<div class=\"homeNavigation\">", "");
		hString = hString.replaceAll("</h1><br/>", "</h1>");
		HBSKResult hbskResult = new HBSKResult();
		hbskResult.mResult = hString;
		hbskResult.mHttpResult.setStat(TuanTripSettings.POSTSUCCESS);

		return hbskResult;
	}

	private static String preProcess(String html) {
		html = html.replaceAll("(?s)<!DOCTYPE.*?>", "");
		html = html.replaceAll("(?s)<!--.*?-->", ""); // remove html comment
		html = html.replaceAll("(?s)<script.*?>.*?</script>", ""); // remove
																	// javascript
		html = html.replaceAll("(?s)<style.*?>.*?</style>", ""); // remove css
		html = html.replaceAll("(?s)<SCRIPT.*?>.*?</SCRIPT>", ""); // remove
																	// javascript
		html = html.replaceAll("(?s)<STYLE.*?>.*?</STYLE>", ""); // remove css
		html = html.replaceAll("&.{2,5};|&#.{2,5};", " "); // remove special
															// char
		html = html.replaceAll("(?s)<.*?>", "");
		html = html.replaceAll("(?s)<head.*?>.*?</head>", ""); // remove css
		html = html.replaceAll("(?s)<HEAD.*?>.*?</HEAD>", ""); // remove css
		html = html.replaceAll("(?s)<noScript.*?>.*?</noScript>", ""); // remove
																		// css

		return html;
	}
}