/*
 ============================================================================
 Project Name : Parikh_Meet_Varen_Technologies.c
 Author       : Meet Parikh
 Version      :
 Copyright    : Your copyright notice
 Description  : Paging mission control main.h
 ============================================================================
 */

/* Complier Includes */
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <inttypes.h>
#include <time.h>

/* Local Includes */
#include "main.h"

/* Constant Variables */
const char MSG_ERROR_FOPEN[] = "Cannot Open File.";
const char COMPONENT_BATTERY[] = "BATT";
const char COMPONENT_TSTAT[] = "TSTAT";
const char SEVERITY_LOW[] = "RED LOW";
const char SEVERITY_HIGH[] = "RED HIGH";

const uint16_t sat_id_1 = 1000;
const uint16_t sat_id_2 = 1001;

/* Error Variable */
error_t error;

/* BATT Frame Buffer, initialized to Zero */
batt_frame_t batt_frames[BATT_FRAME_MAXBUFFER] = {0};
/* BATT Frame count */
uint8_t batt_frame_count = 0;
/* TSTAT Frame Buffer */
tstat_frame_t tstat_frames[TSTAT_FRAME_MAXBUFFER] = {0};
/* TSTAT Frame count */
uint8_t tstat_frame_count;
/* Output Frame Buffer */
output_frame_t output_frames[OUTPUT_FRAME_MAXBUFFER] = {0};
/* Output Frame count */
uint8_t output_frame_count;

/* Modified TimeStamp */
char ModTimeStamp[MOD_TIMESTAMP];

/* Three Voltage Readings */
float previousVoltageReadings[3];

/*
 * Brief:
 * Error Handling
 *
 * Parameter:
 * Error
 *
 * Returns:
 * Void
 */
static void
Error(uint8_t error)
{
    switch(error)
    {
    case ERROR_FOPEN:
        printf(" %s \n\r", MSG_ERROR_FOPEN);
        break;
    case ERROR_INVALIDINPUT:
    	printf("Invalid Input \n\r");
    	break;
    default:
        break;
    }

    while(TRUE)
    {

    }

}

/*
 * Brief:
 * Converts TimeStamp to a Epoch Time Object
 *
 * Parameters:
 * TimeValue - A pointer to an char array that stores the TimeStamp
 * retunTimeValue - A pointer to a time_t object
 *
 * Returns: Void
 *
 */
static void
ConvertStringToTime(char *TimeValue, time_t *returnTimeValue)
{
	struct tm breakdown = {0};
	int year = 0, month = 0, day = 0, hour = 0, min = 0, seconds = 0, millis = 0;

	sscanf(TimeValue,"%4d%2d%2d %2d:%2d:%2d.%3d", &year, &month, &day, &hour, &min, &seconds, &millis);

	breakdown.tm_year = year - 1900;
	breakdown.tm_mon = month;
	breakdown.tm_mday = day;
	breakdown.tm_hour = hour;
	breakdown.tm_min = min;
	breakdown.tm_sec = seconds;
	*returnTimeValue = mktime(&breakdown);
}

/*
 * Brief:
 * Converts Input Timestamp to a proper output format
 *
 * Parameters:
 * TimeValue- A pointer to an char array that stores the TimeStamp
 * ReturnTimeValue- A pointer to an char array that stores the modified TimeStamp
 *
 * Returns: Void
 */
static void
TimeStamp(char *TimeValue, char *ReturnTimeValue)
{

	char buffer[MOD_TIMESTAMP];
	int year = 0, month = 0, day = 0, hour = 0, min = 0, seconds = 0, millis = 0;

	sscanf(TimeValue,"%4d%2d%2d %2d:%2d:%2d.%3d", &year, &month, &day, &hour, &min, &seconds, &millis);
	snprintf(buffer,sizeof(buffer), "%04d-%02d-%02dT%02d:%02d:%02d.%03dZ", year, month, day, hour, min, seconds, millis);

	memcpy(ReturnTimeValue, buffer, sizeof(buffer));
}

