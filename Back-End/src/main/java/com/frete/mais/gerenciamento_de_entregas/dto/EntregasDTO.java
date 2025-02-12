package com.frete.mais.gerenciamento_de_entregas.dto;

import com.frete.mais.gerenciamento_de_entregas.entities.Entrega;
import com.frete.mais.gerenciamento_de_entregas.enuns.StatusEntrega;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor  // Construtor vazio necessário para a desserialização
@AllArgsConstructor // Construtor completo para inicialização rápida
public class EntregasDTO {

    private Long id;
    private String descricao;
    private StatusEntrega status;
    private Long usuarioID;
    private String usuarioNome;

    // Construtor para conversão da entidade Entrega -> DTO
    public EntregasDTO(Entrega entrega) {
        this.id = entrega.getId();
        this.descricao = entrega.getDescricao();
        this.status = entrega.getStatus();
        this.usuarioID = entrega.getUsuario().getId();
        this.usuarioNome = entrega.getUsuario().getNome();
    }

    // Construtor para criar uma entrega com status PENDENTE por padrão
    public EntregasDTO(String descricao, Long usuarioID) {
        this.descricao = descricao;
        this.usuarioID = usuarioID;
        this.status = StatusEntrega.PENDENTE; // Sempre começa como PENDENTE
    }
}


