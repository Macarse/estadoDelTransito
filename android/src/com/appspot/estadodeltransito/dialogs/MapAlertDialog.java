package com.appspot.estadodeltransito.dialogs;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.domain.IPublicTransportService;
import com.appspot.estadodeltransito.domain.PointInfo;
import com.appspot.estadodeltransito.domain.bus.Bus;
import com.appspot.estadodeltransito.domain.subway.SubwayStation;
import com.appspot.estadodeltransito.domain.train.Train;
import com.appspot.estadodeltransito.util.GeoPointUtils;
import com.appspot.estadodeltransito.util.Request;
import com.appspot.estadodeltransito.util.UnicodeUtils;

public class MapAlertDialog extends Dialog {

	private static String MAP_INFO_URL = "http://recorridos.usig.buenosaires.gob.ar/info_transporte/?x=%6.1f&y=%6.1f";
	private static final String MAP_ADDRESS_URL = "http://mapas.usig.buenosaires.gob.ar/getMapInfo/?x=%6.1f&y=%6.1f&scl=566.92944";
	
	private TextView mBusesView;
	private TextView mTrainsView;
	private TextView mSubwaysView;
	private TextView mServicesView;
	private ProgressBar mProgressBar;
	private View mSeparator;
	private String transportUrl;
	private String addressUrl;
	private Context mContext;
	private TextView mAddress;
	private double lat;
	private double lng;
	private TextView mTrainsTitleView;
	private TextView mBusesTitleView;
	private TextView mSubwaysTitleView;

	private static final String TAG = "MapAlertDialog";
	
