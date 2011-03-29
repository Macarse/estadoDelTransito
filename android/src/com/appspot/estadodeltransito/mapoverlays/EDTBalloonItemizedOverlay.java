package com.appspot.estadodeltransito.mapoverlays;

import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

abstract public class EDTBalloonItemizedOverlay<Item extends OverlayItem> extends BalloonItemizedOverlay<Item> {

	public EDTBalloonItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker, mapView);
	}

	protected BalloonOverlayView<Item> createBalloonOverlayView() {
		return new EDTBalloonOverlayView<Item>(getMapView().getContext(), getBalloonBottomOffset());
	}
}
