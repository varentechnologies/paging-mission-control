package com.example.pagingmissioncontrol.dataprocessor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.pagingmissioncontrol.model.AlertResponseObject;
import com.example.pagingmissioncontrol.model.TelemetryObject;


public class DataProcessor {

	private static final String dateFormat = "yyyyMMdd HH:mm:ss.SSS";
	private static final String dataDelimiter = "\\|";
	private static final String TSTAT = "TSTAT";
	private static final String BATT = "BATT";
	private static final String RED_HIGH = "RED HIGH";
	private static final String RED_LOW = "RED LOW";
	private String fileName = "";
	private boolean isCustomFile = false;
	private InputProcessor inputProcessor;
	
	public DataProcessor(InputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
	}


	public JSONArray processData() {
		
		// use to store unique satellite and its information telemetery entries
		Map<String, List<TelemetryObject>> telemetryDataMap = new HashMap<String, List<TelemetryObject>>();
		Scanner input = null;
		JSONArray response = null;
		this.setFileName(inputProcessor.getFile());
		this.setCustomFile(inputProcessor.getIsCustomFile());
		try {
			if (isCustomFile) {
				BufferedInputStream bufferedInputStream = new BufferedInputStream(
	                      new FileInputStream(fileName));
				// read in file
				input = new Scanner(bufferedInputStream);
			} else {
				ClassLoader classLoader = getClass().getClassLoader();
				InputStream file = classLoader.getResourceAsStream(fileName);
				// read in file
				input = new Scanner(file);
			}
			
			// <timestamp>|<satellite-id>|<red-high-limit>|<yellow-high-limit>|<yellow-low-limit>|<red-low-limit>|<raw-value>|<component>
			while (input.hasNextLine()) {
				String[] inputList = input.nextLine().split(dataDelimiter);
				String telemetryId = inputList[1];
				TelemetryObject telemetryData = createTelemetryObject(inputList);
				// check if in map, if so add to list, otherwise create new map entry
				if (telemetryDataMap.containsKey(telemetryId)) {
					telemetryDataMap.get(telemetryId).add(telemetryData);
				} else {
					List<TelemetryObject> tempList = new ArrayList<TelemetryObject>();
					tempList.add(telemetryData);
					telemetryDataMap.put(telemetryId, tempList);
				}
			}

			// if map empty after trying to read lines, empty file
			if (telemetryDataMap.isEmpty()) {
				System.out.print("Empty data set. Please include data in the input file.");
			} else {
				// create list for all response data
				List<AlertResponseObject> responseList = new ArrayList<AlertResponseObject>();

				// check readings for thermostat and battery
				responseList.addAll(checkReadings(telemetryDataMap, TSTAT, RED_HIGH));
				responseList.addAll(checkReadings(telemetryDataMap, BATT, RED_LOW));
				// convert list to json and return response to user
				return convertToJson(responseList);
			}

			
		} catch (NullPointerException | FileNotFoundException ex) {
			System.out.print("Unable to find file.");
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return response;
	}

	

	private JSONArray convertToJson(List<AlertResponseObject> responseList) {
		// take error events and create objects and add them to array to be returned to
		// user
		JSONArray responseObjectArray = new JSONArray();
		// create new Json object for each violation reading to be added to response
		// array
		for (int i = 0; i < responseList.size(); i++) {
			responseObjectArray.put(responseList.get(i).toJsonObject());
		}
		return responseObjectArray;
	}

	private List<AlertResponseObject> checkReadings(Map<String, List<TelemetryObject>> telemetryDataMap,
			String component, String severity) {

		List<AlertResponseObject> responseList = new ArrayList<AlertResponseObject>();
		// loop through all unique satellite ids
		// for (String key : telemetryDataMap.keySet()) {
		for (Map.Entry<String, List<TelemetryObject>> entry : telemetryDataMap.entrySet()) {
			List<TelemetryObject> telemetryList = entry.getValue();
			// filter out readings that aren't the component we are checking , non
			// violations and sort
			telemetryList = filterAndSortTelemtryList(telemetryList, component);
			// if all results filtered skip this satellite id
			if (telemetryList.isEmpty()) {
				continue;
			}

			// set start to earliest time, end to start+5mins
			// since data is ordered we can take first element as
			DateTime startDate = telemetryList.get(0).getTimestamp();
			DateTime endDate = startDate.plusMinutes(5);

			int exceedCount = 0;
			DateTime firstOccurrence = null;

			// loop through all violations events
			for (TelemetryObject teleMeteryData : telemetryList) {

				if (startDate == null) {
					startDate = teleMeteryData.getTimestamp();
					endDate = startDate.plusMinutes(5);
				}
				if (teleMeteryData.getTimestamp().equals(startDate) || teleMeteryData.getTimestamp().equals(endDate)
						|| (teleMeteryData.getTimestamp().isAfter(startDate)
								&& teleMeteryData.getTimestamp().isBefore(endDate))) {

					if (firstOccurrence == null) {
						firstOccurrence = teleMeteryData.getTimestamp();
					}
					exceedCount++;
					// if violations are 3 or more then we need to report to user
					if (exceedCount == 3) {
						AlertResponseObject alertResponseObject = new AlertResponseObject();
						alertResponseObject.setComponent(teleMeteryData.getComponent());
						alertResponseObject.setSateliteId(Integer.parseInt(teleMeteryData.getSatelliteId()));
						alertResponseObject.setSeverity(severity);
						alertResponseObject.setTimestamp(firstOccurrence);
						responseList.add(alertResponseObject);

						// set start date, first occurrence to null so next element updates it
						// reset the exceedCount
						exceedCount = 0;
						startDate = null;
						firstOccurrence = null;
					}

				} else {

					// we found event outside of time range since first event
					// set exceed to 1 since all events are violations, start to current event
					// set this event as firstOccurrence
					exceedCount = 1;
					startDate = telemetryList.get(0).getTimestamp();
					endDate = startDate.plusMinutes(5);
					firstOccurrence = startDate;

				}
			}
		}
		return responseList;

	}

	private List<TelemetryObject> filterAndSortTelemtryList(List<TelemetryObject> telemetryList, String component) {
		// filter out elements not component
		telemetryList = telemetryList.stream().filter(telemetryData -> telemetryData.getComponent().equals(component))
				.collect(Collectors.toList());

		// leave only violation data for tsat and batt
		if (component.equals(TSTAT)) {
			telemetryList = telemetryList.stream()
					.filter(telemetryData -> telemetryData.getRawValue() > telemetryData.getRedHighLimit())
					.collect(Collectors.toList());
		}
		if (component.equals(BATT)) {
			telemetryList = telemetryList.stream()
					.filter(telemetryData -> telemetryData.getRawValue() < telemetryData.getRedLowLimit())
					.collect(Collectors.toList());
		}

		// sort filtered data by date
		telemetryList.sort((list1, list2) -> list1.getTimestamp().compareTo(list2.getTimestamp()));

		return telemetryList;
	}

	private TelemetryObject createTelemetryObject(String[] inputList) {
		TelemetryObject telemetryObject = new TelemetryObject();

		DateTimeFormatter strDate = DateTimeFormat.forPattern(dateFormat);
		DateTime date2 = strDate.parseDateTime(inputList[0]);
		//strDate = new SimpleDateFormat(dateFormat).parse(inputList[0]);
		telemetryObject.setTimestamp(date2);
		telemetryObject.setSatelliteId(inputList[1]);
		telemetryObject.setRedHighLimit(Double.parseDouble(inputList[2]));
		telemetryObject.setYellowHighLimit(Double.parseDouble(inputList[3]));
		telemetryObject.setYellowLowLimit(Double.parseDouble(inputList[4]));
		telemetryObject.setRedLowLimit(Double.parseDouble(inputList[5]));
		telemetryObject.setRawValue(Double.parseDouble(inputList[6]));
		telemetryObject.setComponent(inputList[7]);
		return telemetryObject;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public boolean isCustomFile() {
		return isCustomFile;
	}



	public void setCustomFile(boolean isCustomFile) {
		this.isCustomFile = isCustomFile;
	}

}
