package com.appspot.estadodeltransito.mapoverlays;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.appspot.estadodeltransito.domain.TransportLine;
import com.appspot.estadodeltransito.util.IconsUtil;
import com.google.android.maps.MapView;

public class TransportServiceOverlayProvider {

	private static TransportServiceOverlayProvider instance;
	private TransportServiceDataProvider dataProvider;
	private List<TransportLineSegmentItemizedOverlay> subwayLinesOverlay;
	private Context context;
	private MapView mapView;
	private List<TransportLineSegmentItemizedOverlay> trainLinesOverlay;

	private TransportServiceOverlayProvider () {
		dataProvider = TransportServiceDataProvider.getInstance();
	}
	
	static public TransportServiceOverlayProvider getInstance(){
		if ( instance == null ){
			instance = new TransportServiceOverlayProvider();
		}
		return instance;
	}
	
	public void setContext(Context context){
		this.context = context;
		dataProvider.setContext(context);
	}

	public void setMapView(MapView mapView){
		this.mapView = mapView;
	}
	
	public void loadSubwayOverlays() {
		dataProvider.loadSubwayLines();
		if ( subwayLinesOverlay == null ){
			TransportOverlay transportOverlay = new TransportOverlay(mapView);
			for (TransportLine sl:dataProvider.getSubwayLines()){
				String lineName = sl.getName();
				Drawable lineIcon = context.getResources().getDrawable(IconsUtil.getSubwayLineIconResource(lineName));
				int colorRGB = IconsUtil.getSubwayLineColor(lineName);
				transportOverlay.addLine(lineIcon, colorRGB, sl, true);

			}	
			subwayLinesOverlay = transportOverlay.getSegmentItemizedOverlays(); 
		}
	}
	
	public List<TransportLineSegmentItemizedOverlay> getSubwaysOverlays(){
		loadSubwayOverlays();
		return subwayLinesOverlay;
	}
	
	public void loadTrainOverlays() {
		dataProvider.loadTrainLines();
		if ( trainLinesOverlay == null ){
			TransportOverlay transportOverlay = new TransportOverlay(mapView);
			for (TransportLine sl:dataProvider.getTrainLines()){
				String lineName = sl.getName();
				Drawable lineIcon = context.getResources().getDrawable(IconsUtil.getTrainLineIconResource(lineName));
				int colorRGB = IconsUtil.getTrainLineColor(lineName);
				transportOverlay.addLine(lineIcon, colorRGB, sl, false);

			}	
			trainLinesOverlay = transportOverlay.getSegmentItemizedOverlays(); 
		}
	}
	
	public List<TransportLineSegmentItemizedOverlay> getTrainsOverlays(){
		loadTrainOverlays();
		return trainLinesOverlay;
	}
}
