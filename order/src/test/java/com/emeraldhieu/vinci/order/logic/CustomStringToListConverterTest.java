package com.emeraldhieu.vinci.order.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomStringToListConverterTest {

    private CustomStringToListConverter customStringToListConverter;

    @BeforeEach
    public void setUp() {
        customStringToListConverter = new CustomStringToListConverter();
    }

    @Test
    public void given_when_then() {
        // GIVEN
        String str = "fieldA,asc|fieldB,desc";
        List<String> expectedList = List.of(
            "fieldA,asc",
            "fieldB,desc"
        );

        // WHEN
        List<String> list = customStringToListConverter.convert(str);

        // THEN
        assertEquals(expectedList, list);
    }
}