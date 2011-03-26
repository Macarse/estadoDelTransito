package com.appspot.estadodeltransito.activities;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.LoaderActionBarItem;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.admob.android.ads.AdView;
import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.preferences.Preferences;

public abstract class AbstractActivity extends GDActivity {

	private BroadcastReceiver mReceiver;
	private ProgressDialog pd;
	IntentFilter mFilter;

	protected static final int REFRESH_ID = 1;
    protected static final int MAP_ID = 2;
    protected static final int SETTINGS_ID = 3;

	private ListView mListView;
    private ListAdapter mAdapter;
    private LoaderActionBarItem mLoader;
    AdView mAd;

	protected abstract BroadcastReceiver getReceiver();

	protected abstract Intent getServerIntent();

	protected abstract IntentFilter getIntentFilter();

	protected abstract ListAdapter getAdapter(Object items);

	protected abstract int getTitleId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setActionBarContentView(R.layout.list_activity);
		addActionBarItem(Type.Refresh, REFRESH_ID);
		addActionBarItem(Type.Settings, SETTINGS_ID);
		setTitle(getTitleId());

		mAd = (AdView) findViewById(R.id.ad);
		mListView = (ListView) findViewById(R.id.listview);
		pd = ProgressDialog.show(this, "", getString(R.string.loading_msg),
				true);
		pd.setCancelable(true);

		registerForContextMenu(getListView());

		/* instantiate the filter and the receiver */
		mFilter = getIntentFilter();
		mReceiver = getReceiver();

		/* Start the service if it's not up */
		startService(getServerIntent());
	}

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch ( item.getItemId() ) {
        case SETTINGS_ID:
            startActivity(new Intent(this, Preferences.class));
            return true;

        case REFRESH_ID:
            mLoader = (LoaderActionBarItem) item;
            startService(getServerIntent());
            return true;

        default:
            break;
        }
        
        return super.onHandleActionBarItemClick(item, position);
    }


	private View getListView() {
	    return mListView;
    }

    private void setListAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }

    protected Adapter getListAdapter() {
        return mAdapter;
    }

    @Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}

	protected void update(Object items) {
		if (items != null) {
			ListAdapter adapter = getAdapter(items);
			setListAdapter(adapter);
		} else {
			Toast.makeText(this, getString(R.string.no_inet), Toast.LENGTH_LONG).show();
		}

		if (!mAd.isShown()) {
		    mAd.setVisibility(View.VISIBLE);
		}

		if (pd.isShowing()) {
		    pd.dismiss();
		}

		if ( mLoader != null ) {
		    mLoader.setLoading(false);
		}
	}
}
