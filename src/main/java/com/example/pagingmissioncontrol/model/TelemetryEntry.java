package com.example.pagingmissioncontrol.model;


import java.util.Date;

//<timestamp>|<satellite-id>|<red-high-limit>|<yellow-high-limit>|<yellow-low-limit>|<red-low-limit>|<raw-value>|<component>
public class TelemetryEntry {
    private Date timestamp;
    private String satelliteId;
    private double redHighLimit;
    private double yellowHighLimit;
    private double yellowLowLimit;
    private double redLowLimit;
    private double rawValue;
    private Component component;

    public TelemetryEntry(Date timestamp, String satelliteId, double redHighLimit, double yellowHighLimit, double yellowLowLimit, double redLowLimit, double rawValue, Component component) {
        this.timestamp = timestamp;
        this.satelliteId = satelliteId;
        this.redHighLimit = redHighLimit;
        this.yellowHighLimit = yellowHighLimit;
        this.yellowLowLimit = yellowLowLimit;
        this.redLowLimit = redLowLimit;
        this.rawValue = rawValue;
        this.component = component;
    }

    public TelemetryEntry() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

}
