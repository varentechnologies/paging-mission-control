from datetime import datetime, timezone

thermostat = "TSTAT"
battery = "BATT"
red_high_severity = "RED HIGH"
red_low_severity = "RED LOW"
input_date_format = "%Y%m%d %H:%M:%S.%f"
alert_interval = 300


class DataRecord:
    def __init__(self, timestamp, satellite_id, red_high_limit,
                 red_low_limit, raw_value, component):
        self.timestamp = timestamp
        self.satellite_id = int(satellite_id)
        self.red_high_limit = float(red_high_limit)
        self.red_low_limit = float(red_low_limit)
        self.raw_value = float(raw_value)
        self.component = component

    def __sub__(self, other):
        return (self.timestamp - other.timestamp).total_seconds()

    def __repr__(self):
        return (self.__dict__.__repr__())


def is_violation(data_record):
    if data_record.component == thermostat:
        return data_record.raw_value > data_record.red_high_limit
    else:
        return data_record.raw_value < data_record.red_low_limit


def create_alert(data_record):
    if data_record.component == thermostat:
        severity = red_high_severity
    else:
        severity = red_low_severity
    alert_timestamp = data_record.timestamp.isoformat()[:-3] + 'Z'
    return {"satelliteId": data_record.satellite_id,
            "severity": severity,
            "component": data_record.component,
            "timestamp": alert_timestamp}


def parse_telemetry_data(data):
    alerts = []
    violations = {}
    for line in data:
        (timestamp, satellite_id, red_high_limit, _, _,
         red_low_limit, raw_value, component) = line.strip().split("|")
        data_record = DataRecord(datetime.strptime(timestamp, input_date_format),
                                 satellite_id, red_high_limit, red_low_limit,
                                 raw_value, component)
        if is_violation(data_record):
            component_violations = violations.setdefault(data_record.component, [])
            violation_len = len(component_violations)
            if violation_len == 0:
                component_violations.append(data_record)
            else:
                time_diff = data_record - component_violations[0]
                if time_diff < alert_interval:
                    if violation_len == 2:
                        alerts.append(create_alert(component_violations[0]))
                        violations[data_record.component] = [data_record]
                    else:
                        component_violations.append(data_record)
                else:
                    violations[data_record.component] = [data_record]
    return alerts