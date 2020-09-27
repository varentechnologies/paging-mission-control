package com.varentechnologies.pagingmissioncontrol;

import com.varentechnologies.pagingmissioncontrol.entity.Alert;
import com.varentechnologies.pagingmissioncontrol.util.AlertSerializationUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertSerializationTests {

    @Test
    public void canSerialize() throws IOException {
        // given
        Alert first = new Alert(1000, "RED HIGH", "TSTAT", LocalDateTime.of(2018, 1, 1, 23, 1, 38, 1000000));
        Alert second = new Alert(1000, "RED LOW", "BATT", LocalDateTime.of(2018, 1, 1, 23, 1, 9, 521000000));
        List<Alert> alerts = new ArrayList<>();
        alerts.add(first);
        alerts.add(second);
        String expected = Files.readString(Path.of("src/test/resources/alerts_example_001.txt"));

        // when
        String result = AlertSerializationUtil.deserialize(alerts);

        // then
        // TODO: the example JSON has no space after the key and before the colon ("key: value" instead of "key : value")
        // TODO: likely not something to worry about but wouldn't be that difficult to handle if necessary
        Assert.assertEquals(StringUtils.trimAllWhitespace(expected), StringUtils.trimAllWhitespace(result));
    }
}
