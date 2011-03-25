package com.appspot.estadodeltransito.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.application.EDTApplication;
import com.appspot.estadodeltransito.service.asyncTasks.AvenuesAsyncTask;
import com.appspot.estadodeltransito.service.asyncTasks.HighwaysAsyncTask;
import com.appspot.estadodeltransito.service.asyncTasks.SubwaysAsyncTask;
import com.appspot.estadodeltransito.service.asyncTasks.TrainsAsyncTask;
import com.appspot.estadodeltransito.service.receivers.AlarmReceiver;
import com.appspot.estadodeltransito.service.receivers.ConnectivityReceiver;


public class StatusService extends Service {

    public static final String FIRST_RUN = "FIRST_RUN";

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
		Log.d(TAG, "SERVICE STARTED");

		/* We were called by a change in the connection status */
		if (ConnectivityReceiver.INET_ACTION.equals(intent.getAction())) {

		    Log.d(TAG, "Started by the Connectivity receiver");
			/* If no inet connection do nothing and cancel the alarm. */
			ConnectivityManager mConnectivity = (ConnectivityManager) this
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = mConnectivity.getActiveNetworkInfo();

			if (info == null || !info.isConnected() || !mConnectivity.getBackgroundDataSetting()) {
				Log.d(TAG, "No inet, cancel the the alarm");
				mAlarms.cancel(mAlarmIntent);
			} else {
				setAlarmAndUpdate();
			}
			stopSelf();
			return;
		}
		if (AlarmReceiver.ACTION_REFRESH_ALARM.equals(intent.getAction())) {
		    Log.d(TAG, "Started by the Alarm receiver");
			setAlarmAndUpdate();
			stopSelf();
			return;
		}

		Log.d(TAG, "Performing update with action: " + intent.getAction());

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

	}

	private void setAlarmAndUpdate() {
		/* If we reach here, there is inet */
		setAlarm();

		if (!shouldUpdate()) {
			Log.d(TAG, "We don't update because we didn't wait enough");
		} else {
			setLastUpdateTime(SystemClock.elapsedRealtime());
			updateAllServices();
		}
	}
	
	private void setAlarm() {
		/*
		 * Check the settings and if autoUpdate is on set the alarm for the next
		 * time
		 */
		if (isUpdateNotifications()) {
			Log.d(TAG, "Setting the alarm");
			int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
			mAlarms.setInexactRepeating(alarmType,
					SystemClock.elapsedRealtime() + timeToRefresh(),
					AlarmManager.INTERVAL_HALF_DAY, mAlarmIntent);
		}
	}
	
	private long timeToRefresh() {
		return getEDTApplication().getRefreshNotificationsTime() * 1000L * 60L;
	}
	
	public EDTApplication getEDTApplication(){
		return (EDTApplication) getApplication();
	}

	private boolean shouldUpdate() {
		if ( (getLastUpdateTime() + timeToRefresh()) < SystemClock.elapsedRealtime()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isUpdateNotifications() {
		return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_update_key", true);
	}

	public Long getLastUpdateTime() {
		return PreferenceManager.getDefaultSharedPreferences(this).getLong("last_update", 0);
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putLong("last_update", lastUpdateTime);
		editor.commit();
	}

	private void updateAllServices() {
		for(String task:mTaskProvider.getRegisteredTaskNames())
			mTaskProvider.getAsyncTaskFor(task).execute(mTaskProvider.getAsyncTaskUrlFor(task));
	}
/*
	private void sendEmptyUpdates() {
		for(String task:mTaskProvider.getRegisteredTaskNames())
			mTaskProvider.getAsyncTaskFor(task).sendEmptyUpdates();
	}
*/
}
