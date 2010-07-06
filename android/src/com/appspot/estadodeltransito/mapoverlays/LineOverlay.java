package com.appspot.estadodeltransito.mapoverlays;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class LineOverlay extends Overlay {

	ItemizedOverlay<? extends OverlayItem> itemizedOverlay;
	private static final float LINE_WIDTH = 6f;
	private int colorRGB;
	private Paint p;
	
	public LineOverlay(ItemizedOverlay<? extends OverlayItem> itemizedOverlay, int lineColor) {
		this.itemizedOverlay = itemizedOverlay;
		colorRGB = lineColor;
		p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(colorRGB);
		p.setStrokeWidth(LINE_WIDTH);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if ( itemizedOverlay.size() == 0 || itemizedOverlay.size() == 1 )
			return;
		
		Projection projection = mapView.getProjection();
		
		int i = 0;
		
		while( i < itemizedOverlay.size() - 1 ){
			OverlayItem begin = itemizedOverlay.getItem(i);
			OverlayItem end = itemizedOverlay.getItem(i+1);
			paintLineBetweenStations(begin,end,projection,canvas);
			i++;
		}
		super.draw(canvas, mapView, shadow);
	}
	
	private void paintLineBetweenStations(OverlayItem from, OverlayItem to, Projection projection, Canvas canvas){
		GeoPoint bPoint = from.getPoint();
		GeoPoint ePoint = to.getPoint();
		
		Point bPixel = projection.toPixels(bPoint, null);
		Point ePixel = projection.toPixels(ePoint, null);
		
		canvas.drawLine(bPixel.x, bPixel.y, ePixel.x, ePixel.y, p);
	}
}
