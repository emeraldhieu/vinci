package com.emeraldhieu.vinci.shipping.logic.sort;

import com.emeraldhieu.vinci.shipping.logic.Shipping;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SortOrderValidator {

    public void validate(List<String> sortOrders) {
        sortOrders.forEach(sortOrder -> {
            String[] tokens = sortOrder.split(",");
            if (tokens.length != 2) {
                throw new InvalidSortOrderException("Invalid sort order");
            }
            String property = tokens[0];
            String direction = tokens[1];
            List<String> allowedPropertyNames = Arrays.stream(Shipping.class.getDeclaredFields())
                .map(field -> field.getName())
                .collect(Collectors.toList());

            if (!allowedPropertyNames.contains(property)) {
                throw new InvalidSortOrderException("Invalid property %s".formatted(property));
            }

            try {
                Sort.Direction.fromString(direction);
            } catch (Exception e) {
                throw new InvalidSortOrderException("Invalid direction %s".formatted(direction), e);
            }
        });
    }
}