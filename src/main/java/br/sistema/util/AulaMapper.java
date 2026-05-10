package br.sistema.util;

import br.sistema.controller.dtos.AulaDTO;
import br.sistema.model.entity.Aula;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AulaMapper {

    AulaMapper INSTANCE = Mappers.getMapper(AulaMapper.class);

    @Mapping(target = "turmaId", source = "turma.id")
    @Mapping(target = "professorId", source = "professor.id")
    @Mapping(target = "disciplinaId", source = "disciplina.id")
    AulaDTO toDTO(Aula entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "turma", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "disciplina", ignore = true)
    Aula toEntity(AulaDTO dto);
}