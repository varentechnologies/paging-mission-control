package com.varentechnologies.pagingmissioncontrol.service;

import com.varentechnologies.pagingmissioncontrol.entity.Alert;
import com.varentechnologies.pagingmissioncontrol.util.AlertSerializationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Requirements specify printing all applicable alerts in a JSON list.
 */
@Slf4j
@Service
public class AlertService {

    private final Object lock = new Object();

    private List<Alert> alerts = new LinkedList<>();

    public void handleAlert(Alert alert) {
        synchronized(lock) {
            log.info("Handling alert: {}", alert);
            alerts.add(alert);
        }
    }

    /**
     * Return the JSON to make testing easier.
     */
    public String printAndClearAlerts() {
        synchronized(lock) {
            String json = AlertSerializationUtil.deserialize(alerts);
            log.info("{}", json);
            alerts.clear();
            return json;
        }
    }
}
