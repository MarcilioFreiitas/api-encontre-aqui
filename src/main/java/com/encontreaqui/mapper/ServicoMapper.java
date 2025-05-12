package com.encontreaqui.mapper;

import com.encontreaqui.dto.ServicoDTO;
import com.encontreaqui.model.Foto;
import com.encontreaqui.model.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ServicoMapper {

    ServicoMapper INSTANCE = Mappers.getMapper(ServicoMapper.class);

    // Converter Servico para ServicoDTO: mapeia o id do usuário e a lista de fotos; ignora mediaAvaliacoes.
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "fotos", expression = "java(mapFotos(servico.getFotos()))")
    @Mapping(target = "mediaAvaliacoes", ignore = true)
    ServicoDTO toDTO(Servico servico);

    // Converter ServicoDTO para Servico, ignorando a associação do usuário, e mapeando a lista de fotos.
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fotos", expression = "java(mapStringToFotos(servicoDTO.getFotos()))")
    Servico toEntity(ServicoDTO servicoDTO);

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
