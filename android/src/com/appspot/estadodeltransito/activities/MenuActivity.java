package com.appspot.estadodeltransito.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.firstRun.FirstRun;
import com.appspot.estadodeltransito.preferences.Preferences;
import com.appspot.estadodeltransito.service.StatusService;

public class MenuActivity extends Activity {

	Button mSubways;
	Button mAvenues;
	Button mHighways;
	Button mTrains;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Check if this is the first run. If it is, run firstRun. */
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstRun = sp.getBoolean("first_run_key", false);
        if ( firstRun ) {
            FirstRun.firstRun(this);
            Editor editor = sp.edit();
            editor.putBoolean("first_run_key", false);
            editor.commit();
        }

        /* Set the view status */
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.menu);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.menu_action_bar);

        ImageView menuPref = (ImageView) findViewById(R.id.menu_preferences);
        menuPref.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), Preferences.class));
			}
		});

        ImageView menuMap = (ImageView) findViewById(R.id.menu_map);
        menuMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), MapActivity.class));
			}
		});

        mSubways = (Button) findViewById(R.id.menu_subways_button);
        mSubways.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), SubwaysActivity.class);
				startActivity(i);
				
			}
		});

        mHighways = (Button) findViewById(R.id.menu_highways_button);
        mHighways.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), HighwaysActivity.class);
				startActivity(i);
				
			}
		});

        mAvenues = (Button) findViewById(R.id.menu_avenues_button);
        mAvenues.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), AvenuesActivity.class);
				startActivity(i);
				
			}
		});
        mTrains = (Button) findViewById(R.id.menu_trains_button);
        mTrains.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), TrainsActivity.class);
				startActivity(i);
			}
		});
        
	}

	@Override
	protected void onStart() {
		super.onStart();

		/* We start the service to check for updates and set further alarms */
		startService(new Intent(this, StatusService.class));
	}
}