/*
 * Brief:
 * Checks the Thermostat Frames for faults
 *
 * Parameters: Void
 *
 * Returns: Void
 */
static void
isThermostatfaulty(void)
{
	/* TSTAT frames for Satellite ID: 1000 */
	tstat_frame_t Satellite_ID_1[batt_frame_count];
	/* TSAT SAT_ID_1000 frame count */
	uint8_t sat_id_1_count = 0;
	/* TSTAT frames for Satellite ID: 1001 */
	tstat_frame_t Satellite_ID_2[batt_frame_count];
	/* TSAT SAT_ID_1001 frame count */
	uint8_t sat_id_2_count = 0;
	/* Index Variable */
	uint8_t index = 0;
	/* Counter variable */
	uint8_t counter;
	/* Time Variables */
	time_t time_1, time_2, time_3;
	/* Time Difference Variable */
	double diff_t1 = 0;


	/* Sort the TSTAT Frames based on Satellite ID */
	for (index = 0; index <= tstat_frame_count; index++)
	{
		if(tstat_frames[index].sat_id == sat_id_1)
		{
        	strcpy(Satellite_ID_1[sat_id_1_count].timestamp, tstat_frames[index].timestamp);
            Satellite_ID_1[sat_id_1_count].sat_id = sat_id_1;
            Satellite_ID_1[sat_id_1_count].red_high = tstat_frames[index].red_high;
            Satellite_ID_1[sat_id_1_count].yellow_high = tstat_frames[index].yellow_high;
            Satellite_ID_1[sat_id_1_count].yellow_low = tstat_frames[index].yellow_low;
            Satellite_ID_1[sat_id_1_count].red_low = tstat_frames[index].red_low;
            Satellite_ID_1[sat_id_1_count].raw = tstat_frames[index].raw;
            Satellite_ID_1[sat_id_1_count].component = COMPONENT_THERMOSTAT;
            sat_id_1_count++;
		}
		else if (tstat_frames[index].sat_id == sat_id_2)
		{
        	strcpy(Satellite_ID_2[sat_id_2_count].timestamp, tstat_frames[index].timestamp);
            Satellite_ID_2[sat_id_2_count].sat_id = sat_id_2;
            Satellite_ID_2[sat_id_2_count].red_high = tstat_frames[index].red_high;
            Satellite_ID_2[sat_id_2_count].yellow_high = tstat_frames[index].yellow_high;
            Satellite_ID_2[sat_id_2_count].yellow_low = tstat_frames[index].yellow_low;
            Satellite_ID_2[sat_id_2_count].red_low = tstat_frames[index].red_low;
            Satellite_ID_2[sat_id_2_count].raw = tstat_frames[index].raw;
            Satellite_ID_2[sat_id_2_count].component = COMPONENT_THERMOSTAT;
            sat_id_2_count++;
		}
	}

	/* Check for Error Frames from Satellite 1 */
	for (index = 0; index < sat_id_1_count; index++)
	{
		counter = index;
		if(index > 2 && index <= sat_id_1_count)
		{
			ConvertStringToTime(Satellite_ID_1[counter - 2].timestamp, &time_1);
			ConvertStringToTime(Satellite_ID_1[counter - 1].timestamp, &time_2);
			ConvertStringToTime(Satellite_ID_1[counter].timestamp, &time_3);

			diff_t1 = difftime(time_3, time_1);
			if (diff_t1 < MAX_MIN_SECONDS)
			{
				previousVoltageReadings[0] = Satellite_ID_1[counter - 2].raw;
				previousVoltageReadings[1] = Satellite_ID_1[counter - 1].raw;
				previousVoltageReadings[2] = Satellite_ID_1[counter].raw;

				if (previousVoltageReadings[0] >= Satellite_ID_1[counter].red_high
						&& previousVoltageReadings[1] >= Satellite_ID_1[counter].red_high
						&& previousVoltageReadings[2] >= Satellite_ID_1[counter].red_high)
				{
					TimeStamp(Satellite_ID_1[counter].timestamp, ModTimeStamp);
					/*	If So add record to output frame */
					output_frames[output_frame_count].satellite_id = sat_id_1;
					strcpy(output_frames[output_frame_count].severity_level, SEVERITY_HIGH);
					strcpy(output_frames[output_frame_count].component, COMPONENT_TSTAT);
					strcpy(output_frames[output_frame_count].timestamp, ModTimeStamp);
					output_frame_count++;
				}
			}
		}
		else
		{
			/* Checks for Sample Count Less Than or Equal to 2 */
			if (Satellite_ID_1[index].raw > Satellite_ID_1[index].red_high)
			{
				/*	If So add record to output frame */
				TimeStamp(Satellite_ID_1[index].timestamp, ModTimeStamp);
				output_frames[output_frame_count].satellite_id = sat_id_1;
				strcpy(output_frames[output_frame_count].severity_level, SEVERITY_HIGH);
				strcpy(output_frames[output_frame_count].component, COMPONENT_TSTAT);
				strcpy(output_frames[output_frame_count].timestamp, ModTimeStamp);
				output_frame_count++;
			}
		}
	}

	/* Check for Error Frames from Satellite 2 */
	for (index = 0; index < sat_id_2_count; index++)
	{
		counter = index;

		/* Checks for Sample Count Greater Than 2 */
		if(index > 2 && index <= sat_id_2_count)
		{
			/* Convert timestamps to time object */
			ConvertStringToTime(Satellite_ID_2[counter - 2].timestamp, &time_1);
			ConvertStringToTime(Satellite_ID_2[counter - 1].timestamp, &time_2);
			ConvertStringToTime(Satellite_ID_2[counter].timestamp, &time_3);

			/* Get the difference between time 1 & time 3 objects */
			diff_t1 = difftime(time_3, time_1);

			/* If the difference is < 5 minutes */
			if (diff_t1 < MAX_MIN_SECONDS)
			{
				/* Get the Raw Voltage */
				previousVoltageReadings[0] = Satellite_ID_2[counter - 2].raw;
				previousVoltageReadings[1] = Satellite_ID_2[counter - 1].raw;
				previousVoltageReadings[2] = Satellite_ID_2[counter].raw;

				/* If all three samples are in critical range, send the frame to output */
				if (previousVoltageReadings[0] > Satellite_ID_2[counter].red_high
						&& previousVoltageReadings[1] > Satellite_ID_2[counter].red_high
						&& previousVoltageReadings[2] > Satellite_ID_2[counter].red_high)
				{
					/*	If So add record to output frame */
					TimeStamp(Satellite_ID_2[counter].timestamp, ModTimeStamp);
					output_frames[output_frame_count].satellite_id = sat_id_2;
					strcpy(output_frames[output_frame_count].severity_level, SEVERITY_HIGH);
					strcpy(output_frames[output_frame_count].component, COMPONENT_TSTAT);
					strcpy(output_frames[output_frame_count].timestamp, ModTimeStamp);
					output_frame_count++;
				}
			}
		}
		else
		{
			/* Checks for Sample Count Less Than or Equal to 2 */
			if (Satellite_ID_2[index].raw > Satellite_ID_2[index].red_high)
			{
				/*	If So add record to output frame */
				TimeStamp(Satellite_ID_2[index].timestamp, ModTimeStamp);
				output_frames[output_frame_count].satellite_id = sat_id_2;
				strcpy(output_frames[output_frame_count].severity_level, SEVERITY_HIGH);
				strcpy(output_frames[output_frame_count].component, COMPONENT_TSTAT);
				strcpy(output_frames[output_frame_count].timestamp, ModTimeStamp);
				output_frame_count++;
			}
		}

	}
}

