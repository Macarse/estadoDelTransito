package com.appspot.estadodeltransito.activities;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.firstRun.FirstRun;
import com.appspot.estadodeltransito.preferences.Preferences;
import com.appspot.estadodeltransito.service.StatusService;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class MenuActivity extends GDActivity {
    private static final String FIRST_RUN_KEY = "first_run_key";
    private static final int MAP_ID = 1;
    private static final int SETTINGS_ID = 2;

	private Button mSubways;
	private Button mAvenues;
	private Button mHighways;
	private Button mTrains;
	private GoogleAnalyticsTracker tracker;

	public MenuActivity() {
	    super(ActionBar.Type.Normal);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start(getString(R.string.google_analytics), 20, this);

		/* Check if this is the first run. If it is, run firstRun. */
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

		boolean firstRun = sp.getBoolean(FIRST_RUN_KEY, false);
        if ( firstRun ) {
            FirstRun.firstRun(this);
            Editor editor = sp.edit();
            editor.putBoolean(FIRST_RUN_KEY, false);
            editor.commit();

            setupService();
        }

        /* Set the view status */
        setActionBarContentView(R.layout.menu);
        setTitle(R.string.app_name);
        addActionBarItem(Type.Settings, SETTINGS_ID);
        addActionBarItem(Type.Locate, MAP_ID);

        mSubways = (Button) findViewById(R.id.menu_subways_button);
        mSubways.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    tracker.trackEvent(
			            "Menu",  // Category
			            "Button",  // Action
			            "subways", // Label
			            1);

				Intent i = new Intent(v.getContext(), SubwaysActivity.class);
				startActivity(i);
				
			}
		});

        mHighways = (Button) findViewById(R.id.menu_highways_button);
        mHighways.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    tracker.trackEvent(
                        "Menu",  // Category
                        "Button",  // Action
                        "highways", // Label
                        1);
				Intent i = new Intent(v.getContext(), HighwaysActivity.class);
				startActivity(i);
				
			}
		});

        mAvenues = (Button) findViewById(R.id.menu_avenues_button);
        mAvenues.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    tracker.trackEvent(
                        "Menu",  // Category
                        "Button",  // Action
                        "avenues", // Label
                        1);
				Intent i = new Intent(v.getContext(), AvenuesActivity.class);
				startActivity(i);
				
			}
		});
        mTrains = (Button) findViewById(R.id.menu_trains_button);
        mTrains.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    tracker.trackEvent(
                        "Menu",  // Category
                        "Button",  // Action
                        "trains", // Label
                        1);
				Intent i = new Intent(v.getContext(), TrainsActivity.class);
				startActivity(i);
			}
		});
        
	}

	@Override
	protected void onStart() {
	    super.onStart();
	    tracker.trackPageView("/MenuActivity");
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
	    switch ( item.getItemId() ) {
        case MAP_ID:
            tracker.trackEvent(
                    "Menu",  // Category
                    "ActionBar",  // Action
                    "map", // Label
                    1);
            Toast.makeText(this, getString(R.string.map_not_available), Toast.LENGTH_SHORT).show();
            return true;

        case SETTINGS_ID:
            tracker.trackEvent(
                    "Menu",  // Category
                    "ActionBar",  // Action
                    "settings", // Label
                    1);
            startActivity(new Intent(this, Preferences.class));
            return true;

        default:
            break;
        }

	    return super.onHandleActionBarItemClick(item, position);
	}

	private void setupService() {

		/* We start the service to check for updates and set further alarms */
		Intent i = new Intent(this, StatusService.class);
		i.setAction(StatusService.FIRST_RUN);
		startService(i);
	}

	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    tracker.stop();
	  }
}
