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
import com.appspot.estadodeltransito.activities.SubwaysActivity;
import com.appspot.estadodeltransito.domain.subway.Subway;
import com.google.gson.reflect.TypeToken;

public class SubwaysAsyncTask extends BaseAsyncTask<Subway> {
	
	public SubwaysAsyncTask(Service service) {
		super(service);
	}

	public static final String NEW_SUBWAYS_STATUS = "New_Subways_Status";
	
	@Override
	protected String getIntentName() {
		return NEW_SUBWAYS_STATUS;
	}

	@Override
	protected String getTypeName() {
		return "subways";
	}

	@Override
	protected void sendNotifications(LinkedList<Subway> instances) {
		subwaysNotifications(instances);
	}
	
	private void subwaysNotifications(LinkedList<Subway> subways) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getService());
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
			if ( !subway.getStatus().contains(getService().getString(R.string.subways_notification_normal)) &&
					notificationSubways.contains(subway.getLetter()+ "")
					 ) {
				shouldNotify = true;
				break;
			}
		}

        if ( !shouldNotify )
        	return;

        NotificationManager mNotificationManager = (NotificationManager) getService().getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.subway, getService().getString(R.string.subways_notification_title), 
                System.currentTimeMillis()
        );
        
        CharSequence contentTitle = getService().getString(R.string.subways_notification_title);
        CharSequence contentText = getService().getString(R.string.subways_notification_text);
        Intent i = new Intent(getService(), SubwaysActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(getService(), 0, i, 0);
        notification.setLatestEventInfo(getService().getApplicationContext(), contentTitle, contentText, contentIntent);
        mNotificationManager.notify(SubwaysActivity.NOTIFICATION_ID, notification);
	}

	@Override
	protected Type getDeserializationType() {
		return new TypeToken<LinkedList<Subway>>(){}.getType();
	}
}
