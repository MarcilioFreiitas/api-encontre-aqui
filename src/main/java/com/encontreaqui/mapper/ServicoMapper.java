package com.encontreaqui.mapper;

import com.encontreaqui.dto.ServicoDTO;
import com.encontreaqui.model.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServicoMapper {
    ServicoMapper INSTANCE = Mappers.getMapper(ServicoMapper.class);

    // Entidade -> DTO
    ServicoDTO toDTO(Servico servico);

    // DTO -> Entidade
    Servico toEntity(ServicoDTO servicoDTO);
}
