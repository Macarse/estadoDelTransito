package com.edt.dbclasses;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class AccessEn {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private transient Key key;
		
	@Persistent
	private String name;
	
	@Persistent
	private String directionTo;
	 
	@Persistent
	private String directionFrom;
	 
	@Persistent
	private String delayTo;
	 
	@Persistent
	private String delayFrom;
	 
	@Persistent
	private String statusTo;
	 
	@Persistent
	private String statusFrom;
	 
	@SuppressWarnings("unused")
	@Persistent
	private transient String messageTo;
	
	@SuppressWarnings("unused")
	@Persistent
	private transient String messageFrom;
	
	@Persistent
	private Text statusMessageTo;
	 
	@Persistent
	private Text statusMessageFrom;

	@Persistent
	private transient String type;
	
	public Key getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "Access name=" + name + " [delayFrom=" + delayFrom + ", delayTo=" + delayTo
				+ ", directionFrom=" + directionFrom + ", directionTo="
				+ directionTo + ", key=" + key + ", messageFrom=" + statusMessageFrom
				+ ", messageTo=" + statusMessageTo
				+ ", statusFrom=" + statusFrom + ", statusTo=" + statusTo + "]";
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirectionTo() {
		return directionTo;
	}

	public void setDirectionTo(String directionTo) {
		this.directionTo = directionTo;
	}

	public String getDirectionFrom() {
		return directionFrom;
	}

	public void setDirectionFrom(String directionFrom) {
		this.directionFrom = directionFrom;
	}

	public String getDelayTo() {
		return delayTo;
	}

	public void setDelayTo(String delayTo) {
		this.delayTo = delayTo;
	}

	public String getDelayFrom() {
		return delayFrom;
	}

	public void setDelayFrom(String delayFrom) {
		this.delayFrom = delayFrom;
	}

	public String getStatusTo() {
		return statusTo;
	}

	public void setStatusTo(String statusTo) {
		this.statusTo = statusTo;
	}

	public String getStatusFrom() {
		return statusFrom;
	}

	public void setStatusFrom(String statusFrom) {
		this.statusFrom = statusFrom;
	}

	public String getStatusMessageTo() {
		if(statusMessageTo == null)
			return null;
		return statusMessageTo.getValue();
	}

	public void setStatusMessageTo(String messageTo) {
		if(messageTo != null)
			this.statusMessageTo = new Text(messageTo);
	}

	public String getStatusMessageFrom() {
		if(statusMessageFrom == null)
			return null;
		return statusMessageFrom.getValue();
	}

	public void setStatusMessageFrom(String messageFrom) {
		if(messageFrom != null)
			this.statusMessageFrom = new Text(messageFrom);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	 
	 
}
