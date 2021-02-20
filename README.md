# Run Instructions

Compile code with

    javac -d . -cp gson-2.8.6.jar sethkitchen/*.java

Run code with

    java -cp gson-2.8.6.jar:. sethkitchen.PagingMissionControl

Compile tests with

    javac -d . -cp junit-4.13.2.jar:gson-2.8.6.jar tests/*.java sethkitchen/*.java

Run tests with

    java -cp junit-4.13.2.jar:gson-2.8.6.jar:.:hamcrest-2.2.jar org.junit.runner.JUnitCore tests.SatelliteInfoTest tests.TelemetryEntryTest
    
# Paging Mission Control

> You are tasked with assisting satellite ground operations for an earth science mission that monitors magnetic field variations at the Earth's poles. A pair of satellites fly in tandem orbit such that at least one will have line of sight with a pole to take accurate readings. The satellite’s science instruments are sensitive to changes in temperature and must be monitored closely. Onboard thermostats take several temperature readings every minute to ensure that the precision magnetometers do not overheat. Battery systems voltage levels are also monitored to ensure that power is available to cooling coils. Design a monitoring and alert application that processes status telemetry from the satellites and generates alert messages in cases of certain limit violation scenarios.  Fork this repository, build your program in the language of your choice, then submit a pull request with your code.

## Requirements
Ingest status telemetry data and create alert messages for the following violation conditions:

- If for the same satellite there are three battery voltage readings that are under the red low limit within a five minute interval.
- If for the same satellite there are three thermostat readings that exceed the red high limit within a five minute interval.

### Input Format
The program is to accept a file as input. The file is an ASCII text file containing pipe delimited records.

The ingest of status telemetry data has the format:

```
<timestamp>|<satellite-id>|<red-high-limit>|<yellow-high-limit>|<yellow-low-limit>|<red-low-limit>|<raw-value>|<component>
```

You may assume that the input files are correctly formatted. Error handling for invalid input files may be ommitted.

### Output Format
The output will specify alert messages.  The alert messages should be in JSON format with the following structure:

```javascript
{
    satelliteId: 1234,
    severity: "severity",
    component: "component",
    timestamp: "timestamp"
}
```

The program will output to screen or console (and not to a file). 

## Sample Data
The following may be used as sample input and output datasets.

### Input

```
20180101 23:01:05.001|1001|101|98|25|20|99.9|TSTAT
20180101 23:01:09.521|1000|17|15|9|8|7.8|BATT
20180101 23:01:26.011|1001|101|98|25|20|99.8|TSTAT
20180101 23:01:38.001|1000|101|98|25|20|102.9|TSTAT
20180101 23:01:49.021|1000|101|98|25|20|87.9|TSTAT
20180101 23:02:09.014|1001|101|98|25|20|89.3|TSTAT
20180101 23:02:10.021|1001|101|98|25|20|89.4|TSTAT
20180101 23:02:11.302|1000|17|15|9|8|7.7|BATT
20180101 23:03:03.008|1000|101|98|25|20|102.7|TSTAT
20180101 23:03:05.009|1000|101|98|25|20|101.2|TSTAT
20180101 23:04:06.017|1001|101|98|25|20|89.9|TSTAT
20180101 23:04:11.531|1000|17|15|9|8|7.9|BATT
20180101 23:05:05.021|1001|101|98|25|20|89.9|TSTAT
20180101 23:05:07.421|1001|17|15|9|8|7.9|BATT
```

### Ouput

```javascript
[
    {
        satelliteId: 1000,
        severity: "RED HIGH",
        component: "TSTAT",
        timestamp: "2018-01-01T23:01:38.001Z"
    },
    {
        satelliteId: 1000,
        severity: "RED LOW",
        component: "BATT",
        timestamp: "2018-01-01T23:01:09.521Z"
    }
]
```
