package com.varentechnologies.pagingmissioncontrol;

import com.varentechnologies.pagingmissioncontrol.helpers.ResultCaptor;
import com.varentechnologies.pagingmissioncontrol.service.AlertService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(args = "--filename=src/test/resources/telemetry_example_001.txt")
public class InputFileLoaderIntegrationTests {

    @SpyBean
    private AlertService alertService;

    @Autowired
    private InputFileLoader loader;

    /**
     * Test the result alert json after processing a file.
     */
    @Test
    public void fileLoadedOnStartup() throws Exception {
        // given
        ResultCaptor<String> printAndClearAlertsCaptor = new ResultCaptor<>();
        doAnswer(printAndClearAlertsCaptor).when(alertService).printAndClearAlerts();
        String expected = Files.readString(Path.of("src/test/resources/alerts_example_001.txt"));

        // when
        when(alertService.printAndClearAlerts()).thenAnswer(printAndClearAlertsCaptor);
        loader.process("src/test/resources/telemetry_example_001.txt", false);

        // then
        verify(alertService, times(2)).handleAlert(any());
        String result = printAndClearAlertsCaptor.getResult();
        Assert.assertEquals(StringUtils.trimAllWhitespace(expected), StringUtils.trimAllWhitespace(result));
    }
}
