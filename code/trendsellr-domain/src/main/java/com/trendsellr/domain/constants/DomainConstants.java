package com.trendsellr.domain.constants;

import java.time.format.DateTimeFormatter;

public final class DomainConstants {

  public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd'T'HH:mm:ss'Z'");

  private DomainConstants() {
    // Private constructor to prevent instantiation
  }
}
