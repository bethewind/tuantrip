package com.tudaidai.tuantrip;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mapabc.mapapi.GeoPoint;
import com.mapabc.mapapi.ItemizedOverlay;
import com.mapabc.mapapi.MapActivity;
import com.mapabc.mapapi.MapController;
import com.mapabc.mapapi.MapView;
import com.mapabc.mapapi.OverlayItem;
import com.mapabc.mapapi.PoiItem;
import com.mapabc.mapapi.PoiOverlay;
import com.tudaidai.tuantrip.types.Nearby;
import com.tudaidai.tuantrip.util.LocationUtil;
import com.tudaidai.tuantrip.util.TuanUtils;

public class Map extends MapActivity {
	private MapView mMapView = null;
	public static final String POIGROUPS = "com.tudaidai.tuantrip.Map.Poigroups";
	public static final String MYLOGROUPS = "com.tudaidai.tuantrip.Map.Mylogroups";
	public static final int DISTANCE = 10000;
	private ArrayList<Nearby> poiList;
	private ArrayList<Location> mylolist;
	MapController mMapController;
	Nearby nearby1;
	Location location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mMapView = ((MapView) findViewById(R.id.mapview));
		mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
		poiList = getIntent().getExtras().getParcelableArrayList(POIGROUPS);
		mylolist = getIntent().getExtras().getParcelableArrayList(MYLOGROUPS);
		List<PoiItem> poiItems = new ArrayList<PoiItem>();
		mMapController = mMapView.getController();
		mMapController.setZoom(15); // 设置地图zoom级别
		if (mylolist != null) {
			Drawable marker = getResources().getDrawable(
					R.drawable.da_marker_red); // 得到需要标在地图上的资源
			marker.setBounds(0, 0, marker.getIntrinsicWidth(),
					marker.getIntrinsicHeight()); // 为maker定义位置和边界

			mMapView.getOverlays().add(new OverItemT(marker, this, mylolist));

			location = mylolist.get(0);
		}

		if (poiList != null) {
			for (Nearby nearby : poiList) {
				GeoPoint point = new GeoPoint((int) (nearby.getWeidu() * 1E6),
						(int) (nearby.getJingdu() * 1E6));
				PoiItem poiItem = new PoiItem(String.valueOf(nearby.getFid()),
						point, nearby.getName(), nearby.getAddress());
				poiItem.setTel("");
				poiItem.setTypeCode("0010");
				poiItem.setAdCode(nearby.getType());
				int type = Integer.parseInt(nearby.getType());
				switch (type) {
				case NearbyActivity.AIRPORT:
					poiItem.setTypeDes("机场 - 机场");
					break;
				case NearbyActivity.HOTEL:
					poiItem.setTypeDes("酒店 - 酒店");
					break;
				case NearbyActivity.VIEWPOINT:
					poiItem.setTypeDes("景点 - 景点");
					break;
				case NearbyActivity.HUOCHE:
					poiItem.setTypeDes("火车站 - 火车站");
					break;
				default:
					poiItem.setTypeDes("全部 - 全部");
					break;
				}
				poiItems.add(poiItem);

				if (nearby1 == null && nearby != null
						&& nearby.getJingdu() != 0 && nearby.getJingdu() != -1) {
					nearby1 = nearby;
				}

			}
		}
		GeoPoint point = null;
		if (location != null) {
			if (nearby1 != null) {
				double distance = LocationUtil.distanceByLnglat(
						nearby1.getJingdu(), nearby1.getWeidu(),
						location.getLongitude(), location.getLatitude());
				if (distance < DISTANCE)
					point = new GeoPoint((int) (location.getLatitude() * 1E6),
							(int) (location.getLongitude() * 1E6));
				else
					point = new GeoPoint((int) (nearby1.getWeidu() * 1E6),
							(int) (nearby1.getJingdu() * 1E6));
			} else {
				point = new GeoPoint((int) (location.getLatitude() * 1E6),
						(int) (location.getLongitude() * 1E6));
			}

		} else {
			if (nearby1 != null) {
				point = new GeoPoint((int) (nearby1.getWeidu() * 1E6),
						(int) (nearby1.getJingdu() * 1E6));
			}
		}
		if (point != null) {
			mMapController.setCenter(point);
		}

