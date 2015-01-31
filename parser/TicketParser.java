package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.Ticket;
import com.tudaidai.tuantrip.types.TicketResult;

public class TicketParser extends AbstractParser<TicketResult> {

	@Override
	public TicketResult parse(JSONObject json) throws JSONException {

		TicketResult aListResult = new TicketResult();

		JSONObject httpResultObject = json.getJSONObject("HttpResult");
		if (httpResultObject.has("Status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("Info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}

		if (json.has("Tickets")) {
			JSONObject jObject = json.getJSONObject("Tickets");
			if (jObject.has("Total")) {
				aListResult.setTotla(Integer.parseInt(jObject
						.getString("Total")));
			}
			if (jObject.has("Ticket")) {
				JSONArray orderArray = jObject.getJSONArray("Ticket");
				for (int i = 0; i < orderArray.length(); i++) {
					Ticket ticket = new Ticket();
					JSONObject jObject2 = orderArray.getJSONObject(i);
					if (jObject2.has("Tid")) {
						ticket.setTid(jObject2.getString("Tid"));
					}
					if (jObject2.has("Title")) {
						ticket.setTitle(jObject2.getString("Title"));
					}
					if (jObject2.has("Date")) {
						ticket.setDate(jObject2.getString("Date"));
					}
					if (jObject2.has("TicketStatus")) {
						ticket.setTicketStatus(jObject2
								.getString("TicketStatus"));
					}
					if (jObject2.has("Code")) {
						ticket.setCode(jObject2.getString("Code"));
					}
					if (jObject2.has("Pwd")) {
						ticket.setPwd(jObject2.getString("Pwd"));
					}
					if (jObject2.has("TravelDate")) {
						ticket.setTravelDate(jObject2.getString("TravelDate"));
					}

					aListResult.mGroup.add(ticket);
				}
			}
		}

		return aListResult;
	}

}