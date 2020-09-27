# Satellite Component Condition Monitoring

## Running
This has the following system dependencies:
* Java 11

To run the application simply compile it and execute it as a simple Java jar while specifying the filename.
* compile it: `mvn clean compile`
* run it `java -jar target/paging-mission-control-0.0.1-SNAPSHOT.jar --filename=input.txt`

## Example Output
See `example_output.txt` for a snippet of the example output of using this program.

## Thinking Out Loud: Development Notes
Tried to combine some elements of forward-thinking where significant development time was not added, and added notes in the __looking forward__ section on some things we can consider beyond a rapid application prototype.

__Assumptions & Requirements__

A quick search suggests there are under 3000 total satellites in the air.
* Satellite IDs fit within an integer.
* Assume satellite telemetry data value precision is appropriate for a float.

Requirements specify checks 'within a five minute interval' which could be interpreted multiple ways:
1. intervals of an hour [0,5], [5-10], [10-15], ..., [55-60]
1. any given minute, and 5 minutes after that [0-5], [1-6], [2-7], ..., [55-60], [56-1], [57-2] ...
1. any possible 5 minute interval with millisecond precision, starting with the first message.

Let's assume the most precise with #3.

We take this to mean if some alert with a 5 minute threshold interval is triggered at 9:00:00.000 AM, then the next possible alert cannot be triggered until 9:05:00.000 AM.

__Data: Batches vs Stream__

__Looking Forward__

As requirements get more complex, bringing in some streaming mechanism like Kafka would be appropriate.


The example data is a simple 5 minute data batch but under the hood we want to support streams. Directing our design towards this solution is not necessarily more work now. We will support loading a file, but the design will lend itself towards plug-and-play streams in the future.


Satellites in real operation probably have many conditions which need to be monitored. As our monitoring needs grow we can horizontally scale this application by partitioning by satellite.
* We would find as the number of conditions grow we may want to create some sort of service provider interface for conditions to improve maintenance and testability.
* Individual or groups of satellites could be configured to monitor specific conditions.

__Tests__

A small suite of unit tests and integration tests are included in `src/main/test`.
