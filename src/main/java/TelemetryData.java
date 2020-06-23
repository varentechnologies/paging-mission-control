import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TelemetryData {

    private int satelliteId;
    private LocalDateTime timestamp;
    private Component component;
    private boolean isAlert;
    private String alertName;

    public TelemetryData(String rawTelemetryData) {
        double highLimit = 0;
        double lowLimit = 0;
        double rawValue = 0;

        Scanner telemetryScanner = new Scanner(rawTelemetryData);
        telemetryScanner.useDelimiter("\\|");

        this.setTimestamp(telemetryScanner.next());
        this.setSatelliteId(telemetryScanner.nextInt());
        highLimit = telemetryScanner.nextDouble();
        telemetryScanner.next(); //We don't care about yellow limits, so we can ignore these 2
        telemetryScanner.next();
        lowLimit = telemetryScanner.nextDouble();
        rawValue = telemetryScanner.nextDouble();
        this.setComponent(telemetryScanner.next());

        this.determineIfIsAlert(highLimit, lowLimit, rawValue);
    }

    public int getSatelliteId() {
        return satelliteId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getTimestampString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:ss.SSS'Z'");
        return this.getTimestamp().format(formatter);
    }

    public Component getComponent() {
        return this.component;
    }

    public boolean isAlert() {
        return isAlert;
    }

    public String toJSONString() {
        if (this.isAlert()){
            JSONObject json = new JSONObject();
            json.put("satelliteId", this.getSatelliteId());
            json.put("severity", this.alertName);
            json.put("component", this.getComponent().toString());
            json.put("timestamp", this.getTimestampString());
            return json.toString();
        }

        return null;
    }

    // Private Methods:

    private void setSatelliteId(int id) {
        this.satelliteId = id;
    }
    private void setTimestamp(String rawTimestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
        this.timestamp = LocalDateTime.parse(rawTimestamp, formatter);
    }

    private void setComponent(String componentName) {
        switch(componentName.toUpperCase()) {
            case "TSTAT":
                this.component = Component.TSTAT;
                this.alertName = "RED HIGH";
                break;
            case "BATT":
                this.component = Component.BATT;
                this.alertName = "RED LOW";
                break;
        }
    }

    private void determineIfIsAlert(double highLimit, double lowLimit, double rawValue) {
        switch (component)
        {
            case BATT:
                this.isAlert = rawValue < lowLimit;
                break;
            case TSTAT:
                this.isAlert = rawValue > highLimit;
                break;
        }
    }

    public enum Component {
        TSTAT,
        BATT
    }
}
