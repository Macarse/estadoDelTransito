package com.appspot.estadodeltransito.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.estadodeltransito.Highway;
import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.Highway.Text;
import com.appspot.estadodeltransito.adapters.HighwayAdapter;
import com.appspot.estadodeltransito.preferences.Preferences;

public class HighwayDetailsActivity extends Activity {
	private static final int MENU_MAIN = 1;
	public static final String DETAIL_ACTION = "DETAIL_ACTION";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Set the view status */
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.highway_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.menu_action_bar);

		ImageView menuPref = (ImageView) findViewById(R.id.menu_preferences);
		menuPref.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), Preferences.class);
				startActivity(i);
			}
		});

		Intent i = getIntent();
		if (DETAIL_ACTION.equals(i.getAction())) {
			Highway item = (Highway) i.getExtras().get("item");
			fillView(item);
		}
	}

	private void fillView(Highway highway) {
		ImageView iv;
		TextView tv;
		TextView direction;
		ImageView status;
		TextView smallText;
		TextView longText;

		iv = (ImageView) findViewById(R.id.avenue_highways_details_icon);
		iv.setImageResource(HighwayAdapter.getIcon(highway.getName()));

		tv = (TextView) findViewById(R.id.avenue_highways_details_title);
		tv.setText(highway.getName());

		direction = (TextView) findViewById(R.id.avenue_highways_details_from);
		status =  (ImageView) findViewById(R.id.avenue_highways_details_from_status);
		smallText = (TextView) findViewById(R.id.avenue_highways_details_from_text);
		longText = (TextView) findViewById(R.id.avenue_highways_details_from_explanation);

		if ( highway.getDirectionFrom() != null ) {
			direction.setText(highway.getDirectionFrom());
			status.setImageResource(HighwayAdapter.getStatusIcon(highway
					.getStatusFrom()));
			smallText.setText(highway.getDelayFrom());
			longText.setText(convertToLongText(highway.getStatusMessageFrom()));
	
		} else {
			direction.setVisibility(View.GONE);
			status.setVisibility(View.GONE);
			smallText.setVisibility(View.GONE);
			longText.setVisibility(View.GONE);
		}


		direction = (TextView) findViewById(R.id.avenue_highways_details_to);
		status =  (ImageView) findViewById(R.id.avenue_highways_details_to_status);
		smallText = (TextView) findViewById(R.id.avenue_highways_details_to_text);
		longText = (TextView) findViewById(R.id.avenue_highways_details_to_explanation);

		if ( highway.getDirectionTo() != null ) {
			direction.setText(highway.getDirectionTo());
			status.setImageResource(HighwayAdapter.getStatusIcon(highway
					.getStatusTo()));
			smallText.setText(highway.getDelayTo());
			longText.setText(convertToLongText(highway.getStatusMessageTo()));
	
		} else {
			direction.setVisibility(View.GONE);
			status.setVisibility(View.GONE);
			smallText.setVisibility(View.GONE);
			longText.setVisibility(View.GONE);
		}
	}

	private static final String convertToLongText(Text text) {
		if (text != null) {
			return "- " + text.getValue();
		} else {
			return null;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_MAIN, 0, R.string.menu_home).setIcon(
				R.drawable.ic_menu_home);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent i;
		switch (item.getItemId()) {

			case MENU_MAIN:
				i = new Intent(this, MenuActivity.class);
				startActivity(i);
				return true;
		}

		return false;
	}
}
