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
    private final br.sistema.model.service.interfaces.DisciplinaService disciplinaService;

    public ProfessorControllerImpl(ProfessorService professorService, br.sistema.model.service.interfaces.DisciplinaService disciplinaService) {
        this.professorService = professorService;
        this.disciplinaService = disciplinaService;
    }

    @Override
    public void save(ProfessorDTO professorDTO) {
        Professor professor = ProfessorMapper.INSTANCE.toEntity(professorDTO);
        mapDisciplinas(professorDTO, professor);
        professorService.save(professor);
    }

    @Override
    public void update(ProfessorDTO professorDTO) {
        Professor professor = ProfessorMapper.INSTANCE.toEntity(professorDTO);
        mapDisciplinas(professorDTO, professor);
        professorService.update(professor);
    }

    private void mapDisciplinas(ProfessorDTO dto, Professor entity) {
        if (dto.disciplinas() != null) {
            List<br.sistema.model.entity.Disciplina> entities = dto.disciplinas().stream()
                    .map(nome -> {
                        br.sistema.model.entity.Disciplina d = disciplinaService.findByName(nome);
                        if (d == null) {
                            d = new br.sistema.model.entity.Disciplina();
                            d.setNome(nome);
                        }
                        return d;
                    })
                    .collect(java.util.stream.Collectors.toList());
            entity.setDisciplinas(entities);
        }
    }

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