package com.trendsellr.domain.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecurityDefaultError implements DefaultGenericError {

    ACCESS_DENIED("SECURITY.ACCESS_DENIED", "Access Denied", "You do not have permission to access this resource.");

    private final String type;

    private final String title;

    private final String message;
}
