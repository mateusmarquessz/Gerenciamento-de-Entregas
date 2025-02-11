package com.frete.mais.gerenciamento_de_entregas.entities;

import com.frete.mais.gerenciamento_de_entregas.DTO.UserDTO;
import com.frete.mais.gerenciamento_de_entregas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.frete.mais.gerenciamento_de_entregas.enuns.UserRoles;

@Configuration
public class UsuarioPadrao {

    @Autowired
    private UsuarioService usuarioService;

    @Bean
    public CommandLineRunner loadData(){
        return args -> {
            if(usuarioService.getByEmail("admin@admin.com") == null){
                UserDTO userDTO = new UserDTO();
                userDTO.setNome( "admin" );
                userDTO.setEmail( "admin@admin.com" );
                userDTO.setSenha( "admin" );
                userDTO.setRole(UserRoles.ADMIN);
                usuarioService.registerFirstManager(userDTO);
            }
        };
    }

}
