package com.appspot.estadodeltransito.domain;

import java.util.List;

public class TransportLine {

	private String name;
	private List<TransportLineSegment> segments;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TransportLineSegment> getSegments() {
		return segments;
	}
	public void setSegments(List<TransportLineSegment> segments) {
		this.segments = segments;
	}

}
