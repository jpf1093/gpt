package org.agenda.model;

import java.time.LocalDate;

public class Contato {
    private int id;
    private String nome;
    private String telefone;
    private String email;
    private LocalDate dataNascimento;

    public Contato(int id, String nome, String telefone, String email, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public LocalDate getDataNascimento() { return dataNascimento; }

    public void setNome(String nome) { this.nome = nome; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEmail(String email) { this.email = email; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}