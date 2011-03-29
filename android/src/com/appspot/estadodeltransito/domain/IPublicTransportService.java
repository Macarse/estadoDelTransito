package com.appspot.estadodeltransito.domain;

import java.io.Serializable;

/**
 * Common interface for public transport services
 * 
 */
public interface IPublicTransportService extends Serializable {

	/**
	 * @return Name of the public transport
	 */
	public String getName();
	
	/**
	 * @return Message to show when sharing
	 */
	public String getShareMsg();
}
