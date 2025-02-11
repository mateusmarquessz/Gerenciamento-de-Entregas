package com.frete.mais.gerenciamento_de_entregas.DTO;


import com.frete.mais.gerenciamento_de_entregas.enuns.UserRoles;

public record ResponseDTO (String nome, String token, long userId, UserRoles role){
}
