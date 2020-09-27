package com.varentechnologies.pagingmissioncontrol.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BucketResult {
    private boolean triggered;
    private LocalDateTime firstTime;

    public BucketResult() {
        this.triggered = false;
        this.firstTime = null;
    }
}
