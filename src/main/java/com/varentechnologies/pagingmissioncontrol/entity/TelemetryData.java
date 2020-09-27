package com.varentechnologies.pagingmissioncontrol.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TelemetryData {
    private LocalDateTime time;
    private Integer satelliteId;
    private Integer redHighLimit;
    private Integer yellowHighLimit;
    private Integer yellowLowLimit;
    private Integer redLowLimit;
    private Float value;

    /**
     * Avoid an enum for the component as any time a new component is added in the future
     * we want to be thinking about how to eliminate code changes.
     */
    private String component;
}
