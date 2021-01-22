package com.example.pagingmissioncontrol;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.pagingmissioncontrol.dataprocessor.DataProcessor;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class PagingMissionControlApplicationTests {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;
	
	@Autowired
	DataProcessor dataProcessor;
	
	@BeforeEach
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@AfterEach
	public void restoreStreams() {
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	}
	
	@Test
	public void testEmptyFileReturnsMessageToUser() {
		dataProcessor.setFileName("nodata.txt");

		dataProcessor.processData();
		assertEquals("Empty data set. Please include data in the input file.", outContent.toString());
	}
	
	@Test
	public void testTwoDevicesData() {
		String dataJsonString = "[{\"severity\":\"RED HIGH\",\"component\":\"TSTAT\",\"satelliteId\":1000,\"timestamp\":\"2018-01-01T23:01:38.001Z\"},{\"severity\":\"RED LOW\",\"component\":\"BATT\",\"satelliteId\":1000,\"timestamp\":\"2018-01-01T23:01:09.521Z\"}]\n" +"";
		dataProcessor.setFileName("twodevicesdata.txt");
		dataProcessor.processData();
		assertThat(dataJsonString, containsString(outContent.toString()));
	}
	
	@Test
	public void testFileNotFoundCaught() {
		dataProcessor.setFileName("wrongFile.txt");
		dataProcessor.processData();
		assertEquals("Unable to find file.", outContent.toString());
	}
	
	@Test
	public void testLargeDataSetWithMultipleEventsIn5MinRange() {
		dataProcessor.setFileName("largerdataset.txt");
		String expectedJsonString = "[{\"severity\":\"RED HIGH\",\"component\":\"TSTAT\",\"satelliteId\":1006,\"timestamp\":\"2018-01-01T23:07:49.021Z\"},{\"severity\":\"RED HIGH\",\"component\":\"TSTAT\",\"satelliteId\":1004,\"timestamp\":\"2018-01-01T23:01:26.011Z\"},{\"severity\":\"RED HIGH\",\"component\":\"TSTAT\",\"satelliteId\":1001,\"timestamp\":\"2018-01-01T23:01:26.011Z\"},{\"severity\":\"RED HIGH\",\"component\":\"TSTAT\",\"satelliteId\":1001,\"timestamp\":\"2018-01-01T23:07:49.021Z\"},{\"severity\":\"RED HIGH\",\"component\":\"TSTAT\",\"satelliteId\":1001,\"timestamp\":\"2018-01-01T23:17:49.021Z\"},{\"severity\":\"RED HIGH\",\"component\":\"TSTAT\",\"satelliteId\":1000,\"timestamp\":\"2018-01-01T23:01:38.001Z\"},{\"severity\":\"RED LOW\",\"component\":\"BATT\",\"satelliteId\":1000,\"timestamp\":\"2018-01-01T23:01:09.521Z\"}]";
		dataProcessor.processData();
		assertEquals(expectedJsonString, outContent.toString());
	}

}
