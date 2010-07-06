package com.appspot.estadodeltransito.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.util.ValueParser;

public class SubwaysNotificationsPreference extends DialogPreference {

    private static final String androidns = "http://schemas.android.com/apk/res/android";
    private Context mContext;
    private String mDefault, mValue;
    private ValueParser mValueParser;

    public SubwaysNotificationsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mDefault = mContext.getResources().getString(attrs.getAttributeResourceValue(androidns,"defaultValue", 100));
        mValue = mContext.getResources().getString(attrs.getAttributeResourceValue(androidns,"defaultValue", 100));
        mValueParser = new ValueParser(mDefault);
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue) {
        super.onSetInitialValue(restore, defaultValue);

        if (restore) {
            mValue = shouldPersist() ? getPersistedString(mDefault) : mDefault;
        } else {
            mValue = mDefault;
        }
    }

    @Override 
    protected View onCreateDialogView() {

        // This is called again with mValue because this preference might
        // be reloaded without calling the constructor.
        onSetInitialValue(true, "");
        mValueParser = new ValueParser(mValue, mDefault);

        ScrollView ret = new ScrollView(mContext);

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6,6,6,6);

        LanguageButtonListener btListener = new LanguageButtonListener();

        for (String subway : mContext.getResources().getStringArray(R.array.subways_list) ) {
            CheckBox bt = new CheckBox(mContext);
            bt.setText(subway);
            
            if ( mValueParser.contains(subway) ) {
            	bt.setChecked(true);
            }

            bt.setOnCheckedChangeListener(btListener);
            layout.addView(bt);
        }

        if (shouldPersist())
            mValue = getPersistedString(mDefault);

        ret.addView(layout);
        return ret;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
    	super.onDialogClosed(positiveResult);
    	if ( positiveResult ) {
    		mValue = mValueParser.toString();
            if ( shouldPersist() ) {
                persistString(mValue);
            }
    	}
    		
    }
    private final class LanguageButtonListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if ( isChecked ) {
                mValueParser.addValue((String)buttonView.getText());
                mValue = mValueParser.toString();
                if ( shouldPersist() ) {
                    persistString(mValue);
                }
            } else {
                mValueParser.removeValue((String)buttonView.getText());
                mValue = mValueParser.toString();
                if ( shouldPersist() ) {
                    persistString(mValue);
                }
            }
        }
    }
}