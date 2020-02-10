"""
mission_control.py

module docstring for mission_control
"""

import time
import argparse


class AlertGeneration():
    """
    class AlertGeneration()

    AlertGeneration is a class for digesting satellite information output
    and outputing formatted JSON data according to requirred paramaters
    """
    def __init__(self):
        self.alerted = {}
        self.count = 0
        self.output = '''
        satelliteId: {0},
        severity: "{1}",
        component: "{2}",
        timestamp: "{3}"'''

    def add_id(self, id_to_add):
        """
        add_id(self, str id_to_add)
        add_id takes in the string ID of a provided satellite and creates
        the dictionary entry within self.alerted, which contains a
        dictionary with 2 empty lists

        If the dictionary entry ID exsists already, this function will
        return a 1, on success it will return a 0
        """
        if self.alerted.get(id_to_add, None) is None:
            self.alerted[id_to_add] = {"BATT": [], "TSTAT": []}
            return 0
        return 1

    def time_check(self, check_id, component, _time):
        """
        time_check(self, str check_id, str, component, str _time)

        time_check checks to see if the current alert time is within 5 minutes
        of alerts that have already been registered, in the event that it is
        outside of the time window, it will pop off the oldest element of its
        list and recursively call again, if it is within the time window, the
        alert will be added to its list, in the event of its list being empty
        the alert will be added to its list
        """
        time_value = time.mktime(time.strptime(_time[:-4], "%Y%m%d %H:%M:%S"))
        if len(self.alerted[check_id][component]) > 0:
            if time_value - self.alerted[check_id][component][0][0] > 300:
                self.alerted[check_id][component].pop(0)
                self.time_check(check_id, component, _time)
            else:
                self.alerted[check_id][component].append([time_value, _time])
        else:
            self.alerted[check_id][component].append([time_value, _time])

    def check(self, high_limit, low_limit, value, component, work_id, _time):
        """
        check(self, str high_limit, str low_limit,
              str value, str component, str work_id, str _time)

        check takes in the limits, value, component, id, and timestamp, if the
        id is not in the dictionary self.alerted self.add_id will be called to
        populate the dictionary, based on the component, the value will be
        checked according to the requirred paramaters, in the event that a
        warning has been flagged, self.time_check will check the alerts and
        handle according to its results, finally it will check to see if the
        alert dictionary for the component has over 2 entries, in the event it
        does, self.printer will be called to output the formatted data

        """
        warn = 0
        if self.alerted.get(work_id, None) is None:
            self.add_id(work_id)
        if component == "BATT":
            if float(value) < float(low_limit):
                warn = 1
        elif component == "TSTAT":
            if float(value) > float(high_limit):
                warn = 1
        if warn == 1:
            self.time_check(work_id, component, _time)
        if len(self.alerted[work_id][component]) > 2:
            self.printer(work_id, component)

    def printer(self, id_to_print, component):
        """
        printer(self, str id_to_print, str component)

        printer will pop the oldest element of the alert list, and output the
        data in a JSON format according to the requirred specifications, as
        there was no specified way to handle the remaining data, it is left
        in place
        """
        if self.count == 0:
            print("[")
            self.count = 1
        else:
            print("    },")
        timestamp = self.alerted[id_to_print][component].pop(0)[1]
        ts_end = timestamp[-4:]
        timestamp = time.strptime(timestamp[:-4], "%Y%m%d %H:%M:%S")
        timestamp = time.strftime("%Y-%m-%dT%H:%M:%S"+ts_end+"Z", timestamp)
        severity = "RED HIGH"
        if component == "BATT":
            severity = "RED LOW"
        print("    {")
        print(self.output.format(ID, severity, component, timestamp))


if __name__ == "__main__":
    PARSER = argparse.ArgumentParser(description='''
    mission_control takes a correctly formatted input file and outputs a JSON
    style alerts
    ''')
    PARSER.add_argument('input_file', type=str, help='file to parse')
    ARGS = PARSER.parse_args()
    ALERT_GENERATOR = AlertGeneration()
    with open(ARGS.input_file, "r") as in_file:
        while True:
            LINE = in_file.readline().strip("\n")
            if LINE:
                TIME, ID, HIGH, ___, LOW, __, VALUE, COMP = LINE.split("|")
                ALERT_GENERATOR.check(HIGH, LOW, VALUE, COMP, ID, TIME)
            else:
                print("    }")
                print("]")
                break
