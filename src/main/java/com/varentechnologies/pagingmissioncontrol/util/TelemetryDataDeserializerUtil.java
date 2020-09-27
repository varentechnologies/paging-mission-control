package com.varentechnologies.pagingmissioncontrol.util;

import com.varentechnologies.pagingmissioncontrol.entity.TelemetryData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class TelemetryDataDeserializerUtil {
    private TelemetryDataDeserializerUtil() { }

    /**
     * We could use something like OpenCSV here for reading pipe-delimited data if our need grew.
     * @param file
     * @return
     */
    public static List<TelemetryData> fromPipeDelimitedFile(File file) {
        try {
            // incoming timestamps with format 20180101 23:01:05.001
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
            List<TelemetryData> pojoList = new LinkedList<TelemetryData>();
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while((line = br.readLine()) != null) {
                String[] fields = line.split("\\|");
                TelemetryData p = new TelemetryData(LocalDateTime.parse(fields[0], formatter), Integer.valueOf(fields[1]), Integer.valueOf(fields[2]), Integer.valueOf(fields[3]), Integer.valueOf(fields[4]), Integer.valueOf(fields[5]), Float.valueOf(fields[6]), fields[7]);
                pojoList.add(p);
            }
            return pojoList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
