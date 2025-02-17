package com.frete.mais.gerenciamento_de_entregas.service;

import com.frete.mais.gerenciamento_de_entregas.dto.UserDTO;
import com.frete.mais.gerenciamento_de_entregas.entities.Usuario;
import com.frete.mais.gerenciamento_de_entregas.enuns.UserRoles;
import com.frete.mais.gerenciamento_de_entregas.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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

    //Pega dados do Usuario
    public Usuario getUserById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }


    //Atualiza dados do Usuario
    public Usuario AtualizaUsuario(Long id, Usuario updatedUser) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioAutenticado = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        if (usuarioAutenticado.getRole() == UserRoles.ADMIN || usuarioAutenticado.getId().equals(id)) {
            return usuarioRepository.findById(id)
                    .map(usuario -> {
                        usuario.setNome(updatedUser.getNome());
                        usuario.setEmail(updatedUser.getEmail());

                        if (updatedUser.getSenha() != null && !updatedUser.getSenha().isEmpty()) {
                            usuario.setSenha(passwordEncoder.encode(updatedUser.getSenha()));
                        }

                        return usuarioRepository.save(usuario);
                    })
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        } else {
            throw new RuntimeException("Você não tem permissão para atualizar dados de outros usuários!");
        }
    }


    //Deleta Usuarios
    @Secured("ROLE_ADMIN")
    public boolean deleteUserById(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //Lista todos os usuarios
    @Secured("ROLE_ADMIN")
    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }
}