		PoiOverlay poiOverlay = new PoiOverlay(getResources().getDrawable(
				R.drawable.da_marker_yellow), poiItems) {
			@Override
			protected MapView.LayoutParams getLayoutParam(int paramInt) {
				GeoPoint point = this.createItem(paramInt).getPoint();
				return new MapView.LayoutParams(-2, -2, point, 25, -15, 85);
			}

			@Override
			protected Drawable getPopupMarker(PoiItem paramPoiItem) {
				return getResources().getDrawable(R.drawable.da_marker_yellow);
			}

			// @Override
			// protected Drawable getPopupBackground()
			// {
			// return getResources().getDrawable(R.drawable.popup_bg);
			// }
			@Override
			protected View getPopupView(PoiItem paramPoiItem) {
				View view = super.getPopupView(paramPoiItem);
				view.setTag(paramPoiItem);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						PoiItem pItem = (PoiItem) v.getTag();

						Intent intent = null;
						int fid = Integer.parseInt(pItem.getPoiId());
						int type = 0;
						try {
							type = Integer.parseInt(pItem.getAdCode());
						} catch (Exception e) {
							return;
						}
						switch (type) {
						case NearbyActivity.AIRPORT:
							intent = new Intent(Map.this,
									AirportDetailActivity.class);
							intent.putExtra(AirportDetailActivity.ID, fid);
							break;
						case NearbyActivity.HOTEL:
							intent = new Intent(Map.this,
									HotelDetailActivity.class);
							intent.putExtra(HotelDetailActivity.HOTEL_ID, fid);
							String[] as = TuanUtils.getTommory();
							intent.putExtra(HotelDetailActivity.COME_DATE,
									as[0]);
							intent.putExtra(HotelDetailActivity.OUT_DATE, as[1]);
							break;
						case NearbyActivity.VIEWPOINT:
							intent = new Intent(Map.this,
									ViewpointDetailActivity.class);
							intent.putExtra(ViewpointDetailActivity.VID, fid);

							break;

						default:
							break;
						}

						if (intent != null) {
							startActivity(intent);
						}

					}
				});
				return view;
			}
		}; //
		poiOverlay.addToMap(mMapView); // 将poiOverlay标注在地图上
		mMapView.invalidate();

	}

	class OverItemT extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
		private Drawable marker;
		private Context mContext;

		public OverItemT(Drawable marker, Context context,
				ArrayList<Location> locations) {
			super(boundCenterBottom(marker));

			this.marker = marker;
			this.mContext = context;

			for (Location location : locations) {
				// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
				GeoPoint p1 = new GeoPoint(
						(int) (location.getLatitude() * 1E6),
						(int) (location.getLongitude() * 1E6));

				// 构造OverlayItem的三个参数依次为：item的位置，标题文本，文字片段
				GeoList.add(new OverlayItem(p1, "P1", "我的位置"));
			}

			populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
		}

		// @Override
		// public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		//
		// // Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
		// Projection projection = mapView.getProjection();
		// for (int index = size() - 1; index >= 0; index--) { // 遍历GeoList
		// OverlayItem overLayItem = getItem(index); // 得到给定索引的item
		//
		// String title = overLayItem.getTitle();
		// // 把经纬度变换到相对于MapView左上角的屏幕像素坐标
		// Point point = projection.toPixels(overLayItem.getPoint(), null);
		//
		// Paint paintCircle = new Paint();
		// paintCircle.setColor(Color.RED);
		// canvas.drawCircle(point.x, point.y, 5, paintCircle); // 画圆
		//
		// Paint paintText = new Paint();
		// paintText.setColor(Color.BLACK);
		// paintText.setTextSize(15);
		// canvas.drawText(title, point.x, point.y - 25, paintText); // 绘制文本
		//
		// }
		//
		// super.draw(canvas, mapView, shadow);
		// // 调整一个drawable边界，使得（0，0）是这个drawable底部最后一行中心的一个像素
		// boundCenterBottom(marker);
		// }

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return GeoList.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return GeoList.size();
		}

		@Override
		// 处理当点击事件
		protected boolean onTap(int i) {
			setFocus(GeoList.get(i));
			Toast.makeText(this.mContext, GeoList.get(i).getSnippet(),
					Toast.LENGTH_SHORT).show();
			return true;
		}
	}
}
