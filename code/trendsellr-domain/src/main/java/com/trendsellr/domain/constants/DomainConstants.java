package com.trendsellr.domain.constants;

import java.time.format.DateTimeFormatter;

public class DomainConstants {

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    private DomainConstants() {
        // Private constructor to prevent instantiation
    }
}
