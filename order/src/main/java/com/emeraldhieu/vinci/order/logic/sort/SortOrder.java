package com.emeraldhieu.vinci.order.logic.sort;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A class that converts a sort order string to an object.
 * Example of sort order strings: "propertyA,asc", "propertyB,desc".
 */
@Builder
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SortOrder {

    private final String propertyName;
    private final String direction;

    public static SortOrder from(String sortOrderStr) {
        String[] tokens = sortOrderStr.split(",");
        if (tokens.length != 2) {
            throw new InvalidSortOrderException("Invalid sort order");
        }
        String propertyName = tokens[0];
        String direction = tokens[1];
        return SortOrder.builder()
            .propertyName(propertyName)
            .direction(direction)
            .build();
    }
}
