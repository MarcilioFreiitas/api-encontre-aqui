package com.encontreaqui.mapper;

import com.encontreaqui.dto.UsuarioDTO;
import com.encontreaqui.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    // Entidade -> DTO
    UsuarioDTO toDTO(Usuario usuario);

    // DTO -> Entidade
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
