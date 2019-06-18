# Paging Mission Control

Task Prompt:

> You are tasked with assisting satellite ground operations for an earth science mission that monitors magnetic field variations at the Earth's poles. A pair of satellites fly in tandem orbit such that at least one will have line of sight with a pole to take accurate readings. The satellite’s science instruments are sensitive to changes in temperature and must be monitored closely. Onboard thermostats take several temperature readings every minute to ensure that the precision magnetometers do not overheat. Battery systems voltage levels are also monitored to ensure that power is available to cooling coils. Design a monitoring and alert application that processes status telemetry from the satellites and generates alert messages in cases of certain limit violation scenarios.  Fork this repository, build your program in the language of your choice, then submit a pull request with your code.

## Implementation
This is a command line application written with Python. It is confirmed to be functional with version 3.5 and newer, though I surmise that any instance of Python 3 should be supported.

## Usage
You have the option of running the program from within the main app directory:

```sh
$ python __main__.py <input-file>
```

Alternatively, it can be run from outside as well:

```sh
$ python <app-directory> <input-file>
```

Upon a successful run, the resulting JSON will be printed
to the console.

## Input Format
The program accepts pipe-delimited ASCII text files as input. Each line of telemetry data from ingest is assumed to be of the form:

```
<timestamp>|<satellite-id>|<red-high-limit>|<yellow-high-limit>|<yellow-low-limit>|<red-low-limit>|<raw-value>|<component>
```

## Output Format
The program outputs valid JSON to standard output. The alert messages have the following structure:

```json
{
    "satelliteId": 1234,
    "severity": "severity",
    "component": "component",
    "timestamp": "timestamp"
}
```

Error messages for improper usage (missing arguments, supplying a non-existant file, etc.) are directed to standard error.
