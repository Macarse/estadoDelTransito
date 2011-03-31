package com.appspot.estadodeltransito.mapoverlays;

import java.util.LinkedList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.appspot.estadodeltransito.domain.TransportLine;
import com.appspot.estadodeltransito.domain.TransportLineSegment;
import com.google.android.maps.MapView;

public class TransportOverlay {

	private MapView mapView;
	private List<TransportLineSegmentItemizedOverlay> segmentItemizedOverlays;

	public TransportOverlay(MapView mapView) {
		this.mapView = mapView;
		segmentItemizedOverlays = new LinkedList<TransportLineSegmentItemizedOverlay>();
	}
	
	public void addLine(Drawable defaultMarker, int defaultColor, TransportLine transportLine, boolean boundToBottom){
		for(TransportLineSegment segment : transportLine.getSegments()){
			segmentItemizedOverlays.add(new TransportLineSegmentItemizedOverlay(defaultMarker, defaultColor, transportLine.getName(), segment, mapView, boundToBottom));
		}
	}
	
	public List<TransportLineSegmentItemizedOverlay> getSegmentItemizedOverlays() {
		return segmentItemizedOverlays;
	}	
	
}
