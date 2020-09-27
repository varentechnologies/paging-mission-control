package com.varentechnologies.pagingmissioncontrol.util;

import java.time.LocalDateTime;

public class LocalDateTimeUtil {
    private LocalDateTimeUtil() { }

    public static final boolean isGreaterThanOrEqual(LocalDateTime left, LocalDateTime right) {
        return ((left == null && right == null) || (left != null && right != null && !left.isBefore(right)));
    }
}
