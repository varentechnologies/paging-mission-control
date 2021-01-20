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
		
		//dataProcessor.setFilePath("src/test/resources/nodata.txt");
		dataProcessor.setFileName("nodata.txt");

		dataProcessor.processData();
		assertEquals("Empty data set. Please include data in the input file.", outContent.toString());
	}
	
	@Test
	public void testTwoDevicesData() {
		String dataJsonString = "[{\"severity\":\"RED HIGH\",\"component\":\"TSTAT\",\"satelliteId\":1000,\"timestamp\":\"2018-01-01T23:01:38.001Z\"},{\"severity\":\"RED LOW\",\"component\":\"BATT\",\"satelliteId\":1000,\"timestamp\":\"2018-01-01T23:01:09.521Z\"}]\n" +"";
		//dataProcessor.setFilePath("src/test/resources/twodevicesdata.txt");
		dataProcessor.setFileName("twodevicesdata.txt");
		dataProcessor.processData();
		assertThat(dataJsonString, containsString(outContent.toString()));
	}
	
	@Test
	public void testFileNotFoundCaught() {
		//dataProcessor.setFilePath("src/test/resources/wrongFile.txt");
		dataProcessor.setFileName("wrongFile.txt");
		dataProcessor.processData();
		assertEquals("Unable to find file.", outContent.toString());
	}

}
