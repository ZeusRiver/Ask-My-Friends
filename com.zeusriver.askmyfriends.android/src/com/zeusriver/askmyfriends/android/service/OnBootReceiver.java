package com.zeusriver.askmyfriends.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {
	
	private static final int PERIOD=600000;   // 10 minute
	private static final String TAG = "RiteReferral";

  	@Override
	public void onReceive(Context context, Intent intent) {
  		
  		try {
  			if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            	
  				AlarmManager mgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
  				  				
          		Intent i=new Intent(context, OnAlarmReceiver.class);
          		PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);
            
          		Log.e("Screen on Intent Received", "Test");
          		
          		mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
          			SystemClock.elapsedRealtime()+60000,
                    PERIOD,
                    pi);
            	
            		Log.d(TAG, "Notified of boot");
            }
            
  		} catch (Exception e) {
  		Log.d(TAG, "An alarm was received but there was an error");
  		e.printStackTrace();
  		}
      }
}