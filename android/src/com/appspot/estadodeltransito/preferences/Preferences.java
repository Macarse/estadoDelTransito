package com.appspot.estadodeltransito.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.appspot.estadodeltransito.R;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}
}
