package com.appspot.estadodeltransito.domain;

import java.util.List;

public class TransportLineSegment {

	private List<TransportStation> stations;
		
	public List<TransportStation> getStations() {
		return stations;
	}
	public void setStations(List<TransportStation> stations) {
		this.stations = stations;
	}
}
