package com.zeusriver.askmyfriends.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zeusriver.askmyfriends.android.service.WakefulIntentService;

public class OnAlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		WakefulIntentService.sendWakefulWork(context, AppService.class);
	}
}