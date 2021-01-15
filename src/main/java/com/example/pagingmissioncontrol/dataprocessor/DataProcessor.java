package com.example.pagingmissioncontrol.dataprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.pagingmissioncontrol.model.AlertResponseObject;
import com.example.pagingmissioncontrol.model.TelemetryObject;

public class DataProcessor {

	private static final String dateFormat = "yyyyMMdd HH:mm:ss.S";
	private static final String dataDelimiter = "\\|";

	public void processData() {

		// use to store unique satellite and its information telemetery entries
		HashMap<String, List<TelemetryObject>> telemetryDataMap = new HashMap<String, List<TelemetryObject>>();

		//read in file 
		File file = new File("src/main/resources/data.txt");
		Scanner input = null;
		try {
			input = new Scanner(file);
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//create list for all data
		List<AlertResponseObject> responseList = new ArrayList<AlertResponseObject>();

		// check readings for thermostat and battery
		responseList.addAll(checkReadings(telemetryDataMap, "TSTAT", "RED HIGH"));
		responseList.addAll(checkReadings(telemetryDataMap, "BATT", "RED LOW"));
		//convert list to json and return response to user
		convertToJson(responseList);

	}

	private void convertToJson(List<AlertResponseObject> responseList) {
		// take error events and create objects and add them to array to be returned to user
		JSONArray responseObjectArray = new JSONArray();
		for (int i = 0; i < responseList.size(); i++) {
			JSONObject object = new JSONObject();
			// convert date back to format requests, add all values to json object, add object to array
			SimpleDateFormat dateResponseFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String timestamp = dateResponseFormat.format(responseList.get(i).getTimestamp());
			object.put("timestamp", timestamp);
			object.put("satelliteId", responseList.get(i).getSateliteId());
			object.put("component", responseList.get(i).getComponent());
			object.put("severity", responseList.get(i).getSeverity());
			responseObjectArray.put(object);
		}

		System.out.println(responseObjectArray);
	}

	private List<AlertResponseObject> checkReadings(HashMap<String, List<TelemetryObject>> telemetryDataMap,
			String component, String severity) {

		
		List<AlertResponseObject> responseList = new ArrayList<AlertResponseObject>();
		//loop through all unique sattelitte ids
		for (String key : telemetryDataMap.keySet()) {
			List<TelemetryObject> telemetryList = telemetryDataMap.get(key);
			// filter out readings that aren't component
			telemetryList = telemetryList.stream()
					.filter(telemetryData -> telemetryData.getComponent().equals(component))
					.collect(Collectors.toList());
			// sort filtered data by date
			telemetryList.sort((list1, list2) -> list1.getTimestamp().compareTo(list2.getTimestamp()));
			// if all results filtered skip this satellite id
			if(telemetryList.isEmpty()) {
				continue;
			}
			
			// set start to earliest time, end to start+5mins
			Date startDate = telemetryList.get(0).getTimestamp();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.MINUTE, 5);
			Date endDate = calendar.getTime();

			System.out.println("Start date" + startDate);
			System.out.println("End date" + endDate);

			int exceedCount = 0;

			for (TelemetryObject telemetry : telemetryList) {
				// check if time is equal or in between five min
				if (telemetry.getTimestamp().equals(startDate) || telemetry.getTimestamp().equals(endDate)
						|| (telemetry.getTimestamp().after(startDate) && telemetry.getTimestamp().before(endDate))) {
					if (component.equals("TSTAT") && telemetry.getRawValue() > telemetry.getRedHighLimit()) {
						exceedCount++;
					} else if (component.equals("BATT") && telemetry.getRawValue() < telemetry.getRedLowLimit()) {
						exceedCount++;
					}

					if (exceedCount >= 3) {
						AlertResponseObject alertResponseObject = new AlertResponseObject();
						alertResponseObject.setComponent(telemetry.getComponent());
						alertResponseObject.setSateliteId(Integer.parseInt(telemetry.getSatelliteId()));
						alertResponseObject.setSeverity(severity);
						alertResponseObject.setTimestamp(startDate);
						responseList.add(alertResponseObject);
					}
				} else {
					// outside of time range, reset for next time range
					exceedCount = 0;
					startDate = endDate;
					Calendar calendar2 = Calendar.getInstance();
					calendar2.setTime(startDate);
					calendar2.add(Calendar.MINUTE, 5);
					endDate = calendar.getTime();

				}
			}
		}

		System.out.println("Alert list is :" + responseList.toString());
		return responseList;

	}

	private TelemetryObject createTelemetryObject(String[] inputList) {
		TelemetryObject telemetryObject = new TelemetryObject();

		Date strDate = null;
		try {
			strDate = new SimpleDateFormat(dateFormat).parse(inputList[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		telemetryObject.setTimestamp(strDate);
		telemetryObject.setSatelliteId(inputList[1]);
		telemetryObject.setRedHighLimit(Double.parseDouble(inputList[2]));
		telemetryObject.setYellowHighLimit(Double.parseDouble(inputList[3]));
		telemetryObject.setYellowLowLimit(Double.parseDouble(inputList[4]));
		telemetryObject.setRedLowLimit(Double.parseDouble(inputList[5]));
		telemetryObject.setRawValue(Double.parseDouble(inputList[6]));
		telemetryObject.setComponent(inputList[7]);
		return telemetryObject;
	}

}
