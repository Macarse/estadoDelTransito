package com.appspot.estadodeltransito.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.appspot.estadodeltransito.util.LaunchEmailUtil;

public class EmailDialog extends DialogPreference {
    Context mContext;

    public EmailDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public EmailDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);

        if (which == DialogInterface.BUTTON_POSITIVE) {
            LaunchEmailUtil.launchEmailToIntent(mContext);
        }
    }
}
