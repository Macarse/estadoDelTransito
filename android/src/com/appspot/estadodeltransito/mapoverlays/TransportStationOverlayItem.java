package com.appspot.estadodeltransito.mapoverlays;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class TransportStationOverlayItem extends OverlayItem {

	public TransportStationOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
	}

}
