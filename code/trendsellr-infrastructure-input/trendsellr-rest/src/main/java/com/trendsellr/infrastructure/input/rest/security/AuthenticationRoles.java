package com.trendsellr.infrastructure.input.rest.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthenticationRoles {

    public static final String ANONYMOUS = "hasRole('ROLE_ANONYMOUS')";

    public static final String USER = "hasRole('ROLE_USER')";

    public static final String ADMIN = "hasRole('ROLE_ADMIN')";


    public static final String ANY_USER = ANONYMOUS + " or " + USER + " or " + ADMIN;

    public static final String ANY_REGISTERED_USER = USER;
}
