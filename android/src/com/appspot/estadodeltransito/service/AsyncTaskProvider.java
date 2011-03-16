package com.appspot.estadodeltransito.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.os.AsyncTask;

public class AsyncTaskProvider {

	public static Map<String, Class<? extends AsyncTask<String, ?, ?>>> asyncTasks = new HashMap<String, Class<? extends AsyncTask<String, ?, ?>>>();
	
	public static Map<String, String> asyncTasksUrls = new HashMap<String, String>();
	
	private static AsyncTaskProvider instance;

	public static AsyncTaskProvider getInstance() {
		if ( instance == null )
			instance = new AsyncTaskProvider();
		return instance;
	}

	public void addAsyncTaskFor(String taskName, Class<? extends AsyncTask<String, ?, ?>> taskClass){
		asyncTasks.put(taskName, taskClass);
	}
	
	public void addUrlFor(String taskName, String url){
		asyncTasksUrls.put(taskName, url);
	}
	
	public AsyncTask<String, ?, ?> getAsyncTaskFor(String taskName){
		Class<? extends AsyncTask<String, ?, ?>> class1 = asyncTasks.get(taskName);
		try {
			return class1.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Set<String> getRegisteredTaskNames(){
		return asyncTasks.keySet();
	}
	
	public String getAsyncTaskUrlFor(String taskName){
		return asyncTasksUrls.get(taskName);
	}
}
