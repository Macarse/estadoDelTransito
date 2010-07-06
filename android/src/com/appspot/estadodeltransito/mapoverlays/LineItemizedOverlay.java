package com.appspot.estadodeltransito.mapoverlays;

import android.graphics.drawable.Drawable;

import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


abstract public class LineItemizedOverlay<OI extends OverlayItem> extends com.google.android.maps.ItemizedOverlay<OI> {

	private LineOverlay lineOverlay;
	
	public LineItemizedOverlay(Drawable defaultMarker, int lineColor) {
		super(defaultMarker);
		lineOverlay = new LineOverlay(this,lineColor);
	}

	public Overlay getLineOverlay(){
		return lineOverlay;
	}
}
