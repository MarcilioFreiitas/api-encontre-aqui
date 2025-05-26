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

    // Converte entidade para DTO, incluindo campos de moderação
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(target = "fotos", expression = "java(mapFotos(comercio.getFotos()))")
    @Mapping(source = "categoria", target = "categoria")
    @Mapping(source = "flagged", target = "flagged")
    @Mapping(source = "flagReason", target = "flagReason")
    ComercioDTO toDTO(Comercio comercio);

    // Converte DTO para entidade, ignorando associação de usuário e incluindo moderação
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fotos", expression = "java(mapStringToFotos(comercioDTO.getFotos()))")
    @Mapping(source = "categoria", target = "categoria")
    @Mapping(source = "flagged", target = "flagged")
    @Mapping(source = "flagReason", target = "flagReason")
    Comercio toEntity(ComercioDTO comercioDTO);

    // Mapeia List<Foto> para List<String>
    default List<String> mapFotos(List<Foto> fotos) {
        if (fotos == null) return null;
        return fotos.stream()
                .map(Foto::getCaminho)
                .collect(Collectors.toList());
    }

    // Mapeia List<String> para List<Foto>
    default List<Foto> mapStringToFotos(List<String> caminhos) {
        if (caminhos == null) return null;
        return caminhos.stream()
                .map(caminho -> {
                    Foto foto = new Foto();
                    foto.setCaminho(caminho);
                    return foto;
                })
                .collect(Collectors.toList());
    }
}
