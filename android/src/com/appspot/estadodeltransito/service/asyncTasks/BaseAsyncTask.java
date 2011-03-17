package com.appspot.estadodeltransito.service.asyncTasks;

import java.lang.reflect.Type;
import java.util.LinkedList;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;

import com.appspot.estadodeltransito.util.Request;
import com.google.gson.Gson;

abstract public class BaseAsyncTask<T> extends AsyncTask<String, Void, LinkedList<T>> {

	private Service service;

	public BaseAsyncTask(Service service) {
		this.service = service;
	}
	
	@Override
	protected LinkedList<T> doInBackground(String... gaeUrl) {
		LinkedList<T> instances = null;
		String json = Request.getJson(getIntentName(),gaeUrl[0]);
		
		Gson gson = new Gson();
		Type collectionType = getDeserializationType();
		instances = gson.fromJson(json, collectionType);
		
		return instances;
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

	public Service getService() {
		return service;
	}
}
