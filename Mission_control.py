import time
import argparse

class Alert_generation():
	def __init__(self):
		self.alerted = {}
		self.count = 0
		self.output = \
'''		satelliteId: {0},
		severity: "{1}",
		component: "{2}",
		timestamp: "{3}"'''

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
			if float(value) < float(low_limit):
				warn = 1
		elif component == "TSTAT":
			if float(value) > float(high_limit):
				warn = 1
		if warn == 1:
			self.time_check(ID, component, _time)
		if len(self.alerted[ID][component]) > 2:
			self.printer(ID, component)
			
	def printer(self, ID, component):
		if self.count == 0:
			print("[")
			self.count = 1
		else:
			print("	},")
		timestamp = self.alerted[ID][component][0][1]
		ts_end = timestamp[-4:]
		timestamp = time.strptime(timestamp[:-4], "%Y%m%d %H:%M:%S")
		timestamp = time.strftime("%Y-%m-%dT%H:%M:%S"+ts_end+"Z", timestamp)
		self.alerted[ID][component] = []
		severity = "RED HIGH"
		if component == "BATT":
			severity = "RED LOW"
		print("	{")
		print(self.output.format(ID, severity, component, timestamp))
		
	def end(self):
		print("	}")
		print("]")
		
if __name__ == "__main__":
	parser = argparse.ArgumentParser(description='')
	parser.add_argument('input_file', metavar='F', type=str, 
						help='')
	args = parser.parse_args()
	alert_generator = Alert_generation()
	with open(args.input_file, "r") as in_file:
		while True:
			line = in_file.readline().strip("\n")
			if line:
				_time, ID, h_limit, __, l_limit, __, value, component = line.split("|")
				alert_generator.check(h_limit, l_limit, value, component, ID, _time)
			else:
				break
		alert_generator.end()
		
