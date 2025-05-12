package com.encontreaqui.mapper;

import com.encontreaqui.dto.AvaliacaoDTO;
import com.encontreaqui.model.Avaliacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AvaliacaoMapper {

    AvaliacaoMapper INSTANCE = Mappers.getMapper(AvaliacaoMapper.class);

    // Converter a entidade Avaliacao para AvaliacaoDTO
    @Mapping(source = "usuario.id", target = "usuarioId")
    AvaliacaoDTO toDTO(Avaliacao avaliacao);

    // Converter AvaliacaoDTO para a entidade Avaliacao
    @Mapping(source = "usuarioId", target = "usuario.id")
    Avaliacao toEntity(AvaliacaoDTO avaliacaoDTO);
}
