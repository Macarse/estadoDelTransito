package com.appspot.estadodeltransito.service;

import java.lang.reflect.Type;
import java.util.LinkedList;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
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

import com.appspot.estadodeltransito.Highway;
import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.Subway;
import com.appspot.estadodeltransito.Train;
import com.appspot.estadodeltransito.activities.AvenuesActivity;
import com.appspot.estadodeltransito.activities.HighwaysActivity;
import com.appspot.estadodeltransito.activities.SubwaysActivity;
import com.appspot.estadodeltransito.service.receivers.AlarmReceiver;
import com.appspot.estadodeltransito.util.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class StatusService extends Service {
	private static final String TAG = "StatusService";
	public static final String NEW_SUBWAYS_STATUS = "New_Subways_Status";
	public static final String NEW_HIGHWAYS_STATUS = "New_Highways_Status";
	public static final String NEW_AVENUES_STATUS = "New_Avenues_Status";
	public static final String NEW_TRAINS_STATUS = "New_Trains_Status";

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
	}

	@Override
	public void onStart(Intent intent, int arg1) {
		super.onStart(intent, arg1);

		/* If no inet connection let the Activity know with an empty list. */
		ConnectivityManager mConnectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if ( info == null || !mConnectivity.getBackgroundDataSetting() ) {
			Log.d(TAG, "No more inet, don't set the alarm");
			sendSubwaysUpdates(null);
			sendHighwaysUpdates(null);
			sendTrainsUpdates(null);
			stopSelf();
			return;
		}

		String subways_url = getString(R.string.subways_url);
		String highways_url = getString(R.string.highways_url);
		String avenues_url = getString(R.string.avenues_url);
		String trains_url = getString(R.string.trains_url);
		
		/* If the service was lunch by an activity, just update what they
		 * want and leave. */
		if ( NEW_HIGHWAYS_STATUS.equals(intent.getAction()) ) {
			new SubwaysAsyncTask().execute(subways_url);
			stopSelf();
			return;
		} else if ( NEW_SUBWAYS_STATUS.equals(intent.getAction())) {
			new SubwaysAsyncTask().execute(subways_url);
			stopSelf();
			return;
		} else if ( NEW_AVENUES_STATUS.equals(intent.getAction()) ) {
			new HighwaysAsyncTask().execute(avenues_url);
			stopSelf();
			return;
		} else if ( NEW_TRAINS_STATUS.equals(intent.getAction()) ) {
			new TrainsAsyncTask().execute(trains_url);
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
			new SubwaysAsyncTask().execute(subways_url);
			new HighwaysAsyncTask().execute(highways_url);
			new HighwaysAsyncTask().execute(avenues_url);
			new TrainsAsyncTask().execute(trains_url);
			stopSelf();
		}

	}

	private void sendSubwaysUpdates(LinkedList<Subway> subways) {
		  Intent intent = new Intent(NEW_SUBWAYS_STATUS);
		  intent.putExtra("subways", subways);

		  sendBroadcast(intent);
	}

	private void sendHighwaysUpdates(LinkedList<Highway> highways) {
		  Intent intent = new Intent(NEW_HIGHWAYS_STATUS);
		  intent.putExtra("highways", highways);

		  sendBroadcast(intent);
	}

	private void sendTrainsUpdates(LinkedList<Train> trains) {
		  Intent intent = new Intent(NEW_TRAINS_STATUS);
		  intent.putExtra("trains", trains);

		  sendBroadcast(intent);
	}

	private boolean inArray(String[] list, String name) {
		boolean flag = false;
		for(int i=0 ; i < list.length ; i++ ) {
			if ( list[i].equals(name) ) {
				flag = true;
				break;
			}
		}

		return flag;
	}
	private void avenuesNotifications(LinkedList<Highway> highways) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean sendNotifications = sp.getBoolean("pref_notifications_key", true);
		String notificationHighways = sp.getString("pref_avenues_notifications_key", "");
		String[] avenuesList = getResources().getStringArray(R.array.avenues_list);

		/* if the user doesn't want notifications, just quit. */
        if ( !sendNotifications )
            return;

        boolean shouldNotify = false;
        for (Highway highway : highways) {
			if ( 	!highway.isOK(getString(R.string.avenues_notification_normal)) &&
					notificationHighways.contains(highway.getName()) &&
					inArray(avenuesList, highway.getName())
					) {
				shouldNotify = true;
				break;
			}
		}

        if ( !shouldNotify )
        	return;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.avenue, getString(R.string.avenues_notification_title), 
                System.currentTimeMillis()
        );
        
        CharSequence contentTitle = getString(R.string.avenues_notification_title);
        CharSequence contentText = getString(R.string.avenues_notification_text);
        Intent i = new Intent(this, AvenuesActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);
        notification.setLatestEventInfo(this.getApplicationContext(), contentTitle, contentText, contentIntent);
        mNotificationManager.notify(HighwaysActivity.NOTIFICATION_ID, notification);
	}

	private void subwaysNotifications(LinkedList<Subway> subways) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean sendNotifications = sp.getBoolean("pref_notifications_key", true);
		String notificationSubways = sp.getString("pref_subways_notifications_key", "");

		/* if the user doesn't want notifications, just quit. */
        if ( !sendNotifications )
            return;

        /* FIXME: For now we are just checking if any of the subways have
         * a different status than "Normal". If it will show a notification
         * with an intent to go to the main app. We should send the user
         * to the activity that holds information of a single line but
         * it doesn't exist :)  */
        boolean shouldNotify = false;
        for (Subway subway : subways) {
			if ( !subway.getStatus().contains(getString(R.string.subways_notification_normal)) &&
					notificationSubways.contains(subway.getLetter()+ "")
					 ) {
				shouldNotify = true;
				break;
			}
		}

        if ( !shouldNotify )
        	return;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.subway, getString(R.string.subways_notification_title), 
                System.currentTimeMillis()
        );
        
        CharSequence contentTitle = getString(R.string.subways_notification_title);
        CharSequence contentText = getString(R.string.subways_notification_text);
        Intent i = new Intent(this, SubwaysActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);
        notification.setLatestEventInfo(this.getApplicationContext(), contentTitle, contentText, contentIntent);
        mNotificationManager.notify(SubwaysActivity.NOTIFICATION_ID, notification);
	}

	private class TrainsAsyncTask extends AsyncTask<String, Void, LinkedList<Train>> {

		@Override
		protected LinkedList<Train> doInBackground(String... gaeUrl) {
			LinkedList<Train> trains = null;
			String json = Request.getJson(TAG,gaeUrl[0]);

			Gson gson = new Gson();
			Type collectionType = new TypeToken<LinkedList<Train>>(){}.getType();
			trains = gson.fromJson(json, collectionType);
			
			return trains;

		}

		@Override
		protected void onPostExecute(LinkedList<Train> trains) {
			super.onPostExecute(trains);
			sendTrainsUpdates(trains);
		}
	}

	private class HighwaysAsyncTask extends AsyncTask<String, Void, LinkedList<Highway>> {

		@Override
		protected LinkedList<Highway> doInBackground(String... gaeUrl) {
			LinkedList<Highway> highways = null;
			String json = Request.getJson(TAG,gaeUrl[0]);

			Gson gson = new Gson();
			Type collectionType = new TypeToken<LinkedList<Highway>>(){}.getType();
			highways = gson.fromJson(json, collectionType);
			
			return highways;

		}

		@Override
		protected void onPostExecute(LinkedList<Highway> highways) {
			super.onPostExecute(highways);
			sendHighwaysUpdates(highways);
			if ( highways != null ) {
				avenuesNotifications(highways);
			}
		}
	}

	private class SubwaysAsyncTask extends AsyncTask<String, Void, LinkedList<Subway>> {

		@Override
		protected LinkedList<Subway> doInBackground(String... gaeUrl) {
			LinkedList<Subway> subways = null;
			String json = Request.getJson(TAG,gaeUrl[0]);

			Gson gson = new Gson();
			Type collectionType = new TypeToken<LinkedList<Subway>>(){}.getType();
			subways = gson.fromJson(json, collectionType);
			
			return subways;
		}

		@Override
		protected void onPostExecute(LinkedList<Subway> subways) {
			super.onPostExecute(subways);

			/* If subways is null it's because there is no internet or somehow
			 * the server is dead. We send the broadcast with subways == null
			 * to let the activity show a toast to the user.
			 * On the other side, if subways == null it doesn't make sense to
			 * check if we need to send notifications.  */
			sendSubwaysUpdates(subways);
			if ( subways != null ) {
				subwaysNotifications(subways);
			}
		}
	}
}
