package com.encontreaqui.mapper;

import com.encontreaqui.dto.AvaliacaoDTO;
import com.encontreaqui.model.Avaliacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AvaliacaoMapper {

    AvaliacaoMapper INSTANCE = Mappers.getMapper(AvaliacaoMapper.class);

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "flagged", target = "flagged")
    @Mapping(source = "flagReason", target = "flagReason")
    AvaliacaoDTO toDTO(Avaliacao avaliacao);

    @Mapping(source = "usuarioId", target = "usuario.id")
    @Mapping(source = "flagged", target = "flagged")
    @Mapping(source = "flagReason", target = "flagReason")
    Avaliacao toEntity(AvaliacaoDTO avaliacaoDTO);
}
