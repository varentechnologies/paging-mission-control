package com.varentechnologies.pagingmissioncontrol;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test that a file is automatically processed on startup.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"file.auto-process=true"}, args = "--filename=src/test/resources/telemetry_example_001.txt")
public class AutomaticInputFileProcessingTests {

    @Autowired
    private InputFileLoader inputFileLoader;

    @Test
    public void fileLoadedOnStartup() throws Exception {
        Assert.assertEquals(true, inputFileLoader.isProcessed());
    }
}