/*
 * Brief:
 * Checkes Battery Frames for Faults
 *
 * Parameters: None
 *
 * Returns: Void
 */
static void
isBatteryfaulty(void)
{
	/* BATT frames for Satellite ID: 1000 */
	batt_frame_t Satellite_ID_1[batt_frame_count];
	/* BATT SAT_ID_1000 frame count */
	uint8_t sat_id_1_count = 0;
	/* BATT frames for Satellite ID: 1001 */
	batt_frame_t Satellite_ID_2[batt_frame_count];
	/* BATT SAT_ID_1001 frame count */
	uint8_t sat_id_2_count = 0;
	/* Index Variable */
	uint8_t index = 0;
	/* Counter variable */
	uint8_t counter;
	/* Time Variables */
	time_t time_1, time_2, time_3;
	/* Time Difference Variable */
	double diff_t1 = 0;


	/* Sort the Battery Frames based on Satellite ID */
	for (index = 0; index <= batt_frame_count; index++)
	{
		if(batt_frames[index].sat_id == sat_id_1)
		{
        	strcpy(Satellite_ID_1[sat_id_1_count].timestamp, batt_frames[index].timestamp);
            Satellite_ID_1[sat_id_1_count].sat_id = sat_id_1;
            Satellite_ID_1[sat_id_1_count].red_high = batt_frames[index].red_high;
            Satellite_ID_1[sat_id_1_count].yellow_high = batt_frames[index].yellow_high;
            Satellite_ID_1[sat_id_1_count].yellow_low = batt_frames[index].yellow_low;
            Satellite_ID_1[sat_id_1_count].red_low = batt_frames[index].red_low;
            Satellite_ID_1[sat_id_1_count].raw = batt_frames[index].raw;
            Satellite_ID_1[sat_id_1_count].component = COMPONENT_BATT;
            sat_id_1_count++;
		}
		else if (batt_frames[index].sat_id == sat_id_2)
		{
        	strcpy(Satellite_ID_2[sat_id_2_count].timestamp, batt_frames[index].timestamp);
            Satellite_ID_2[sat_id_2_count].sat_id = sat_id_2;
            Satellite_ID_2[sat_id_2_count].red_high = batt_frames[index].red_high;
            Satellite_ID_2[sat_id_2_count].yellow_high = batt_frames[index].yellow_high;
            Satellite_ID_2[sat_id_2_count].yellow_low = batt_frames[index].yellow_low;
            Satellite_ID_2[sat_id_2_count].red_low = batt_frames[index].red_low;
            Satellite_ID_2[sat_id_2_count].raw = batt_frames[index].raw;
            Satellite_ID_2[sat_id_2_count].component = COMPONENT_BATT;
            sat_id_2_count++;
		}
	}

	/* Check for Error Frames from Satellite 1 */
	for (index = 0; index < sat_id_1_count; index++)
	{
		counter = index;

		/* Checks for Sample Count Greater Than 2 */
		if(index > 2 && index <= sat_id_1_count)
		{
			/* Convert timestamps to time object */
			ConvertStringToTime(Satellite_ID_1[counter - 2].timestamp, &time_1);
			ConvertStringToTime(Satellite_ID_1[counter - 1].timestamp, &time_2);
			ConvertStringToTime(Satellite_ID_1[counter].timestamp, &time_3);

			/* Get the difference between time 1 & time 3 objects */
			diff_t1 = difftime(time_3, time_1);

			/* If the difference is < 5 minutes */
			if (diff_t1 < MAX_MIN_SECONDS)
			{
				/* Get the Raw Voltage */
				previousVoltageReadings[0] = Satellite_ID_1[counter - 2].raw;
				previousVoltageReadings[1] = Satellite_ID_1[counter - 1].raw;
				previousVoltageReadings[2] = Satellite_ID_1[counter].raw;

				/* If all three samples are in critical range, send the frame to output */
				if (previousVoltageReadings[0] < Satellite_ID_1[counter].red_low
						&& previousVoltageReadings[1] < Satellite_ID_1[counter].red_low
						&& previousVoltageReadings[2] < Satellite_ID_1[counter].red_low)
				{
					/*	If So add record to output frame */
					TimeStamp(Satellite_ID_1[counter].timestamp, ModTimeStamp);
					output_frames[output_frame_count].satellite_id = sat_id_1;
					strcpy(output_frames[output_frame_count].severity_level, SEVERITY_LOW);
					strcpy(output_frames[output_frame_count].component, COMPONENT_BATTERY);
					strcpy(output_frames[output_frame_count].timestamp, ModTimeStamp);
					output_frame_count++;
				}
			}
		}
		else
		{
			/* Checks for Sample Count Less Than or Equal to 2 */
			if (Satellite_ID_1[index].raw < Satellite_ID_1[index].red_low)
			{
				/*	If So add record to output frame */
				TimeStamp(Satellite_ID_1[index].timestamp, ModTimeStamp);
				output_frames[output_frame_count].satellite_id = sat_id_1;
				strcpy(output_frames[output_frame_count].severity_level, SEVERITY_LOW);
				strcpy(output_frames[output_frame_count].component, COMPONENT_BATTERY);
				strcpy(output_frames[output_frame_count].timestamp, ModTimeStamp);
				output_frame_count++;
			}
		}
	}

	/* Check for Error Frames from Satellite 2 */
	for (index = 0; index < sat_id_2_count; index++)
	{
		counter = index;

		/* Checks for Sample Count Greater Than 2 */
		if(index > 2 && index <= sat_id_2_count)
		{
			/* Convert timestamps to time object */
			ConvertStringToTime(Satellite_ID_2[counter - 2].timestamp, &time_1);
			ConvertStringToTime(Satellite_ID_2[counter - 1].timestamp, &time_2);
			ConvertStringToTime(Satellite_ID_2[counter].timestamp, &time_3);

			/* Get the difference between time 1 & time 3 objects */
			diff_t1 = difftime(time_3, time_1);

			/* If the difference is < 5 minutes */
			if (diff_t1 < MAX_MIN_SECONDS)
			{
				previousVoltageReadings[0] = Satellite_ID_2[counter - 2].raw;
				previousVoltageReadings[1] = Satellite_ID_2[counter - 1].raw;
				previousVoltageReadings[2] = Satellite_ID_2[counter].raw;

				/* If all three samples are in critical range, send the frame to output */
				if (previousVoltageReadings[0] < Satellite_ID_2[counter].red_low
						&& previousVoltageReadings[1] < Satellite_ID_2[counter].red_low
						&& previousVoltageReadings[2] < Satellite_ID_2[counter].red_low)
				{
					/*	If So add record to output frame */
					TimeStamp(Satellite_ID_2[counter].timestamp, ModTimeStamp);
					output_frames[output_frame_count].satellite_id = sat_id_2;
					strcpy(output_frames[output_frame_count].severity_level, SEVERITY_LOW);
					strcpy(output_frames[output_frame_count].component, COMPONENT_BATTERY);
					strcpy(output_frames[output_frame_count].timestamp, ModTimeStamp);
					output_frame_count++;
				}
			}
		}
		else
		{
			/* Checks for Sample Count Less Than or Equal to 2 */
			if (Satellite_ID_2[index].raw < Satellite_ID_2[index].red_low)
			{
				/*	If So add record to output frame */
				TimeStamp(Satellite_ID_2[index].timestamp, ModTimeStamp);
				output_frames[output_frame_count].satellite_id = sat_id_2;
				strcpy(output_frames[output_frame_count].severity_level, SEVERITY_LOW);
				strcpy(output_frames[output_frame_count].component, COMPONENT_BATTERY);
				strcpy(output_frames[output_frame_count].timestamp, ModTimeStamp);
				output_frame_count++;
			}
		}
	}
}

