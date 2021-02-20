package sethkitchen;

////////////////////////////////////////////////////////////// Imports
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import com.google.gson.Gson; // Must download gson jar
import com.google.gson.GsonBuilder; // Must download gson jar


////////////////////////////////////////////////////////////// Class Def
//
//                          Runner
// Provides non-static root object for program start from Main method.
//
//////////////////////////////////////////////////////////////
public class Runner {

    /////////////////////////////////////////////////// Member Vars
    //////////// JSON Requirement
    // This is not needed except to have [] in the final output json. Without the need for square brackets
    // we could just out.println each telemetry data as they are processed. Instead we hold them in here
    // until we are all the way done.
    private List<TelemetryEntry> mFinalAnswer = new ArrayList<TelemetryEntry>();

    //////////// Core Data Types
    // With the possibility of thousands of satellites sending telemetry data to the system,
    // it's important that the relevant satellite history can be pulled out efficiently.
    // By mapping the satellite id to a history list relevant history is grabbed in O(1)-O(n)
    //
    // The history list is further improved by using a Queue. We know the data is coming in sequentially,
    // we do not need to update in the middle of the list, and can check the top to see if the data is
    // within 5 minutes. If not pull off and check again.
    //
    // One of these checks battery low, the other checks thermo high
    private Map<String, Queue<TelemetryEntry>> mSatelliteBatteryExceptionQueue = new HashMap<String, Queue<TelemetryEntry>>();
    private Map<String, Queue<TelemetryEntry>> mSatelliteThermoExceptionQueue = new HashMap<String, Queue<TelemetryEntry>>();
    
    
    /////////////////////////////////////////////////// Functions
    // If battery is not in red zone do nothing.
    // If battery is in red zone and nothing is in history, create a new history.
    // If battery is in red zone and there is history: 
    //     - Pull off data that is not in the last 5 minutes.
    //     - If there is 3 or more history points remaining, add alert to final answer
    void checkBattery(TelemetryEntry telemetryEntry) {
        if(!telemetryEntry.batteryOk()){
            Queue<TelemetryEntry> thisSatelliteQueue = mSatelliteBatteryExceptionQueue.get(telemetryEntry.getSatellite().getId());
            if(thisSatelliteQueue == null) {
                thisSatelliteQueue = new LinkedList<TelemetryEntry>();
                thisSatelliteQueue.add(telemetryEntry);
                mSatelliteBatteryExceptionQueue.put(telemetryEntry.getSatellite().getId(), thisSatelliteQueue);
            }
            else
            {
                while(!TelemetryEntry.areWithin5Minutes(thisSatelliteQueue.peek(), telemetryEntry)){
                    thisSatelliteQueue.remove();
                }
                thisSatelliteQueue.add(telemetryEntry);
                if(thisSatelliteQueue.size() > 2) {
                    mFinalAnswer.add(thisSatelliteQueue.peek());
                }
                mSatelliteBatteryExceptionQueue.put(telemetryEntry.getSatellite().getId(), thisSatelliteQueue);
            } 
        }
    }

    // If thermo is not in red zone do nothing.
    // If thermo is in red zone and nothing is in history, create a new history.
    // If thermo is in red zone and there is history: 
    //     - Pull off data that is not in the last 5 minutes.
    //     - If there is 3 or more history points remaining, add alert to final answer
    void checkThermo(TelemetryEntry telemetryEntry) {
        if(!telemetryEntry.thermoOk()) {
            Queue<TelemetryEntry> thisSatelliteQueue = mSatelliteThermoExceptionQueue.get(telemetryEntry.getSatellite().getId());
            if(thisSatelliteQueue == null) {
                thisSatelliteQueue = new LinkedList<TelemetryEntry>();
                thisSatelliteQueue.add(telemetryEntry);
                mSatelliteThermoExceptionQueue.put(telemetryEntry.getSatellite().getId(), thisSatelliteQueue);
            }
            else
            {
                while(!TelemetryEntry.areWithin5Minutes(thisSatelliteQueue.peek(), telemetryEntry)){
                    thisSatelliteQueue.remove();
                }
                thisSatelliteQueue.add(telemetryEntry);
                if(thisSatelliteQueue.size() > 2) {
                    mFinalAnswer.add(thisSatelliteQueue.peek());
                } 
                mSatelliteThermoExceptionQueue.put(telemetryEntry.getSatellite().getId(), thisSatelliteQueue);
            } 
        }
    }

    // Show final answer in expected format.
    void prettyPrintResult() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(mFinalAnswer);
        System.out.println(json);
    }

    // Parse line from file to object then run core checks
    void processNextEntry(String line) {
        TelemetryEntry telemetryEntry= new TelemetryEntry(line);
        checkBattery(telemetryEntry);
        checkThermo(telemetryEntry);
    }

    // Wrapper for main method... reads from file and runs program.
    void runProgram() {
        File f=new File("./input.txt");
        try(Scanner sc= new Scanner(f, StandardCharsets.UTF_8)) { // Ensures Scanner Disposed
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                processNextEntry(line);
            }
            prettyPrintResult();
        } catch(Exception ex) {
            System.out.println(ex);
        }
    }
}
