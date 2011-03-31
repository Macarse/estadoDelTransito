package com.appspot.estadodeltransito.mapoverlays;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.domain.TransportLine;
import com.appspot.estadodeltransito.domain.subway.Subway;
import com.appspot.estadodeltransito.domain.train.Train;

public class TransportServiceDataProvider {

	private TransportLine [] subwayLines = null;
	private TransportLine [] trainLines = null;
	private Context context;
	
	private static TransportServiceDataProvider instance;

	private TransportServiceDataProvider () {}
	
	public static TransportServiceDataProvider getInstance(){
		if ( instance == null ){
			instance = new TransportServiceDataProvider();
		}
		return instance;
	}
	
	public static TransportLine[] readSubwayInfo(InputStream subwayJSON) throws IOException {
		String json = extractJson(subwayJSON);
		TransportLine[] lines = Subway.fromJson(json);
		return lines;
	}

	public static TransportLine[] readTrainInfo(InputStream trainJSON) throws IOException {
		String json = extractJson(trainJSON);
		TransportLine[] lines = Train.fromJson(json);
		return lines;
	}

	private static String extractJson(InputStream jsonStream) throws IOException {
		byte [] data = new byte[jsonStream.available()];
		
		while( jsonStream.read(data) != -1);
		return new String(data);
	}
	
	
	public void loadSubwayLines(){
		if ( subwayLines == null ){
			InputStream subwayJSON = context.getResources().openRawResource(R.raw.subwaygeo);
			try {
				subwayLines = readSubwayInfo(subwayJSON);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadTrainLines(){
		if ( trainLines == null ){
			InputStream trainJSON = context.getResources().openRawResource(R.raw.traingeo);
			try {
				trainLines = readTrainInfo(trainJSON);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public TransportLine[] getSubwayLines() {
		loadSubwayLines();
		return subwayLines;
	}

	public TransportLine[] getTrainLines() {
		loadTrainLines();
		return trainLines;
	}
}
