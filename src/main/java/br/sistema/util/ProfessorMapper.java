package br.sistema.util;
import br.sistema.model.entity.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import br.sistema.controller.dtos.ProfessorDTO;
import org.mapstruct.Mapping;

@Mapper
public interface ProfessorMapper {
    ProfessorMapper INSTANCE = Mappers.getMapper(ProfessorMapper.class);

    @Mapping(target = "disciplinas", expression = "java(professor.getDisciplinas() != null ? professor.getDisciplinas().stream().map(br.sistema.model.entity.Disciplina::getNome).collect(java.util.stream.Collectors.toList()) : null)")
    ProfessorDTO toDTO(Professor professor);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "disciplinas", ignore = true)
    Professor toEntity(ProfessorDTO dto);
}
