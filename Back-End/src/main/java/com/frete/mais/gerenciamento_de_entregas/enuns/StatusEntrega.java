package com.frete.mais.gerenciamento_de_entregas.enuns;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusEntrega {
    PENDENTE,
    EM_TRANSITO,
    ENTREGUE;

    @JsonCreator
    public static StatusEntrega fromString(String string) {
        return StatusEntrega.valueOf(string.toUpperCase());
    }
}