/*
 * Brief:
 *
 * Parameters:
 * TimeValue
 * ReturnTimeValue
 *
 * Returns: Void
 */
error_t
GetDataFromFile(FILE **input, char* delimiter)
{
	char singleInput;
	char TimeStamp[INGEST_TIMESTAMP_MAX_SIZE];
	float SatId;
	float RedHigh;
	float YellowHigh;
	float YellowLow;
	float RedLow;
	float Raw;
	char  component[INGEST_COMPONENT_MAX_SIZE];

    while((singleInput = getc(*input)) != EOF)
    {
    	if(singleInput != '\n')
    	{
    		ungetc(singleInput, *input);
    	}

    	fscanf(*input, "%[^|]|%f|%f|%f|%f|%f|%f|%s", TimeStamp, &SatId, &RedHigh,&YellowHigh, &YellowLow, &RedLow,&Raw,component);

        if(strncmp(COMPONENT_BATTERY, component, sizeof(COMPONENT_BATTERY)) == 0)
        {
        	strcpy(batt_frames[batt_frame_count].timestamp, TimeStamp);
            batt_frames[batt_frame_count].sat_id = SatId;
            batt_frames[batt_frame_count].red_high = RedHigh;
            batt_frames[batt_frame_count].yellow_high = YellowHigh;
            batt_frames[batt_frame_count].yellow_low = YellowLow;
            batt_frames[batt_frame_count].red_low = RedLow;
            batt_frames[batt_frame_count].raw = Raw;
            batt_frames[batt_frame_count].component = COMPONENT_BATT;
            batt_frame_count++;
        }

        else if (strncmp(COMPONENT_TSTAT, component, sizeof(COMPONENT_TSTAT)) == 0)
        {
        	strcpy(tstat_frames[tstat_frame_count].timestamp, TimeStamp);
            tstat_frames[tstat_frame_count].sat_id = SatId;
            tstat_frames[tstat_frame_count].red_high = RedHigh;
            tstat_frames[tstat_frame_count].yellow_high = YellowHigh;
            tstat_frames[tstat_frame_count].yellow_low = YellowLow;
            tstat_frames[tstat_frame_count].red_low = RedLow;
            tstat_frames[tstat_frame_count].raw = Raw;
            tstat_frames[tstat_frame_count].component = COMPONENT_THERMOSTAT;
            tstat_frame_count++;

        }
        else
            return ERROR_INVALIDINPUT;
    }
	 return ERROR_NONE;
}

