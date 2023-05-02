package com.emeraldhieu.vinci.order.logic.sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SortOrderValidatorTest {

    private SortOrderValidator sortOrderValidator;

    @BeforeEach
    public void setUp() {
        sortOrderValidator = new SortOrderValidator();
    }

    @Test
    public void givenInvalidTokenSortOrder_whenValidate_thenThrowsInvalidSortOrderException() {
        // GIVEN
        List<String> sortOrders = List.of("id");

        // WHEN and THEN
        assertThrows(InvalidSortOrderException.class,
            () -> sortOrderValidator.validate(sortOrders));
    }

    @Test
    public void givenNonexistentField_whenValidate_thenThrowsInvalidSortOrderException() {
        // GIVEN
        List<String> sortOrders = List.of("nonexistentProperty,asc");

        // WHEN and THEN
        assertThrows(InvalidSortOrderException.class,
            () -> sortOrderValidator.validate(sortOrders));
    }

    @Test
    public void givenNonexistentSortDirection_whenValidate_thenThrowsInvalidSortOrderException() {
        // GIVEN
        List<String> sortOrders = List.of("id,nonexistentSortDirection");

        // WHEN and THEN
        assertThrows(InvalidSortOrderException.class,
            () -> sortOrderValidator.validate(sortOrders));
    }
}