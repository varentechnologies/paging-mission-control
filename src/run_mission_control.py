from json import dumps
from sys import argv, exit
from missioncontrol.missioncontrol import parse_telemetry_data


if __name__ == "__main__":
    if len(argv) < 2:
        print("Please enter an input telemetry file")
        exit(1)
    with open(argv[1], "r") as fh:
        alarms = parse_telemetry_data(fh.readlines())
        print(dumps(alarms, sort_keys=True, indent=4))
