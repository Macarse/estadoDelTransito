package com.appspot.estadodeltransito.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.service.asyncTasks.AvenuesAsyncTask;
import com.appspot.estadodeltransito.service.asyncTasks.HighwaysAsyncTask;
import com.appspot.estadodeltransito.service.asyncTasks.SubwaysAsyncTask;
import com.appspot.estadodeltransito.service.asyncTasks.TrainsAsyncTask;
import com.appspot.estadodeltransito.service.receivers.AlarmReceiver;


public class StatusService extends Service {

	private static final String TAG = StatusService.class.getCanonicalName();
	private AsyncTaskProvider mTaskProvider;

	private AlarmManager mAlarms;
	private PendingIntent mAlarmIntent;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mAlarms = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intentToFire = new Intent(AlarmReceiver.ACTION_REFRESH_SUBWAY_ALARM);
		mAlarmIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);
		mTaskProvider = AsyncTaskProvider.getInstance();
		mTaskProvider.setService(this);
	}

	@Override
	public void onStart(Intent intent, int arg1) {
		super.onStart(intent, arg1);

		/* If no inet connection let the Activity know with an empty list. */
		ConnectivityManager mConnectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if ( info == null || !mConnectivity.getBackgroundDataSetting() ) {
			Log.d(TAG, "No more inet, don't set the alarm");
			sendEmptyUpdates();
			stopSelf();
			return;
		}

		mTaskProvider.addUrlFor(SubwaysAsyncTask.NEW_SUBWAYS_STATUS,getString(R.string.subways_url));
		mTaskProvider.addUrlFor(HighwaysAsyncTask.NEW_HIGHWAYS_STATUS,getString(R.string.highways_url));
		mTaskProvider.addUrlFor(AvenuesAsyncTask.NEW_AVENUES_STATUS,getString(R.string.avenues_url));
		mTaskProvider.addUrlFor(TrainsAsyncTask.NEW_TRAINS_STATUS,getString(R.string.trains_url));
		
		AsyncTask<String, ?, ?> asyncTask = mTaskProvider.getAsyncTaskFor(intent.getAction());

		/* If the service was lunch by an activity, just update what they
		 * want and leave. */
		if ( asyncTask != null ){
			asyncTask.execute(mTaskProvider.getAsyncTaskUrlFor(intent.getAction()));
			stopSelf();
			return;
		}
		
		/* Check the settings and if autoUpdate is on set the alarm for
		 * the next time */
		SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(this);
		boolean autoUpdate = prefs.getBoolean("pref_update_key", true);

		if ( autoUpdate ) {
			int updateFreq = Integer.parseInt(prefs.getString("pref_update_time_key", "60"));
	
			int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
			long timeToRefresh = SystemClock.elapsedRealtime() +
			                       updateFreq*60*1000;

			mAlarms.setInexactRepeating(alarmType, timeToRefresh,
					AlarmManager.INTERVAL_FIFTEEN_MINUTES, mAlarmIntent);


			/* We should update everything now because we were waken up
			 * by an alarm */
			updateAllServices();
			stopSelf();
		}

	}

	private void updateAllServices() {
		for(String task:mTaskProvider.getRegisteredTaskNames())
			mTaskProvider.getAsyncTaskFor(task).execute(mTaskProvider.getAsyncTaskUrlFor(task));
	}

	private void sendEmptyUpdates() {
		for(String task:mTaskProvider.getRegisteredTaskNames())
			mTaskProvider.getAsyncTaskFor(task).sendEmptyUpdates();
	}

}
