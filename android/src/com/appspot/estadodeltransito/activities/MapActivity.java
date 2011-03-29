package com.appspot.estadodeltransito.activities;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.domain.IPublicTransportLineService;
import com.appspot.estadodeltransito.domain.subway.Subway;
import com.appspot.estadodeltransito.domain.subway.SubwayStation;
import com.appspot.estadodeltransito.util.IconsUtil;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.appspot.estadodeltransito.domain.train.Train;
import com.appspot.estadodeltransito.mapoverlays.PointInfoOverlay;
import com.appspot.estadodeltransito.mapoverlays.TransportLineSegmentItemizedOverlay;
import com.appspot.estadodeltransito.mapoverlays.TransportServiceOverlayProvider;
import com.appspot.estadodeltransito.mapoverlays.TransportStationOverlayItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapActivity extends GDMapActivity {

//	private static final String TAG = MapActivity.class.getCanonicalName();
	public static final String SHOW_SUBWAY_ACTION = "ShowSubwayLine";
	public static final String SHOW_SUBWAYS_ACTION = "ShowSubwayLines";
	public static final String SHOW_TRAINS_ACTION = "ShowTrainsLine";
	public static final String SHOW_TRAIN_ACTION = "ShowTrainLine";

	private IPublicTransportLineService transportService = null;
	private MapView mapView;
	protected MapController mapController;
    private GoogleAnalyticsTracker tracker;
	
	private TransportServiceOverlayProvider overlayProvider;

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    tracker = GoogleAnalyticsTracker.getInstance();
        tracker.start(getString(R.string.google_analytics), this);

	    setActionBarContentView(R.layout.map);
	    
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapController = mapView.getController();

		initMyLocation();
		mapView.getOverlays().add(new PointInfoOverlay(this));
		initOverlayProvider();
	    
	    List<TransportLineSegmentItemizedOverlay> mapOverlaysToAdd = getMapOverlaysToAdd(getIntent());
			
		/* Set the zoomToSpan an center it */
		if ( !mapOverlaysToAdd.isEmpty() && ! ( mapOverlaysToAdd.size() == 1 && mapOverlaysToAdd.get(0).size() == 0 ) ){
			TransportLineSegmentItemizedOverlay lineOverlay = mapOverlaysToAdd.get(0);
			TransportStationOverlayItem firstStation = lineOverlay.getItem(0);
			TransportStationOverlayItem lastStation = lineOverlay.getItem(lineOverlay.size()-1);
			int halfLat = ( firstStation.getPoint().getLatitudeE6() + lastStation.getPoint().getLatitudeE6() ) / 2;
			int halfLong = ( firstStation.getPoint().getLongitudeE6() + lastStation.getPoint().getLongitudeE6() ) / 2;
			int diffLat = Math.abs( firstStation.getPoint().getLatitudeE6() - lastStation.getPoint().getLatitudeE6() );
			int diffLong = Math.abs( firstStation.getPoint().getLongitudeE6() - lastStation.getPoint().getLongitudeE6() );
			mapController.zoomToSpan(diffLat, diffLong);
			mapController.animateTo(new GeoPoint(halfLat,halfLong));
		}else
			return;
		
		List<Overlay> overlays = mapView.getOverlays();

		for(TransportLineSegmentItemizedOverlay lio:mapOverlaysToAdd){
			overlays.add(lio.getSegmentOverlay());
		}

		overlays.addAll(mapOverlaysToAdd);
	}

	private List<TransportLineSegmentItemizedOverlay> getMapOverlaysToAdd(Intent intent) {
		List<TransportLineSegmentItemizedOverlay> mapOverlaysToAdd = new LinkedList<TransportLineSegmentItemizedOverlay>();

		if ( SHOW_SUBWAY_ACTION.equals(intent.getAction()) ){
			//Show that line only
			transportService = (Subway) intent.getExtras().get("line");
			addLineOverlay(mapOverlaysToAdd, overlayProvider.getSubwaysOverlays());
			setTitle(transportService.getName());
		}else if ( SHOW_SUBWAYS_ACTION.equals(intent.getAction())) {
			addAllLinesOverlays(mapOverlaysToAdd, overlayProvider.getSubwaysOverlays());
		    setTitle(R.string.map_title);
		}else if ( SHOW_TRAIN_ACTION.equals(intent.getAction()) ){
			//Show that line only
			transportService = (Train) intent.getExtras().get("line");
			addLineOverlay(mapOverlaysToAdd, overlayProvider.getTrainsOverlays());
			setTitle(transportService.getName());
		}else if ( SHOW_TRAINS_ACTION.equals(intent.getAction())) {
			overlayProvider.getTrainsOverlays();
			addAllLinesOverlays(mapOverlaysToAdd, overlayProvider.getTrainsOverlays());
		    setTitle(R.string.map_title);
		}else{
			addAllLinesOverlays(mapOverlaysToAdd, overlayProvider.getSubwaysOverlays());
			addAllLinesOverlays(mapOverlaysToAdd, overlayProvider.getTrainsOverlays());
		}
		return mapOverlaysToAdd;
	}

	private void initOverlayProvider() {
		if ( overlayProvider == null )
			overlayProvider = TransportServiceOverlayProvider.getInstance();
		overlayProvider.setContext(this);
		overlayProvider.setMapView(mapView);
	}

	private void initMyLocation() {
		final MyLocationOverlay overlay = new MyLocationOverlay(this, mapView);
		overlay.enableMyLocation();

		mapView.getOverlays().add(overlay);
	}

	
	@Override
    protected void onStart() {
        super.onStart();
        tracker.trackPageView(getString(R.string.map_title));
    }

	private void addAllLinesOverlays(List<TransportLineSegmentItemizedOverlay> mapOverlays, List<TransportLineSegmentItemizedOverlay> transportOverlays) {
		transportService = null;
		for (TransportLineSegmentItemizedOverlay lineOverlay:transportOverlays){
 				mapOverlays.add(lineOverlay);
		}
	}

	private void addLineOverlay(List<TransportLineSegmentItemizedOverlay> mapOverlays, List<TransportLineSegmentItemizedOverlay> transportOverlays) {
		for (TransportLineSegmentItemizedOverlay lineOverlay:transportOverlays){
			if ( transportService.getLineName().equals(lineOverlay.getLineName()) )
				mapOverlays.add(lineOverlay);
		}
	}

	@Override
    protected void onDestroy() {
      super.onDestroy();
      tracker.stop();
    }

}
