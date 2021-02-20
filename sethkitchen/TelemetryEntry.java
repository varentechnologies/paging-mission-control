package sethkitchen;

//////////////////////////////////////// Imports
import java.text.SimpleDateFormat;
import java.util.Date;

////////////////////////////////////////////////////////////// Class Def
//
//                      TelemetryEntry
// Core class to abstract the text file readings into a
// model which can keep track of itself and run checks.
//
//////////////////////////////////////////////////////////////
public class TelemetryEntry {

    ///////////////////////////////// Member Vars
    // Actual use fields -- transient -- not used in GSON/JSON
    private transient String mComponent;
    private transient SatelliteInfo mSatellite;
    private transient Date mTimestamp;

    // JSON fields - for GSON must be part of class
    private int satelliteId;
    private String severity;
    private String component;
    private String timestamp;

    //////////////////////////////// Functions
    // Comparison function for checking if entries are relevant to each other.
    // Requirements state relevancy is within 5 minutes.
    public static boolean areWithin5Minutes(TelemetryEntry one, TelemetryEntry two) {
        long diff = Math.abs(one.getTimestamp().getTime() - two.getTimestamp().getTime());
        if (diff > 5L * 60 * 1000) {
            return false;
        }
        return true;
    }

    // If the telemetry log is not a battery type, it automatically passes battery check.
    // otherwise we need to check it's reading is above the red low threshold.
    public boolean batteryOk() {
        if(!mComponent.equals("BATT")) {
            return true;
        }
        return mSatellite.isLowOk();
    }

    // Used for getting satellite id.
    public SatelliteInfo getSatellite() {
        return mSatellite;
    }

    // Used in static comparison function.
    public Date getTimestamp() {
        return mTimestamp;
    }

    // Sets the JSON member variables
    private void prepareForPrettyPrint() {
        satelliteId = Integer.parseInt(mSatellite.getId());
        if(mComponent.equals("TSTAT"))
        {
            severity = "RED HIGH";
        }
        else
        {
            severity = "RED LOW";
        }
        component = mComponent;
        // 2018-01-01T23:01:38.001Z"
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss.SSS'Z'");
        timestamp = parser.format(mTimestamp);
    }
    
    // Constructor: Parses raw input string
    public TelemetryEntry(String encodedString) {
        try {
            String[] lineSplit = encodedString.split("\\|");
            SimpleDateFormat parser = new SimpleDateFormat("yyyyMMDD HH:mm:ss.SSS");
            mTimestamp = parser.parse(lineSplit[0]);
            mSatellite = new SatelliteInfo(lineSplit[1], Integer.parseInt(lineSplit[2]), Integer.parseInt(lineSplit[3]), Integer.parseInt(lineSplit[4]), Integer.parseInt(lineSplit[5]), Float.parseFloat(lineSplit[6]));
            mComponent = lineSplit[7];
            prepareForPrettyPrint();
        } catch (Exception ex) {
            System.out.println("Warning::: Invalid Telemetry Entry -- filling with defaults: "+ex.getMessage());
            mTimestamp = new Date();
            mSatellite = new SatelliteInfo("", 0, 0, 0, 0, 0);
            mComponent = "";
        }
    }

    // If the telemetry log is not a tstat type, it automatically passes thermostat check.
    // otherwise we need to check it's reading is below the red high threshold.
    public boolean thermoOk() {
        if(!mComponent.equals("TSTAT")) {
            return true;
        }
        return mSatellite.isHighOk();
    }
}
