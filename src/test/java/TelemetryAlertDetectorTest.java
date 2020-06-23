import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;

import static org.junit.Assert.*;


public class TelemetryAlertDetectorTest {

    TelemetryAlertDetector tad;
    @Before
    public void beforeEachTest() {
        this.tad = new TelemetryAlertDetector();
    }

    @Test
    public void parseTelemetryFileForAlerts() {
        //final PrintStream stdOut = System.out;
        final ByteArrayOutputStream newOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newOut));
        String expected = "{\"severity\":\"RED HIGH\",\"component\":\"TSTAT\",\"satelliteId\":1000,\"timestamp\":\"20180101T23:03:05.009Z\"}\r\n" +
                "{\"severity\":\"RED LOW\",\"component\":\"BATT\",\"satelliteId\":1000,\"timestamp\":\"20180101T23:04:11.531Z\"}\r\n" +
                "{\"severity\":\"RED HIGH\",\"component\":\"TSTAT\",\"satelliteId\":1001,\"timestamp\":\"20180101T23:08:05.021Z\"}\r\n";
        try {
            this.tad.parseTelemetryFileForAlerts(System.getProperty("user.dir") + "\\src\\test\\SampleTelemetryData.txt");
        } catch( FileNotFoundException e) {
        }
        assertEquals(expected, newOut.toString());


    }

    @Test
    public void testGetTelemetryDataMap_TSTAT() {
        TelemetryData tstat = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|99.9|TSTAT");
        assertEquals(tad.getThermostatAlertMap(), tad.getTelemetryDataMap(tstat));
    }

    @Test
    public void testGetTelemetryDataMap_BATT() {
        TelemetryData batt = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|99.9|BATT");
        assertEquals(tad.getBatteryAlertMap(), tad.getTelemetryDataMap(batt));
    }

    @Test
    public void getTelemetryAlertListFromTelemetryData() {
        TelemetryData batt = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|99.9|BATT");
        LinkedList<TelemetryData> list = this.tad.getTelemetryAlertListFromTelemetryData(batt);
        assertEquals(list.size(), 0);

        list.add(batt);
        LinkedList<TelemetryData> list2 = tad.getTelemetryAlertListFromTelemetryData(batt);
        assertEquals(list2.size(), 1);
    }

    @Test
    public void testCheckForAlertSequence_ALERT() {
        LinkedList<TelemetryData> list = new LinkedList<>();
        list.add(new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|9|BATT"));
        list.add(new TelemetryData("20180101 23:01:06.001|1001|101|98|25|20|9|BATT"));
        list.add(new TelemetryData("20180101 23:01:07.001|1001|101|98|25|20|9|BATT"));

        tad.checkForAlertSequence(list);
        assertEquals(list.size(), 0);
    }

    @Test
    public void testCheckForAlertSequence_NO_ALERT() {
        LinkedList<TelemetryData> list = new LinkedList<>();
        list.add(new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|9|BATT"));
        list.add(new TelemetryData("20180101 23:03:16.001|1001|101|98|25|20|9|BATT"));
        list.add(new TelemetryData("20180101 23:06:17.001|1001|101|98|25|20|9|BATT"));

        tad.checkForAlertSequence(list);
        assertEquals(list.size(), 2);
    }


}