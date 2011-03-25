package com.appspot.estadodeltransito.service.receivers;

import com.appspot.estadodeltransito.service.StatusService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {

	public static final String INET_ACTION = "INET_ACTION";

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.d("REC", "connectivityManager.getActiveNetworkInfo(): " + connectivityManager.getActiveNetworkInfo());
		Intent i = new Intent(context, StatusService.class);
		i.setAction(INET_ACTION);
		context.startService(i);
	}
	
} 