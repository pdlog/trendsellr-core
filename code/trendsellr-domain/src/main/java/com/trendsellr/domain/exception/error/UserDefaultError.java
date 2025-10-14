package com.trendsellr.domain.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserDefaultError implements DefaultGenericError {

    USER_ALREADY_EXISTS("USER.ALREADY_EXISTS", "User Already Exists",
            "A user with the provided email already exists.");

    private final String type;

    private final String title;

    private final String message;
}