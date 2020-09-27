package com.varentechnologies.pagingmissioncontrol.entity;

import com.varentechnologies.pagingmissioncontrol.util.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Given a configured duration and event count, keep track of times within an amount of time relative to that interval.
 *
 * If needed we could record a list appropriate pieces of the telemetry data instead of just the time.
 */
@Slf4j
public class EventDurationBucket {
    private List<LocalDateTime> times;
    private LocalDateTime nextTrueTime;
    private Duration window;
    private int threshold;

    public EventDurationBucket(Duration duration, int threshold) {
        this.window = duration;
        this.threshold = threshold;
        this.times = new LinkedList<>();
        this.nextTrueTime = null;
    }

    /**
     * @return the earliest time if the recorded times meet the count and window threshold.
     *         null otherwise
     */
    public synchronized BucketResult record(LocalDateTime time) {
        BucketResult result = new BucketResult();
        if (nextTrueTime == null) nextTrueTime = time;

        times.add(time);
        LocalDateTime staleRecordMaxAge = time.minus(window);

        // remove all records older than time minus window
        times = times.stream().filter(t -> t.isAfter(staleRecordMaxAge)).collect(Collectors.toList());

        // if the count meets the threshold and the time is not before our nextTrueTime
        if (times.size() == threshold && LocalDateTimeUtil.isGreaterThanOrEqual(time, nextTrueTime)) {
            nextTrueTime = time.plus(window);
            result.setFirstTime(times.get(0));
            result.setTriggered(true);
            times.clear();
            log.debug("Window threshold triggered");
        }

        return result;
    }
}
