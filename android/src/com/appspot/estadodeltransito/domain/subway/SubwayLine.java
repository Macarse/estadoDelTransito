package com.appspot.estadodeltransito.domain.subway;

import java.util.List;

public class SubwayLine {

	private String nombre;
	private List<SubwayStation> estaciones;
	
	public String getName() {
		return nombre;
	}
	public void setName(String name) {
		this.nombre = name;
	}
	public List<SubwayStation> getStations() {
		return estaciones;
	}
	public void setStations(List<SubwayStation> stations) {
		this.estaciones = stations;
	}
	
}
