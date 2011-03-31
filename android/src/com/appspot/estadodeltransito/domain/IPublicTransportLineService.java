package com.appspot.estadodeltransito.domain;

/**
 * Common interface for public transport services that are station based ( with lines )
 * 
 */
public interface IPublicTransportLineService extends IPublicTransportService {

	/**
	 * @return Name of the line of the service
	 */
	public String getLineName();

}
