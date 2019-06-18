import collections
import datetime

class Monitor:
    """Each reading that flows through an instance of this class is
    tested against a series of functions to see if a given satellite
    component is in a critical state.

    This class maintains an invariant for all of its queues: there
    should be less than <limit> unreported critical readings within a
    <delta.minute> minute interval that we track at any given time.
    When a queue reaches <limit>, an alert is reported.

    Attributes:
        alerts (list): A list of reported readings.
        checks (list): A list of predicate functions to be evaluated for each Reading.
        queues (dict): A dict of lists of readings, each indexed a by
            satellite id and component.
        limit (int): The number of readings to hold before reporting a
            violation (defaults to 3).
        delta (datetime): The maximum duration considered when recording
            a reading (defaults to 5 minutes). 
    """

    def __init__(self, checks, limit=3, delta=datetime.timedelta(minutes=5)):
        self.alerts = []
        self.checks = checks
        self.queues = collections.defaultdict(list)
        self.limit = limit
        self.delta = delta
    
    def __call__(self, reading):
        queue = self.queues[(reading.satellite_id, reading.component)]

        if any(fn(reading) for fn in self.checks):
            self.record(reading, queue)
            if self.is_violation(queue):
                self.report(queue)
    
    def is_violation(self, queue):
        return len(queue) == self.limit

    def record(self, reading, queue):
        while queue and reading.timestamp - queue[0].timestamp > self.delta:
            queue.pop(0)
        queue.append(reading)

    def report(self, queue):
        self.alerts.append(queue.pop(0))