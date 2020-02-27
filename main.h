/*
 ============================================================================
 Project Name : Parikh_Meet_Varen_Technologies.c
 Author       : Meet Parikh
 Version      :
 Copyright    : Your copyright notice
 Description  : Paging mission control main.h
 ============================================================================
 */

#ifndef MAIN_H_
#define MAIN_H_

/* Compliler Includes */
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

/* True */
#define TRUE 1
/* False */
#define FALSE 0
/* SUCCESS */
#define SUCCESS 1
/* NO SUCCESS */
#define NOSUCCESS 0

/* Five Minute Interval */
#define MAX_MINUTES 5
/* Minutes to Seconds Converstion */
#define MIN_TO_SECONDS 60
/* Total number of Seconds in 5 minutes */
#define MAX_MIN_SECONDS MAX_MINUTES * MIN_TO_SECONDS
/* 3 Samples */
#define MIN_SAMPLES 0x03
/* Input Timestamp Char Size */
#define INGEST_TIMESTAMP_MAX_SIZE 22
/* Input Component Char Size */
#define INGEST_COMPONENT_MAX_SIZE 5


/* Component Enum */
typedef enum
{
    COMPONENT_BATT,
    COMPONENT_THERMOSTAT,
    COMPONENT_COUNTOF,
}component_t;


/* BATT Struct  */
typedef struct
{
   char timestamp[INGEST_TIMESTAMP_MAX_SIZE];
   float sat_id;
   float red_high;
   float yellow_high;
   float yellow_low;
   float red_low;
   float raw;
   uint8_t component;
} batt_frame_t;

/* Maximum amount of BATT Frames */
#define BATT_FRAME_MAXBUFFER 0x0F

/* TSTAT Struct */
typedef struct __attribute__ ((packed))
{
	char timestamp[INGEST_TIMESTAMP_MAX_SIZE];
	float sat_id;
	float red_high;
	float yellow_high;
	float yellow_low;
	float red_low;
	float raw;
	uint8_t component;
} tstat_frame_t;

/* Maximum amount of Thermostat Frames */
#define TSTAT_FRAME_MAXBUFFER 0x0F

/* Severity Char Size */
#define SEVERITY_BYTES_MAX 9
/* Component Char Size */
#define COMPONENT_BYTES_MAX 6
/* Modified Timestamp Char Size */
#define MOD_TIMESTAMP 25

/* Output Struct*/
typedef struct
{
	uint16_t satellite_id;
	char  severity_level[SEVERITY_BYTES_MAX];
	char  component[COMPONENT_BYTES_MAX];
	char  timestamp[MOD_TIMESTAMP];
} output_frame_t;

/* Maximum amount of Alert frames */
#define OUTPUT_FRAME_MAXBUFFER 0x20


/* Error Enum */
typedef enum
{
    ERROR_NONE,
    ERROR_FOPEN,
    ERROR_INVALIDINPUT,
    ERROR_COUNTOF,
} error_t;

/* Prototyping Functions */
error_t
GetDataFromFile(FILE **input, char* delimiter);

#endif /* MAIN_H_ */
