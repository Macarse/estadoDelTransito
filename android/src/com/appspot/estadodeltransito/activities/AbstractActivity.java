package com.appspot.estadodeltransito.activities;

import greendroid.app.GDActivity;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.admob.android.ads.AdView;
import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.preferences.Preferences;

public abstract class AbstractActivity extends GDActivity {

	private BroadcastReceiver mReceiver;
	private ProgressDialog pd;
	IntentFilter mFilter;
	AdView mButonAd;
	
//	ImageView mRefresh;
//	ImageView mRefreshSeparator;
//	ProgressBar mProgressBar;
//	ImageView mProgressBarSeparator;
	private ListView mListView;
    private ListAdapter mAdapter;

	protected abstract BroadcastReceiver getReceiver();

	protected abstract Intent getServerIntent();

	protected abstract IntentFilter getIntentFilter();

	protected abstract ListAdapter getAdapter(Object items);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_activity);
		setActionBarContentView(R.layout.list_activity);
		addActionBarItem(Type.Refresh);
		addActionBarItem(Type.Locate);
		addActionBarItem(Type.Edit);

		mListView = (ListView) findViewById(R.id.listview);
		pd = ProgressDialog.show(this, "", getString(R.string.loading_msg),
				true);
		pd.setCancelable(true);
		

		mButonAd = (AdView) findViewById(R.id.ad);
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


//		mRefresh.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mProgressBar.setVisibility(View.VISIBLE);
//				mProgressBarSeparator.setVisibility(View.VISIBLE);
//				mRefresh.setVisibility(View.GONE);
//				mRefreshSeparator.setVisibility(View.GONE);
//				startService(getServerIntent());
//			}
//		});

		registerForContextMenu(getListView());

		/* instantiate the filter and the receiver */
		mFilter = getIntentFilter();
		mReceiver = getReceiver();

		/* Start the service if it's not up */
		startService(getServerIntent());
	}

   public void startLoading() {
        LoaderActionBarItem loading = (LoaderActionBarItem) getActionBar()
                .getItem(0);
        loading.setLoading(true);
    }

    public void stopLoading() {
        LoaderActionBarItem loading = (LoaderActionBarItem) getActionBar()
                .getItem(0);
        loading.setLoading(false);
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
			Toast
					.makeText(this, getString(R.string.no_inet),
							Toast.LENGTH_LONG).show();
		}

		if (!mButonAd.isShown()) {
			mButonAd.setVisibility(View.VISIBLE);
		}
		if (pd.isShowing())
			pd.dismiss();

//		mProgressBar.setVisibility(View.GONE);
//		mProgressBarSeparator.setVisibility(View.GONE);
//		mRefresh.setVisibility(View.VISIBLE);
//		mRefreshSeparator.setVisibility(View.VISIBLE);
	}
}
