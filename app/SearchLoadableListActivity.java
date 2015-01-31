package com.tudaidai.tuantrip.app;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tudaidai.tuantrip.R;

public class SearchLoadableListActivity extends ListActivity {

	private int mNoSearchResultsString = getNoSearchResultsStringId();

	protected ProgressBar mEmptyProgress;
	protected TextView mEmptyText;
	protected ProgressDialog mProgressDialog;

	protected void dismissProgressDialog() {
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	protected void startProgressBar(String title, String content) {
		// if(mProgressDialog==null)
		mProgressDialog = ProgressDialog.show(this, title, content);

		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_loadable_list_activity);
		mEmptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		mEmptyText = (TextView) findViewById(R.id.emptyText);
		setLoadingView();

		getListView().setCacheColorHint(Color.TRANSPARENT);
		getListView().setDivider(
				getResources().getDrawable(R.color.black_start));
		getListView().setDividerHeight(1);
	}

	public void setEmptyView() {
		mEmptyProgress.setVisibility(ViewGroup.GONE);
		mEmptyText.setText(mNoSearchResultsString);
	}

	public void setLoadingView() {
		mEmptyProgress.setVisibility(ViewGroup.VISIBLE);
		mEmptyText.setText("加载中...");// R.string.loading);
	}

	public void setLoadingView(String loadingText) {
		setLoadingView();
		mEmptyText.setText(loadingText);
	}

	public int getNoSearchResultsStringId() {
		return R.string.no_search_results;
	}
}
