package com.varentechnologies.pagingmissioncontrol;

import com.varentechnologies.pagingmissioncontrol.entity.TelemetryData;
import com.varentechnologies.pagingmissioncontrol.util.TelemetryDataDeserializerUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class TelemetryDeserializationTests {

    @Test
    public void canDeserialize() {
        // given
        File file = new File("src/test/resources/telemetry_example_001.txt");

        // when
        List<TelemetryData> data = TelemetryDataDeserializerUtil.fromPipeDelimitedFile(file);

        // then
        Assert.assertEquals(14, data.size());
        TelemetryData first = data.get(0);
        Assert.assertEquals(LocalDateTime.of(2018, 1, 1, 23, 1, 5, 1000000), first.getTime());
        Assert.assertEquals(1001, first.getSatelliteId().intValue());
        Assert.assertEquals(101, first.getRedHighLimit().intValue());
        Assert.assertEquals(98, first.getYellowHighLimit().intValue());
        Assert.assertEquals(25, first.getYellowLowLimit().intValue());
        Assert.assertEquals(20, first.getRedLowLimit().intValue());
        Assert.assertEquals(Float.valueOf("99.9"), first.getValue());
    }
}
