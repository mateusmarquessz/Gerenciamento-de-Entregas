package com.frete.mais.gerenciamento_de_entregas.controller;

import com.frete.mais.gerenciamento_de_entregas.dto.LoginRequestDTO;
import com.frete.mais.gerenciamento_de_entregas.dto.RegisterRequestDTO;
import com.frete.mais.gerenciamento_de_entregas.dto.ResponseDTO;
import com.frete.mais.gerenciamento_de_entregas.config.TokenService;
import com.frete.mais.gerenciamento_de_entregas.entities.Usuario;
import com.frete.mais.gerenciamento_de_entregas.enuns.UserRoles;
import com.frete.mais.gerenciamento_de_entregas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body) {
        try {
            Usuario user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
            if (passwordEncoder.matches(body.senha(), user.getSenha())) {
                String token = tokenService.generateToken(user);
                return ResponseEntity.ok(new ResponseDTO(user.getNome(), token, user.getId(), user.getRole()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao realizar o login: " + e.getMessage());
        }
    }

    //Create do Crud Usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO body) {
        try {
            Optional<Usuario> user = this.repository.findByEmail(body.email());

            if (user.isEmpty()) {
                Usuario newUser = new Usuario();
                newUser.setSenha(passwordEncoder.encode(body.senha()));
                newUser.setEmail(body.email());
                newUser.setNome(body.nome());
                newUser.setRole(UserRoles.USER);
                this.repository.save(newUser);
                String token = this.tokenService.generateToken(newUser);
                return ResponseEntity.ok(new ResponseDTO(newUser.getNome(), token, newUser.getId(), newUser.getRole()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao registrar usuário: " + e.getMessage());
        }
    }


    //Create do Crud Usuario com Role_Admin
    @PostMapping("/register-admin")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequestDTO body) {
        try {
            Optional<Usuario> user = this.repository.findByEmail(body.email());

            if (user.isEmpty()) {
                Usuario newUser = new Usuario();
                newUser.setSenha(passwordEncoder.encode(body.senha()));
                newUser.setEmail(body.email());
                newUser.setNome(body.nome());
                newUser.setRole(UserRoles.ADMIN);
                this.repository.save(newUser);
                String token = this.tokenService.generateToken(newUser);
                return ResponseEntity.ok(new ResponseDTO(newUser.getNome(), token, newUser.getId(), newUser.getRole()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Administrador já existe");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao registrar administrador: " + e.getMessage());
        }
    }
}