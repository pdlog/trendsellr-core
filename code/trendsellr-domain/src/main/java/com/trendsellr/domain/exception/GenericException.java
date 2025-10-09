package com.trendsellr.domain.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenericException extends RuntimeException {

  private final Integer code;
  private final String type;
  private final String title;

  public GenericException(Integer code, String type, String title, String message,
      Throwable throwable) {
    super(message, throwable);
    this.code = code;
    this.type = type;
    this.title = title;
  }

  public GenericException(String type, String title, String message) {
    this(null, type, title, message, null);
  }

  public GenericException(String type, String title, String message, Throwable throwable) {
    this(null, type, title, message, throwable);
  }
}