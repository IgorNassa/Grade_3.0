package br.sistema.util;
import br.sistema.controller.dtos.DisciplinaDTO;
import br.sistema.model.entity.Disciplina;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
import org.mapstruct.Mapping;

@Mapper
public interface DisciplinaMapper {

    DisciplinaMapper INSTANCE = Mappers.getMapper(DisciplinaMapper.class);

    DisciplinaDTO toDTO(Disciplina entity);

    @Mapping(target = "id", source = "id")
    Disciplina toEntity(DisciplinaDTO dto);

    List<DisciplinaDTO> toDTOList(List<Disciplina> disciplinas);
}
