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
import com.appspot.estadodeltransito.adapters.TrainAdapter;
import com.appspot.estadodeltransito.domain.train.Train;
import com.appspot.estadodeltransito.service.StatusService;
import com.appspot.estadodeltransito.service.asyncTasks.TrainsAsyncTask;

public class TrainsActivity extends AbstractActivityWithMap<Train> {

    private static final String TAG = TrainsActivity.class.getCanonicalName();
    private static final int QA_SHARE_POS = 0;
    private static final int QA_MAP_POS = 1;

    private Train mLastTrain;
    private QuickActionWidget mBar;

    public TrainsActivity() {
        super(TAG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareQuickActionBar();
    }

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

    @SuppressWarnings("unchecked")
    @Override
    protected ListAdapter getAdapter(Object items) {
        return new TrainAdapter(this, (ArrayList<Train>) items);
    }

    @Override
    protected String getMapAction() {
        return MapActivity.SHOW_TRAINS_ACTION;
    }

    @Override
    protected String getEachMapAction() {
        return MapActivity.SHOW_TRAIN_ACTION;
    }

    private void prepareQuickActionBar() {
        mBar = new QuickActionBar(this);
        mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_share, R.string.context_menu_share));
        mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_locate, R.string.context_menu_map));

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
                i.putExtra(Intent.EXTRA_TEXT, mLastTrain.getShareMsg());
                startActivity(Intent.createChooser(i, getString(R.string.context_menu_share)));
                break;

            case QA_MAP_POS:
                i = new Intent(TrainsActivity.this, MapActivity.class);
                i.setAction(getEachMapAction());
                i.putExtra("line", mLastTrain);
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

                mLastTrain= (Train) getListAdapter().getItem(position);
                mBar.show(view);
            }
        };
    }
}
