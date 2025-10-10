package com.trendsellr.domain.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents the metadata associated with a domain model. This class is immutable.
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public final class Metadata {

  @Builder.Default
  private final OffsetDateTime creationDateTime = OffsetDateTime.now(ZoneOffset.UTC);

  @Builder.Default
  private final OffsetDateTime lastUpdatedDateTime = OffsetDateTime.now(ZoneOffset.UTC);

  private final Integer version;

}