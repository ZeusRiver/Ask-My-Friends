package com.zeusriver.askmyfriends.android.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

abstract public class WakefulIntentService extends IntentService {
	abstract void doWakefulWork(Intent intent);
  
	public static final String LOCK_NAME_STATIC="com.ritereferral.mobile.service.WakefulIntentService";
	private static PowerManager.WakeLock lockStatic=null;
    
	synchronized private static PowerManager.WakeLock getLock(Context context) {
		if (lockStatic==null) {
			PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
			lockStatic=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,LOCK_NAME_STATIC);
			lockStatic.setReferenceCounted(true);
		} 
		return(lockStatic);
	}
	
	public static void sendWakefulWork(Context ctxt, Intent i) {
		getLock(ctxt.getApplicationContext()).acquire();
		ctxt.startService(i);
	}

	public static void sendWakefulWork(Context ctxt, Class<?> clsService) {
		sendWakefulWork(ctxt, new Intent(ctxt, clsService));
	}
  
	public WakefulIntentService(String name) {
		super(name);
		setIntentRedelivery(true);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if ((flags & START_FLAG_REDELIVERY)!=0) { // if crash restart...
			getLock(this.getApplicationContext()).acquire();	// ...then quick grab the lock
		}

		super.onStartCommand(intent, flags, startId);

		return(START_REDELIVER_INTENT);
	}
  
	@Override
	final protected void onHandleIntent(Intent intent) {
		try {
			doWakefulWork(intent);
		}
		finally {
			getLock(this.getApplicationContext()).release();
		}
	}
}