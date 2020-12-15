package com.example.pagingmissioncontrol.controllers;

import com.example.pagingmissioncontrol.model.Alert;
import com.example.pagingmissioncontrol.model.Component;
import com.example.pagingmissioncontrol.model.Severity;
import com.example.pagingmissioncontrol.model.TelemetryEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MonitorController {
    private static final String inputFile = "input.txt";
    private static final String inputDelimiter = "\\|";
    private static final String dateFormat = "yyyyMMdd HH:mm:ss.S";
    private static final long fiveMinutesInMil = 300000;

    public ArrayList<TelemetryEntry> loadData() {


        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream fis = classloader.getResourceAsStream(inputFile);
        InputStreamReader streamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

        ArrayList<TelemetryEntry> telemetryData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(streamReader)) {

            String entry;
            while ((entry = reader.readLine()) != null) {
                telemetryData.add(parseData(entry));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return telemetryData;
    }

    public ArrayList<Alert> alertTemp(ArrayList<TelemetryEntry> telemetryData){
        List<TelemetryEntry> telemetryDataTemp = telemetryData.stream().filter(data -> data.getComponent().equals(Component.TSTAT)).collect(Collectors.toList());
        HashSet<String> ids = new HashSet<>();
        ArrayList alerts = new ArrayList();

        //generate list of ids to test
        for (int i = 0; i < telemetryDataTemp.size(); i++) {
            //System.out.println(telemetryDataTemp.get(i).getComponent());
            ids.add(telemetryData.get(i).getSatelliteId());
        }

        for (String id : ids) {
            System.out.println("Testing temp for " + id);
            List<TelemetryEntry> telemetryDataTemperatureTemp = telemetryDataTemp.stream().filter(data -> data.getSatelliteId().equals(id)).sorted(Comparator.comparingLong(d -> d.getTimestamp().getTime()))
                    .collect(Collectors.toList());

            Long startTime = telemetryDataTemperatureTemp.get(0).getTimestamp().getTime();
            int startEntry = 0;
            int redHighCount = 0;


            for (TelemetryEntry entry : telemetryDataTemperatureTemp) {

                for (int i = startEntry; i < telemetryDataTemperatureTemp.size(); i++) {


                    if (telemetryDataTemperatureTemp.get(i).getTimestamp().getTime() - startTime < fiveMinutesInMil) {
                        if (telemetryDataTemperatureTemp.get(i).getRawValue() > telemetryDataTemperatureTemp.get(i).getRedHighLimit()) {
                            redHighCount++;
                        }
                    } else {
                        break;
                    }

                }

                if (redHighCount >= 3) {
                    alerts.add(generateTemperatureAlert(telemetryDataTemperatureTemp.get(startEntry)));
                }

                redHighCount = 0;
                startEntry++;
            }


        }

        return alerts;
    }

    private Alert generateTemperatureAlert(TelemetryEntry telemetryEntry) {
        Alert temperatureAlert = new Alert(telemetryEntry.getSatelliteId(),telemetryEntry.getTimestamp(), Severity.RED_HIGH, telemetryEntry.getComponent());
        //System.out.println(temperatureAlert.toString());
        return temperatureAlert;
    }

    public  ArrayList<Alert> alertBattery(ArrayList<TelemetryEntry> telemetryData) {
        List<TelemetryEntry> telemetryDataBattery = telemetryData.stream().filter(data -> data.getComponent().equals(Component.BATT)).collect(Collectors.toList());
        HashSet<String> ids = new HashSet<>();
        ArrayList alerts = new ArrayList();


        //generate list of ids to test
        for (int i = 0; i < telemetryDataBattery.size(); i++) {
            //System.out.println(telemetryDataBattery.get(i).getComponent());
            ids.add(telemetryData.get(i).getSatelliteId());
        }

        for (String id : ids) {
            System.out.println("Testing battery voltage for " + id);
            List<TelemetryEntry> telemetryDataBatteryTemp = telemetryDataBattery.stream().filter(data -> data.getSatelliteId().equals(id)).sorted(Comparator.comparingLong(d -> d.getTimestamp().getTime()))
                    .collect(Collectors.toList());

            Long startTime = telemetryDataBatteryTemp.get(0).getTimestamp().getTime();
            int startEntry = 0;
            int redLowCount = 0;


            for (TelemetryEntry entry : telemetryDataBatteryTemp) {

                for(int i = startEntry; i < telemetryDataBatteryTemp.size(); i++) {


                    if (telemetryDataBatteryTemp.get(i).getTimestamp().getTime() - startTime < fiveMinutesInMil) {
                        if (telemetryDataBatteryTemp.get(i).getRawValue() < telemetryDataBatteryTemp.get(i).getRedLowLimit()) {
                            redLowCount++;
                        }
                    }else{
                        break;
                    }

                }

                if(redLowCount >= 3){
                    alerts.add(generateBattAlert(telemetryDataBatteryTemp.get(startEntry)));
                }

                redLowCount=0;
                startEntry++;
            }



        }
        return alerts;
    }

    private Alert generateBattAlert(TelemetryEntry telemetryEntry) {
        Alert battAlert = new Alert(telemetryEntry.getSatelliteId(),telemetryEntry.getTimestamp(), Severity.RED_LOW, telemetryEntry.getComponent());
        //System.out.println(battAlert.toString());
        return battAlert;
    }

    //<timestamp>|<satellite-id>|<red-high-limit>|<yellow-high-limit>|<yellow-low-limit>|<red-low-limit>|<raw-value>|<component>
    private TelemetryEntry parseData(String entry) throws ParseException {

        String[] splitEntry = entry.split(inputDelimiter);
        System.out.println(Arrays.toString(splitEntry));

        TelemetryEntry parsedEntry = new TelemetryEntry();

        parsedEntry.setTimestamp(new SimpleDateFormat(dateFormat).parse(splitEntry[0]));
        parsedEntry.setSatelliteId(splitEntry[1]);
        parsedEntry.setRedHighLimit(Double.parseDouble(splitEntry[2]));
        parsedEntry.setYellowHighLimit(Double.parseDouble(splitEntry[3]));
        parsedEntry.setYellowLowLimit(Double.parseDouble(splitEntry[4]));
        parsedEntry.setRedLowLimit(Double.parseDouble(splitEntry[5]));
        parsedEntry.setRawValue(Double.parseDouble(splitEntry[6]));
        parsedEntry.setComponent(Component.valueOf(splitEntry[7]));

        return parsedEntry;
    }
}
