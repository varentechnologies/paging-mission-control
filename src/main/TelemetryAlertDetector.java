package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TelemetryAlertDetector {

    public static void main (String[] args) {
        return;
    }

    public static void parseTelemetryFileForAlerts(String telemetryFile) throws FileNotFoundException {
        File file = new File(telemetryFile);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine()) {
            //ITelemetryData telemetryData = TelemetryDataFactory.getTelemetry(scanner.nextLine());

        }

    }
}
