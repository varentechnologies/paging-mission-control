package com.example.pagingmissioncontrol.model;

import org.joda.time.DateTime;

public class TelemetryObject {
    private DateTime timestamp;
    private String satelliteId;
    private double redHighLimit;
    private double yellowHighLimit;
    private double yellowLowLimit;
    private double redLowLimit;
    private double rawValue;
    private String component;
	public DateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getSatelliteId() {
		return satelliteId;
	}
	public void setSatelliteId(String satelliteId) {
		this.satelliteId = satelliteId;
	}
	public double getRedHighLimit() {
		return redHighLimit;
	}
	public void setRedHighLimit(double redHighLimit) {
		this.redHighLimit = redHighLimit;
	}
	public double getYellowHighLimit() {
		return yellowHighLimit;
	}
	public void setYellowHighLimit(double yellowHighLimit) {
		this.yellowHighLimit = yellowHighLimit;
	}
	public double getYellowLowLimit() {
		return yellowLowLimit;
	}
	public void setYellowLowLimit(double yellowLowLimit) {
		this.yellowLowLimit = yellowLowLimit;
	}
	public double getRedLowLimit() {
		return redLowLimit;
	}
	public void setRedLowLimit(double redLowLimit) {
		this.redLowLimit = redLowLimit;
	}
	public double getRawValue() {
		return rawValue;
	}
	public void setRawValue(double rawValue) {
		this.rawValue = rawValue;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	@Override
	public String toString() {
		return "TelemetryObject [timestamp=" + timestamp + ", satelliteId=" + satelliteId + ", redHighLimit="
				+ redHighLimit + ", yellowHighLimit=" + yellowHighLimit + ", yellowLowLimit=" + yellowLowLimit
				+ ", redLowLimit=" + redLowLimit + ", rawValue=" + rawValue + ", component=" + component + "]";
	}
}
