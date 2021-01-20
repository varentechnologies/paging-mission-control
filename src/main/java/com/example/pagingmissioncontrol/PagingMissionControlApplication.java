package com.example.pagingmissioncontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.pagingmissioncontrol.dataprocessor.DataProcessor;

@SpringBootApplication
public class PagingMissionControlApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PagingMissionControlApplication.class, args);
		
		DataProcessor dataProcessor = new DataProcessor();
		dataProcessor.processData();
	}

}
