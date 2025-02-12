package com.frete.mais.gerenciamento_de_entregas.dto;


import com.frete.mais.gerenciamento_de_entregas.enuns.UserRoles;

public record ResponseDTO (String nome, String token, long userId, UserRoles role){
}
