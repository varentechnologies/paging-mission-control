package tests;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import sethkitchen.SatelliteInfo;
import sethkitchen.TelemetryEntry;

public class TelemetryEntryTest {

    @Test
    public void yesWithin5Minutes() {
        TelemetryEntry one = new TelemetryEntry("20180101 23:01:05.001|1001|101|98|25|20|99.9|TSTAT");
        TelemetryEntry two = new TelemetryEntry("20180101 23:01:09.521|1000|17|15|9|8|7.8|BATT");
        assertEquals(true, TelemetryEntry.areWithin5Minutes(one, two));
    }

    @Test
    public void notWithin5Minutes() {
        TelemetryEntry one = new TelemetryEntry("20180101 23:01:05.001|1001|101|98|25|20|99.9|TSTAT");
        TelemetryEntry two = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|7.9|BATT");
        assertEquals(false, TelemetryEntry.areWithin5Minutes(one, two));
    }

    @Test
    public void tstatIsBatteryOkWithLowRawValue() {
        TelemetryEntry te = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|7.9|TSTAT");
        assertEquals(true,te.batteryOk());
    }

    @Test
    public void tstatIsBatteryOkWithHighRawValue() {
        TelemetryEntry te = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|15|TSTAT");
        assertEquals(true,te.batteryOk());
    }

    @Test
    public void battIsNOTBatteryOkWithLowRawValue() {
        TelemetryEntry te = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|7.9|BATT");
        assertEquals(false,te.batteryOk());
    }

    @Test
    public void battIsBatteryOkWithHighRawValue() {
        TelemetryEntry te = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|15|BATT");
        assertEquals(true,te.batteryOk());
    }

    @Test
    public void getSatelliteShouldMatchConstructor() {
        TelemetryEntry te = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|15|TSTAT");
        SatelliteInfo si = te.getSatellite();
        assertEquals("1001",si.getId());
    }

    @Test
    public void timestampShouldMatchConstructor() {
        TelemetryEntry te = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|15|TSTAT");
        Date d = te.getTimestamp();
        assertEquals(1514869807421L,d.getTime());
    }
    
    @Test
    public void tstatIsThermoOkWithLowRawValue() {
        TelemetryEntry te = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|7.9|TSTAT");
        assertEquals(true,te.thermoOk());
    }

    @Test
    public void tstatIsNOTThermoOkWithHighRawValue() {
        TelemetryEntry te = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|20|TSTAT");
        assertEquals(false,te.thermoOk());
    }

    @Test
    public void battIsBatteryOkWithLowRawValue() {
        TelemetryEntry te = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|7.9|BATT");
        assertEquals(true,te.thermoOk());
    }

    @Test
    public void battIsThermoOkWithHighRawValue() {
        TelemetryEntry te = new TelemetryEntry("20180101 23:10:07.421|1001|17|15|9|8|20|BATT");
        assertEquals(true,te.thermoOk());
    }
}
