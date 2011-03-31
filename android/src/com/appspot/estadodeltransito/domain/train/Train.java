package com.appspot.estadodeltransito.domain.train;

import com.appspot.estadodeltransito.domain.IPublicTransportLineService;
import com.appspot.estadodeltransito.domain.TransportLine;
import com.google.gson.Gson;

public class Train implements IPublicTransportLineService {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String status;
	private String line;

	public Train() {

	}

	@Override
	public String toString() {
		return "Train [line=" + line + ", name=" + name + ", status=" + status
				+ "]";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}

	public String getShareMsg() {
		return String.format("%s - %s: (%s)", name, line, status);
	}

	public static TransportLine[] fromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, TransportLine[].class);
	}

	@Override
	public String getLineName() {
		return getLine();
	}
}
