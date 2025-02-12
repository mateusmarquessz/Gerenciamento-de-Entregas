package com.frete.mais.gerenciamento_de_entregas.controller;

import com.frete.mais.gerenciamento_de_entregas.dto.EntregasDTO;
import com.frete.mais.gerenciamento_de_entregas.entities.Entrega;
import com.frete.mais.gerenciamento_de_entregas.repository.EntregaRepository;
import com.frete.mais.gerenciamento_de_entregas.repository.UsuarioRepository;
import com.frete.mais.gerenciamento_de_entregas.service.EntregasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    @Autowired
    private EntregasService entregaService;

    // Criar nova entrega (Create do CRUD)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntregasDTO> criarEntrega(@RequestBody EntregasDTO entregasDTO) {
        EntregasDTO novaEntrega = entregaService.criarEntrega(entregasDTO);
        return ResponseEntity.ok(novaEntrega);
    }

    // Método para alterar o status de uma entrega (Admin) (Update do Crud)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode atualizar entregas
    public ResponseEntity<EntregasDTO> atualizarEntrega(@PathVariable Long id, @RequestBody EntregasDTO entregasDTO) {
        EntregasDTO entregaAtualizada = entregaService.atualizarEntrega(id, entregasDTO);
        return ResponseEntity.ok(entregaAtualizada);
    }

    // Método para alterar o status de uma entrega (user) (Update do Crud)
    @PutMapping("/{entregaId}/atualizar-status")
    public Entrega atualizarStatus(@PathVariable Long entregaId, @RequestBody EntregasDTO entregasDTO) {
        return entregaService.atualizarStatus(entregaId, entregasDTO.getUsuarioID());
    }


    //Delete do Crud
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEntrega(@PathVariable Long id) {
        try {
            boolean isDeleted = entregaService.deleteEntrega(id);
            if (isDeleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //Read do Crud
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EntregasDTO>> listarEntregasUsuario(@PathVariable Long usuarioId) {
        try {
            List<Entrega> entregas = entregaService.listarEntregasPorUsuario(usuarioId);
            List<EntregasDTO> entregasDTO = entregas.stream()
                    .map(EntregasDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(entregasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Listar todas as entregas (somente para administradores) read do crud
    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<EntregasDTO>> listarTodasEntregas() {
        List<EntregasDTO> entregas = entregaService.listarTodasEntregas();
        return ResponseEntity.ok(entregas);
    }

}
