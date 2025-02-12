package com.frete.mais.gerenciamento_de_entregas.dto;

import com.frete.mais.gerenciamento_de_entregas.entities.Entrega;
import com.frete.mais.gerenciamento_de_entregas.enuns.StatusEntrega;


public class EntregasDTO {

    private Long id;  // O ID pode ser nulo quando a entrega for criada
    private String descricao;
    private StatusEntrega status;
    private Long usuarioID;
    private String usuarioNome;

    public EntregasDTO(Entrega entrega) {
        this.id = entrega.getId();
        this.descricao = entrega.getDescricao();
        this.status = entrega.getStatus();
        this.usuarioID = entrega.getUsuario().getId();
        this.usuarioNome = entrega.getUsuario().getNome();
    }

    public EntregasDTO(String descricao, Long usuarioID, StatusEntrega status) {
        this.descricao = descricao;
        this.usuarioID = usuarioID;
        this.status = status;
    }


    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusEntrega getStatus() { return status; }
    public void setStatus(StatusEntrega status) { this.status = status; }

    public Long getUsuarioID() { return usuarioID; }
    public void setUsuarioID(Long usuarioID) { this.usuarioID = usuarioID; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
}
