package com.tudaidai.tuantrip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.location.ILocation;
import com.tudaidai.tuantrip.location.LocationUtil;
import com.tudaidai.tuantrip.location.TuanLocation;
import com.tudaidai.tuantrip.types.BaseLbsDetail;
import com.tudaidai.tuantrip.types.Comment;
import com.tudaidai.tuantrip.types.CommentListResult;
import com.tudaidai.tuantrip.types.DingCaiResult;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.HttpResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.util.TuanUtils;

public class BaseLbsActivity extends Activity implements ILocation {

	public static final String LID = "lid";
	RemoteResourceManager mRrm;
	private RemoteResourceManagerObserver mResourcesObserver;
	private Handler mHandler = new Handler();
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final int ACTIVITY_IMAGE_CAPTURE = 1;
	static final int ACTIVITY_IMAGE_CONTENT = 2;
	static final int NEARBY = 3;
	static final int MENUMAP = 4;
	static final int MYNEARBY = 5;
	static final int HOME = 6;
	static final String TAG = "BaseLbsActivity";
	ImageView commentphoto;
	Uri originalUri;
	BaseLbsDetail baseLbsDetail;
	byte[] picData;
	Bitmap bitmap11;
	protected String lbsId = "1";
	protected ProgressDialog mProgressDialog;
	EditText commentContent;
	TextView comlength;
	public static final int PAGE_NUM = 5;
	public static final int COMMENTLENGTH = 140;
	private int lastItem = 1;
	private int mTotal;
	Button pageUp;
	Button pageDown;
	TextView nonewsText;
	Group<Comment> comments = new Group<Comment>();
	LinearLayout commentsmergeView;
	int commentType = 0;
	ViewHolder viewHoldercomment;
	boolean isFinishDing = true;
	protected TextView detailText;
	protected TextView commentText;
	protected TextView detailTab;
	protected LinearLayout content;
	protected LayoutInflater mInflater;
	View commentlist;
	View detailView;
	String locationString = "此处";
	boolean isWriteRadion = false;
	LocationUtil lUtil;

	private volatile boolean isGps = false;

	Location mLocation;
	RadioButton gpsRadio;
	RadioButton writeRadio;
	EditText locationText;
	RadioButton hereRadio;
	private ConcurrentHashMap<String, ImageView> mImageMap = new ConcurrentHashMap<String, ImageView>();

