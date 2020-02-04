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
        self.output = \
'''     satelliteId: {0},
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
            self.alerted[id_to_add] = {"BATT":[], "TSTAT":[]}
            return 0
        return 1

    def time_check(self, id_to_check, component, _time):
        """
        time_check(self, str id_to_check, str, component, str _time)

        """
        time_value = time.mktime(time.strptime(_time[:-4], "%Y%m%d %H:%M:%S"))
        if len(self.alerted[id_to_check][component]) > 0:
            if time_value - self.alerted[id_to_check][component][0][0] > 300:
                self.alerted[id_to_check][component].pop(0)
                self.time_check(id_to_check, component, _time)
            else:
                self.alerted[id_to_check][component].append([time_value, _time])
        else:
            self.alerted[id_to_check][component].append([time_value, _time])

    def check(self, high_limit, low_limit, value, component, id_to_work, _time):
        """
        check(self, str high_limit, str low_limit,
              str value, str component, str ID, str _time)

        """
        warn = 0
        if self.alerted.get(id_to_work, None) is None:
            self.add_id(id_to_work)
        if component == "BATT":
            if float(value) < float(low_limit):
                warn = 1
        elif component == "TSTAT":
            if float(value) > float(high_limit):
                warn = 1
        if warn == 1:
            self.time_check(id_to_work, component, _time)
        if len(self.alerted[id_to_work][component]) > 2:
            self.printer(id_to_work, component)

    def printer(self, id_to_print, component):
        """
        printer(self, str id_to_print, str component)
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

    def end(self):
        """
        end(self)
        """
        print("    }")
        print("]")

if __name__ == "__main__":
    PARSER = argparse.ArgumentParser(description='')
    PARSER.add_argument('input_file', metavar='F', type=str,
                        help='')
    ARGS = PARSER.parse_args()
    ALERT_GENERATOR = AlertGeneration()
    with open(ARGS.input_file, "r") as in_file:
        while True:
            LINE = in_file.readline().strip("\n")
            if LINE:
                TIME, ID, H_LIMIT, ___, L_LIMIT, __, VALUE, COMPOENT = LINE.split("|")
                ALERT_GENERATOR.check(H_LIMIT, L_LIMIT, VALUE, COMPOENT, ID, TIME)
            else:
                break
        ALERT_GENERATOR.end()
