package com.trendsellr.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Represents the metadata associated to a domain model.
 */

@Data
@Builder
public class Metadata {

    private OffsetDateTime creationDateTime;

    private OffsetDateTime lastUpdatedDateTime;

    private Integer version;
}
