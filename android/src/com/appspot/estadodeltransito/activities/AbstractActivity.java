package com.appspot.estadodeltransito.activities;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.QuickAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.admob.android.ads.AdView;
import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.preferences.Preferences;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public abstract class AbstractActivity extends GDActivity {

	private BroadcastReceiver mReceiver;
	IntentFilter mFilter;

	protected static final int REFRESH_ID = 1;
    protected static final int MAP_ID = 2;
    protected static final int SETTINGS_ID = 3;

	private ListView mListView;
    private ListAdapter mAdapter;
    private LoaderActionBarItem mLoader;
    AdView mAd;
    private GoogleAnalyticsTracker tracker;

	protected abstract BroadcastReceiver getReceiver();

	protected abstract Intent getServerIntent();

	protected abstract IntentFilter getIntentFilter();

	protected abstract ListAdapter getAdapter(Object items);

	protected abstract int getTitleId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start(getString(R.string.google_analytics), 20, this);

		setActionBarContentView(R.layout.list_activity);
		mLoader = (LoaderActionBarItem) addActionBarItem(Type.Refresh, REFRESH_ID);
		addActionBarItem(Type.Settings, SETTINGS_ID);
		setTitle(getTitleId());

		mAd = (AdView) findViewById(R.id.ad);
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(getOnItemClickListener());

		AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        mListView.setLayoutAnimation(controller);

		/* instantiate the filter and the receiver */
		mFilter = getIntentFilter();
		mReceiver = getReceiver();

		/* Start the service if it's not up */
		mLoader.setLoading(true);
		startService(getServerIntent());
	}

    protected abstract OnItemClickListener getOnItemClickListener();

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch ( item.getItemId() ) {

        case SETTINGS_ID:
            tracker.trackEvent(
                    "Abstract",  // Category
                    "ActionBar",  // Action
                    "settings", // Label
                    1);
            startActivity(new Intent(this, Preferences.class));
            return true;

        case REFRESH_ID:
            tracker.trackEvent(
                    "Abstract",  // Category
                    "ActionBar",  // Action
                    "refresh", // Label
                    1);
            startService(getServerIntent());
            return true;

        default:
            break;
        }
        
        return super.onHandleActionBarItemClick(item, position);
    }

    private void setListAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }

    protected Adapter getListAdapter() {
        return mAdapter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        tracker.trackPageView(getString(getTitleId()));
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

	    mLoader.setLoading(false);
	}

    @Override
    protected void onDestroy() {
      super.onDestroy();
      tracker.stop();
    }

    protected static class MyQuickAction extends QuickAction {
        
        private static final ColorFilter BLACK_CF = new LightingColorFilter(Color.BLACK, Color.BLACK);

        public MyQuickAction(Context ctx, int drawableId, int titleId) {
            super(ctx, buildDrawable(ctx, drawableId), titleId);
        }

        private static Drawable buildDrawable(Context ctx, int drawableId) {
            Drawable d = ctx.getResources().getDrawable(drawableId);
            d.setColorFilter(BLACK_CF);
            return d;
        }

    }
}
