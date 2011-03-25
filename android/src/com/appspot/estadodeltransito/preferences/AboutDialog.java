package com.appspot.estadodeltransito.preferences;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appspot.estadodeltransito.R;

public class AboutDialog extends DialogPreference {

    private static final String androidns = "http://schemas.android.com/apk/res/android";
    private Context mContext;
    private String mVersionNumber;

    /**
     * @param context
     * @param attrs
     */
    public AboutDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mVersionNumber = mContext.getResources().getString(attrs.getAttributeResourceValue(androidns, "dialogMessage",1));
    }
    @Override
    protected View onCreateDialogView() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6,6,6,6);

        TextView splashText = new TextView(mContext);

        try {
            String pkg = mContext.getPackageName();
            mVersionNumber = mContext.getPackageManager().getPackageInfo(pkg, 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        if (mVersionNumber != null) {
            String aboutMsg = String.format(mContext.getString(R.string.pref_about_text), mVersionNumber);
            splashText.setText(aboutMsg);
        }

        layout.addView(splashText);

        ImageView authorLogo = new ImageView(mContext);
        authorLogo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.monkey));

        layout.addView(authorLogo);

        return layout;
    }
}