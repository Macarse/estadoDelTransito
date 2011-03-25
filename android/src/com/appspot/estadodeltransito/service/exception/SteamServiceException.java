package com.appspot.estadodeltransito.service.exception;

public class SteamServiceException extends RuntimeException {

	private static final long serialVersionUID = -7118043259523089009L;

	public SteamServiceException(Exception e) {
		super(e);
	}
}
