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

    // Converte Aluguel para AluguelDTO, mapeando o id do usuário e a lista de fotos
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "fotos", expression = "java(mapFotos(aluguel.getFotos()))")
    @Mapping(source = "categoria", target = "categoria") // Mapeia o campo categoria
    AluguelDTO toDTO(Aluguel aluguel);

    // Converte AluguelDTO para Aluguel, ignorando a associação do usuário (será setada separadamente)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fotos", expression = "java(mapStringToFotos(aluguelDTO.getFotos()))")
    @Mapping(source = "categoria", target = "categoria") // Mapeia o campo categoria
    Aluguel toEntity(AluguelDTO aluguelDTO);

    // Método para mapear List<Foto> para List<String>
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