	public MapAlertDialog(Context context, double lat, double lng) {
		super(context);
		mContext = context;
		this.transportUrl = String.format(Locale.US, MAP_INFO_URL, GeoPointUtils.convertLongitudeToMercator(lng), GeoPointUtils.convertLatitudeToMercator(lat));
		this.addressUrl = String.format(Locale.US, MAP_ADDRESS_URL, GeoPointUtils.convertLongitudeToMercator(lng), GeoPointUtils.convertLatitudeToMercator(lat));
		this.lat = lat;
		this.lng = lng;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_alert);
		mServicesView = (TextView) findViewById(R.id.map_alert_nearby_services);
		mBusesView = (TextView) findViewById(R.id.map_alert_buses);
		mBusesTitleView = (TextView) findViewById(R.id.map_alert_buses_title);
		mTrainsView = (TextView) findViewById(R.id.map_alert_trains);
		mTrainsTitleView = (TextView) findViewById(R.id.map_alert_trains_title);
		mSubwaysView = (TextView) findViewById(R.id.map_alert_subways);
		mSubwaysTitleView = (TextView) findViewById(R.id.map_alert_subways_title);
		mProgressBar = (ProgressBar) findViewById(R.id.map_alert_progress);
		mAddress = (TextView) findViewById(R.id.map_alert_location_title);
		mSeparator = findViewById(R.id.map_alert_separator_1);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		new MapsAsyncTask().execute(transportUrl, addressUrl);
	}

	private void update(PointInfo result) {
		if ( result != null ) {
			mAddress.setVisibility(View.VISIBLE);
			mAddress.setText(result.getAddress());
			
			if ( result.getBuses() != null || result.getTrains() != null || result.getSubways() != null ){
				mServicesView.setText(mContext.getString(R.string.map_alert_nearby_services_title));
				mServicesView.setVisibility(View.VISIBLE);
				mSeparator.setVisibility(View.VISIBLE);
				setTrainsText(result.getTrains());
				setSubwaysText(result.getSubways());
				setBusesText(result.getBuses());
			}
			
		}
		mProgressBar.setVisibility(View.GONE);
	}

	private void setSubwaysText(List<SubwayStation> subways) {
		setServiceText(mSubwaysTitleView, mContext.getString(R.string.map_alert_subway_title), mSubwaysView, subways);
	}

	private void setTrainsText(List<Train> trains) {
		setServiceText(mTrainsTitleView,mContext.getString(R.string.map_alert_train_title),mTrainsView,trains);
	}

	private void setBusesText(List<Bus> buses) {
		setServiceText(mBusesTitleView,mContext.getString(R.string.map_alert_bus_title),mBusesView,buses);
	}

	private void setServiceText(TextView serviceTitleView, String serviceTitle, TextView serviceDescView, List<? extends IPublicTransportService> service) {
		if ( service == null )
			return;

		StringBuilder sb = new StringBuilder();
		sb.append(service.get(0).getName());
		int i = 1;
		while ( i < service.size() )
			sb.append(" - ").append(service.get(i++).getName());

		serviceTitleView.setText(serviceTitle);
		serviceTitleView.setVisibility(View.VISIBLE);
		serviceDescView.setText(sb.toString());
		serviceDescView.setVisibility(View.VISIBLE);
	}
	
	private class MapsAsyncTask extends AsyncTask<String, Void, PointInfo> {

		@Override
		protected PointInfo doInBackground(String... url) {
			String json = UnicodeUtils.translateToUnicodeCharacters(Request.getJson(TAG,url[0], "utf-8"));
			Log.d(TAG,"Transports url: "+url[0]);
			Log.d(TAG,"Response: "+json);

			PointInfo pi = new PointInfo(lat,lng);
			
			getPointInfoFromJson(pi,json);
			
			getAddress(pi,url[1]);
			
			return pi;
		}

		protected void onPostExecute(PointInfo result) {
			super.onPostExecute(result);
			update(result);
		}
		
		private void getAddress(PointInfo pi, String url) {
			List<Address> addresses = null;
			String json = Request.getJson(TAG,url);
			Log.d(TAG,"Address url: "+url);
			Log.d(TAG,"Response: "+json);
			Matcher m = addressPattern.matcher(json);
			if ( m.find() ){
				addresses = new LinkedList<Address>();
				Address a = new Address(null);
				a.setThoroughfare(m.group(1));
				a.setSubThoroughfare(m.group(2));
				addresses.add(a);
			}
			
			if ( addresses == null || addresses.isEmpty() ){
				Geocoder gc = new Geocoder(mContext, Locale.getDefault());
				try {
					addresses = gc.getFromLocation(lat, lng, 1);
				} catch (IOException e) {
					Log.d(TAG,e.getMessage());
				}
			}
			
			if ( addresses == null || addresses.isEmpty() )
				pi.setAddress(mContext.getString(R.string.map_alert_address_not_found));
			else{
				Log.d(TAG,"Address :"+addresses.get(0).getThoroughfare()+" "+addresses.get(0).getSubThoroughfare());
				if ( addresses.get(0).getSubThoroughfare() != null )
					pi.setAddress(addresses.get(0).getThoroughfare()+" "+addresses.get(0).getSubThoroughfare());
				else
					pi.setAddress(addresses.get(0).getThoroughfare());
			}
		}

		private void getPointInfoFromJson(PointInfo pi, String json) {
			if ( json.equals("") )
				return;
			
			Matcher m = subwayPattern.matcher(json);
			if ( m.find() ){
				if ( m.group(1) != null )
					Log.d("SUB", m.group(1));
				pi.setSubways(getSubways(m.group(1)));
			}
			
			m = trainPattern.matcher(json);
			if ( m.find() ){
				if ( m.group(1) != null )
					Log.d("TRAIN", m.group(1));
				pi.setTrains(getTrains(m.group(1)));
			}
			
			m = busPattern.matcher(json);
			if ( m.find() ){
				if ( m.group(1) != null )
					Log.d("BUS", m.group(1));
				pi.setBuses(getBuses(m.group(1)));
			}
			
		}
		
		private List<SubwayStation> getSubways(String stations) {
			if ( stations == null )
				return null;

			Matcher m = subwayStationPattern.matcher(stations);
			if ( ! m.find() )
				return null;

			List<SubwayStation> lss = new LinkedList<SubwayStation>();
			SubwayStation subwayStation = new SubwayStation();
			subwayStation.setName(String.format(mContext.getString(R.string.map_alert_subway_station),m.group(1),m.group(2)));
			lss.add(subwayStation);
			
			while( m.find() ){
				SubwayStation ss = new SubwayStation();
				ss.setName(String.format(mContext.getString(R.string.map_alert_subway_station),m.group(1).replaceAll("\\\\","\\"),m.group(2)));
				lss.add(ss);
			}
			return lss;
		}
		
		private List<Bus> getBuses(String buses) {
			if ( buses == null )
				return null;
			
			String[] split = buses.split(",");
			
			List<Bus> lss = new LinkedList<Bus>();
			Bus bus = new Bus();
			bus.setName(split[0].replaceAll("\"", ""));
			lss.add(bus);
			
			int i = 1;
			while( i < split.length ){
				Bus b = new Bus();
				b.setName(split[i++].replaceAll("\"", ""));
				lss.add(b);
			}
			return lss;
		}
		
		private List<Train> getTrains(String trains) {
			if ( trains == null )
				return null;
			
			String[] trainArray = trains.split(",");
			
			List<Train> lss = new LinkedList<Train>();
			Train train = new Train();
			train.setName(trainArray[0].replaceAll("\"",""));
			lss.add(train);
			int i = 1;
			while( i < trainArray.length ){
				Train t = new Train();
				t.setName(trainArray[i++].replaceAll("\"",""));
				lss.add(t);
			}
			return lss;
		}

		private final Pattern busPattern = Pattern.compile("\"bus\"[^\\[]*\\[([^\\]]++)\\]",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		private final Pattern trainPattern = Pattern.compile("\"train\"[^\\[]*\\[([^\\]]++)\\]",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		private final Pattern subwayPattern = Pattern.compile("\"subway\"[^\\[]*\\[([^\\]]++)\\]",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		private final Pattern subwayStationPattern = Pattern.compile("\"\\s*([^\\s]+)\\s+\\(L[^\\s]+\\s+([^\\)])\\)\\s*\"",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		private final Pattern addressPattern = Pattern.compile("\"\\s*calle\\s*\"\\s*:\\s*\"([^\"]++)\",(?:[^,]+,)*\"\\s*altura\\s*\"\\s*:\\s*\"([^\"]++)\"",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	}
}
