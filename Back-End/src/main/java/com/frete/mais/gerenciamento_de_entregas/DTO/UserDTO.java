package com.frete.mais.gerenciamento_de_entregas.DTO;

import com.frete.mais.gerenciamento_de_entregas.enuns.UserRoles;

public class UserDTO {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private UserRoles role;

    //Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}
