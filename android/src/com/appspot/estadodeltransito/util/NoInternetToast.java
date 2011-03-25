package com.appspot.estadodeltransito.util;

import android.content.Context;
import android.widget.Toast;

import com.appspot.estadodeltransito.R;

public class NoInternetToast {

	public static void show(Context ctx) {
		Toast.makeText(ctx, ctx.getString(R.string.no_inet),
				Toast.LENGTH_SHORT).show();
	}

}
