package com.appspot.estadodeltransito.activities;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.appspot.estadodeltransito.Highway;
import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.adapters.HighwayAdapter;
import com.appspot.estadodeltransito.service.StatusService;

public class HighwaysActivity extends AbstractActivity {

	private static final int MENU_MAIN = 1;
	private static final int CONTEXT_MENU_SHARE = MENU_MAIN + 1;
	private static final int CONTEXT_MENU_DETAILS = MENU_MAIN + 2;
	public static final int NOTIFICATION_ID = 101;

	private static final String TAG = "HighwaysActivity";

	@SuppressWarnings("unchecked")
	@Override
	protected ListAdapter getAdapter(Object items) {
		return new HighwayAdapter(this, (ArrayList<Highway>) items);
	}

	@Override
	protected IntentFilter getIntentFilter() {
		return new IntentFilter(StatusService.NEW_HIGHWAYS_STATUS);
	}

	@Override
	protected BroadcastReceiver getReceiver() {
		return new HighwayUpdateReceiver();
	}

	@Override
	protected Intent getServerIntent() {
		Intent i;
		i = new Intent(this, StatusService.class);
		i.setAction(StatusService.NEW_HIGHWAYS_STATUS);
		return i;
	}

	private class HighwayUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent i) {
			update(i.getExtras().get("highways"));
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			Log.e(TAG, "bad menuInfo", e);
			return;
		}

		Highway highway = (Highway) getListAdapter().getItem(info.position);

		menu.setHeaderTitle(highway.getName());
		menu.setHeaderIcon(HighwayAdapter.getIcon(highway.getName()));

		menu.add(0, CONTEXT_MENU_SHARE, 0, R.string.context_menu_share);

		if (HighwayAdapter.shouldShowDetails(highway) ) {
			menu.add(0, CONTEXT_MENU_DETAILS, 0, R.string.details);
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;

		try {
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
			Log.e(TAG, "bad menuInfo", e);
			return false;
		}

		Intent i;
		Highway highway;
		switch (item.getItemId()) {

		case MENU_MAIN:
			i = new Intent(this, MenuActivity.class);
			startActivity(i);
			return true;

		case CONTEXT_MENU_SHARE:
			highway = (Highway) getListAdapter().getItem(info.position);
			i = new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_SUBJECT, R.string.context_menu_share_subject);
			i.putExtra(Intent.EXTRA_TEXT, highway.getShareMsg());
			startActivity(Intent.createChooser(i, getString(R.string.context_menu_share)));
			return true;

		case CONTEXT_MENU_DETAILS:
			highway = (Highway) getListAdapter().getItem(info.position);
			i = new Intent(this, HighwayDetailsActivity.class);
			i.setAction(HighwayDetailsActivity.DETAIL_ACTION);
			i.putExtra("item", highway);
			startActivity(i);
		}

		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_MAIN, 0, R.string.menu_home).setIcon(
				R.drawable.ic_menu_home);

		return true;
	}
}
