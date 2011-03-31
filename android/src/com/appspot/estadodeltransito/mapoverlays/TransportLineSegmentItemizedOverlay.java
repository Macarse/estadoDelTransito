package com.appspot.estadodeltransito.mapoverlays;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.appspot.estadodeltransito.domain.TransportLineSegment;
import com.appspot.estadodeltransito.domain.TransportStation;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class TransportLineSegmentItemizedOverlay extends EDTBalloonItemizedOverlay<TransportStationOverlayItem> {

	private ArrayList<TransportStationOverlayItem> mOverlays = new ArrayList<TransportStationOverlayItem>();
	private LineOverlay lineOverlay;
	private String lineName;

	public TransportLineSegmentItemizedOverlay(Drawable defaultMarker, int defaultColor, String lineName, TransportLineSegment transportLineSegment, MapView mapView, boolean boundToBottom) {
		super(defaultMarker, mapView);

		if ( boundToBottom )
			boundCenterBottom(defaultMarker);
		else
			boundCenter(defaultMarker);
		
		populate();
		lineOverlay = new LineOverlay(this,defaultColor);
		this.lineName = lineName;
		for(TransportStation transportStation:transportLineSegment.getStations())
			addStation(transportStation);
	}
	
	public Overlay getSegmentOverlay(){
		return lineOverlay;
	}

	public void addOverlayItem(TransportStationOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected TransportStationOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
/*
	@Override
	protected boolean onTap(int index) {
		SubwayOverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setIcon(IconsUtil.getLineIconResource(item.getTitle()));
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		
		AlertDialog dialogInstance = dialog.create();
		dialogInstance.show();
		dialogInstance.setCancelable(true);
		dialogInstance.setCanceledOnTouchOutside(true);

		return true;
	}
	*/
	
	public void addStation(TransportStation ss){
		GeoPoint point = new GeoPoint(ss.getLatitudeAsMicroDegrees(),ss.getLongitudeAsMicroDegrees());
	    TransportStationOverlayItem stationItem = new TransportStationOverlayItem(point, getLineName(), ss.getName());
	    addOverlayItem(stationItem);
	}
	
	public String getLineName(){
		return lineName;
	}
}
