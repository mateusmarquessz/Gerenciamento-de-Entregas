package com.frete.mais.gerenciamento_de_entregas.service;

import com.frete.mais.gerenciamento_de_entregas.DTO.UserDTO;
import com.frete.mais.gerenciamento_de_entregas.entities.Usuario;
import com.frete.mais.gerenciamento_de_entregas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // Pega Usuario pelo Email
    public Usuario getByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    // Registra Primeiro Usuario ADMIN
    public void registerFirstManager(UserDTO userDTO) {
        // Verifique se o usuário já existe
        if (getByEmail(userDTO.getEmail()) == null) {
            Usuario user = new Usuario();
            user.setNome(userDTO.getNome());
            user.setEmail(userDTO.getEmail());
            user.setSenha(passwordEncoder.encode(userDTO.getSenha()));
            user.setRole(userDTO.getRole());
            usuarioRepository.save(user);
        } else {
            System.out.println("Usuário com o e-mail " + userDTO.getEmail() + " já existe.");
        }
    }
}
