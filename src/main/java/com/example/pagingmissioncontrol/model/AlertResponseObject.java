package com.example.pagingmissioncontrol.model;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

public class AlertResponseObject{
	
	private Integer sateliteId;
	private String severity;
	private String component;
	private DateTime timestamp;
	
	public Integer getSateliteId() {
		return sateliteId;
	}
	public void setSateliteId(Integer sateliteId) {
		this.sateliteId = sateliteId;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public DateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(DateTime firstOccurrence) {
		this.timestamp = firstOccurrence;
	}
	@Override
	public String toString() {
		return "AlertResponseObject [sateliteId=" + sateliteId + ", severity=" + severity + ", component=" + component
				+ ", timestamp=" + timestamp + "]";
	}
	
	public JSONObject toJsonObject() {
		JSONObject object = new JSONObject();
		// convert date back to requested format, add all values to json object, add
		// objects to response array
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String timestamp = formatter.print(this.getTimestamp());
		
		object.put("satelliteId", this.getSateliteId());
		object.put("severity", this.getSeverity());
		object.put("component", this.getComponent());
		object.put("timestamp", timestamp);
		return object;
	}
}
