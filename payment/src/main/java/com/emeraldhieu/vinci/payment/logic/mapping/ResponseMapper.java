package com.emeraldhieu.vinci.payment.logic.mapping;

import java.util.List;

public interface ResponseMapper<DTO, ENTITY> {

    DTO toDto(ENTITY entity);

    List<DTO> toDto(List<ENTITY> entityList);
}
