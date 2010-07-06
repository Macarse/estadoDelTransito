package com.appspot.estadodeltransito.service.receivers;

import com.appspot.estadodeltransito.service.StatusService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		Log.d("REC", "connectivityManager.getActiveNetworkInfo(): " + connectivityManager.getActiveNetworkInfo());
		if ( connectivityManager.getActiveNetworkInfo() != null )
			context.startService(new Intent(context, StatusService.class));

	}	
} 