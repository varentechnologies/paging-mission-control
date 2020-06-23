package test;

import main.TelemetryAlertDetector;
import main.TelemetryData;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class TelemetryAlertDetectorTest {

    TelemetryAlertDetector tad;
    @BeforeEach
    public void beforeEachTest() {
        this.tad = new TelemetryAlertDetector();
    }

    @Test
    void parseTelemetryFileForAlerts() {
    }

    @Test
    void testGetTelemetryDataMap_TSTAT() {
        TelemetryData tstat = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|99.9|TSTAT");
        assertEquals(tad.getThermostatAlertMap(), tad.getTelemetryDataMap(tstat));
    }

    @Test
    void testGetTelemetryDataMap_BATT() {
        TelemetryData batt = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|99.9|BATT");
        assertEquals(tad.getBatteryAlertMap(), tad.getTelemetryDataMap(batt));
    }

    @Test
    void getTelemetryAlertListFromTelemetryData() {
        TelemetryData batt = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|99.9|BATT");
        LinkedList<TelemetryData> list = this.tad.getTelemetryAlertListFromTelemetryData(batt);
        assertEquals(list.size(), 0);

        list.add(batt);
        LinkedList<TelemetryData> list2 = tad.getTelemetryAlertListFromTelemetryData(batt);
        assertEquals(list2.size(), 1);
    }

    @Test
    void testCheckForAlertSequence_ALERT() {
        LinkedList<TelemetryData> list = new LinkedList<>();
        list.add(new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|9|BATT"));
        list.add(new TelemetryData("20180101 23:01:06.001|1001|101|98|25|20|9|BATT"));
        list.add(new TelemetryData("20180101 23:01:07.001|1001|101|98|25|20|9|BATT"));

        tad.checkForAlertSequence(list);
        assertEquals(list.size(), 0);
    }

    @Test
    void testCheckForAlertSequence_NO_ALERT() {
        LinkedList<TelemetryData> list = new LinkedList<>();
        list.add(new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|9|BATT"));
        list.add(new TelemetryData("20180101 23:01:16.001|1001|101|98|25|20|9|BATT"));
        list.add(new TelemetryData("20180101 23:01:17.001|1001|101|98|25|20|9|BATT"));

        tad.checkForAlertSequence(list);
        assertEquals(list.size(), 2);
    }
}