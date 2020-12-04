#!/usr/bin/python3

import datetime
import random
import argparse

# function to return the appropriate number of
# days in a month
def num_days(month, year):
    feb = 28
    if is_leapyear(year):
        feb = 29

    months = [31,feb,31,30,31,30,31,31,30,31,30,31]
    return months[month-1]

# utility function to determine if it's a leap year
def is_leapyear(year):
    is_leapyear = False
    if year % 4 == 0:
        if year % 100 == 0:
            if year % 400 == 0:
                is_leapyear = True
        else:
            is_leapyear = True
    return is_leapyear

# generate random date and time
def random_date(start=1970, end=2020):
    random.seed()
    year = random.randrange(start,end)
    month = random.randrange(1,12)
    day = random.randrange(1, num_days(month, year))
    hour = random.randrange(0, 23)
    minute = random.randrange(0,59)
    second = random.randrange(0,59)
    milli = random.randrange(0,999999)

    timestamp = datetime.datetime(
        year, month, day, hour, minute, second, milli, fold=0)

    return(datetime.datetime.strftime(timestamp, "%Y%m%d %H:%M:%S.%f")[:-3])

# generate contiguous list of dates
# needs to be randomized slightly better
# but is pretty good so far
def generate_data(date,num_events):
    curr = datetime.datetime.strptime(date,"%Y%m%d %H:%M:%S.%f")
    dates = []
    runs = num_events
    dates.append(datetime.datetime.strftime(curr,"%Y%m%d %H:%M:%S.%f"))
    while runs > 0:
        random.seed()
        new = curr + datetime.timedelta(minutes=random.randrange(1,3),
                        seconds=random.randrange(0,59),
                        microseconds=random.randrange(0,999))
        dates.append(datetime.datetime.strftime(new,"%Y%m%d %H:%M:%S.%f"))
        curr = new
        runs -= 1

    return dates

def parse():
    parser = argparse.ArgumentParser(usage="generate test telemetry data ",
        description="python3 test_generator.py [FILENAME] [NUM_EVENTS]")
    parser.add_argument('FILENAME', type=argparse.FileType('a'), nargs=1, \
            help="file to create to write generated data")
    parser.add_argument('NUM_EVENTS', type=int, nargs=1,
            help="the number of events you want to create")
    return parser.parse_args()

if __name__=="__main__":

    args = parse()
    num_events = args.NUM_EVENTS[0]
    dates = generate_data(random_date(2018,2019), num_events = num_events)
    components = ["TSTAT", "BATT"]
    #components = ["DOG", "CAT"]

    test_file = args.FILENAME[0]
    for date in dates:
        # random.seed(random.randint(0,1000000))
        random.seed()
        satid = random.randrange(1000,1002)
        # satid = 1000
        component = random.choice(components)
        # print(component)
        if component == "TSTAT":
            r_high = 101
            r_low = 20
            y_high = 98
            y_low = 25
            raw = random.randrange(98, 110)
        else:
            r_high = 17
            r_low = 8
            y_high = 15
            y_low = 9
            raw = random.randrange(1.0, 15.0)

        test_file.write("{timestamp}|{satid}|{RED_HIGH}|{YELL_HIGH}|"
                        "{YELL_LOW}|{RED_LOW}|{RAW}|{COMPONENT}\n".format(
                        timestamp=date[:-3],
                        satid=satid,
                        RED_HIGH=r_high,
                        YELL_HIGH=y_high,
                        YELL_LOW=y_low,
                        RED_LOW=r_low,
                        RAW=raw,
                        COMPONENT=component))



    test_file.close()
