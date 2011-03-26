package com.appspot.estadodeltransito.activities;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.adapters.TrainAdapter;
import com.appspot.estadodeltransito.domain.train.Train;
import com.appspot.estadodeltransito.service.StatusService;
import com.appspot.estadodeltransito.service.asyncTasks.TrainsAsyncTask;

public class TrainsActivity extends AbstractActivity {

	private static final int CONTEXT_MENU_SHARE = 1;

	private static final String TAG = TrainsActivity.class.getCanonicalName();

	@Override
	protected BroadcastReceiver getReceiver() {
		return new TrainUpdateReceiver();
	}

	@Override
	protected Intent getServerIntent() {
		Intent i;
		i = new Intent(this, StatusService.class);
		i.setAction(TrainsAsyncTask.NEW_TRAINS_STATUS);
		return i;
	}

	@Override
	protected int getTitleId() {
	    return R.string.menu_trains;
	}

	@Override
	protected IntentFilter getIntentFilter() {
		return new IntentFilter(TrainsAsyncTask.NEW_TRAINS_STATUS);
	}

	private class TrainUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent i) {
			update(i.getExtras().get("trains"));
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

		Train train = (Train) getListAdapter().getItem(info.position);

		String title = String.format(getString(R.string.context_menu_share_title_fmt), train.getName());
		menu.setHeaderTitle(title);
		menu.setHeaderIcon(TrainAdapter.getIcon(train));
		
		menu.add(0, CONTEXT_MENU_SHARE, 0, R.string.context_menu_share);
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
		switch (item.getItemId()) {

		case CONTEXT_MENU_SHARE:
			Train train = (Train) getListAdapter().getItem(info.position);
			i = new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_SUBJECT, R.string.context_menu_share_subject);
			i.putExtra(Intent.EXTRA_TEXT, train.getShareMsg());
			startActivity(Intent.createChooser(i, getString(R.string.context_menu_share)));
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ListAdapter getAdapter(Object items) {
		return new TrainAdapter(this, (ArrayList<Train>) items);
	}
}
