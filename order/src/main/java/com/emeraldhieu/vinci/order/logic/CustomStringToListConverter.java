package com.emeraldhieu.vinci.order.logic;

import io.micrometer.common.util.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Override StringToCollectionConverter that is registered by default.
 * StringToCollectionConverter uses comma as delimiter for collection of values.
 * See https://github.com/spring-projects/spring-framework/issues/23820
 * <p>
 * Register converter by @Component.
 * See https://stackoverflow.com/questions/35025550/register-spring-converter-programmatically-in-spring-boot#41205653
 */
@Component
public class CustomStringToListConverter implements Converter<String, List<String>> {

    @Override
    public List<String> convert(String str) {
        if (StringUtils.isEmpty(str)) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.split("\\|")).stream()
            .collect(Collectors.toList());
    }
}