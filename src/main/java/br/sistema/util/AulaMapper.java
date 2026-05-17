package br.sistema.util;

import br.sistema.controller.dtos.AulaDTO;
import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;
import br.sistema.model.entity.Turma;
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

    @Mapping(target = "id", source = "id")
    @Mapping(target = "turma", expression = "java(mapTurma(dto.turmaId()))")
    @Mapping(target = "professor", expression = "java(mapProfessor(dto.professorId()))")
    @Mapping(target = "disciplina", expression = "java(mapDisciplina(dto.disciplinaId()))")
    Aula toEntity(AulaDTO dto);

    // Métodos auxiliares para montar as entidades apenas com o ID preenchido
    default Turma mapTurma(Long id) {
        if (id == null) return null;
        Turma turma = new Turma();
        turma.setId(id);
        return turma;
    }

    default Professor mapProfessor(Long id) {
        if (id == null) return null;
        Professor professor = new Professor();
        professor.setId(id);
        return professor;
    }

    default Disciplina mapDisciplina(Long id) {
        if (id == null) return null;
        Disciplina disciplina = new Disciplina();
        disciplina.setId(id);
        return disciplina;
    }
}