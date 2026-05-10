package br.sistema.util;
import br.sistema.model.entity.Aula;
import br.sistema.controller.dtos.AulaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        TurmaMapper.class,
        ProfessorMapper.class,
        DisciplinaMapper.class
})
public interface AulaMapper {
    AulaMapper INSTANCE = Mappers.getMapper(AulaMapper.class);

    AulaDTO toDTO(Aula entity);

    Aula toEntity(AulaDTO dto);

}
