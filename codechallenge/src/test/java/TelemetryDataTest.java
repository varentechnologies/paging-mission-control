package test;

import main.TelemetryData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TelemetryDataTest {

    private TelemetryData data;

    @BeforeEach
    public void beforeEachTest() {
        this.data = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|99.9|TSTAT");
    }

    @Test
    void testGetTimestamp() {
        assertEquals(this.data.getTimestamp().toString(), "2018-01-01T23:01:05.001");
    }

    @Test
    void testSatelliteId() {
        assertEquals(this.data.getSatelliteId(), 1001);
    }

    @Test
    void testComponent() {
        assertEquals(this.data.getComponent(), TelemetryData.Component.TSTAT);
    }

    @Test
    void testIsAlert_TSTAT_False() {
        assertFalse(this.data.isAlert());
    }

    @Test
    void testIsAlert_TSTAT_True() {
        TelemetryData data = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|101.1|TSTAT");
        assertTrue(data.isAlert());
    }

    @Test
    void testIsAlert_BATT_False() {
        TelemetryData data = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|101.1|BATT");
        assertFalse(data.isAlert());
    }

    @Test
    void testIsAlert_BATT_True() {
        TelemetryData data = new TelemetryData("20180101 23:01:05.001|1001|101|98|25|20|19.9|BATT");
        assertTrue(data.isAlert());
    }
    @Test
    void testToJSON() {
        JsonObject json = Json.createObjectBuilder()
                .add("satelliteId", 1000);
    }
}