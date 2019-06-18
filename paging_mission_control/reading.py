import datetime


class Reading:
    """A container for parsed and processed status telemetry data.

    Attributes:
        timestamp (datetime): A marker for when the Reading first appeared.
        satellite_id (int): The id of the originating satellite.
        severity (str): The severity of the reading. One of "RED HIGH", "RED LOW", or None.
        component (str): The instrument being measured. One of "TSTAT" or "BATT".
    """

    def __init__(self, timestamp, satellite_id, severity, component):
        self.timestamp = timestamp
        self.satellite_id = satellite_id
        self.severity = severity
        self.component = component


def parse(line, delimeter='|'):
    """Parses an ASCII string into a Reading object.

    Args:
        line (str): The string to be parsed. Formatting is assumed to be correct.
        delimeter (str): A string separating each field (defaults to '|').
    
    Returns:
        A Reading object.
    """

    fns = [
        lambda x: datetime.datetime.strptime(x, "%Y%m%d %H:%M:%S.%f"),
        int,
        int, int, int, int,
        float,
        lambda x: x,
    ]

    line = line.strip().split(delimeter)
    line = [fn(item) for fn, item in zip(fns, line)]
    timestamp, satellite_id, *limits, value, component = line
    return Reading(timestamp, satellite_id, severity(value, *limits), component)


def severity(value, *limits):
    """Identifies the severity of a value according to the given limits.
    
    Args:
        value (float): The satellite component measurement being judged.
        limits (list): Defines the extreme values for a measurement.
    
    Returns:
        Either a string of value "RED HIGH" or "RED LOW", or None.
    """

    red_high, _, _, red_low = limits

    if value > red_high:
        return "RED HIGH"
    if value < red_low:
        return "RED LOW"
    
    return None