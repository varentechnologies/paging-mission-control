package com.varentechnologies.pagingmissioncontrol.router;

import com.varentechnologies.pagingmissioncontrol.entity.TelemetryData;
import com.varentechnologies.pagingmissioncontrol.processor.SatelliteProcessor;
import com.varentechnologies.pagingmissioncontrol.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Take telemetry data and route to the appropriate Processor class.
 */
@Slf4j
@Component
public class TelemetryDataContentBasedRouter {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AlertService alertService;

    // processing segregated by satellite and if our monitoring needs ever become too complex we can plan to partition
    // by groups of satellites by their ID across multiple application instances
    private Map<Integer, SatelliteProcessor> satelliteProcessorMap = new HashMap<>();

    public void process(List<TelemetryData> telemetryDataList) {
        log.debug("Processing {} telemetry objects", telemetryDataList.size());

        for (TelemetryData telemetryData: telemetryDataList) {
            // we can assume the telemetry data is valid

            if (!satelliteProcessorMap.containsKey(telemetryData.getSatelliteId())) {
                // create a separate container-managed prototype bean per satellite processor
                SatelliteProcessor processor = context.getBean(SatelliteProcessor.class);
                processor.setSatelliteId(telemetryData.getSatelliteId());
                satelliteProcessorMap.put(telemetryData.getSatelliteId(), processor);
                log.debug("Created processor for satellite with id {}", telemetryData.getSatelliteId());
            }

            // have the appropriate satellite processor handle this data
            satelliteProcessorMap.get(telemetryData.getSatelliteId()).process(telemetryData);
        }

        // requirements have us print all alerts at the same time
        alertService.printAndClearAlerts();
    }
}
