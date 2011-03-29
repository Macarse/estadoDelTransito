package com.appspot.estadodeltransito.domain;

public class TransportPoint {
	
	private double latitude;
	private double longitude;

	public double getLatitude() {
		return latitude;
	}
	public int getLatitudeAsMicroDegrees() {
		return (int)(latitude*1E6);
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public int getLongitudeAsMicroDegrees() {
		return (int)(longitude*1E6);
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
