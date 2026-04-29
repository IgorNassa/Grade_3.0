package br.sistema.view;

import br.sistema.model.entity.*;
import br.sistema.model.entity.service.*;

import java.time.format.DateTimeParseException;
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
            System.out.println("5. Gerenciar Aulas (Gerador de Grade)");
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

    // ==========================================
    // 1. CRUD: DISCIPLINAS
    // ==========================================
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
                    System.out.print("\nNome da Disciplina que deseja alterar (ignore case): ");
                    String busca = scanner.nextLine().toLowerCase().trim();
                    Disciplina d = disciplinaService.findByName(busca);

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
                    Disciplina d = new Disciplina();
                    d.setNome(scanner.nextLine());
                    disciplinaService.delete(d);
                    System.out.println("Sucesso: Disciplina excluída!");
                }
                case 4 -> listarDisciplinasInterno();
            }
        } catch (NumberFormatException e) {
            System.err.println("[ERRO] Digite um número válido para a opção.");
        } catch (Exception e) {
            System.err.println("[ERRO NA OPERAÇÃO] " + e.getMessage());
        }
    }

    // ==========================================
    // 2. CRUD: PROFESSORES
    // ==========================================
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
                    Professor p = professorService.findByName(scanner.nextLine().toLowerCase().trim());
                    if (p != null) {
                        p.setDisciplinas(selecionarDisciplinas());
                        professorService.update(p);
                        System.out.println("Sucesso: Disciplinas atualizadas!");
                    } else {
                        System.out.println("[AVISO] Professor não encontrado.");
                    }
                }
                case 3 -> {
                    listarProfessoresInterno();
                    System.out.print("Nome do Professor a excluir: ");
                    Professor p = new Professor();
                    p.setNome(scanner.nextLine());
                    professorService.delete(p);
                    System.out.println("Sucesso: Professor excluído!");
                }
                case 4 -> listarProfessoresInterno();
            }
        } catch (NumberFormatException e) {
            System.err.println("[ERRO] Digite um número válido para a opção.");
        } catch (Exception e) {
            System.err.println("[ERRO NO PROFESSOR] " + e.getMessage());
        }
    }

    // ==========================================
    // 3. CRUD: TURNOS
    // ==========================================
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

                    System.out.print("Início (HH:mm - Ex: 07:30): ");
                    t.setInicioTurno(java.time.LocalTime.parse(scanner.nextLine()));

                    System.out.print("Fim (HH:mm - Ex: 12:00): ");
                    t.setFimTurno(java.time.LocalTime.parse(scanner.nextLine()));

                    System.out.print("Tempo de cada aula (minutos): ");
                    t.setTempoAula(Integer.parseInt(scanner.nextLine()));

                    System.out.print("Quantidade total de aulas: ");
                    t.setAulasTurno(Integer.parseInt(scanner.nextLine()));

                    System.out.print("Aulas antes do intervalo: ");
                    t.setAulasAntesIntervalo(Integer.parseInt(scanner.nextLine()));

                    turnoService.save(t);
                    System.out.println("Sucesso: Turno cadastrado!");
                }
                case 2 -> {
                    listarTurnosInterno();
                    System.out.print("Nome do Turno para alterar: ");
                    Turno t = turnoService.findByName(scanner.nextLine().toUpperCase().trim());
                    if (t != null) {
                        System.out.print("Novo tempo de aula (min): ");
                        t.setTempoAula(Integer.parseInt(scanner.nextLine()));
                        System.out.print("Nova quantidade de aulas: ");
                        t.setAulasTurno(Integer.parseInt(scanner.nextLine()));

                        turnoService.update(t);
                        System.out.println("Sucesso: Turno atualizado!");
                    } else {
                        System.out.println("[AVISO] Turno não encontrado.");
                    }
                }
                case 3 -> {
                    listarTurnosInterno();
                    System.out.print("Nome do Turno a excluir: ");
                    String nome = scanner.nextLine().toUpperCase().trim();
                    Turno t = new Turno();
                    t.setNomeTurno(TipoTurno.valueOf(nome));
                    turnoService.delete(t);
                    System.out.println("Sucesso: Turno excluído!");
                }
                case 4 -> listarTurnosInterno();
            }
        } catch (NumberFormatException e) {
            System.err.println("[ERRO] Digite um número válido.");
        } catch (DateTimeParseException e) {
            System.err.println("[ERRO] Formato de hora inválido. Use HH:mm (Ex: 08:00).");
        } catch (IllegalArgumentException e) {
            System.err.println("[ERRO] Valor inválido. Verifique se digitou o tipo do turno corretamente.");
        } catch (Exception e) {
            System.err.println("[ERRO NO TURNO] " + e.getMessage());
        }
    }

    // ==========================================
    // 4. CRUD: TURMAS
    // ==========================================
    private void menuTurmas() {
        System.out.println("\n--- MENU TURMAS ---");
        System.out.println("1. Cadastrar | 2. Alterar | 3. Excluir | 4. Ver Todas | 0. Voltar");
        System.out.print("Escolha: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao) {
                case 1 -> {
                    System.out.print("Nome da Turma (Ex: 1A): ");
                    String nome = scanner.nextLine();
                    listarTurnosInterno();
                    System.out.print("Escolha o Turno (ignore case): ");
                    Turno t = turnoService.findByName(scanner.nextLine().toUpperCase().trim());

                    Turma turma = new Turma();
                    turma.setNome(nome);
                    // turma.setTurno(t); // Descomente quando o relacionamento estiver pronto
                    turmaService.save(turma);
                    System.out.println("Sucesso: Turma cadastrada!");
                }
                case 2 -> {
                    listarTurmasInterno();
                    System.out.print("Nome da Turma para alterar: ");
                    Turma t = turmaService.findByName(scanner.nextLine().toLowerCase().trim());
                    if (t != null) {
                        System.out.print("Novo nome: ");
                        t.setNome(scanner.nextLine());
                        turmaService.update(t);
                        System.out.println("Sucesso: Turma atualizada!");
                    } else {
                        System.out.println("[AVISO] Turma não encontrada.");
                    }
                }
                case 3 -> {
                    listarTurmasInterno();
                    System.out.print("Nome da Turma a excluir: ");
                    Turma t = new Turma();
                    t.setNome(scanner.nextLine());
                    turmaService.delete(t);
                    System.out.println("Sucesso: Turma excluída!");
                }
                case 4 -> listarTurmasInterno();
            }
        } catch (NumberFormatException e) {
            System.err.println("[ERRO] Digite um número válido para a opção.");
        } catch (Exception e) {
            System.err.println("[ERRO NA TURMA] " + e.getMessage());
        }
    }

    // ==========================================
    // 5. AULAS (Gerador de Grade)
    // ==========================================
    private void menuAulas() {
        try {
            listarTurmasInterno();
            System.out.print("Qual o nome da Turma (ignore case)? ");
            String nomeTurma = scanner.nextLine().trim();
            Turma turma = turmaService.findByName(nomeTurma);

            if (turma == null) throw new RuntimeException("Turma não encontrada.");

            Map<Disciplina, Integer> carga = new HashMap<>();
            List<Disciplina> todas = disciplinaService.findAll();

            System.out.println("\n--- Definir Carga Horária Semanal ---");
            for (Disciplina d : todas) {
                System.out.print("Aulas de " + d.getNome() + ": ");
                int qtd = Integer.parseInt(scanner.nextLine());
                if (qtd > 0) {
                    carga.put(d, qtd);
                }
            }

            if (carga.isEmpty()) {
                System.out.println("Nenhuma carga horária definida. Operação cancelada.");
                return;
            }

            System.out.println("A processar a geração da grade...");
            aulaService.gerarGrade(turma, carga);
            // A mensagem de sucesso já está dentro do AulaService.gerarGrade

        } catch (NumberFormatException e) {
            System.err.println("[ERRO] A quantidade de aulas deve ser um número.");
        } catch (Exception e) {
            System.err.println("[ERRO NO GERADOR] " + e.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS AUXILIARES (Listagem e Busca)
    // ==========================================
    private void listarDisciplinasInterno() {
        List<Disciplina> list = disciplinaService.findAll();
        if (list != null && !list.isEmpty()) {
            list.forEach(d -> System.out.println("- " + d.getNome()));
        } else {
            System.out.println("Nenhuma disciplina cadastrada.");
        }
    }

    private void listarProfessoresInterno() {
        List<Professor> list = professorService.findAll();
        if (list != null && !list.isEmpty()) {
            System.out.println(); // Pula linha para organização
            for (Professor p : list) {
                System.out.print("Prof: " + p.getNome() + " | Disciplinas: ");
                if (p.getDisciplinas() != null && !p.getDisciplinas().isEmpty()) {
                    p.getDisciplinas().forEach(d -> System.out.print("[" + d.getNome() + "] "));
                } else {
                    System.out.print("Nenhuma");
                }
                System.out.println();
            }
            System.out.println(); // Pula linha para organização
        } else {
            System.out.println("Nenhum professor cadastrado.");
        }
    }

    private void listarTurnosInterno() {
        List<Turno> list = turnoService.findAll();
        if (list != null && !list.isEmpty()) {
            list.forEach(t -> System.out.println("- " + t.getNomeTurno()));
        } else {
            System.out.println("Nenhum turno cadastrado.");
        }
    }

    private void listarTurmasInterno() {
        List<Turma> list = turmaService.findAll();
        if (list != null && !list.isEmpty()) {
            list.forEach(t -> System.out.println("- " + t.getNome()));
        } else {
            System.out.println("Nenhuma turma cadastrada.");
        }
    }

    private List<Disciplina> selecionarDisciplinas() {
        List<Disciplina> selecionadas = new ArrayList<>();
        listarDisciplinasInterno();
        System.out.println("\nDigite o nome da disciplina exatamente (ignore case) ou '0' para finalizar:");
        while (true) {
            System.out.print("Disciplina: ");
            String nome = scanner.nextLine().toLowerCase().trim();
            if (nome.equals("0")) break;

            Disciplina d = disciplinaService.findByName(nome);
            if (d != null) {
                if (!selecionadas.contains(d)) {
                    selecionadas.add(d);
                    System.out.println("-> [" + d.getNome() + "] adicionada.");
                } else {
                    System.out.println("-> Aviso: Esta disciplina já está na lista.");
                }
            } else {
                System.out.println("-> Disciplina não encontrada.");
            }
        }
        return selecionadas;
    }
}