	protected void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}

	protected void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {

			return;

		}

		ContentResolver resolver = getContentResolver();

		try {

			if (requestCode == ACTIVITY_IMAGE_CONTENT) {

				// 获得图片的uri

				originalUri = data.getData();

			}
			InputStream picStream = resolver.openInputStream(Uri
					.parse(originalUri.toString()));

			Bitmap bitmap = BitmapFactory.decodeStream(picStream);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int newWidth = 321; // 291
			int newHeight = 250; // 220
			if (height < newHeight)
				newHeight = height;
			if (width < newWidth)
				newWidth = width;
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;

			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);

			// create the new Bitmap object
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
					height, matrix, true);
			// Bitmap bitmap = bitmap11;
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			if (resizedBitmap != null) {
				android.graphics.Bitmap.CompressFormat compressformat = android.graphics.Bitmap.CompressFormat.JPEG;
				resizedBitmap.compress(compressformat, 100,
						bytearrayoutputstream);
				picData = bytearrayoutputstream.toByteArray();

				commentphoto.setVisibility(View.VISIBLE);
				commentphoto.setImageBitmap(resizedBitmap);
			} else {
				Toast.makeText(BaseLbsActivity.this, "异常,请重试",
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			if (DEBUG) {
				// Log.i(tag, e);
				Toast.makeText(BaseLbsActivity.this, "异常,请重试",
						Toast.LENGTH_SHORT).show();
			}
		} catch (OutOfMemoryError e) {
			Toast.makeText(BaseLbsActivity.this, "内存不足", Toast.LENGTH_SHORT)
					.show();
		}

	}

	public void onDestroy() {

		super.onDestroy();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MYNEARBY, 0, "我的周边").setIcon(
				R.drawable.btn_icon_nears_normal);
		menu.add(0, NEARBY, 0, "此处周边")
				.setIcon(R.drawable.btn_icon_nears_normal);

		if (baseLbsDetail.getJingdu() != 0 && baseLbsDetail.getJingdu() != -1) {
			menu.add(0, MENUMAP, 0, "查看地图").setIcon(R.drawable.ic_menu_viewmap);
		}
		menu.add(0, HOME, 0, "返回首页").setIcon(R.drawable.btn_icon_nears_normal);

		super.onCreateOptionsMenu(menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		boolean flag = super.onOptionsItemSelected(menuitem);
		Intent intent = null;
		switch (menuitem.getItemId()) {
		case NEARBY:

			String[] as = new String[4];
			as[0] = new Double(baseLbsDetail.getJingdu()).toString();
			as[1] = new Double(baseLbsDetail.getWeidu()).toString();
			as[2] = baseLbsDetail.getCity();
			as[3] = baseLbsDetail.getAddress();

			intent = new Intent(BaseLbsActivity.this, NearbyActivity.class);
			intent.putExtra(NearbyActivity.EXTRA_LOCATION, as);
			startActivity(intent);
			break;
		case MENUMAP:
			Location location = new Location(LocationManager.NETWORK_PROVIDER);
			location.setLongitude(baseLbsDetail.getJingdu());
			location.setLatitude(baseLbsDetail.getWeidu());

			Intent intent2 = new Intent(BaseLbsActivity.this, Map.class);
			ArrayList<Location> arrayList1 = new ArrayList<Location>();
			arrayList1.add(location);
			intent2.putParcelableArrayListExtra(Map.MYLOGROUPS, arrayList1);
			startActivity(intent2);

			break;
		case MYNEARBY:
			intent = new Intent(BaseLbsActivity.this, NearbyActivity.class);
			startActivity(intent);

			break;
		case HOME:
			intent = new Intent(BaseLbsActivity.this, TuanTripMain.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;
		default:
			break;
		}

		return flag;
	}

	protected void onCreate(Bundle bundle) {
		NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNM.cancel(R.drawable.icon);

		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);
		mRrm = ((TuanTrip) getApplication()).getFileRemoteResourceManager();
		mResourcesObserver = new RemoteResourceManagerObserver();

		// lUtil = new LocationUtil(this,this);
		// lUtil.startGetLocation();
	}

	protected void onResume() {
		mRrm.addObserver(mResourcesObserver);

		// lUtil.requestLocationUpdates();

		super.onResume();
	}

	protected void onPause() {
		mRrm.deleteObserver(mResourcesObserver);

		// lUtil.removeLocationUpdates();

		super.onPause();
	}

	protected void ensure(final BaseLbsDetail baseLbsDetail1) {
		if (baseLbsDetail1 == null)
			return;
		this.baseLbsDetail = baseLbsDetail1;

		lbsId = baseLbsDetail.getLbsid();

		ImageView introImageView = (ImageView) findViewById(R.id.introlbsphoto);
		requestImage(introImageView, baseLbsDetail.getImage());

		TextView titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(baseLbsDetail.getTitle());
		TextView addressText = (TextView) findViewById(R.id.addressText);
		addressText.setText("地址:" + baseLbsDetail.getAddress());
		TextView phoneText = (TextView) findViewById(R.id.phoneText);
		phoneText.setText("电话:" + baseLbsDetail.getPhone());
		content = (LinearLayout) findViewById(R.id.content);
		mInflater = LayoutInflater.from(this);

		comments = baseLbsDetail.getComments();

		detailTab = (TextView) findViewById(R.id.detailTab);
		detailTab.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					content.removeAllViews();
					if (detailView == null) {
						detailView = mInflater.inflate(R.layout.lbs_common,
								null);
						detailView.setVisibility(View.VISIBLE);

						detailText = (TextView) detailView
								.findViewById(R.id.detailText);

						detailText.setText(Html.fromHtml(baseLbsDetail
								.getDetail().replaceAll("<img[^>]*?>", "")));
						detailText.setMovementMethod(LinkMovementMethod
								.getInstance());
						content.addView(detailView);
					} else {
						content.addView(detailView);
					}

				}

			}
		});

		mTotal = baseLbsDetail.getTotal();
		commentText = (TextView) findViewById(R.id.commentText);
		commentText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					initiComment();
				}

			}
		});
		commentText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		initiComment();

	}

	private void updateImage() {
		Set<String> keys = mImageMap.keySet();
		for (String key : keys) {
			requestImage(mImageMap.get(key), key);
		}
	}

	class RemoteResourceManagerObserver implements Observer {
		private RemoteResourceManagerObserver() {
		}

		public void update(Observable paramObservable, Object paramObject) {
			if (paramObject == null)
				return;
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					updateImage();
				}
			});
		}
	}

	protected void requestImage(ImageView imageView, String introImg) {
		if (introImg != null && !TextUtils.isEmpty(introImg)
				&& imageView != null && TuanUtils.isPic(introImg)) {
			Uri uri = Uri.parse(introImg);
			if (!mRrm.exists(uri)) {
				TuanTrip tuanTrip = (TuanTrip) getApplicationContext();
				// imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.img_defalt));
				// if(tuanTrip.getShowPic())
				{
					if (DEBUG)
						Log.d(TAG, "!mRrm.exists(uri)" + uri);
					mImageMap.put(introImg, imageView);
					mRrm.request(uri);
				}
			} else {
				if (DEBUG)
					Log.d(TAG, "mRrm exists " + uri);
				try {
					mImageMap.remove(introImg);
					Bitmap bitmap = mRrm.getBitmap(uri);
					imageView.setImageBitmap(bitmap);
					bitmap11 = bitmap;
				} catch (Exception exception) {
				}
			}
		}
	}

	private void getPicFromCapture() {
		// 调用相机

		Intent mIntent = new Intent("android.media.action.IMAGE_CAPTURE");

		// 图片存储路径，可自定义

		File tmpFile = new File(Environment.getExternalStorageDirectory(),

		System.currentTimeMillis() + ".jpg");

		// 获取这个图片的URI

		originalUri = Uri.fromFile(tmpFile);// 这是个实例变量，方便下面获取图片的时候用

		mIntent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);

		startActivityForResult(mIntent, ACTIVITY_IMAGE_CAPTURE);
	}

	private void getPicFromContent() {
		// 调用相册

		Intent mIntent = new Intent(Intent.ACTION_GET_CONTENT);

		mIntent.addCategory(Intent.CATEGORY_OPENABLE);

		mIntent.setType("image/*");

		startActivityForResult(mIntent, ACTIVITY_IMAGE_CONTENT);
	}

	private void getComment() {
		if (mTotal != 0 && nonewsText != null) {
			nonewsText.setVisibility(View.GONE);
		}

		int start = (lastItem - 1) * PAGE_NUM + 1;
		if (comments.size() < start) {
			String[] as = new String[3];
			as[0] = lbsId;
			as[1] = new Integer(start).toString();
			as[2] = new Integer(PAGE_NUM).toString();
			new GetCommentTask().execute(as);
		} else {
			commentsmergeView = (LinearLayout) commentlist
					.findViewById(R.id.commentsmerge);

			mImageMap.clear();
			commentsmergeView.removeAllViews();
			LayoutInflater mInflater = LayoutInflater.from(this);
			int end = comments.size() >= (start + 5) ? (start + 5) : (comments
					.size() + 1);
			for (int i = start; i < end; i++) {
				Comment comment = comments.get(i - 1);
				View view = mInflater.inflate(R.layout.comment_list_item, null);
				TextView username = (TextView) view.findViewById(R.id.username);
				username.setText(comment.getUname());
				TextView commenttime = (TextView) view
						.findViewById(R.id.commenttime);
				commenttime.setText("发表于:" + comment.getComtime());
				TextView content = (TextView) view.findViewById(R.id.content);
				content.setText(Html.fromHtml(comment.getContent()));
				// ImageView imageView =
				// (ImageView)view.findViewById(R.id.uphoto);
				// String url = comment.getUImage();
				// requestImage(imageView,url);
				ImageView imageView1 = (ImageView) view
						.findViewById(R.id.photo);
				String url1 = comment.getImage();
				imageView1.setTag(url1);
				imageView1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String[] images = new String[1];
						images[0] = (String) v.getTag();
						Intent intent = new Intent(BaseLbsActivity.this,
								HotelImageActivity1.class);
						intent.putExtra(HotelImageActivity1.IMAGES, images);
						startActivity(intent);

					}
				});
				if (url1 == null || url1.equals("")) {
					imageView1.setVisibility(View.GONE);
					imageView1.getLayoutParams().height = 0;
				} else
					requestImage(imageView1, url1);

				TextView reply = (TextView) view.findViewById(R.id.reply);
				reply.setTag(comment.getUname());
				reply.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String cString = "回复:" + (String) v.getTag() + ",";
						commentContent.clearFocus();
						commentContent.setText(cString);
						commentContent.requestFocus();
						// Toast.makeText(BaseLbsActivity.this, cString,
						// Toast.LENGTH_SHORT).show();

					}
				});
				TextView dingText = (TextView) view.findViewById(R.id.dingText);
				dingText.setText("支持" + comment.getDing());
				TextView caiText = (TextView) view.findViewById(R.id.caiText);
				caiText.setText("反对" + comment.getCai());
				ImageView ding = (ImageView) view.findViewById(R.id.ding);

				ViewHolder viewHolder = new ViewHolder();
				viewHolder.dingText = dingText;
				viewHolder.caiText = caiText;
				viewHolder.comment = comment;
				dingText.setTag(viewHolder);

				dingText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						viewHoldercomment = (ViewHolder) v.getTag();

						String cid = viewHoldercomment.comment.getCid();
						String uid = "1";
						String type = "0"; // 0支持，1反对

						if (isFinishDing) {
							String[] as = new String[3];
							as[0] = cid;
							as[1] = uid;
							as[2] = type;
							new GetDingCaiTask().execute(as);
						}

					}
				});
				ImageView cai = (ImageView) view.findViewById(R.id.cai);
				caiText.setTag(viewHolder);

				caiText.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						viewHoldercomment = (ViewHolder) v.getTag();

						String cid = viewHoldercomment.comment.getCid();
						String uid = "1";
						String type = "1"; // 0支持，1反对

						if (isFinishDing) {
							String[] as = new String[3];
							as[0] = cid;
							as[1] = uid;
							as[2] = type;
							new GetDingCaiTask().execute(as);
						}

					}
				});

				commentsmergeView.addView(view);
			}
			if (lastItem == 1)
				pageUp.setVisibility(View.INVISIBLE);
			else
				pageUp.setVisibility(View.VISIBLE);

			if (lastItem * PAGE_NUM >= mTotal)
				pageDown.setVisibility(View.INVISIBLE);
			else
				pageDown.setVisibility(View.VISIBLE);

			commentContent.clearFocus();

		}

	}

	class SubmitCommentTask extends AsyncTask<Object, Void, HttpResult> {
		private static final String TAG = "SubmitCommentTask";
		private Exception mReason;

		public SubmitCommentTask() {
		}

		protected HttpResult doInBackground(Object... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			HttpResult hDetailResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) BaseLbsActivity.this
						.getApplication()).getTuanTripApp();
				hDetailResult = localTuanTripApp
						.submitComment(paramArrayOfString);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				hDetailResult = null;
			}

			return hDetailResult;
		}

		protected void onPostExecute(HttpResult mHttpResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (mHttpResult == null) {
				NotificationsUtil.ToastReasonForFailure(BaseLbsActivity.this,
						mReason);
			} else if (mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				Toast.makeText(BaseLbsActivity.this, mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				dismissProgressDialog();
				refresh();
			} else if (mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(BaseLbsActivity.this, mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else if (mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
				Toast.makeText(BaseLbsActivity.this, mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				((TuanTrip) getApplication()).loginOut();
				Intent localIntent4 = new Intent(BaseLbsActivity.this,
						LoginActivity.class);
				localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
						LoginActivity.OPTION_DO_NOTHING);
				startActivity(localIntent4);
			} else {
				NotificationsUtil.ToastReasonForFailure(BaseLbsActivity.this,
						mReason);
			}
			dismissProgressDialog();

			// if(DEBUG&&mReason!=null)
			// {
			// commentContent.setText(mReason.toString());
			// }
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("提交评论", "提交中...");
		}
	}

	class GetCommentTask extends AsyncTask<String, Void, CommentListResult> {
		private static final String TAG = "GetCommentTask";
		private Exception mReason;

		public GetCommentTask() {
		}

		protected CommentListResult doInBackground(String... params) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			CommentListResult hDetailResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) BaseLbsActivity.this
						.getApplication()).getTuanTripApp();
				hDetailResult = localTuanTripApp.getCommentList(params);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				hDetailResult = null;
			}

			return hDetailResult;
		}

		protected void onPostExecute(CommentListResult rListResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (rListResult == null) {
				NotificationsUtil.ToastReasonForFailure(BaseLbsActivity.this,
						mReason);
			} else if (rListResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				if (commentType == 1) {
					mTotal = rListResult.getTotal();
				}

				if (rListResult.getComments().size() == 0) {
					if (commentType == 1) {
						// 0条
					} else {
						NotificationsUtil.ToastReasonForFailure(
								BaseLbsActivity.this, new Exception(
										"异常,请重新刷新评论!"));
					}
				} else {
					for (Comment comment : rListResult.getComments()) {
						comments.add(comment);
					}
					getComment();
				}
			} else if (rListResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(BaseLbsActivity.this,
						rListResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(BaseLbsActivity.this,
						mReason);
			}
			commentType = 0;
			dismissProgressDialog();
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("加载评论", "加载中...");
		}
	}

	class GetDingCaiTask extends AsyncTask<String, Void, DingCaiResult> {
		private static final String TAG = "GetDingCaiTask";
		private Exception mReason;

		public GetDingCaiTask() {
		}

		protected DingCaiResult doInBackground(String... params) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");

			DingCaiResult hDetailResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) BaseLbsActivity.this
						.getApplication()).getTuanTripApp();
				hDetailResult = localTuanTripApp.getDingCaiResult(params);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");
			} catch (Exception localException) {
				this.mReason = localException;
				hDetailResult = null;
			}

			return hDetailResult;
		}

		protected void onPostExecute(DingCaiResult rListResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (rListResult == null) {
				NotificationsUtil.ToastReasonForFailure(BaseLbsActivity.this,
						mReason);
			} else if (rListResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				viewHoldercomment.dingText.setText("支持"
						+ rListResult.comment.getDing());
				viewHoldercomment.caiText.setText("反对"
						+ rListResult.comment.getCai());
				viewHoldercomment.comment
						.setDing(rListResult.comment.getDing());
				viewHoldercomment.comment.setCai(rListResult.comment.getCai());
				Toast.makeText(BaseLbsActivity.this,
						rListResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else if (rListResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(BaseLbsActivity.this,
						rListResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else if (rListResult.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
				Toast.makeText(BaseLbsActivity.this,
						rListResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				((TuanTrip) getApplication()).loginOut();
				Intent localIntent4 = new Intent(BaseLbsActivity.this,
						LoginActivity.class);
				localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
						LoginActivity.OPTION_DO_NOTHING);
				startActivity(localIntent4);
			} else {
				NotificationsUtil.ToastReasonForFailure(BaseLbsActivity.this,
						mReason);
			}
			setProgressBarIndeterminateVisibility(false);
			isFinishDing = true;
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			setProgressBarIndeterminateVisibility(true);
			isFinishDing = false;
		}
	}

	class ViewHolder {

		TextView dingText;
		TextView caiText;
		Comment comment;

		private ViewHolder() {
		}
	}

	protected void invalidate() {
		new Thread() {
			public void run() {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						content.invalidate();

					}
				});
			}
		}.start();
	}

	protected void refresh() {
		picData = null;
		if (commentContent != null) {
			commentContent.setText("");
		}
		commentphoto.setVisibility(View.GONE);
		commentphoto.setImageBitmap(null);

		commentType = 1;

		if (comments == null)
			comments = new Group<Comment>();

		comments.clear();
		lastItem = 1;
		getComment();
	}

	private void setLocationText() {
		if (gpsRadio == null || mLocation == null)
			return;

		if (DEBUG)
			Log.d(TAG, "setLocationText()");
		if (mLocation instanceof TuanLocation) {
			String lString = ((TuanLocation) mLocation).getStreet().replaceAll(
					"中国", "")
					+ "附近";

			gpsRadio.setText(lString);

			if (gpsRadio.isChecked()) {
				locationString = lString;
			}
		}

	}

	private void getAddressFromLocation() {
		new Thread() {
			public void run() {
				Location loc = lUtil.getTuanLocation(mLocation);
				if (loc == null || mLocation == null
						|| (mLocation instanceof TuanLocation)) {
					return;
				}

				mLocation = loc;

				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// Toast.makeText(NearbyActivity.this,((TuanLocation)mLocation).getStreet(),Toast.LENGTH_LONG).show();
						setLocationText();
					}
				});

			}
		}.start();
	}

	private void initiComment() {

		content.removeAllViews();
		if (commentlist == null) {
			commentlist = mInflater.inflate(R.layout.comment_common, null);
			comlength = (TextView) commentlist.findViewById(R.id.comlength);
			commentContent = (EditText) commentlist
					.findViewById(R.id.commentContent);
			commentContent.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					String cString = s.toString();
					if (cString.length() > COMMENTLENGTH) {
						commentContent.setText(cString.substring(0,
								COMMENTLENGTH));

						comlength.setText("信息内容(还可输入0字)");
					} else {
						if (comlength != null) {
							comlength.setText("信息内容(还可输入"
									+ (COMMENTLENGTH - cString.length()) + "字)");
						}

					}

				}
			});
			commentContent.setFocusable(true);
			commentContent.setFocusableInTouchMode(true);

			pageUp = (Button) commentlist.findViewById(R.id.pageUp);
			pageDown = (Button) commentlist.findViewById(R.id.pageDown);
			nonewsText = (TextView) commentlist.findViewById(R.id.nonewsText);

			hereRadio = (RadioButton) commentlist.findViewById(R.id.hereRadio);
			gpsRadio = (RadioButton) commentlist.findViewById(R.id.gpsRadio);
			writeRadio = (RadioButton) commentlist
					.findViewById(R.id.writeRadio);
			locationText = (EditText) commentlist
					.findViewById(R.id.locationText);
			RadioGroup radioGroup = (RadioGroup) commentlist
					.findViewById(R.id.picRadioGroup);
			radioGroup
					.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							if (checkedId == hereRadio.getId()) {
								isWriteRadion = false;
								locationString = hereRadio.getText().toString();
							}
							if (checkedId == gpsRadio.getId()) {
								isWriteRadion = false;
								if (isGps)
									locationString = gpsRadio.getText()
											.toString();
							}
							if (checkedId == writeRadio.getId()) {
								if (isGps
										&& locationText.getText().toString()
												.trim().equals("")) {
									locationText.setText(gpsRadio.getText()
											.toString());
								}

								isWriteRadion = true;
							}

						}
					});

			if (mTotal != 0) {
				nonewsText.setVisibility(View.GONE);
				if (mTotal <= PAGE_NUM) {
					pageUp.setVisibility(View.GONE);
					pageDown.setVisibility(View.GONE);
				} else {
					pageUp.setVisibility(View.INVISIBLE);

				}
				pageUp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						lastItem--;
						getComment();

					}
				});
				pageDown.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						lastItem++;
						getComment();
					}
				});

				getComment();
			} else {
				pageUp.setVisibility(View.GONE);
				pageDown.setVisibility(View.GONE);
			}

			commentphoto = (ImageView) commentlist
					.findViewById(R.id.commentphoto);
			Button getphoto = (Button) commentlist.findViewById(R.id.getphoto);
			getphoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(BaseLbsActivity.this)
							.setTitle("选择相片")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setItems(new String[] { "拍摄一张新相片", "从已有图片中选取" },
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											switch (which) {
											case 0:
												getPicFromCapture();
												break;
											case 1:
												getPicFromContent();
											default:
												break;
											}
										}
									}).setNegativeButton("取消", null).show();

				}
			});

			Button submitComent = (Button) commentlist
					.findViewById(R.id.submitComent);
			submitComent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (commentContent.getText().toString().trim().equals("")) {
						Toast.makeText(BaseLbsActivity.this, "消息内容不能为空",
								Toast.LENGTH_SHORT).show();
						return;
					}
					String lString = "";
					if (!isWriteRadion) {
						lString = "<font color = #999999>在" + locationString
								+ "说：</font><br/>";

						if (gpsRadio.isChecked() && !isGps) {
							lString = "";
						}
					} else {
						if (!locationText.getText().toString().trim()
								.equals(""))
							lString = "<font color = #999999>在"
									+ locationText.getText().toString().trim()
									+ "说：</font><br/>";
					}

					Object[] as = new Object[5];
					as[0] = picData;
					as[1] = lbsId;
					as[2] = "2"; // userid
					as[3] = commentContent.getText().toString(); // content
					as[4] = picData == null ? "0" : "1"; // 0为没图片，1为有图片
					new SubmitCommentTask().execute(as);

				}
			});
			content.addView(commentlist);
		} else {
			commentContent.clearFocus();
			content.addView(commentlist);
		}

		if (isGps)
			setLocationText();

	}

	@Override
	public void locationTaskPost() {
		isGps = true;
		mLocation = lUtil.getmLocation();
		getAddressFromLocation();

	}

	@Override
	public void locationTaskPre() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getNoLocation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLocation() {
		mLocation = lUtil.getmLocation();
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				getAddressFromLocation();
			}
		});

	}
}
