package com.appspot.estadodeltransito.util;

public class GeoPointUtils {

	private static final double LAT_REF = -34.62918;
	private static final double LONG_REF = -58.4635;
	private static final float FALSE_NORTH = 100000;
	private static final float FALSE_EAST = 100000;
	
	public static int getMicroDegrees(double degrees){
		return (int) (degrees*1E6);
	}
	
	public static double getDegrees(int microdegrees){
		return microdegrees/(1.0E6);
	}

	public static double convertLongitudeToMercator(int longitudeE6) {
		return distance( LAT_REF, LONG_REF, LAT_REF, getDegrees(longitudeE6)) + FALSE_EAST;
	}

	public static double convertLatitudeToMercator(int latitudeE6) {
		return distance( LAT_REF, LONG_REF, getDegrees(latitudeE6), LONG_REF) + FALSE_NORTH;
	}

	public static double convertLongitudeToMercator(double longitudeDegrees) {
		return distance( LAT_REF, LONG_REF, LAT_REF, longitudeDegrees ) + FALSE_EAST;
	}

	public static double convertLatitudeToMercator(double latitudeDegrees) {
		return distance( LAT_REF, LONG_REF, latitudeDegrees, LONG_REF) + FALSE_NORTH;
	}
	
	/**
	 * Calculates the distance in meters between two geographic points (a and b)
	 * @param lat1 Latitude of point a in degrees
	 * @param lng1 Longitude of point a in degrees
	 * @param lat2 Latitude of point b in degrees
	 * @param lng2 Longitude of point b in degrees
	 * @return Distance in meters between the points
	 */
	private static double distance( double lat1, double lng1, double lat2, double lng2 ){
	// Distance between 2 geodesical points
	// Haversine formula to calculate great circle distances

		double r = 6371;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        	Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
        	Math.sin(dLng/2) * Math.sin(dLng/2);
        
        double c = 2 * Math.asin(Math.sqrt(a));
        return r * c * 1000;
	}

}
