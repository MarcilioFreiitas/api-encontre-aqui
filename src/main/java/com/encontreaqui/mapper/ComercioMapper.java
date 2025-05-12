package com.encontreaqui.mapper;

import com.encontreaqui.dto.ComercioDTO;
import com.encontreaqui.model.Comercio;
import com.encontreaqui.model.Foto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ComercioMapper {

    ComercioMapper INSTANCE = Mappers.getMapper(ComercioMapper.class);

    // Converter entidade para DTO (não utiliza avaliações diretamente)
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(target = "fotos", expression = "java(mapFotos(comercio.getFotos()))")
    @Mapping(source = "categoria", target = "categoria")
    // O campo mediaAvaliacoes será setado posteriormente na camada service
    ComercioDTO toDTO(Comercio comercio);

    // Converter DTO para entidade
    @Mapping(source = "usuarioId", target = "usuario.id")
    @Mapping(target = "fotos", expression = "java(mapStringToFotos(comercioDTO.getFotos()))")
    @Mapping(source = "categoria", target = "categoria")
    Comercio toEntity(ComercioDTO comercioDTO);

    // Método auxiliar para converter List<Foto> para List<String> (caminhos das fotos)
    default List<String> mapFotos(List<Foto> fotos) {
        if (fotos == null) return null;
        return fotos.stream()
                .map(Foto::getCaminho)
                .collect(Collectors.toList());
    }

    // Método auxiliar para converter List<String> em List<Foto>
    default List<Foto> mapStringToFotos(List<String> caminhos) {
        if (caminhos == null) return null;
        return caminhos.stream().map(caminho -> {
            Foto foto = new Foto();
            foto.setCaminho(caminho);
            return foto;
        }).collect(Collectors.toList());
    }
}
