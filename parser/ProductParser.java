package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.ProductResult;
import com.tudaidai.tuantrip.types.Shop;

public class ProductParser extends AbstractParser<ProductResult> {

	@Override
	public ProductResult parse(JSONObject json) throws JSONException {
		ProductResult productResult = new ProductResult();
		Group<Shop> shops = new Group<Shop>();
		JSONObject httpResultObject = json.getJSONObject("HttpResult");
		if (httpResultObject.has("Status")) {
			productResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("Info")) {
			productResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}
		JSONObject product = json.getJSONObject("Product");
		if (product.has("Pid")) {
			productResult.mProduct.setPid(Integer.parseInt(product
					.getString("Pid")));
		}
		if (product.has("Title")) {
			productResult.mProduct.setTitle(product.getString("Title"));
		}
		if (product.has("Url")) {
			productResult.mProduct.setUrl(product.getString("Url"));
		}
		if (product.has("ImageUrl")) {
			productResult.mProduct.setImageUrl(product.getString("ImageUrl"));
		}
		if (product.has("CityName")) {
			productResult.mProduct.setCityName(product.getString("CityName"));
		}
		if (product.has("PriceOriginal")) {
			productResult.mProduct.setPriceOriginal(Double.parseDouble(product
					.getString("PriceOriginal")));
		}
		if (product.has("Price")) {
			productResult.mProduct.setPrice(Double.parseDouble(product
					.getString("Price")));
		}
		if (product.has("ReBate")) {
			productResult.mProduct.setReBate(Double.parseDouble(product
					.getString("ReBate")));
		}
		if (product.has("StartTime")) {
			productResult.mProduct.setStartTime(Integer.parseInt(product
					.getString("StartTime")));
		}
		if (product.has("EndTime")) {
			productResult.mProduct.setEndTime(Integer.parseInt(product
					.getString("EndTime")));
		}
		if (product.has("Bought")) {
			productResult.mProduct.setBought(Integer.parseInt(product
					.getString("Bought")));
		}
		if (product.has("MinBought")) {
			productResult.mProduct.setMinBought(Integer.parseInt(product
					.getString("MinBought")));
		}
		if (product.has("BoughtLimit")) {
			productResult.mProduct.setBoughtLimit(Integer.parseInt(product
					.getString("BoughtLimit")));
		}
		if (product.has("State")) {
			productResult.mProduct.setState(product.getString("State"));
		}
		if (product.has("SucessTime")) {
			productResult.mProduct.setSucessTime(Integer.parseInt(product
					.getString("SucessTime")));
		}
		if (product.has("Team_type")) {
			productResult.mProduct.setTeamType(Integer.parseInt(product
					.getString("Team_type")));
		}
		if (product.has("Tip")) {
			productResult.mProduct.setTip(product.getString("Tip"));
		}
		if (product.has("Description")) {
			productResult.mProduct.setDescription(product
					.getString("Description"));
		}
		if (product.has("CustomSay")) {
			productResult.mProduct.setCustomSay(product.getString("CustomSay"));
		}
		if (product.has("WeSay")) {
			productResult.mProduct.setWeSay(product.getString("WeSay"));
		}
		if (product.has("Shops")) {
			JSONObject shopjObject = product.getJSONObject("Shops")
					.getJSONObject("Shop");
			Shop shop = new Shop();
			if (shopjObject.has("Same")) {
				shop.setSName(shopjObject.getString("Same"));
			}
			if (shopjObject.has("Sel")) {
				shop.setSel(shopjObject.getString("Sel"));
			}
			if (shopjObject.has("Sddr")) {
				shop.setSddr(shopjObject.getString("Sddr"));
			}
			if (shopjObject.has("Longitude")) {
				shop.setLongitude(shopjObject.getString("Longitude"));
			}
			if (shopjObject.has("Latitude")) {
				shop.setLatitude(shopjObject.getString("Latitude"));
			}

			shops.add(shop);
			productResult.mProduct.setShops(shops);
		}

		long timeNow = System.currentTimeMillis() / 1000;
		productResult.mProduct.setTimeLeft((int) ((long) productResult.mProduct
				.getEndTime() - timeNow));
		return productResult;
	}

}