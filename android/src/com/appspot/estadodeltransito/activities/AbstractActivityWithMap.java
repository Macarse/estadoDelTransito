package com.appspot.estadodeltransito.activities;

import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.domain.IPublicTransportLineService;

abstract public class AbstractActivityWithMap<IPTL extends IPublicTransportLineService> extends AbstractActivity{

	protected static final int CONTEXT_MENU_SHARE = 1;
	protected static final int CONTEXT_MENU_MAP = 2;

	private String tag;

	public AbstractActivityWithMap(String tag) {
		this.tag = tag;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addActionBarItem(Type.Locate, MAP_ID);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
		
		try {
		    info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
		    Log.e(tag, "bad menuInfo", e);
		    return false;
		}

		Intent i;
		IPTL publicTransportService;

		switch (item.getItemId()) {

		case CONTEXT_MENU_SHARE:
			publicTransportService = (IPTL) getListAdapter().getItem(info.position);
			i = new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_SUBJECT, R.string.context_menu_share_subject);
			i.putExtra(Intent.EXTRA_TEXT, publicTransportService.getShareMsg());
			startActivity(Intent.createChooser(i, getString(R.string.context_menu_share)));
			return true;

		case CONTEXT_MENU_MAP:
			publicTransportService = (IPTL) getListAdapter().getItem(info.position);
			i = new Intent(this, MapActivity.class);
			i.setAction(getEachMapAction());
			i.putExtra("line", publicTransportService);
			startActivity(i);
			return true;
		}

		return false;
	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
	    switch (item.getItemId()) {
        case MAP_ID:
        	Intent i = new Intent(this, MapActivity.class);
			i.setAction(getMapAction());
            startActivity(i);
            return true;

        default:
            break;
        }
	    return super.onHandleActionBarItemClick(item, position);
	}

	abstract protected String getMapAction();

	abstract protected String getEachMapAction();
}
