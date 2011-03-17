package com.appspot.estadodeltransito.service.asyncTasks;

import java.lang.reflect.Type;
import java.util.LinkedList;

import android.app.Service;

import com.appspot.estadodeltransito.domain.train.Train;
import com.google.gson.reflect.TypeToken;

public class TrainsAsyncTask extends BaseAsyncTask<Train> {
	
	public TrainsAsyncTask(Service service) {
		super(service);
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
}
