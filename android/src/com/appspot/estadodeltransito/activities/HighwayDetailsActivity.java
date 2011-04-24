package com.appspot.estadodeltransito.activities;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.adapters.HighwayAdapter;
import com.appspot.estadodeltransito.domain.highway.Highway;
import com.appspot.estadodeltransito.domain.highway.Highway.Text;
import com.appspot.estadodeltransito.preferences.Preferences;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class HighwayDetailsActivity extends GDActivity {

    private static final int SHARE_ID = 1;
    private static final int PREFERENCES_ID = 2;
    public static final String DETAIL_ACTION = "DETAIL_ACTION";
    private GoogleAnalyticsTracker tracker;
    private Highway mHighway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tracker = GoogleAnalyticsTracker.getInstance();
        tracker.start(getString(R.string.google_analytics), 20, this);

        Intent i = getIntent();
        if (DETAIL_ACTION.equals(i.getAction())) {
            mHighway = (Highway) i.getExtras().get("item");
            fillView(mHighway);
        }

        setActionBarContentView(R.layout.highway_detail);
        addActionBarItem(Type.Share, SHARE_ID);
        addActionBarItem(Type.Settings, PREFERENCES_ID);

    }

    @Override
    protected void onStart() {
        super.onStart();
        tracker.trackPageView("Details" + mHighway.getName());
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
        case PREFERENCES_ID:
            startActivity(new Intent(this, Preferences.class));
            return true;

        case SHARE_ID:
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.context_menu_share_subject));
            i.putExtra(Intent.EXTRA_TEXT, mHighway.getShareMsg());
            startActivity(Intent.createChooser(i, getString(R.string.context_menu_share)));
            return true;

        default:
            break;
        }

        return super.onHandleActionBarItemClick(item, position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tracker.stop();
    }

    private void fillView(Highway highway) {
        ImageView iv;
        TextView tv;
        TextView direction;
        ImageView status;
        TextView smallText;
        TextView longText;

        setTitle(highway.getName());

        iv = (ImageView) findViewById(R.id.avenue_highways_details_icon);
        iv.setImageResource(HighwayAdapter.getIcon(highway.getName()));

        tv = (TextView) findViewById(R.id.avenue_highways_details_title);
        tv.setText(highway.getName());

        direction = (TextView) findViewById(R.id.avenue_highways_details_from);
        status = (ImageView) findViewById(R.id.avenue_highways_details_from_status);
        smallText = (TextView) findViewById(R.id.avenue_highways_details_from_text);
        longText = (TextView) findViewById(R.id.avenue_highways_details_from_explanation);

        if (highway.getDirectionFrom() != null) {
            direction.setText(highway.getDirectionFrom());
            status.setImageResource(HighwayAdapter.getStatusIcon(highway
                    .getStatusFrom()));
            smallText.setText(highway.getDelayFrom());
            longText.setText(convertToLongText(highway.getStatusMessageFrom()));

        } else {
            direction.setVisibility(View.GONE);
            status.setVisibility(View.GONE);
            smallText.setVisibility(View.GONE);
            longText.setVisibility(View.GONE);
        }

        direction = (TextView) findViewById(R.id.avenue_highways_details_to);
        status = (ImageView) findViewById(R.id.avenue_highways_details_to_status);
        smallText = (TextView) findViewById(R.id.avenue_highways_details_to_text);
        longText = (TextView) findViewById(R.id.avenue_highways_details_to_explanation);

        if (highway.getDirectionTo() != null) {
            direction.setText(highway.getDirectionTo());
            status.setImageResource(HighwayAdapter.getStatusIcon(highway
                    .getStatusTo()));
            smallText.setText(highway.getDelayTo());
            longText.setText(convertToLongText(highway.getStatusMessageTo()));

        } else {
            direction.setVisibility(View.GONE);
            status.setVisibility(View.GONE);
            smallText.setVisibility(View.GONE);
            longText.setVisibility(View.GONE);
        }
    }

    private static final String convertToLongText(Text text) {
        if (text != null) {
            return "- " + text.getValue();
        } else {
            return null;
        }
    }
}
