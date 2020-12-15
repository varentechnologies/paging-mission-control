package com.example.pagingmissioncontrol;

import com.example.pagingmissioncontrol.controllers.MonitorController;
import com.example.pagingmissioncontrol.model.Alert;
import com.example.pagingmissioncontrol.model.TelemetryEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PagingMissionControlApplication {


	public static void main(String[] args) throws JSONException {
		MonitorController monitorController = new MonitorController();
		ArrayList<TelemetryEntry> telemetryData = monitorController.loadData();

		ArrayList<Alert> alerts = new ArrayList();
		alerts.addAll(monitorController.alertBattery(telemetryData));
		alerts.addAll(monitorController.alertTemp(telemetryData));

		JSONArray json = new JSONArray();

		for (int i = 0; i < alerts.size(); i++) {
			System.out.println(alerts.get(i).toString());
			JSONObject item = new JSONObject();
			item.put("satelliteId", alerts.get(i).getId());
			item.put("severity", alerts.get(i).getSeverity());
			item.put("component", alerts.get(i).getComponent());

			//"2018-01-01T23:01:38.001Z"
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String timestamp = sdf.format(alerts.get(i).getTimestamp());


			item.put("timestamp", timestamp);
			json.put(item);

		}


		System.out.println(json);

	}



}
