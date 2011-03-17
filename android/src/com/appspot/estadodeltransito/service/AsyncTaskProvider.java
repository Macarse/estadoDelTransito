package com.appspot.estadodeltransito.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Service;

import com.appspot.estadodeltransito.service.asyncTasks.AvenuesAsyncTask;
import com.appspot.estadodeltransito.service.asyncTasks.BaseAsyncTask;
import com.appspot.estadodeltransito.service.asyncTasks.HighwaysAsyncTask;
import com.appspot.estadodeltransito.service.asyncTasks.SubwaysAsyncTask;
import com.appspot.estadodeltransito.service.asyncTasks.TrainsAsyncTask;

public class AsyncTaskProvider {

	// public Map<String, Class<? extends AsyncTask<String, ?, ?>>> asyncTasks =
	// new HashMap<String, Class<? extends AsyncTask<String, ?, ?>>>();

	public Map<String, String> asyncTasksUrls = new HashMap<String, String>();
	private LinkedList<String> asyncTasksNames;

	private Service service;


	private static AsyncTaskProvider instance;

	public AsyncTaskProvider() {
		asyncTasksNames = new LinkedList<String>();
		asyncTasksNames.add(TrainsAsyncTask.NEW_TRAINS_STATUS);
		asyncTasksNames.add(SubwaysAsyncTask.NEW_SUBWAYS_STATUS);
		asyncTasksNames.add(HighwaysAsyncTask.NEW_HIGHWAYS_STATUS);
		asyncTasksNames.add(AvenuesAsyncTask.NEW_AVENUES_STATUS);
	}
	
	public static AsyncTaskProvider getInstance() {
		if (instance == null)
			instance = new AsyncTaskProvider();
		return instance;
	}

	// public void addAsyncTaskFor(String taskName, Class<? extends
	// AsyncTask<String, ?, ?>> taskClass){
	// asyncTasks.put(taskName, taskClass);
	// }

	public void addUrlFor(String taskName, String url) {
		asyncTasksUrls.put(taskName, url);
	}

	public BaseAsyncTask<?> getAsyncTaskFor(String taskName) {
		/*
		 * Class<? extends AsyncTask<String, ?, ?>> class1 =
		 * asyncTasks.get(taskName); if ( taskName == null || class1 == null )
		 * return null;
		 * 
		 * try { return class1.newInstance(); } catch (Exception e) { throw new
		 * RuntimeException(e); }
		 */
		if ( taskName == null )
			return null;
		
		if (taskName.contains(SubwaysAsyncTask.NEW_SUBWAYS_STATUS))
			return new SubwaysAsyncTask(service);
		if (taskName.contains(HighwaysAsyncTask.NEW_HIGHWAYS_STATUS))
			return new HighwaysAsyncTask(service);
		if (taskName.contains(AvenuesAsyncTask.NEW_AVENUES_STATUS))
			return new AvenuesAsyncTask(service);
		if (taskName.contains(TrainsAsyncTask.NEW_TRAINS_STATUS))
			return new TrainsAsyncTask(service);

		return null;
	}

	public List<String> getRegisteredTaskNames() {
		return asyncTasksNames;
	}

	public String getAsyncTaskUrlFor(String taskName) {
		return asyncTasksUrls.get(taskName);
	}

	public void setService(Service statusService) {
		this.service = statusService;
	}
}
