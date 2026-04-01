package org.agenda.controller;

import java.time.format.DateTimeFormatter;
import org.agenda.model.Contato;
import org.agenda.service.AgendaService;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AgendaController {

    public void iniciar() {

        Scanner sc = new Scanner(System.in);
        AgendaService agenda = new AgendaService();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("Bem-vindo a sua agenda!");
        System.out.println("!!!! Os dados sao mantidos apenas durante a execucao !!!!");

        int opcao;

        do {
            System.out.println("\n=================================");
            System.out.println("             AGENDA");
            System.out.println("=================================");
            System.out.println("1 - Adicionar contato");
            System.out.println("2 - Listar contatos");
            System.out.println("3 - Remover contato");
            System.out.println("4 - Editar contato");
            System.out.println("5 - Buscar contatos");
            System.out.println("6 - Aniversariantes do mes");
            System.out.println("0 - Sair");
            System.out.println("=================================");

            while (true) {
                try {
                    System.out.print("Escolha uma opcao: ");
                    opcao = Integer.parseInt(sc.nextLine());
                    break;
                } catch (Exception e) {
                    System.out.println("Número invalido!");
                }
            }

            switch (opcao) {

                case 1 -> {
                    System.out.println("\n--- NOVO CONTATO ---");

                    String nome;
                    while (true) {
                        System.out.print("Nome: ");
                        nome = sc.nextLine();
                        if (!nome.trim().isEmpty()) break;
                        System.out.println("Nome nao pode ser vazio!");
                    }

                    String telefone;
                    while (true) {
                        System.out.print("Telefone: ");
                        telefone = sc.nextLine();

                        if (!telefone.trim().isEmpty()) break;

                        System.out.println("Telefone invalido!");
                    }

                    String email;
                    while (true) {
                        System.out.print("Email: ");
                        email = sc.nextLine();
                        if (email.contains("@") && email.contains(".")) break;
                        System.out.println("Email invalido!");
                    }

                    LocalDate data;
                    while (true) {
                        try {
                            System.out.print("Data nascimento (dd/MM/yyyy): ");
                            data = LocalDate.parse(sc.nextLine(), formatter);
                            break;
                        } catch (Exception e) {
                            System.out.println("Data invalida!");
                        }
                    }

                    if (agenda.adicionar(nome, telefone, email, data)) {
                        System.out.println("Contato adicionado!");
                    } else {
                        System.out.println("❌ Ja existe um contato com esse email!");
                    }
                }

                case 2 -> {
                    System.out.println("\n--------- CONTATOS ---------");

                    List<Contato> lista = agenda.listar();

                    if (lista.isEmpty()) {
                        System.out.println("Nenhum contato cadastrado.");
                    } else {
                        lista.forEach(c ->
                                System.out.println(
                                        "ID: " + c.getId() +
                                                " | Nome: " + c.getNome() +
                                                " | Tel: " + c.getTelefone() +
                                                " | Email: " + c.getEmail() +
                                                " | Nasc: " + c.getDataNascimento().format(formatter)
                                )
                        );
                    }

                    System.out.println("----------------------------");
                }

                case 3 -> {
                    System.out.println("\n--- REMOVER CONTATO ---");

                    int id;
                    while (true) {
                        try {
                            System.out.print("ID: ");
                            id = Integer.parseInt(sc.nextLine());
                            break;
                        } catch (Exception e) {
                            System.out.println("ID invalido!");
                        }
                    }

                    Contato c = agenda.buscarPorId(id);

                    if (c == null) {
                        System.out.println("Contato nao encontrado.");
                        break;
                    }

                    System.out.println("Contato:");
                    System.out.println("Nome: " + c.getNome() + " | Email: " + c.getEmail());

                    System.out.print("Deseja realmente remover? (s/n): ");
                    String conf = sc.nextLine();

                    if (conf.equalsIgnoreCase("s")) {
                        agenda.remover(id);
                        System.out.println("Removido!");
                    } else {
                        System.out.println("Operaçao cancelada.");
                    }
                }

                case 4 -> {
                    System.out.println("\n--- EDITAR CONTATO ---");

                    int id;
                    while (true) {
                        try {
                            System.out.print("ID: ");
                            id = Integer.parseInt(sc.nextLine());
                            break;
                        } catch (Exception e) {
                            System.out.println("ID invalido!");
                        }
                    }

                    Contato c = agenda.buscarPorId(id);

                    if (c == null) {
                        System.out.println("Contato nao encontrado.");
                        break;
                    }

                    int opcaoEdit;

                    do {
                        System.out.println("\nEditando: " + c.getNome());
                        System.out.println("1 - Nome");
                        System.out.println("2 - Telefone");
                        System.out.println("3 - Email");
                        System.out.println("4 - Data de nascimento");
                        System.out.println("0 - Voltar");

                        while (true) {
                            try {
                                System.out.print("Escolha: ");
                                opcaoEdit = Integer.parseInt(sc.nextLine());
                                break;
                            } catch (Exception e) {
                                System.out.println("Número invalido!");
                            }
                        }

                        switch (opcaoEdit) {
                            case 1 -> {
                                System.out.print("Novo nome: ");
                                String novo = sc.nextLine();

                                System.out.print("Salvar? (s/n): ");
                                if (sc.nextLine().equalsIgnoreCase("s")) {
                                    c.setNome(novo);
                                    System.out.println("Atualizado!");
                                }
                            }

                            case 2 -> {
                                System.out.print("Novo telefone: ");
                                String novo = sc.nextLine();

                                System.out.print("Salvar? (s/n): ");
                                if (sc.nextLine().equalsIgnoreCase("s")) {
                                    c.setTelefone(novo);
                                    System.out.println("Atualizado!");
                                }
                            }

                            case 3 -> {
                                System.out.print("Novo email: ");
                                String novo = sc.nextLine();

                                if (!novo.contains("@") || !novo.contains(".")) {
                                    System.out.println("Email invalido!");
                                    break;
                                }

                                if (agenda.emailJaExiste(novo, c.getId())) {
                                    System.out.println("Email ja em uso!");
                                    break;
                                }

                                System.out.print("Salvar? (s/n): ");
                                if (sc.nextLine().equalsIgnoreCase("s")) {
                                    c.setEmail(novo);
                                    System.out.println("Atualizado!");
                                }
                            }

                            case 4 -> {
                                LocalDate nova;
                                while (true) {
                                    try {
                                        System.out.print("Nova data (dd/MM/yyyy): ");
                                        nova = LocalDate.parse(sc.nextLine(), formatter);
                                        break;
                                    } catch (Exception e) {
                                        System.out.println("Data invalida!");
                                    }
                                }

                                System.out.print("Salvar? (s/n): ");
                                if (sc.nextLine().equalsIgnoreCase("s")) {
                                    c.setDataNascimento(nova);
                                    System.out.println("Atualizado!");
                                }
                            }
                        }

                    } while (opcaoEdit != 0);
                }

                case 5 -> {
                    System.out.println("\n--- BUSCAR CONTATO ---");

                    int tipo;
                    while (true) {
                        try {
                            System.out.println("1 - Nome");
                            System.out.println("2 - Telefone");
                            System.out.println("3 - Email");
                            System.out.print("Escolha: ");
                            tipo = Integer.parseInt(sc.nextLine());

                            if (tipo >= 1 && tipo <= 3) break;
                            System.out.println("opcao invalida!");
                        } catch (Exception e) {
                            System.out.println("Número invalido!");
                        }
                    }

                    List<Contato> resultados;

                    if (tipo == 1) {
                        System.out.print("Nome: ");
                        resultados = agenda.pesquisarPorPrefixo(sc.nextLine());
                    } else if (tipo == 2) {
                        System.out.print("Telefone: ");
                        resultados = agenda.pesquisarPorTelefone(sc.nextLine());
                    } else {
                        System.out.print("Email: ");
                        resultados = agenda.pesquisarPorEmail(sc.nextLine());
                    }

                    if (resultados.isEmpty()) {
                        System.out.println("Contato nao localizado.");
                    } else {
                        resultados.forEach(c ->
                                System.out.println(
                                        "ID: " + c.getId() +
                                                " | Nome: " + c.getNome() +
                                                " | Tel: " + c.getTelefone() +
                                                " | Email: " + c.getEmail()
                                )
                        );
                    }
                }

                case 6 -> {
                    System.out.println("\n--- ANIVERSARIANTES ---");

                    int mes;
                    while (true) {
                        try {
                            System.out.print("mes (1-12): ");
                            mes = Integer.parseInt(sc.nextLine());

                            if (mes >= 1 && mes <= 12) break;
                            System.out.println("mes invalido!");
                        } catch (Exception e) {
                            System.out.println("Número invalido!");
                        }
                    }

                    List<Contato> lista = agenda.aniversariantesDoMes(mes);

                    if (lista.isEmpty()) {
                        System.out.println("Nenhum aniversariante neste mes.");
                    } else {
                        lista.forEach(c ->
                                System.out.println(
                                        "ID: " + c.getId() +
                                                " | Nome: " + c.getNome() +
                                                " | Tel: " + c.getTelefone() +
                                                " | Email: " + c.getEmail() +
                                                " | Nasc: " + c.getDataNascimento().format(formatter)
                                )
                        );
                    }
                }
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("opcao invalida!");
            }

        } while (opcao != 0);

        sc.close();
    }
}