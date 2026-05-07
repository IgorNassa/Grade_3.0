package br.sistema.util;
import br.sistema.model.entity.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import br.sistema.controller.dtos.ProfessorDTO;

@Mapper
public interface ProfessorMapper {
    ProfessorMapper INSTANCE = Mappers.getMapper(ProfessorMapper.class);

    ProfessorDTO toDTO(Professor professor);
    Professor toEntity(ProfessorDTO professorDTO);
}
