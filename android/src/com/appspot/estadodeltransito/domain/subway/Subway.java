package com.appspot.estadodeltransito.domain.subway;

import java.io.Serializable;


public class Subway implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String status;
	private String frequency;
	
	@Override
	public String toString() {
		return "Subway [freq=" + frequency + ", name=" + name + ", status=" + status
				+ "]";
	}

	public Subway() {
	}

	public Subway(String name, String status, String freq) {
		this.name = name;
		this.status = status;
		this.frequency = freq;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String freq) {
		this.frequency = freq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getLetter() {
		return name.charAt(name.length()-1);
	}

	public String getShareMsg() {
		return String.format("Línea %s: %s (%s)", getLetter(), status, frequency);
	}
}
