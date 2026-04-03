package org.agenda.service;

import org.agenda.model.Contato;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AgendaService {

    private List<Contato> contatos = new ArrayList<>();
    private int contadorId = 1;

    public boolean adicionar(String nome, String telefone, String email, LocalDate dataNascimento) {

        for (Contato c : contatos) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }

        contatos.add(new Contato(contadorId++, nome, telefone, email, dataNascimento));
        return true;
    }

    public List<Contato> listar() {
        return contatos.stream()
                .sorted(Comparator.comparing(Contato::getNome))
                .collect(Collectors.toList());
    }

    public boolean remover(int id) {
        return contatos.removeIf(c -> c.getId() == id);
    }

    public boolean editar(int id, String nome, String telefone, String email, LocalDate dataNascimento) {
        for (Contato c : contatos) {
            if (c.getId() == id) {
                c.setNome(nome);
                c.setTelefone(telefone);
                c.setEmail(email);
                c.setDataNascimento(dataNascimento);
                return true;
            }
        }
        return false;
    }

    public List<Contato> pesquisarPorPrefixo(String prefixo) {
        return contatos.stream()
                .filter(c -> c.getNome().toLowerCase().startsWith(prefixo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Contato> pesquisarPorTelefone(String telefone) {
        return contatos.stream()
                .filter(c -> c.getTelefone().contains(telefone))
                .collect(Collectors.toList());
    }

    public List<Contato> pesquisarPorEmail(String email) {
        return contatos.stream()
                .filter(c -> c.getEmail().toLowerCase().contains(email.toLowerCase()))
                .collect(Collectors.toList());
    }

    public boolean emailJaExiste(String email, int idIgnorar) {
        for (Contato c : contatos) {
            if (c.getEmail().equalsIgnoreCase(email) && c.getId() != idIgnorar) {
                return true;
            }
        }
        return false;
    }

    public List<Contato> aniversariantesDoMes(int mes) {
        return contatos.stream()
                .filter(c -> c.getDataNascimento().getMonthValue() == mes)
                .sorted(Comparator.comparing(Contato::getDataNascimento))
                .collect(Collectors.toList());
    }

    public Contato buscarPorId(int id) {
        for (Contato c : contatos) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }
}

