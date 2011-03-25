package com.appspot.estadodeltransito.application;

import android.app.Application;
import android.preference.PreferenceManager;

public class EDTApplication extends Application{

	public int getRefreshNotificationsTime() {
		return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_update_time_key", "60"));
	}

	public int getRefreshNotificationsTimeInSeconds() {
		return getRefreshNotificationsTime() * 1000;
	}
}
