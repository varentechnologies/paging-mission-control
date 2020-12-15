package com.example.pagingmissioncontrol.model;

import java.util.Date;

public class Alert {
    private String id;
    private Date timestamp;
    private Severity severity;
    private Component component;

    public Alert(String id, Date timestamp, Severity severity, Component component) {
        this.id = id;
        this.timestamp = timestamp;
        this.severity = severity;
        this.component = component;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", severity=" + severity +
                ", component=" + component +
                '}';
    }
}
