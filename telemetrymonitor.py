#!/usr/bin/python3

import datetime
import json
import argparse
import collections

class Violation:
    def __init__(self, timestamp, component, severity, satid):
        self.satelliteId = int(satid)
        self.severity = severity
        self.component = component
        self.timestamp = timestamp

    def __str__(self):
        return "satelliteId: {}, severity: '{}', component: '{}',"\
               " timestamp: '{}'".format(
                                self.satelliteId,
                                self.severity,
                                self.component,
                                self.timestamp,)

class Monitor:

    def __init__(self, alert_threshold=5, violation_limit=3):
        self.oldest = 0
        self.newest = violation_limit
        self.equipment = {}
        self.alerts = []
        self.alert_threshold = datetime.timedelta(minutes=alert_threshold)
        self.violation_limit = violation_limit

    # add_violation:
    # creates a violation object and appends violations to an array
    # per satid+component. It then passes the uid to check_for_alerts()
    def add_violation(self, timestamp, component, severity, satid):

        # utilized for unique dictionary entries
        uid = satid + component

        # create a violation
        violation = Violation(timestamp, component, severity, satid)

        # create uid violation dict if it doesn't exist .
        violations = self.equipment.get(uid, None)
        if not violations:
            self.equipment[uid] = [violation]
            return

        # If a violation does exist, append it and check for alerts
        self.equipment[uid].append(violation)
        self.check_for_alert(uid)

        return

    # check_for_alert: if there are three violations in the uid, check to see
    # if they meet the requirements to trigger an alert.
    def check_for_alert(self,uid):

        # see if the uid has enough violaltions to be checked for alets
        if len(self.equipment[uid]) == self.violation_limit:

            violations = self.equipment.get(uid)

            # see if delta between oldest and newest is within alert threshold
            time_diff = violations[self.newest - 1].timestamp - \
                                        violations[self.oldest].timestamp

            if time_diff < self.alert_threshold :
                self.alert(violations[self.oldest])

            # drop the oldest violation
            self.equipment[uid] = violations[1:]

        return

    # put timestamp in desired format and append to uid alert array
    def alert(self, violation):
        violation.timestamp = violation.timestamp.isoformat()[:-3]+"Z"
        self.alerts.append(violation.__dict__)
        return

    # parse events and check to see if they are violations
    def process(self, event):

        # create a named tuple to match the json format
        Data = collections.namedtuple("data", "timestamp satelliteId \
                                               red_high yellow_high \
                                               yellow_low red_low \
                                               raw component")

        # ensure the file is an acutal telemtry file
        try:
            event = Data._make(event.strip("\n").split("|"))
        except TypeError as err:
            print("There is something wrong with the format of your file "
                   "please provide a file with events in the format:\n"
                   "\t<timestamp>|<satellite-id>|<red-high-limit>|"
                   "<yellow-high-limit>|<yellow-low-limit>|<red-low-limit>|"
                   "<raw-value>|<component>\nexiting...")
            exit()

        if event.component != "TSTAT" and event.component != "BATT":
            print("Your data is not in the appropriate format, exiting...")
            exit()

        # sanity check that the timestamp is inputed correctly
        try:
            timestamp = datetime.datetime.strptime(event.timestamp,
                                                    "%Y%m%d %H:%M:%S.%f")
        except ValueError as err:
            print(err)
            exit()

        #  condition for TSTAT
        if float(event.raw) > float(event.red_high) and \
                                                     event.component == "TSTAT":
            severity = "RED HIGH"
        # condition for BATT
        elif float(event.raw) < float(event.red_low) and \
                                                      event.component == "BATT":
            severity = "RED LOW"
        else:
            return

        self.add_violation(timestamp,event.component,severity,event.satelliteId)

    # turn alerts to json objs, format satid, and print
    def print_alerts(self, filename):
        if len(self.alerts) != 0:
            x = json.dumps(self.alerts, indent=4)
            print(str(x).replace('"satelliteId"', "satelliteId")\
                    .replace('"severity"', "severity")\
                    .replace('"component"', "component")\
                    .replace('"timestamp"', "timestamp"))
        else:
            print("There appear to be no alerts in {}".format(filename))


def parse():
    parser = argparse.ArgumentParser(usage=
                "telemetrymonitor.py --help", description="python3 "\
                "telemetrymonitor.py [-h] [FILE]")

    parser.add_argument('FILE', help="telemetry files to be processed. "\
                        "Atleast one is required",
                        type=argparse.FileType('r'), nargs='+')

    return parser.parse_args()

if __name__=="__main__":

    args = parse()

    # alert_threshold and violation_limit are set up for future
    # configuration if desired.


    for file in args.FILE:
        monitor = Monitor(alert_threshold=5, violation_limit=3)
    # parse the files passed
        for data in file:
            # print(telemetry_data)
            # with open(telemetry_data) as data:
            if data == "\n":
                continue
            #events = data.readlines()
            monitor.process(data)
            # for event in events:
            #    monitor.process(event)

        monitor.print_alerts(file.name)
        del(monitor)
        file.close()
