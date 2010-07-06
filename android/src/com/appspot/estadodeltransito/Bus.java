package com.appspot.estadodeltransito;

import java.io.Serializable;

public class Bus implements Serializable, IPublicTransportService {

	private static final long serialVersionUID = 1L;

	public String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Bus [name=" + name + "]";
	}
	
	
}
