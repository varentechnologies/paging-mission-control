package com.example.pagingmissioncontrol;

import org.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.pagingmissioncontrol.dataprocessor.DataProcessor;
import com.example.pagingmissioncontrol.dataprocessor.InputProcessor;

@SpringBootApplication
public class PagingMissionControlApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PagingMissionControlApplication.class, args);
		InputProcessor inputProcessor = new InputProcessor();
		DataProcessor dataProcessor = new DataProcessor(inputProcessor);
		JSONArray output = dataProcessor.processData();
		if(output!=null) {
			System.out.print(output.toString(4));
		}
	}

}
