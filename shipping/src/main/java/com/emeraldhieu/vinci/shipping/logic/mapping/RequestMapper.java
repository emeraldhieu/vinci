package com.emeraldhieu.vinci.shipping.logic.mapping;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface RequestMapper<DTO, ENTITY> {

    ENTITY toEntity(DTO dto);

    List<ENTITY> toEntity(List<DTO> dtoList);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget ENTITY entity, DTO dto);
}
