package com.appspot.estadodeltransito.application;

import com.appspot.estadodeltransito.activities.MenuActivity;

import greendroid.app.GDApplication;
import android.preference.PreferenceManager;

public class EDTApplication extends GDApplication {

	public int getRefreshNotificationsTime() {
		return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_update_time_key", "60"));
	}

	public int getRefreshNotificationsTimeInSeconds() {
		return getRefreshNotificationsTime() * 1000;
	}
	
	@Override
	public Class<?> getHomeActivityClass() {
	    return MenuActivity.class;
	}
}
