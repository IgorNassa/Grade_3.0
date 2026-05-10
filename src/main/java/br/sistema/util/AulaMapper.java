package br.sistema.util;
import br.sistema.model.entity.Aula;
import br.sistema.controller.dtos.AulaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapping;

@Mapper(uses = {
        TurmaMapper.class,
        ProfessorMapper.class,
        DisciplinaMapper.class
})
public interface AulaMapper {
    AulaMapper INSTANCE = Mappers.getMapper(AulaMapper.class);

    AulaDTO toDTO(Aula entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "turma", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "disciplina", ignore = true)
    @Mapping(target = "diaDaSemana", ignore = true)
    @Mapping(target = "slotHorario", ignore = true)
    Aula toEntity(AulaDTO dto);

}
