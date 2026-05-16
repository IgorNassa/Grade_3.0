package br.sistema.util;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import br.sistema.model.entity.Turno;
import br.sistema.controller.dtos.TurnoDTO;
import org.mapstruct.Mapping;

@Mapper
public interface TurnoMapper {

    TurnoMapper INSTANCE = Mappers.getMapper(TurnoMapper.class);


    TurnoDTO toDTO(Turno entity);


    @Mapping(target = "id", source = "id")
    Turno toEntity(TurnoDTO dto);
}
