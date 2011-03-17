package com.appspot.estadodeltransito.service.asyncTasks;

import android.app.Service;

import com.appspot.estadodeltransito.activities.AvenuesActivity;

public class AvenuesAsyncTask extends HighwaysAsyncTask {
	
	public static final String NEW_AVENUES_STATUS = "New_Avenues_Status";

	public AvenuesAsyncTask(Service service) {
		super(service);
	}
	
	protected Class<?> getActivityClass() {
		return AvenuesActivity.class;
	}
}