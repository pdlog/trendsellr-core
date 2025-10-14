package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.infrastructure.input.rest.api.HealthApi;
import com.trendsellr.infrastructure.input.rest.dto.CheckHealth200ResponseDTO;
import com.trendsellr.infrastructure.input.rest.security.AuthenticationRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthController implements HealthApi {

    @Override
    @PreAuthorize(AuthenticationRoles.ANY_USER)
    public ResponseEntity<CheckHealth200ResponseDTO> checkHealth() {
        log.info("Executing health check");

        final CheckHealth200ResponseDTO response = new CheckHealth200ResponseDTO().status("UP");

        return ResponseEntity.ok(response);
    }
}