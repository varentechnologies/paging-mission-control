# run_mission_control.py

> Given an input file containing ingest status telemetry data, creates alert messages if the following occurs
- If for the same satellite there are three battery voltage readings that are under the red low limit within a five minute interval.
- If for the same satellite there are three thermostat readings that exceed the red high limit within a five minute interval.

## Usage
```
python3 run_mission_control.py [input_file]

OR

python run_mission_control.py [input_file]
```

## Dependencies
Python 3.7.3