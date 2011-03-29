package com.appspot.estadodeltransito.domain.train;

import com.appspot.estadodeltransito.domain.IPublicTransportService;
import com.appspot.estadodeltransito.domain.TransportStation;


public class TrainStation extends TransportStation implements IPublicTransportService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getShareMsg() {
		return toString();
	}
}
