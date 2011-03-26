package com.appspot.estadodeltransito.activities;

import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.adapters.SubwayAdapter;
import com.appspot.estadodeltransito.domain.subway.Subway;
import com.appspot.estadodeltransito.service.StatusService;
import com.appspot.estadodeltransito.service.asyncTasks.SubwaysAsyncTask;

public class SubwaysActivity extends AbstractActivity {

	public static final int NOTIFICATION_ID = 100;
	private static final int CONTEXT_MENU_SHARE = 1;
	private static final int CONTEXT_MENU_MAP = 2;

	private static final String TAG = SubwaysActivity.class.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addActionBarItem(Type.Locate, MAP_ID);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
	    switch (item.getItemId()) {
        case MAP_ID:
            startActivity(new Intent(this, MapActivity.class));
            return true;

        default:
            break;
        }
	    return super.onHandleActionBarItemClick(item, position);
	}
	@Override
	protected BroadcastReceiver getReceiver() {
		return new SubwayUpdateReceiver();
	}

	@Override
	protected Intent getServerIntent() {
		Intent i;
		i = new Intent(this, StatusService.class);
		i.setAction(SubwaysAsyncTask.NEW_SUBWAYS_STATUS);
		return i;
	}

	@Override
	protected int getTitleId() {
	    return R.string.menu_subways;
	}

	@Override
	protected IntentFilter getIntentFilter() {
		return new IntentFilter(SubwaysAsyncTask.NEW_SUBWAYS_STATUS);
	}

	private class SubwayUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent i) {
			update(i.getExtras().get("subways"));
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

		Subway subway = (Subway) getListAdapter().getItem(info.position);

		String title = String.format(getString(R.string.context_menu_share_title_fmt), subway.getLetter());
		menu.setHeaderTitle(title);
		menu.setHeaderIcon(SubwayAdapter.getIcon(subway));
		
		menu.add(0, CONTEXT_MENU_SHARE, 0, R.string.context_menu_share);
		menu.add(0, CONTEXT_MENU_MAP, 0, R.string.context_menu_map);
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
		Subway subway;

		switch (item.getItemId()) {

		case CONTEXT_MENU_SHARE:
			subway = (Subway) getListAdapter().getItem(info.position);
			i = new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_SUBJECT, R.string.context_menu_share_subject);
			i.putExtra(Intent.EXTRA_TEXT, subway.getShareMsg());
			startActivity(Intent.createChooser(i, getString(R.string.context_menu_share)));
			return true;

		case CONTEXT_MENU_MAP:
			subway = (Subway) getListAdapter().getItem(info.position);
			i = new Intent(this, MapActivity.class);
			i.setAction(MapActivity.SHOW_SUBWAY_ACTION);
			i.putExtra("subway_line", subway);
			startActivity(i);
			return true;
		}

		return false;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected ListAdapter getAdapter(Object items) {
		return new SubwayAdapter(this, (ArrayList<Subway>) items);
	}
}
