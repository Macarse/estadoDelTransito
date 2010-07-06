package com.appspot.estadodeltransito;

import java.io.Serializable;

public class Highway implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String directionTo;
	private String directionFrom;
	private String delayTo;
	private String delayFrom;
	private String statusTo;
	private String statusFrom;
	private Text statusMessageTo;
	private Text statusMessageFrom;

	public Highway() {

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

	public Text getStatusMessageTo() {
		return statusMessageTo;
	}

	public void setStatusMessageTo(Text statusMessageTo) {
		this.statusMessageTo = statusMessageTo;
	}

	public Text getStatusMessageFrom() {
		return statusMessageFrom;
	}

	public void setStatusMessageFrom(Text statusMessageFrom) {
		this.statusMessageFrom = statusMessageFrom;
	}

	public boolean isOK(String normal) {

		if ( statusFrom != null ) {
			if ( !statusFrom.equals(normal) ) {
				return false;
			}
		}

		if ( statusTo != null ) {
			if ( !statusTo.equals(normal) ) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return "Highway [delayFrom=" + delayFrom + ", delayTo=" + delayTo
				+ ", directionFrom=" + directionFrom + ", directionTo="
				+ directionTo + ", name=" + name + ", statusFrom=" + statusFrom
				+ ", statusMessageFrom=" + statusMessageFrom
				+ ", statusMessageTo=" + statusMessageTo + ", statusTo="
				+ statusTo + "]";
	}

	public String getShareMsg() {
		StringBuffer sb = new StringBuffer(name);
		sb.append(". ");

		if ( directionFrom != null ) {
			sb.append(directionFrom);
			sb.append(": ");
			sb.append(statusFrom);
			sb.append(".");
		}

		if ( directionTo != null ) {
			sb.append(" ");
			sb.append(directionTo);
			sb.append(": ");
			sb.append(statusTo);
			sb.append(".");
		}

		return sb.toString();
	}

	public static class Text implements Serializable {
		private static final long serialVersionUID = 1L;
		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "Text [value=" + value + "]";
		}

	}

}
