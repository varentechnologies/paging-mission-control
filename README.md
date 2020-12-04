# telemetrymonitor.py

> telemetrymonitor.py ingests ASCII files with pipe delimited records, processes the records, and outputs json in the requested format. The program is written in python3
and implements an object oriented approach to process the records. The program can accept multiple files, and will only parse files with records in the appropriate
format below. The program was written to meet the requirements of the exercise. Additionally, the program was implemented with flexibility in mind. Data format's
can and do often change. As a result, with very little modification, one would be able to modify the number of violations required to trigger alerts, as well as, the desired elapsed time between alerts. It would also be easy to change the data record delimiter, and order of the records. Implementing this functionality is outside
the scope of this project, however, it is important to keep such things in mind in real world scenarios.

# test_generator.py

> test_generator.py creates random telemetry records, and writes them to a file. The number of records and the file to write to, are required command line parameters.
The time range is configurable from within the program.  While this program is also written in python3, it follows a more procedural programming approach.  This program was essential for testing telemetrymonitor.py. test_generator.py has not been stress tested, and was only written for testing.

## Usage
```
python3 test_generator.py [FILENAME] [NUM_OF_RECORDS]
python3 telemetrymonitor.py [FILE] [optional_FILE_1] [optional_FILE_2]
```

### Input Format
```
<timestamp>|<satellite-id>|<red-high-limit>|<yellow-high-limit>|<yellow-low-limit>|<red-low-limit>|<raw-value>|<component>
```
### Output Format

```javascript
{
    satelliteId: 1234,
    severity: "severity",
    component: "component",
    timestamp: "timestamp"
}
```
