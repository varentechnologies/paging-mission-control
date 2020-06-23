import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class TelemetryAlertDetector {

    /*
      Using a hashmap will give O(1) insertions and lookups, which will be important if there are many satellites / component

      I am making the assumption that the telemetry data is already sorted by its timestamp. If the data was unsorted,
      then a PriorityQueue may be the better option here rather than LinkedList.
    */
    private HashMap<Integer, LinkedList<TelemetryData>> batteryAlertMap;
    private HashMap<Integer, LinkedList<TelemetryData>> thermostatAlertMap;

    public static void main (String[] args) {
        return;
    }

    public TelemetryAlertDetector() {
        this.batteryAlertMap = new HashMap<>();
        this.thermostatAlertMap = new HashMap<>();
    }

    public HashMap<Integer, LinkedList<TelemetryData>> getBatteryAlertMap() {
        return batteryAlertMap;
    }
    public HashMap<Integer, LinkedList<TelemetryData>> getThermostatAlertMap() {
        return thermostatAlertMap;
    }


    /*
        Takes a Telemetry File and parses each line, looking for Alerts. If an alert is found, it is added to the appropriate hashmap linked list
        based on component type. If the added value is the 3rd element in the linked list, the values are checked to determine if there have been 3 alerts
        within the allotted time window
     */
    public void parseTelemetryFileForAlerts(String telemetryFile) throws FileNotFoundException {
        File file = new File(telemetryFile);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine()) {
            TelemetryData td = new TelemetryData(scanner.nextLine());
            if(td.isAlert()) {
                LinkedList<TelemetryData> telemetryList = this.getTelemetryAlertListFromTelemetryData(td);
                telemetryList.add(td);
                this.checkForAlertSequence(telemetryList);
            }
        }
    }

    /*
        This function will check the telemetry list to see if the time difference between the first element and the last element is < 5 seconds.
        This function will immediately return if there are not 3 elements in the list. Again, we are assuming that the telemetry data is in order.
        If the data was not in order, changing the linked list to a priority queue would solve this issue. Additionally, the linked list is cleared
        when 3 alerts in 5 seconds are detected. This is based on my interpretation of the results. If the window should be a rolling 5 second window,
        then the clear() should be replaced by removeLast().
     */
    public void checkForAlertSequence(LinkedList<TelemetryData>  telemetryList) {
        if (telemetryList.size() != 3) return;

        if (Duration.between(telemetryList.getFirst().getTimestamp(), telemetryList.getLast().getTimestamp()).toMillis() < 5000) {
            System.out.println(telemetryList.getLast().toJSON());
            telemetryList.clear(); //Clearing the list here to reset the 5-second window
        } else {
            telemetryList.removeLast(); //this will move the 5-second window forward
        }
    }

    /*
        This function returns the telemetry list from the correct hash map based on the satellite ID. It also puts an empty list
        if this is the first time this satellite has been recorded.
     */
    public LinkedList<TelemetryData> getTelemetryAlertListFromTelemetryData(TelemetryData data) {
        if (!this.getTelemetryDataMap(data).containsKey(data.getSatelliteId())) {
            this.getTelemetryDataMap(data).put(data.getSatelliteId(), new LinkedList<TelemetryData>());
        }
        return this.getTelemetryDataMap(data).get(data.getSatelliteId());
    }

    /*
        I considered returning a new empty hash map here, but in this case I want to know if I have an error. If I wanted this to run
        regardless of error conditions, returning a new map might be a better alternative.
    */
    public HashMap<Integer, LinkedList<TelemetryData>> getTelemetryDataMap(TelemetryData data) {
        switch (data.getComponent()) {
            case BATT:
                return this.getBatteryAlertMap();
            case TSTAT:
                return this.getThermostatAlertMap();
        }
        return null;
    }
}
