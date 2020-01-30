import time
import argparse

class Alert_generation():
	def __init__(self):
		self.alerted = {}
		self.output = '''{
							satelliteId: {0},
							severity: {1},
							component: {2},
							timestamp: {3}
						}'''

	def add_ID(self, ID):
		self.alerted[ID] = {"BATT":[], "TSTAT":[]}
		
	def time_check(self, ID, component, _time):
		t = time.mktime(time.strptime(_time[:-4], "%Y%m%d %H:%M:%S"))
		if len(self.alerted[ID][component]) > 0:
			if t - self.alerted[ID][component][0][0] > 300:
				self.alerted[ID][component].pop(0)
				self.time_check(ID, component, _time)
			else:
				self.alerted[ID][component].append([t, _time])
		else:
			self.alerted[ID][component].append([t, _time])
	
	def check(self, high_limit, low_limit, value, component, ID, _time):
		warn = 0
		if self.alerted.get(ID, None) == None:
			self.add_ID(ID)
		if component == "BATT":
			if value < low_limit:
				warn = 1
		elif component == "TSTAT":
			if value > high_limit:
				warn = 1
		if warn:
			self.time_check(ID, component, _time)
		if len(self.alerted[ID][component]) > 2:
			self.output(ID, component, _time)
			
	def output(self, ID, component, timestamp):
		severity = "RED HIGH"
		if component == "BATT":
			severity = "RED LOW"
		print(self.output.format(ID, severity, component, timestamp)
		self.alerted[ID][component].pop(0)
		
if __name__ == "__main__":
	parser = argparse.ArgumentParser(description='')
	parser.add_argument('integers', metavar='N', type=int, nargs='+',
						help='an integer for the accumulator')
	parser.add_argument('--sum', dest='accumulate', action='store_const',
						const=sum, default=max,
						help='sum the integers (default: find the max)')

	args = parser.parse_args()
