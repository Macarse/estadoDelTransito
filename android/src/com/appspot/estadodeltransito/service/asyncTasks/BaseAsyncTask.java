package com.appspot.estadodeltransito.service.asyncTasks;

import java.lang.reflect.Type;
import java.util.LinkedList;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.appspot.estadodeltransito.service.StatusService;
import com.appspot.estadodeltransito.util.Request;
import com.google.gson.Gson;

abstract public class BaseAsyncTask<T> extends AsyncTask<String, Void, LinkedList<T>> {

    private static final String TAG = BaseAsyncTask.class.getCanonicalName();
    private static final String LAST_UPDATE = "_last_update";
    private static final String LAST_UPDATE_JSON = "_last_update_json";
	private Service service;
	private String taskName;

	public BaseAsyncTask(Service service, String taskName) {
		this.service = service;
		this.taskName = taskName;
	}
	
	@Override
	protected LinkedList<T> doInBackground(String... gaeUrl) {

	    Log.d(TAG, taskName + ": calling doInBackground");

		LinkedList<T> instances = null;
		long lastUpdateTime = PreferenceManager.getDefaultSharedPreferences(service).getLong(taskName + LAST_UPDATE, 0);
		long currentTime = System.currentTimeMillis();

		Log.d(TAG, taskName + "lastUpdateTime: " + lastUpdateTime + ": currentTime: " + currentTime);
		if ( currentTime - lastUpdateTime > getService().getEDTApplication().getRefreshNotificationsTimeInSeconds() ){

		    Log.d(TAG, taskName + ": getting instances from Server");
			String jsonFromServer = getFromServer(gaeUrl[0]);
			instances = getInstancesFromJson(jsonFromServer);

			if ( instances == null ) {
			    Log.d(TAG, taskName + "instances: null");
			} else {
			    Log.d(TAG, taskName + "instances: " + instances.toString());
			}

			if ( instances != null && !instances.isEmpty() ){
			    Log.d(TAG, taskName + ": saving instances in cache");
				Editor editor = PreferenceManager.getDefaultSharedPreferences(service).edit();
				editor.putString(taskName + LAST_UPDATE_JSON, jsonFromServer);
				editor.putLong(taskName + LAST_UPDATE, currentTime);
				editor.commit();
			}
		}

		if ( instances == null || instances.isEmpty() ) {
		    Log.d(TAG, taskName + ": getting instances from cache");
		    instances = getInstancesFromJson(getFromCache());
		}
		
		return instances;
	}

	protected LinkedList<T> getInstancesFromJson(String json) {
		LinkedList<T> instances = null;
		
		Gson gson = new Gson();
		Type collectionType = getDeserializationType();
		try{
			instances = gson.fromJson(json, collectionType);
		}catch(Exception e){
			Log.e(taskName, e.getMessage());
		}
		return instances;
	}
	
	protected String getFromServer(String gaeUrl) {
		Log.d(TAG, taskName + ": Getting json from server");
		return Request.getJson(getIntentName(), gaeUrl);
	}

	private String getFromCache() {
		Log.d(taskName, taskName + ": Getting json from cache");
		return PreferenceManager.getDefaultSharedPreferences(service).getString(taskName + LAST_UPDATE_JSON, "");
	}

	@Override
	protected void onPostExecute(LinkedList<T> instances) {
		super.onPostExecute(instances);
		sendUpdates(instances);
		if ( instances != null )
			sendNotifications(instances);
	}
	
	private void sendUpdates(LinkedList<T> instances) {
		  Intent intent = new Intent(getIntentName());
		  intent.putExtra(getTypeName(), instances);

		  getService().sendBroadcast(intent);
	}

	public void sendEmptyUpdates() {
		sendUpdates(null);
	}

	abstract protected String getIntentName();
	
	abstract protected String getTypeName();
	
	abstract protected void sendNotifications(LinkedList<T> instances);
	
	abstract protected Type getDeserializationType();

	public StatusService getService() {
		return (StatusService) service;
	}
}
