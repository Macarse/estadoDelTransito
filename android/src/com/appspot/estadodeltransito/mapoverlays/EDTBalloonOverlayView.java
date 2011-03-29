package com.appspot.estadodeltransito.mapoverlays;

import android.content.Context;

import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class EDTBalloonOverlayView<Item extends OverlayItem> extends BalloonOverlayView<Item> {

	public EDTBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
	}

	public void setData(Item item) {
		super.setData(item);
	}
}
