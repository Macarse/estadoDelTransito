package com.appspot.estadodeltransito.mapoverlays;

import android.app.Dialog;
import android.content.Context;

import com.appspot.estadodeltransito.dialogs.MapAlertDialog;
import com.appspot.estadodeltransito.util.GeoPointUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class PointInfoOverlay extends com.google.android.maps.Overlay {

	private Context mContext;

	public PointInfoOverlay(Context context) {
		mContext = context;
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		Dialog dialogInstance = new MapAlertDialog(mContext,GeoPointUtils.getDegrees(p.getLatitudeE6()), GeoPointUtils.getDegrees(p.getLongitudeE6()));
		
		dialogInstance.show();
		dialogInstance.setCancelable(true);
		dialogInstance.setCanceledOnTouchOutside(true);

		return true;
	}

}
