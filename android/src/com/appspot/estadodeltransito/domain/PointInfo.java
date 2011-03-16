package com.appspot.estadodeltransito.domain;

import java.io.Serializable;
import java.util.List;

import com.appspot.estadodeltransito.domain.bus.Bus;
import com.appspot.estadodeltransito.domain.subway.SubwayStation;
import com.appspot.estadodeltransito.domain.train.Train;


public class PointInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private double latitude;
	private double longitude;
	private List<Bus> buses;
	private List<Train> trains;
	private List<SubwayStation> subways;
	private String address;

	public PointInfo(double lat, double lng) {
		this.latitude = lat;
		this.longitude = lng;
	}


	public List<Bus> getBuses() {
		return buses;
	}
	
	public void setBuses(List<Bus> buses) {
		this.buses = buses;
	}
	
	public List<Train> getTrains() {
		return trains;
	}
	
	public void setTrains(List<Train> trains) {
		this.trains = trains;
	}
	
	public List<SubwayStation> getSubways() {
		return subways;
	}
	
	public void setSubways(List<SubwayStation> subways) {
		this.subways = subways;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "PointInfo [address=" + address + ", buses=" + buses
		+ ", latitude=" + latitude + ", longitude=" + longitude
		+ ", subways=" + subways + ", trains=" + trains + "]";
	}
}
