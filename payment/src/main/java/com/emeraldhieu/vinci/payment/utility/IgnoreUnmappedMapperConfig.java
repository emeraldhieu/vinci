package com.emeraldhieu.vinci.payment.utility;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * An interface used to ignore unmapped target properties when using mapstruct.
 * See https://www.baeldung.com/mapstruct-ignore-unmapped-properties#2-use-a-shared-mapperconfig
 */
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IgnoreUnmappedMapperConfig {
}