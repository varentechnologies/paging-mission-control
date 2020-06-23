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

## Comments

The key element of this task was filtering the telemetry data by Satellite and Component. A hashmap is perfect for filtering by satellite, as we can simply use the satteliteID as our key and get O(1) insertion/retrieval. 

We only care about the alerts if there have been 3 in the last 5 minutes, so we only ever need to keep track of the last 3 alerts for any given component/satellite pair. Since we will  be constantly adding/removing alerts, a Linked List is the obvious choice, since it trivializes this add/remove process and gives us O(1) time. The time complexity is not a major concern here, however, since we're only ever keeping the latest 3 alerts.

### Complexity

#### Time

We will have to iterate over the entire list of telemetry data, but we only have to make a single pass, so our base complexity is at least O(n). Since our HashMap lookups/inserts are O(1) and our LinkedList add/removes are also O(1), our overall time complexity is O(n + 1 + 1), or O(n).   

#### Space

Our worst-case space complexity would be if the entire list contains all unique satellite/component pairs, so O(n). Our best case space complexity would occur if there is only a single satellite/component pair. This would only use 3 elements in the LinkedList and 1 element in the HashMap. 

### Concerns

The limiting scalability factor for this implementation is the handling of the various components. At first I considered making TelemetryData an abstract class and having the specific component data classes extend the TelemetryData class. Since there were only two components for this challenge, this seemed unnecessary. In a real-world scenario, this would very likely need to be handled differently. 

This concern also applies to the HashMaps in the TelemetryAlertDetector class. Since there are a finite and known number of satellite components, it is easy to use explicitly declared HashMaps for each component type. If this was required to scale, then the HashMap instantiation/handling would need to be changed accordingly.  