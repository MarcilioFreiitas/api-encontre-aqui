package com.encontreaqui.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.encontreaqui.dto.AluguelDTO;
import com.encontreaqui.model.Aluguel;

@Mapper
public interface AluguelMapper {

	AluguelMapper INSTANCE = Mappers.getMapper(AluguelMapper.class);
	
	AluguelDTO toDTO(Aluguel aluguel);
	
	Aluguel toEntity(AluguelDTO alugueldto);
	
}
