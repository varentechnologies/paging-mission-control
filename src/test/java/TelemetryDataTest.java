package test;

import org.junit.Before;
import org.junit.Test;
import main.TelemetryData;

import static org.junit.jupiter.api.Assertions.*;

public class TelemetryDataTest {

    private main.TelemetryData data;

    @Before
    public void beforeEachTest() {
        this.data = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|99.9|TSTAT");
    }

    @Test
    public void testGetTimestamp() {
        assertEquals(this.data.getTimestamp().toString(), "2018-01-01T23:01:05.001");
    }

    @Test
    public void testSatelliteId() {
        assertEquals(this.data.getSatelliteId(), 1001);
    }

    @Test
    public void testComponent() {
        assertEquals(this.data.getComponent(), TelemetryData.Component.TSTAT);
    }

    @Test
    public void testIsAlert_TSTAT_False() {
        assertFalse(this.data.isAlert());
    }

    @Test
    public void testIsAlert_TSTAT_True() {
        main.TelemetryData data = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|101.1|TSTAT");
        assertTrue(data.isAlert());
    }

    @Test
    public void testIsAlert_BATT_False() {
        TelemetryData data = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|101.1|BATT");
        assertFalse(data.isAlert());
    }

    @Test
    public void testIsAlert_BATT_True() {
        TelemetryData data = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|19.9|BATT");
        assertTrue(data.isAlert());
    }
    @Test
    public void testToJSON() {
        //JsonObject json = Json.createObjectBuilder()
        //        .add("satelliteId", 1000);
    }
}