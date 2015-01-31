package com.tudaidai.tuantrip;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.AddrInfo;
import com.tudaidai.tuantrip.types.AddrListResult;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.widget.AddrListAdapter;

public class AddrActivity extends Activity
{
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	  public static final String EXTRA_ADDR = "com.tudaidai.tuantrip.AddrActivity.ADDR";
	  public static final String EXTRA_OPTION = "com.tudaidai.tuantrip.AddrActivity.OPTION";
	  public static final int OPTION_ORDER = 1;
	  public static final int OPTION_SET = 2;
	  public static final int REQUEST_CODE = 1;
	  public static final int RESPONSE_SUCCESS = 1;
	  public static final int RESPONSE_FAILED = 2;
	  static final String TAG = "AddrActivity";
	  private AddrListAdapter mListAdapter;
	  private ArrayList<AddrInfo> mAddr;
	  private Group<AddrInfo> mgAddr=new Group<AddrInfo>();
	  private int mOption;
	  private ProgressDialog mProgressDialog;
	 
	  
	  private void onAddrTaskComplete(AddrListResult addrlistresult, Exception exception)
	  {
		  
		  
		  if(addrlistresult==null)
		  {
			  NotificationsUtil.ToastReasonForFailure(this,exception);
		  }else if (addrlistresult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS)
		  {
			  mAddr=new ArrayList<AddrInfo>();
			  Iterator localIterator = addrlistresult.mGroup.iterator();
			  while(localIterator.hasNext())
			  {
				  mAddr.add((AddrInfo)localIterator.next());
			  }
			  ensureUi();
			 
		  } else if (addrlistresult.mHttpResult.getStat()==TuanTripSettings.POSTFAILED) 
			 {
				 Toast.makeText(this,addrlistresult.mHttpResult.getMessage(),Toast.LENGTH_SHORT).show();
			 }else if (addrlistresult.mHttpResult.getStat()==TuanTripSettings.LOGINFAILED) 
			 {
				 Toast.makeText(this,addrlistresult.mHttpResult.getMessage(),Toast.LENGTH_SHORT).show();
				 ((TuanTrip)getApplication()).loginOut();
			 }else
			 {
				 NotificationsUtil.ToastReasonForFailure(this,exception);
			 }
			 
			 
			 dismissProgressDialog();
	  }
	  private void startProgressBar(String title,String content) {
	        mProgressDialog = ProgressDialog.show(this,title,content);
	        mProgressDialog.show();
	    }
	  private void dismissProgressDialog()
	  {
	    this.mProgressDialog.dismiss();
	  }
	  public void onActivityResult(int requestcode, int resultCode, Intent data)
		 {
			 if(requestcode==REQUEST_CODE && resultCode==RESPONSE_SUCCESS)
			 {
				 Void[] as = new Void[0];
				new AddrTask().execute(as);
			 }
		 }
	  private void ensureUi()
	  {
		  TextView addClick = (TextView)findViewById(R.id.addClick);
		  addClick.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(AddrActivity.this,EditAddrActivity.class);
					intent.putExtra(EditAddrActivity.EXTRA_OPTION,EditAddrActivity.OPTION_ADD);
					startActivityForResult(intent,REQUEST_CODE);
				}});
		  ListView listview = (ListView)findViewById(R.id.listView);
		  mListAdapter = new AddrListAdapter(this);
		  mgAddr = new Group<AddrInfo>();
		  for(AddrInfo aInfo : mAddr)
		  {
			  mgAddr.add(aInfo);
		  }
		  mListAdapter.setGroup(mgAddr);
		  listview.setAdapter(mListAdapter);
		  listview.setSmoothScrollbarEnabled(true);
		  listview.setCacheColorHint(0);
			//listview.setBackgroundResource(0x7f040000);
		  listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent,View view,
					int i,long id)
			{
				AddrInfo addrinfo = (AddrInfo)mListAdapter.getItem(i);
				if (addrinfo != null && mOption == OPTION_ORDER)
				{
					Intent intent = new Intent();
					intent.putExtra(BuyActivity.ADDRPARCEL,addrinfo);
					setResult(-1, intent);
					finish();
				}
				
			}});
	  }
	  protected void onCreate(Bundle paramBundle)
	  {
	    super.onCreate(paramBundle);
	    setContentView(R.layout.addr_activity);
	    if ((getIntent() == null) || (getIntent().getExtras() == null) || (!getIntent().getExtras().containsKey(EXTRA_OPTION)))
	    {
	      finish();
	      return;
	    }
	    this.mOption = getIntent().getExtras().getInt(EXTRA_OPTION);
	    mAddr = getIntent().getExtras().getParcelableArrayList(EXTRA_ADDR);
	    
	    ensureUi();
	    
	  }
	  
	  
	  class AddrTask extends AsyncTask<Void, Void, AddrListResult>
	  {
	    private static final boolean DEBUG = TuanTripSettings.DEBUG;
	    private static final String TAG = "AddrTask";
	    private Exception mReason=null;

	    private AddrTask()
	    {
	    }

	    protected AddrListResult doInBackground(Void... paramArrayOfVoid)
	    {
	      if(DEBUG) Log.d(TAG, "doInBackground()");
	      AddrListResult localAddrListResult1;
	      try
	      {
	        TuanTrip tuanTrip = (TuanTrip)AddrActivity.this.getApplication();
	        TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
	        localAddrListResult1 = tuanTripApp.getAddress();
	        
	      }
	      catch (Exception localException)
	      {
	    	  if(DEBUG) Log.d("AddrTask", "Caught Exception.", localException);
	        this.mReason = localException;
	        localAddrListResult1 = null;
	      }
	      return localAddrListResult1;
	    }

	    protected void onCancelled()
	    {
	      super.onCancelled();
	      AddrActivity.this.dismissProgressDialog();
	    }

	    protected void onPostExecute(AddrListResult paramAddrListResult)
	    {
	    	if(DEBUG) Log.d(TAG, "onPostExecute(): ");
	    	AddrActivity.this.onAddrTaskComplete(paramAddrListResult, this.mReason);
	    }

	    protected void onPreExecute()
	    {
	    	if(DEBUG) Log.d(TAG, "onPreExecute()");
	    	startProgressBar("获取地址","获取地址中...");
	    }
	  }
}