/*
 * Brief:
 *
 * Parameters:
 * TimeValue
 * ReturnTimeValue
 *
 * Returns: Void
 */
int
main()
{
    /* Initialize Variables */
    FILE *input_file;
    uint8_t count = 0;

    /* Open the Input File */
    input_file = fopen("src/Sample_Data_Input.txt", "rt");
    if(input_file == NULL)
    {
    	Error(ERROR_FOPEN);
    }

    /* Store Input Data */
    error = GetDataFromFile(&input_file, "|");
    if (error != ERROR_NONE)
    {
        Error(error);
    }
    /* Close the Input File */
    fclose(input_file);


    /* Process Data from TSTAT Buffer*/
    isThermostatfaulty();
    /* Process Data from BATT Buffer*/
    isBatteryfaulty();

    /* Create and output any alert messages to the console in javascript format */
    if (output_frame_count > 0)
    {
        printf("javascript \n");

        for(count = 0; count < output_frame_count; count++)
        {
            printf("\t {\n\r");
            printf("\t\t satelliteId: %u \n\r",   output_frames[count].satellite_id);
            printf("\t\t severity: \"%s\",\n\r",  output_frames[count].severity_level);
            printf("\t\t component: \"%s\",\n\r", output_frames[count].component);
            printf("\t\t timestamp: \"%s\" \n\r", output_frames[count].timestamp);

            if(count == output_frame_count - 1)
                printf("\t } \n\r");
            else
                printf("\t },\n");
        }
    }
    else
    	printf("No Errors Found in the Sample Data File \n\r");

    return 0;
}
