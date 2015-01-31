package com.tudaidai.tuantrip;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class BaseBuyActivity extends Activity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "BaseBuyActivity";
	protected int pid = -1; // 产品id
	protected int quantity = 1; // 购买的数量
	protected int boughtLimit = 0; // 限制购买多少单
	protected boolean isBuy = false; // 是否已经购买此商品
	protected double balance = 0.0; // 余额
	protected double price = 0.0; // 单价
	protected double totalPrice = 0.0; // 总价
	protected int idType = 1; // 证件类型
	protected TextView priceView;
	protected TextView totalView;
	protected TextView finalPriceView;
	protected EditText buyNumView;
	protected EditText userName;
	protected EditText phoneEdit;
	protected EditText remarkView;
	protected EditText idCodeView;
	protected Spinner sp1;
	protected ArrayAdapter<String> adapter;
	protected List<String> allnum;
	protected static String[] arr = { "身份证", "驾驶证", "军官证", "工作证", "其他证件" };
	protected static String[] Value = { "1", "2", "3", "4", "5" };

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	private void SetIdType() {
		sp1 = (Spinner) findViewById(R.id.idTypeSp);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(adapter);
		sp1.setSelection(idType - 1);
		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				idType = Integer.parseInt(Value[arg2]);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	protected void ensureUi() {
		TuanTrip tuanTrip = (TuanTrip) getApplication();
		pid = tuanTrip.getProduct().getPid();

		TextView limitView = (TextView) findViewById(R.id.limitText);
		boughtLimit = tuanTrip.getProduct().getBoughtLimit();

		if (boughtLimit != 0) {
			limitView.setText("每人限购" + boughtLimit + "个");
		} else {
			limitView.setVisibility(View.INVISIBLE);
			limitView.getLayoutParams().height = 0;
		}

		Button backButton = (Button) findViewById(R.id.cancelButton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		SetIdType();
	}
}
