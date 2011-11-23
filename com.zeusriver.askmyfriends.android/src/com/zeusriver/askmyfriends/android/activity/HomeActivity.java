package com.zeusriver.askmyfriends.android.activity;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.zeusriver.askmyfriends.android.activity.HomeActivity;
import com.zeusriver.askmyfriends.android.entity.AccountEntity;
import com.zeusriver.askmyfriends.android.provider.CopyOfDatabaseAdapter;
import com.zeusriver.askmyfriends.android.service.OnAlarmReceiver;
import com.zeusriver.askmyfriends.android.shared.WebServiceAsyncTask;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends Activity {
	
	public CopyOfDatabaseAdapter mDbHelper;
	//private static final String WEB_SERVICE_POST_URL = "http://ritereferral.appspot.com/testregistration";
	private static final String WEB_SERVICE_POST_URL = "http://askmyfriends-qa.appspot.com/testregistration";

	public static final int menu1 = 1;
	private static final String PREF = "RiteReferralPrefences";
	private static final int DIALOG_ACCOUNTS = 0;
	private static final int PERIOD=600000;   // 10 minute
	private static final String TAG = "RiteReferral";
    ObjectMapper mapper = new ObjectMapper();  

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		if (isConnected()) {
			this.setAlarm(getBaseContext());
		}
				
		SharedPreferences preferences = getSharedPreferences(PREF, 0);
		
		mDbHelper = new CopyOfDatabaseAdapter(this);
		mDbHelper.open();
		
		if (preferences.contains("accountName")==false) {
			
			AccountManager manager = AccountManager.get(this);
		    Account[] accounts = manager.getAccountsByType("com.google");
		    int size = accounts.length;
		    
		    if (size > 1) {
		    	showDialog(DIALOG_ACCOUNTS);
		    }
		    else {
	        	gotAccount(manager, accounts[0]);
		    }
		}
		
		TextView requestsNotviewed = (TextView)findViewById(R.id.TextView01);
		TextView responsesNotviewed = (TextView)findViewById(R.id.responses);
			    
		Cursor c = mDbHelper.getPendingMessages("Request");
        startManagingCursor(c);

		if (c.getCount()!= 0 ) {
			String numberOfRequests = Integer.toString(c.getCount());
			requestsNotviewed.setText(numberOfRequests);
		}
		else if (c.getCount()==0) {
			requestsNotviewed.setBackgroundDrawable(null);
			requestsNotviewed.setText(null);
		}
				
		c.close();
		
		Cursor c2 = mDbHelper.getPendingMessages("Response");
        startManagingCursor(c2);

		if (c2.getCount()!= 0) {
			String numberOfResponses = Integer.toString(c2.getCount());	
			responsesNotviewed.setText(numberOfResponses);
		}
		else if (c2.getCount()==0) {
			responsesNotviewed.setBackgroundDrawable(null);
			responsesNotviewed.setText(null);
		}
		
		c2.close();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	      case DIALOG_ACCOUNTS:
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("Select a Google account");
	        final AccountManager manager = AccountManager.get(this);
	        final Account[] accounts = manager.getAccountsByType("com.google");
	        final int size = accounts.length;
	        String[] names = new String[size];
	        	for (int i = 0; i < size; i++) {
	        		names[i] = accounts[i].name;
	        	}
	        builder.setItems(names, new DialogInterface.OnClickListener() {
	        
	        public void onClick(DialogInterface dialog, int i) {
	        
	        	gotAccount(manager, accounts[i]);

	    	   	/* Active only when more then one account found for user */
	    	   	if (size > 1) {
    	   			showDialog(DIALOG_ACCOUNTS);   	    		
	    	    }	    	
	        }
	        });
	        return builder.create();
	    }
	    return null;
    }
	
	private void gotAccount(final AccountManager manager,final Account account) {
		SharedPreferences settings = getSharedPreferences(PREF, 0);
		SharedPreferences.Editor editor = settings.edit();
		String accountID = account.name;
		editor.putString("accountName", accountID);
		editor.commit();
		registerAccountOnline(accountID);
    }
	
	private void registerAccountOnline(String accountID){
		
		AccountEntity registrationRequest = new AccountEntity(accountID);
		
		Cursor c3 = mDbHelper.checkRegistration();
        startManagingCursor(c3);
        	        
        if (c3.getColumnIndexOrThrow(CopyOfDatabaseAdapter.REGISTRATIONFLAG)<1); {
		
		try {
			String JacksonRepresentation = mapper.writeValueAsString(registrationRequest);
    		WebServiceAsyncTask webServiceTask = new WebServiceAsyncTask();
    		webServiceTask.execute(WEB_SERVICE_POST_URL,JacksonRepresentation);
    		
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			mDbHelper.saveRegistration(1);
        }
        c3.close();
	}
	
	private void setAlarm(Context context) {
		AlarmManager mgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
  		Intent i=new Intent(context, OnAlarmReceiver.class);
  		PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);
    
  		Log.i("Alarm Set from HomeActivity", "Alarm Set from HomeActivity");
  		
  		mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
  			SystemClock.elapsedRealtime()+60000,
            PERIOD,
            pi);
	}
	
	public void askFriends(View v){
		Intent i = new Intent(this, CategoryActivity.class);	     
		startActivity(i);
	}
	
	public void viewReferrals(View v){
		Intent i = new Intent(this, RequestActivity.class);	     
		startActivity(i);
	}
	
	public void makeAreferral(View v){
		Intent i = new Intent(this, ReferralActivity.class);	     
		startActivity(i);
	}
		
	@Override
	protected void onResume() {
		super.onResume();
		
		TextView requestsNotviewed = (TextView)findViewById(R.id.TextView01);
		TextView responsesNotviewed = (TextView)findViewById(R.id.responses);
			    
		Cursor c4 = mDbHelper.getPendingMessages("Request");
        startManagingCursor(c4);

		if (c4.getCount()!= 0 ) {
			String numberOfRequests = Integer.toString(c4.getCount());
			requestsNotviewed.setText(numberOfRequests);
		}
		else if(c4.getCount()== 0) {
			requestsNotviewed.setBackgroundDrawable(null);
			requestsNotviewed.setText(null);
		}
		c4.close();
		
		Cursor c5 = mDbHelper.getPendingMessages("Response");
        startManagingCursor(c5);

		if (c5.getCount()!= 0) {
			String numberOfResponses = Integer.toString(c5.getCount());	
			responsesNotviewed.setText(numberOfResponses);
		}
		else if (c5.getCount()== 0) {
			responsesNotviewed.setBackgroundDrawable(null);
			responsesNotviewed.setText(null);
		}
		c5.close();	
	}
	
	public boolean isConnected() {
		String cs = Context.CONNECTIVITY_SERVICE;		
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(cs);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDbHelper.close();

		Log.i(TAG,"On Destroy");
	}
}