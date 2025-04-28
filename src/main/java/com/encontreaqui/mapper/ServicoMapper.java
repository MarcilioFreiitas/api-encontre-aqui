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

    // Converte a entidade para o DTO
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "fotos", expression = "java(mapFotos(servico.getFotos()))")
    ServicoDTO toDTO(Servico servico);

    // Converte o DTO para a entidade e ignora a associação do usuário
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fotos", expression = "java(mapStringToFotos(servicoDTO.getFotos()))")
    Servico toEntity(ServicoDTO servicoDTO);

    // Método para mapear List<Foto> para List<String> (caminhos)
    default List<String> mapFotos(List<Foto> fotos) {
        if (fotos == null) {
            return null;
        }
        return fotos.stream()
                    .map(Foto::getCaminho)
                    .collect(Collectors.toList());
    }

    // Método para mapear List<String> para List<Foto>
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
