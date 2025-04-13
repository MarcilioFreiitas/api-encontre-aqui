package com.encontreaqui.mapper;

import com.encontreaqui.dto.ComercioDTO;
import com.encontreaqui.model.Comercio;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ComercioMapper {
    ComercioMapper INSTANCE = Mappers.getMapper(ComercioMapper.class);

    // Entidade -> DTO
    ComercioDTO toDTO(Comercio comercio);

    // DTO -> Entidade
    Comercio toEntity(ComercioDTO comercioDTO);
}
