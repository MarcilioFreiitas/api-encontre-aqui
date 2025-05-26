package com.encontreaqui.mapper;

import com.encontreaqui.dto.ServicoDTO;
import com.encontreaqui.model.Foto;
import com.encontreaqui.model.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre entidade Servico e ServicoDTO,
 * incluindo campos de moderação e relacionamento.
 */
@Mapper(componentModel = "spring")
public interface ServicoMapper {

    ServicoMapper INSTANCE = Mappers.getMapper(ServicoMapper.class);

    /**
     * Converte Servico em ServicoDTO, mapeando campos básicos, lista de fotos e moderação.
     */
    @Mappings({
        @Mapping(source = "usuario.id", target = "usuarioId"),
        @Mapping(target = "fotos", expression = "java(mapFotos(servico.getFotos()))"),
        @Mapping(source = "flagged", target = "flagged"),
        @Mapping(source = "flagReason", target = "flagReason"),
        @Mapping(source = "dataCriacao", target = "dataCriacao"),
        @Mapping(source = "dataAtualizacao", target = "dataAtualizacao")
    })
    ServicoDTO toDTO(Servico servico);

    /**
     * Converte ServicoDTO em Servico, ignorando associação de usuário,
     * mapeando lista de fotos e campos de moderação.
     */
    @Mappings({
        @Mapping(target = "usuario", ignore = true),
        @Mapping(target = "fotos", expression = "java(mapStringToFotos(servicoDTO.getFotos()))"),
        @Mapping(source = "flagged", target = "flagged"),
        @Mapping(source = "flagReason", target = "flagReason"),
        @Mapping(source = "dataCriacao", target = "dataCriacao"),
        @Mapping(source = "dataAtualizacao", target = "dataAtualizacao")
    })
    Servico toEntity(ServicoDTO servicoDTO);

    /**
     * Mapeia lista de entidades Foto para lista de caminhos.
     */
    default List<String> mapFotos(List<Foto> fotos) {
        if (fotos == null) {
            return null;
        }
        return fotos.stream()
                    .map(Foto::getCaminho)
                    .collect(Collectors.toList());
    }

    /**
     * Mapeia lista de caminhos para entidades Foto.
     */
    default List<Foto> mapStringToFotos(List<String> caminhos) {
        if (caminhos == null) {
            return null;
        }
        return caminhos.stream()
                       .map(caminho -> {
                           Foto foto = new Foto();
                           foto.setCaminho(caminho);
                           return foto;
                       })
                       .collect(Collectors.toList());
    }
}
