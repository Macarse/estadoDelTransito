package com.appspot.estadodeltransito.service.asyncTasks;

import java.lang.reflect.Type;
import java.util.LinkedList;

import android.app.Service;

import com.appspot.estadodeltransito.domain.train.Train;
import com.appspot.estadodeltransito.parsers.TrainsParser;
import com.google.gson.reflect.TypeToken;

public class TrainsAsyncTask extends BaseAsyncTask<Train> {
	
	public TrainsAsyncTask(Service service) {
		super(service, NEW_TRAINS_STATUS);
	}

	public static final String NEW_TRAINS_STATUS = "New_Trains_Status";

	@Override
	protected String getIntentName() {
		return NEW_TRAINS_STATUS;
	}

	@Override
	protected String getTypeName() {
		return "trains";
	}

	@Override
	protected void sendNotifications(LinkedList<Train> instances) {
		return;
	}

	@Override
	protected Type getDeserializationType() {
		return new TypeToken<LinkedList<Train>>(){}.getType();
	}

	   @Override
	    protected String getFromServer(String gaeUrl) {
	        return TrainsParser.getUrlContent();
	    }

	    @Override
	    protected LinkedList<Train> getInstancesFromJson(String json) {
	        return TrainsParser.getTrains(json);
	    }
}
