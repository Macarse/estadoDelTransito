package com.appspot.estadodeltransito.activities;

import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionBar;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.adapters.SubwayAdapter;
import com.appspot.estadodeltransito.domain.subway.Subway;
import com.appspot.estadodeltransito.service.StatusService;
import com.appspot.estadodeltransito.service.asyncTasks.SubwaysAsyncTask;

public class SubwaysActivity extends AbstractActivityWithMap<Subway> {

    private static final String TAG = SubwaysActivity.class.getCanonicalName();
	public static final int NOTIFICATION_ID = 100;
	private static final int QA_SHARE_POS = 0;
	private static final int QA_MAP_POS = 1;

	private Subway mLastSubway;
	private QuickActionWidget mBar;

	public SubwaysActivity() {
		super(TAG);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    prepareQuickActionBar();
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

    private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
        public void onQuickActionClicked(QuickActionWidget widget, int position) {

            Intent i = new Intent();

            switch (position) {

            case QA_SHARE_POS:
                i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, R.string.context_menu_share_subject);
                i.putExtra(Intent.EXTRA_TEXT, mLastSubway.getShareMsg());
                startActivity(Intent.createChooser(i, getString(R.string.context_menu_share)));
                break;

            case QA_MAP_POS:
                i = new Intent(SubwaysActivity.this, MapActivity.class);
                i.setAction(getEachMapAction());
                i.putExtra("line", mLastSubway);
                startActivity(i);
                break;
            }
        }
    };

    private void prepareQuickActionBar() {
        mBar = new QuickActionBar(this);
        mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_share, R.string.context_menu_share));
        mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_locate, R.string.context_menu_map));

        mBar.setOnQuickActionClickListener(mActionListener);
    }

    @Override
    protected OnItemClickListener getOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                mLastSubway = (Subway) getListAdapter().getItem(position);
                mBar.show(view);
            }
        };
    }
}
