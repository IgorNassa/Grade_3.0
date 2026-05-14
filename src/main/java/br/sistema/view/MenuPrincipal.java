package br.sistema.view;

import br.sistema.model.entity.*;
import br.sistema.model.service.interfaces.*;
import br.sistema.model.exception.BusinessException;
import br.sistema.model.exception.NotFoundException;
import br.sistema.model.exception.ValidationException;

import java.util.*;

public class MenuPrincipal {

    private final Scanner scanner;
    private final DisciplinaService disciplinaService;
    private final ProfessorService professorService;
    private final TurnoService turnoService;
    private final TurmaService turmaService;
    private final AulaService aulaService;

    public MenuPrincipal(DisciplinaService ds, ProfessorService ps, TurnoService tns, TurmaService ts, AulaService as) {
        this.scanner = new Scanner(System.in);
        this.disciplinaService = ds;
        this.professorService = ps;
        this.turnoService = tns;
        this.turmaService = ts;
        this.aulaService = as;
    }

    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n========================================");
            System.out.println("  SISTEMA DE GERAÇÃO DE GRADE (SGDG)");
            System.out.println("========================================");
            System.out.println("1. Gerenciar Disciplinas");
            System.out.println("2. Gerenciar Professores");
            System.out.println("3. Gerenciar Turnos");
            System.out.println("4. Gerenciar Turmas");
            System.out.println("5. Gerar Grade Horária (Backtracking)");
            System.out.println("0. Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1 -> menuDisciplinas();
                    case 2 -> menuProfessores();
                    case 3 -> menuTurnos();
                    case 4 -> menuTurmas();
                    case 5 -> menuAulas();
                    case 0 -> System.out.println("Encerrando o sistema...");
                    default -> System.out.println("[AVISO] Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERRO] Digite apenas números.");
            } catch (Exception e) {
                System.out.println("[ERRO CRÍTICO] " + e.getMessage());
            }
        }
    }

    private void menuDisciplinas() {
        System.out.println("\n--- MENU DISCIPLINAS ---");
        System.out.println("1. Cadastrar | 2. Alterar | 3. Excluir | 4. Ver Todas | 0. Voltar");
        System.out.print("Escolha: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao) {
                case 1 -> {
                    System.out.print("Nome da nova Disciplina: ");
                    Disciplina d = new Disciplina();
                    d.setNome(scanner.nextLine());
                    disciplinaService.save(d);
                    System.out.println("Sucesso: Disciplina cadastrada!");
                }
                case 2 -> {
                    listarDisciplinasInterno();
                    System.out.print("\nNome da Disciplina que deseja alterar: ");
                    Disciplina d = disciplinaService.findByName(scanner.nextLine().trim());
                    if (d != null) {
                        System.out.print("Novo nome: ");
                        d.setNome(scanner.nextLine());
                        disciplinaService.update(d);
                        System.out.println("Sucesso: Disciplina alterada!");
                    } else {
                        System.out.println("[AVISO] Disciplina não encontrada.");
                    }
                }
                case 3 -> {
                    listarDisciplinasInterno();
                    System.out.print("Nome da Disciplina a excluir: ");
                    Disciplina d = disciplinaService.findByName(scanner.nextLine().trim());
                    if (d != null) {
                        disciplinaService.delete(d);
                        System.out.println("Sucesso: Disciplina excluída!");
                    }
                }
                case 4 -> listarDisciplinasInterno();
            }
        } catch (Exception e) {
            System.err.println("[ERRO] " + e.getMessage());
        }
    }

    private void menuProfessores() {
        System.out.println("\n--- MENU PROFESSORES ---");
        System.out.println("1. Cadastrar | 2. Alterar Disciplinas | 3. Excluir | 4. Ver Todos | 0. Voltar");
        System.out.print("Escolha: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao) {
                case 1 -> {
                    System.out.print("Nome do Professor: ");
                    String nome = scanner.nextLine();
                    List<Disciplina> selecionadas = selecionarDisciplinas();
                    Professor p = new Professor();
                    p.setNome(nome);
                    p.setDisciplinas(selecionadas);
                    professorService.save(p);
                    System.out.println("Sucesso: Professor cadastrado!");
                }
                case 2 -> {
                    listarProfessoresInterno();
                    System.out.print("Nome do Professor para alterar: ");
                    Professor p = professorService.findByName(scanner.nextLine().trim());
                    if (p != null) {
                        p.setDisciplinas(selecionarDisciplinas());
                        professorService.update(p);
                        System.out.println("Sucesso: Disciplinas atualizadas!");
                    }
                }
                case 3 -> {
                    listarProfessoresInterno();
                    System.out.print("Nome do Professor a excluir: ");
                    Professor p = professorService.findByName(scanner.nextLine().trim());
                    if (p != null) {
                        professorService.delete(p);
                        System.out.println("Sucesso: Professor excluído!");
                    }
                }
                case 4 -> listarProfessoresInterno();
            }
        } catch (Exception e) {
            System.err.println("[ERRO] " + e.getMessage());
        }
    }

    private void menuTurnos() {
        System.out.println("\n--- MENU TURNOS ---");
        System.out.println("1. Cadastrar | 2. Alterar | 3. Excluir | 4. Ver Todos | 0. Voltar");
        System.out.print("Escolha: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao) {
                case 1 -> {
                    Turno t = new Turno();
                    System.out.print("Tipo (MATUTINO, VESPERTINO, NOTURNO): ");
                    t.setNomeTurno(TipoTurno.valueOf(scanner.nextLine().toUpperCase().trim()));
                    System.out.print("Início (HH:mm): ");
                    t.setInicioTurno(java.time.LocalTime.parse(scanner.nextLine()));
                    System.out.print("Fim (HH:mm): ");
                    t.setFimTurno(java.time.LocalTime.parse(scanner.nextLine()));
                    System.out.print("Tempo de aula (min): ");
                    t.setTempoAula(Integer.parseInt(scanner.nextLine()));
                    System.out.print("Qtd aulas: ");
                    t.setAulasTurno(Integer.parseInt(scanner.nextLine()));
                    System.out.print("Aulas antes do intervalo: ");
                    t.setAulasAntesIntervalo(Integer.parseInt(scanner.nextLine()));
                    turnoService.save(t);
                    System.out.println("Sucesso: Turno cadastrado!");
                }
                case 4 -> listarTurnosInterno();
            }
        } catch (Exception e) {
            System.err.println("[ERRO] " + e.getMessage());
        }
    }

    private void menuTurmas() {
        System.out.println("\n--- MENU TURMAS ---");
        System.out.println("1. Cadastrar | 2. Alterar | 3. Excluir | 4. Ver Todas | 0. Voltar");
        System.out.print("Escolha: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao) {
                case 1 -> {
                    System.out.print("Nome da Turma (Ex: 2º A): ");
                    String nome = scanner.nextLine();
                    System.out.print("É Ensino Médio? (true/false): ");
                    boolean medio = Boolean.parseBoolean(scanner.nextLine());
                    Turma turma = new Turma();
                    turma.setNome(nome);
                    turma.seteMedio(medio);
                    turmaService.save(turma);
                    System.out.println("Sucesso: Turma cadastrada!");
                }
                case 4 -> listarTurmasInterno();
            }
        } catch (Exception e) {
            System.err.println("[ERRO] " + e.getMessage());
        }
    }

    private void menuAulas() {
        try {
            listarTurmasInterno();
            System.out.print("Qual o nome da Turma para gerar a grade? ");
            Turma turma = turmaService.findByName(scanner.nextLine().trim());

            if (turma == null) throw new NotFoundException("Turma não encontrada.");

            Map<Disciplina, Integer> carga = new HashMap<>();
            List<Disciplina> todas = disciplinaService.findAll();

            System.out.println("\n--- Definir Carga Horária Semanal ---");
            for (Disciplina d : todas) {
                System.out.print("Aulas de " + d.getNome() + ": ");
                int qtd = Integer.parseInt(scanner.nextLine());
                if (qtd > 0) carga.put(d, qtd);
            }

            System.out.println("[PROCESSANDO] Calculando combinações via Backtracking...");
            aulaService.gerarGrade(turma, carga);

            // Exibe o resultado final formatado
            imprimirGradeNaTela(turma);

        } catch (BusinessException | ValidationException | NotFoundException e) {
            System.err.println("[CONFLITO] " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERRO] " + e.getMessage());
        }
    }

    private void imprimirGradeNaTela(Turma turma) {
        List<Aula> aulasDaTurma = aulaService.findAll().stream()
                .filter(a -> a.getTurma().getId().equals(turma.getId()))
                .toList();

        String[] dias = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};

        System.out.println("\n=====================================================================================================================");
        System.out.printf("%-10s | %-18s | %-18s | %-18s | %-18s | %-18s%n", "SLOT", "SEG", "TER", "QUA", "QUI", "SEX");
        System.out.println("---------------------------------------------------------------------------------------------------------------------");

        for (int slot = 1; slot <= 5; slot++) {
            System.out.printf("AULA %-5d |", slot);
            for (String diaStr : dias) {
                int finalSlot = slot;
                Aula aula = aulasDaTurma.stream()
                        .filter(a -> a.getDiaDaSemana().name().equals(diaStr) && a.getSlotHorario() == finalSlot)
                        .findFirst().orElse(null);

                if (aula != null) {
                    String info = aula.getDisciplina().getNome() + " (" + aula.getProfessor().getNome() + ")";
                    System.out.printf(" %-18s |", info.length() > 18 ? info.substring(0, 15) + "..." : info);
                } else {
                    System.out.printf(" %-18s |", "-");
                }
            }
            System.out.println();
        }
        System.out.println("=====================================================================================================================\n");
    }

    private void listarDisciplinasInterno() {
        disciplinaService.findAll().forEach(d -> System.out.println("- " + d.getNome()));
    }

    private void listarProfessoresInterno() {
        professorService.findAll().forEach(p -> System.out.println("- " + p.getNome()));
    }

    private void listarTurmasInterno() {
        turmaService.findAll().forEach(t -> System.out.println("- " + t.getNome()));
    }

    private void listarTurnosInterno() {
        turnoService.findAll().forEach(t -> System.out.println("- " + t.getNomeTurno()));
    }

    private List<Disciplina> selecionarDisciplinas() {
        List<Disciplina> selecionadas = new ArrayList<>();
        System.out.println("Digite o nome da disciplina ou '0' para sair:");
        while (true) {
            String nome = scanner.nextLine().trim();
            if (nome.equals("0")) break;
            Disciplina d = disciplinaService.findByName(nome);
            if (d != null) selecionadas.add(d);
            else System.out.println("Não encontrada.");
        }
        return selecionadas;
    }
}