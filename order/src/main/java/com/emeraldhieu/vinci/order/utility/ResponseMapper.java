package com.emeraldhieu.vinci.order.utility;

import java.util.List;

public interface ResponseMapper<DTO, ENTITY> {

    DTO toDto(ENTITY entity);

    List<DTO> toDto(List<ENTITY> entityList);
}
