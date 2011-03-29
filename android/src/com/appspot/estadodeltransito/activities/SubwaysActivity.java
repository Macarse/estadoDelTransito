package com.appspot.estadodeltransito.activities;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.adapters.SubwayAdapter;
import com.appspot.estadodeltransito.domain.subway.Subway;
import com.appspot.estadodeltransito.service.StatusService;
import com.appspot.estadodeltransito.service.asyncTasks.SubwaysAsyncTask;
import com.appspot.estadodeltransito.util.IconsUtil;

public class SubwaysActivity extends AbstractActivityWithMap<Subway> {

	public static final int NOTIFICATION_ID = 100;

	private static final String TAG = SubwaysActivity.class.getCanonicalName();

	public SubwaysActivity() {
		super(TAG);
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
		menu.setHeaderIcon(IconsUtil.getSubwayIcon(subway.getName()));
		
		menu.add(0, CONTEXT_MENU_SHARE, 0, R.string.context_menu_share);
		menu.add(0, CONTEXT_MENU_MAP, 0, R.string.context_menu_map);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ListAdapter getAdapter(Object items) {
		return new SubwayAdapter(this, (ArrayList<Subway>) items);
	}
	
	@Override
	protected String getMapAction() {
		return MapActivity.SHOW_SUBWAYS_ACTION;
	}

	@Override
	protected String getEachMapAction() {
		return MapActivity.SHOW_SUBWAY_ACTION;
	}
}
