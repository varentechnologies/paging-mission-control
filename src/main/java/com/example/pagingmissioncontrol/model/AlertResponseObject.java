package com.example.pagingmissioncontrol.model;
import java.util.Date;

public class AlertResponseObject{
	
	private Integer sateliteId;
	private String severity;
	private String component;
	private Date timestamp;
	
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
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "AlertResponseObject [sateliteId=" + sateliteId + ", severity=" + severity + ", component=" + component
				+ ", timestamp=" + timestamp + "]";
	}
	
}
