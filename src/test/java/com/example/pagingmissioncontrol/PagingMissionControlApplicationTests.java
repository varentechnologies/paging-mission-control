package com.example.pagingmissioncontrol;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.pagingmissioncontrol.dataprocessor.DataProcessor;
import com.example.pagingmissioncontrol.dataprocessor.InputProcessor;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class PagingMissionControlApplicationTests {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	@Test
	public void testDefaultFileData() {
		InputProcessor inputProcessor = new InputProcessor();
		DataProcessor dataProcessor = new DataProcessor(inputProcessor);
	    String input = "1";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
		JSONArray response = dataProcessor.processData();
		assert (response.toString(4).equals("[\n" + 
				"    {\n" + 
				"        \"severity\": \"RED HIGH\",\n" + 
				"        \"component\": \"TSTAT\",\n" + 
				"        \"satelliteId\": 1000,\n" + 
				"        \"timestamp\": \"2018-01-01T23:01:38.001Z\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"severity\": \"RED LOW\",\n" + 
				"        \"component\": \"BATT\",\n" + 
				"        \"satelliteId\": 1000,\n" + 
				"        \"timestamp\": \"2018-01-01T23:01:09.521Z\"\n" + 
				"    }\n" + 
				"]"));
	}
	
	@Test
	public void testEmptyFileReturnsMessageToUser() {
		InputProcessor inputProcessorMock = Mockito.mock(InputProcessor.class);
		Mockito.when(inputProcessorMock.getFile()).thenReturn("nodata.txt");
		Mockito.doReturn(false).when(inputProcessorMock).getIsCustomFile();	
		
		DataProcessor dataProcessor = new DataProcessor(inputProcessorMock);
	    String input = "1";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
		

	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
		dataProcessor.processData();
		assertEquals("Empty data set. Please include data in the input file.", outContent.toString());
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	}
	
	@Test
	public void testTwoDevicesData() {		
		InputProcessor inputProcessorMock = Mockito.mock(InputProcessor.class);
		Mockito.when(inputProcessorMock.getFile()).thenReturn("twodevicesdata.txt");
		Mockito.doReturn(false).when(inputProcessorMock).getIsCustomFile();	
		
		DataProcessor dataProcessor = new DataProcessor(inputProcessorMock);
	    String input = "1";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
		dataProcessor.processData();
		
		JSONArray response = dataProcessor.processData();
		System.out.println(response.toString(4));
		assert (response.toString(4).equals("[\n" + 
				"    {\n" + 
				"        \"severity\": \"RED HIGH\",\n" + 
				"        \"component\": \"TSTAT\",\n" + 
				"        \"satelliteId\": 1000,\n" + 
				"        \"timestamp\": \"2018-01-01T23:01:38.001Z\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"severity\": \"RED LOW\",\n" + 
				"        \"component\": \"BATT\",\n" + 
				"        \"satelliteId\": 1000,\n" + 
				"        \"timestamp\": \"2018-01-01T23:01:09.521Z\"\n" + 
				"    }\n" + 
				"]"));
	}
	
	
	@Test
	public void testFileNotFoundCaught() {
		InputProcessor inputProcessorMock = Mockito.mock(InputProcessor.class);
		Mockito.when(inputProcessorMock.getFile()).thenReturn("wrongFile.txt");
		Mockito.doReturn(false).when(inputProcessorMock).getIsCustomFile();	
		DataProcessor dataProcessor = new DataProcessor(inputProcessorMock);
	    String input = "1";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
		dataProcessor.processData();
		assertEquals("Unable to find file.", outContent.toString());
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	}
	
	@Test
	public void testLargeDataSetWithMultipleEventsIn5MinRange() {
		InputProcessor inputProcessorMock = Mockito.mock(InputProcessor.class);
		Mockito.when(inputProcessorMock.getFile()).thenReturn("largerdataset.txt");
		Mockito.doReturn(false).when(inputProcessorMock).getIsCustomFile();	
		DataProcessor dataProcessor = new DataProcessor(inputProcessorMock);
	    String input = "1";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
				
		JSONArray response = dataProcessor.processData();
		assert (response.toString(4).equals("[\n" + 
				"    {\n" + 
				"        \"severity\": \"RED HIGH\",\n" + 
				"        \"component\": \"TSTAT\",\n" + 
				"        \"satelliteId\": 1004,\n" + 
				"        \"timestamp\": \"2018-01-01T23:01:26.011Z\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"severity\": \"RED HIGH\",\n" + 
				"        \"component\": \"TSTAT\",\n" + 
				"        \"satelliteId\": 1001,\n" + 
				"        \"timestamp\": \"2018-01-01T23:01:26.011Z\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"severity\": \"RED HIGH\",\n" + 
				"        \"component\": \"TSTAT\",\n" + 
				"        \"satelliteId\": 1000,\n" + 
				"        \"timestamp\": \"2018-01-01T23:01:38.001Z\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"        \"severity\": \"RED LOW\",\n" + 
				"        \"component\": \"BATT\",\n" + 
				"        \"satelliteId\": 1000,\n" + 
				"        \"timestamp\": \"2018-01-01T23:01:09.521Z\"\n" + 
				"    }\n" + 
				"]"));
	}

}
