package com.encontreaqui.mapper;

import com.encontreaqui.dto.AluguelDTO;
import com.encontreaqui.model.Aluguel;
import com.encontreaqui.model.Foto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AluguelMapper {

    AluguelMapper INSTANCE = Mappers.getMapper(AluguelMapper.class);

    // Converte Aluguel para AluguelDTO, incluindo moderação
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "fotos", expression = "java(mapFotos(aluguel.getFotos()))")
    @Mapping(source = "categoria", target = "categoria")
    @Mapping(source = "flagged", target = "flagged")
    @Mapping(source = "flagReason", target = "flagReason")
    AluguelDTO toDTO(Aluguel aluguel);

    // Converte AluguelDTO para Aluguel, ignorando usuário e mapeando moderação
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fotos", expression = "java(mapStringToFotos(aluguelDTO.getFotos()))")
    @Mapping(source = "categoria", target = "categoria")
    @Mapping(source = "flagged", target = "flagged")
    @Mapping(source = "flagReason", target = "flagReason")
    Aluguel toEntity(AluguelDTO aluguelDTO);

    // Mapeia List<Foto> para List<String>
    default List<String> mapFotos(List<Foto> fotos) {
        if (fotos == null) {
            return null;
        }
        return fotos.stream()
                .map(Foto::getCaminho)
                .collect(Collectors.toList());
    }

    // Mapeia List<String> para List<Foto>
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
