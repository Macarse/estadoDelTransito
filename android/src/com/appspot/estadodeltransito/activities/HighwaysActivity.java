package com.appspot.estadodeltransito.activities;

import greendroid.widget.QuickActionBar;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.adapters.HighwayAdapter;
import com.appspot.estadodeltransito.domain.highway.Highway;
import com.appspot.estadodeltransito.service.StatusService;
import com.appspot.estadodeltransito.service.asyncTasks.HighwaysAsyncTask;

public class HighwaysActivity extends AbstractActivity {

    private static final int QA_SHARE_POS = 0;
    private static final int QA_DETAILS_POS = 1;

	public static final int NOTIFICATION_ID = 101;

	private Highway mLastHighway;
	private QuickActionWidget mBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    prepareQuickActionBar();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ListAdapter getAdapter(Object items) {
		return new HighwayAdapter(this, (ArrayList<Highway>) items);
	}

	@Override
	protected IntentFilter getIntentFilter() {
		return new IntentFilter(HighwaysAsyncTask.NEW_HIGHWAYS_STATUS);
	}

	@Override
	protected BroadcastReceiver getReceiver() {
		return new HighwayUpdateReceiver();
	}

	@Override
	protected Intent getServerIntent() {
		Intent i;
		i = new Intent(this, StatusService.class);
		i.setAction(HighwaysAsyncTask.NEW_HIGHWAYS_STATUS);
		return i;
	}

	private class HighwayUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent i) {
			update(i.getExtras().get("highways"));
		}
	}

	@Override
	protected int getTitleId() {
	    return R.string.menu_highways;
	}

    private void prepareQuickActionBar() {
        mBar = new QuickActionBar(this);
        mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_share, R.string.context_menu_share));

        if (HighwayAdapter.shouldShowDetails(mLastHighway) ) {
            mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_info, R.string.details));
        }

        mBar.setOnQuickActionClickListener(mActionListener);
    }

    private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
        public void onQuickActionClicked(QuickActionWidget widget, int position) {

            Intent i = new Intent();

            switch (position) {

            case QA_SHARE_POS:
                i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.context_menu_share_subject));
                i.putExtra(Intent.EXTRA_TEXT, mLastHighway.getShareMsg());
                startActivity(Intent.createChooser(i, getString(R.string.context_menu_share)));
                break;

            case QA_DETAILS_POS:
                i = new Intent(HighwaysActivity.this, HighwayDetailsActivity.class);
                i.setAction(HighwayDetailsActivity.DETAIL_ACTION);
                i.putExtra("item", mLastHighway);
                startActivity(i);
                break;
            }
        }
    };

    @Override
    protected OnItemClickListener getOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                mLastHighway = (Highway) getListAdapter().getItem(position);
                prepareQuickActionBar();
                mBar.show(view);
            }
        };
    }
}
