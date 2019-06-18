import collections
import json
import paging_mission_control as pmc


def run(filename):
    monitor = pmc.Monitor([overheated, underpowered])

    with open(filename) as file:
        for line in file:
            monitor(pmc.reading.parse(line))

    return json.dumps([formatted(alert) for alert in monitor.alerts], indent=4)


def overheated(reading):
    return reading.component == "TSTAT" and reading.severity == "RED HIGH"


def underpowered(reading):
    return reading.component == "BATT" and reading.severity == "RED LOW"


def formatted(reading):
    return collections.OrderedDict(
        satelliteId=reading.satellite_id,
        severity=reading.severity,
        component=reading.component,
        timestamp="{:%Y-%m-%dT%H:%M:%S.%f}".format(reading.timestamp)[:-3] + "Z"
    )