package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.infrastructure.input.rest.api.HealthApi;
import com.trendsellr.infrastructure.input.rest.dto.CheckHealth200ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthController implements HealthApi {

    @Override
    public ResponseEntity<CheckHealth200ResponseDTO> checkHealth() {
        log.info("Executing health check");

        final CheckHealth200ResponseDTO response = new CheckHealth200ResponseDTO().status("UP");

        return ResponseEntity.ok(response);
    }
}