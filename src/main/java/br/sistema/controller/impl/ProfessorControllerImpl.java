package br.sistema.controller.impl;
import br.sistema.model.entity.Professor;
import br.sistema.util.ProfessorMapper;
import java.util.stream.Collectors;
import br.sistema.controller.dtos.ProfessorDTO;
import br.sistema.controller.interfaces.ProfessorController;
import br.sistema.model.service.interfaces.ProfessorService;
import java.util.List;

public class ProfessorControllerImpl implements ProfessorController {

    private final ProfessorService professorService;

    public ProfessorControllerImpl(ProfessorService professorService) {
        this.professorService = professorService;
    }


    @Override
    public void save(ProfessorDTO professorDTO) {
     Professor professor = ProfessorMapper.INSTANCE.toEntity(professorDTO);
     professorService.save(professor);
     //pega o ProfessorDTO que vem do controller
        //converte para Professor entity.
        //manda para o service, que trabalha com entity
    }

    @Override
    public void update(ProfessorDTO professorDTO) {
        Professor professor = ProfessorMapper.INSTANCE.toEntity(professorDTO);
        professorService.update(professor);
    }
    //mesma coisa de cimma.

    @Override
    public void delete(ProfessorDTO professorDTO) {
        Professor professor = ProfessorMapper.INSTANCE.toEntity(professorDTO);
        professorService.delete(professor);
    }

    @Override
    public List<ProfessorDTO> findAll() {
        return professorService.findAll().stream().map(ProfessorMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }
    //service devolve List<Professor>
    //controller precisa devolver List<ProfessorDTO>
    //mapper converte cada Professor para ProfessorDTO

    @Override
    public ProfessorDTO findByName(String nome) {
        Professor professor = professorService.findByName(nome);
        return  ProfessorMapper.INSTANCE.toDTO(professor);
    }
    //entrada:
    //ProfessorDTO -> Professor -> Service
    //
    //saída:
    //Service -> Professor -> ProfessorDTO
}