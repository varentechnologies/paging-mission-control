package com.varentechnologies.pagingmissioncontrol.processor;

import com.varentechnologies.pagingmissioncontrol.codes.SatelliteComponentCodes;
import com.varentechnologies.pagingmissioncontrol.codes.SatelliteConditionCodes;
import com.varentechnologies.pagingmissioncontrol.entity.Alert;
import com.varentechnologies.pagingmissioncontrol.entity.BucketResult;
import com.varentechnologies.pagingmissioncontrol.entity.EventDurationBucket;
import com.varentechnologies.pagingmissioncontrol.entity.TelemetryData;
import com.varentechnologies.pagingmissioncontrol.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Set satellite ID before processing.
 */
@Slf4j
@Component
@Scope(value = "prototype")
public class SatelliteProcessor {

    @Autowired
    private AlertService alertService;

    private int satelliteId;

    // supporting different component types is as simple as adding another entry to this map
    private Map<String, Map<String, EventDurationBucket>> componentLimitMap;

    public void setSatelliteId(int satelliteId) {
        this.satelliteId = satelliteId;
        setup();
    }

    /**
     * At the moment all satellites have the same monitoring requirements:
     * - If for the same satellite there are three battery voltage readings that are under the red low limit within a five minute interval.
     * - If for the same satellite there are three thermostat readings that exceed the red high limit within a five minute interval.
     *
     * Hard code these requirements for now. Could handle this through configuration in the future.
     */
    private void setup() {
        componentLimitMap = new HashMap<>();

        Map<String, EventDurationBucket> batteryLimitMap = new HashMap<>();
        batteryLimitMap.put(SatelliteConditionCodes.RED_LOW, new EventDurationBucket(Duration.ofMinutes(5), 3));
        componentLimitMap.put(SatelliteComponentCodes.BATTERY_VOLTAGE, batteryLimitMap);
        log.debug("Ready to monitor condition {} for component {} for satellite {}", SatelliteConditionCodes.RED_LOW, SatelliteComponentCodes.BATTERY_VOLTAGE, satelliteId);

        Map<String, EventDurationBucket> thermostatLimitMap = new HashMap<>();
        thermostatLimitMap.put(SatelliteConditionCodes.RED_HIGH, new EventDurationBucket(Duration.ofMinutes(5), 3));
        componentLimitMap.put(SatelliteComponentCodes.THERMOSTAT, thermostatLimitMap);
        log.debug("Ready to monitor condition {} for component {} for satellite {}", SatelliteConditionCodes.RED_HIGH, SatelliteComponentCodes.THERMOSTAT, satelliteId);
    }

    public synchronized void process(TelemetryData telemetryData) {
        // is this component being monitored
        if (componentLimitMap.containsKey(telemetryData.getComponent())) {
            Map<String, EventDurationBucket> limitMap = componentLimitMap.get(telemetryData.getComponent());

            // RED_LOW condition being monitored?
            if (limitMap.containsKey(SatelliteConditionCodes.RED_LOW)) {
                if (telemetryData.getValue() < telemetryData.getRedLowLimit()) {
                    log.debug("Satellite {} component {} condition {} triggered", satelliteId, telemetryData.getComponent(), SatelliteConditionCodes.RED_LOW);
                    EventDurationBucket bucket = limitMap.get(SatelliteConditionCodes.RED_LOW);

                    BucketResult result = bucket.record(telemetryData.getTime());
                    processBucketResult(result, SatelliteConditionCodes.RED_LOW, telemetryData.getComponent());
                }
            }

            // RED_HIGH condition being monitored?
            if (limitMap.containsKey(SatelliteConditionCodes.RED_HIGH)) {
                if (telemetryData.getValue() > telemetryData.getRedHighLimit()) {
                    log.debug("Satellite {} component {} condition {} triggered", satelliteId, telemetryData.getComponent(), SatelliteConditionCodes.RED_HIGH);
                    EventDurationBucket bucket = limitMap.get(SatelliteConditionCodes.RED_HIGH);

                    BucketResult result = bucket.record(telemetryData.getTime());
                    processBucketResult(result, SatelliteConditionCodes.RED_HIGH, telemetryData.getComponent());
                }
            }
        }
    }

    private void processBucketResult(BucketResult result, String condition, String component) {
        if (result.isTriggered()) {
            Alert alert = new Alert(satelliteId, condition, component, result.getFirstTime());
            alertService.handleAlert(alert);
        }
    }
}
