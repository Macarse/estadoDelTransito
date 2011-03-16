package com.appspot.estadodeltransito.mapoverlays;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.appspot.estadodeltransito.domain.subway.SubwayLine;
import com.appspot.estadodeltransito.domain.subway.SubwayStation;
import com.appspot.estadodeltransito.util.IconsUtil;
import com.google.android.maps.GeoPoint;

public class SubwaysItemizedOverlay extends LineItemizedOverlay<SubwayOverlayItem> {


	private ArrayList<SubwayOverlayItem> mOverlays = new ArrayList<SubwayOverlayItem>();
	private Context mContext;
	private SubwayLine subwayLine;

	public SubwaysItemizedOverlay(Drawable defaultMarker, int defaultColor, SubwayLine sl) {
		super(boundCenterBottom(defaultMarker),defaultColor);
		subwayLine = sl;
	}

	public SubwaysItemizedOverlay(Drawable defaultMarker, Context context, int defaultColor, SubwayLine sl) {
		this(defaultMarker, defaultColor, sl);
		mContext = context;
	}

	public void addOverlayItem(SubwayOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected SubwayOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

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
	
	public void addStation(SubwayStation ss){
		GeoPoint point = new GeoPoint(ss.getLatitudeAsMicroDegrees(),ss.getLongitudeAsMicroDegrees());
	    SubwayOverlayItem stationItem = new SubwayOverlayItem(point, subwayLine.getName(), ss.getName());
	    addOverlayItem(stationItem);
	}
	
	public String getLineName(){
		return subwayLine.getName();
	}
}
