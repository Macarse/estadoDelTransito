package com.appspot.estadodeltransito.service.asyncTasks;

import java.lang.reflect.Type;
import java.util.LinkedList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.activities.HighwaysActivity;
import com.appspot.estadodeltransito.domain.highway.Highway;
import com.google.gson.reflect.TypeToken;

public class HighwaysAsyncTask extends BaseAsyncTask<Highway> {
	
	public static final String NEW_HIGHWAYS_STATUS = "New_Highways_Status";

	public HighwaysAsyncTask(Service service) {
		super(service, NEW_HIGHWAYS_STATUS);
	}
	
	public HighwaysAsyncTask(Service service, String taskName) {
		super(service, taskName);
	}

	@Override
	protected String getIntentName() {
		return NEW_HIGHWAYS_STATUS;
	}

	@Override
	protected String getTypeName() {
		return "highways";
	}

	@Override
	protected void sendNotifications(LinkedList<Highway> instances) {
		highwayAvenueNotifications(instances);
	}

	private void highwayAvenueNotifications(LinkedList<Highway> highways) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getService());
		boolean sendNotifications = sp.getBoolean("pref_notifications_key", true);
		String notificationHighways = sp.getString("pref_avenues_notifications_key", "");
		String[] avenuesList = getService().getResources().getStringArray(R.array.avenues_list);

		/* if the user doesn't want notifications, just quit. */
        if ( !sendNotifications )
            return;

        boolean shouldNotify = false;
        for (Highway highway : highways) {
			if ( 	!highway.isOK(getService().getString(R.string.avenues_notification_normal)) &&
					notificationHighways.contains(highway.getName()) &&
					inArray(avenuesList, highway.getName())
					) {
				shouldNotify = true;
				break;
			}
		}

        if ( !shouldNotify )
        	return;

        NotificationManager mNotificationManager = (NotificationManager) getService().getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.avenue, getService().getString(R.string.avenues_notification_title), 
                System.currentTimeMillis()
        );
        
        CharSequence contentTitle = getService().getString(R.string.avenues_notification_title);
        CharSequence contentText = getService().getString(R.string.avenues_notification_text);
        Intent i = new Intent(getService(), getActivityClass());

        PendingIntent contentIntent = PendingIntent.getActivity(getService(), 0, i, 0);
        notification.setLatestEventInfo(getService().getApplicationContext(), contentTitle, contentText, contentIntent);
        mNotificationManager.notify(HighwaysActivity.NOTIFICATION_ID, notification);
	}
	
	protected Class<?> getActivityClass() {
		return HighwaysActivity.class;
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

	@Override
	protected Type getDeserializationType() {
		return new TypeToken<LinkedList<Highway>>(){}.getType();
	}
}