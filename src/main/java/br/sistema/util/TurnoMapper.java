package br.sistema.util;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import br.sistema.model.entity.Turno;
import br.sistema.controller.dtos.TurnoDTO;

@Mapper
public interface TurnoMapper {

    TurnoMapper INSTANCE = Mappers.getMapper(TurnoMapper.class);


    TurnoDTO toDTO(Turno entity);


    Turno toEntity(TurnoDTO dto);
}
