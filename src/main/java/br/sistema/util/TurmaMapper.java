package br.sistema.util;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import br.sistema.controller.dtos.TurmaDTO;
import br.sistema.model.entity.Turma;
import org.mapstruct.Mapping;

@Mapper(uses = {DisciplinaMapper.class})
public interface TurmaMapper {

    TurmaMapper INSTANCE = Mappers.getMapper(TurmaMapper.class);

    TurmaDTO toDTO(Turma entity);

    @Mapping(target = "id", ignore = true)
    Turma toEntity(TurmaDTO dto);
}
