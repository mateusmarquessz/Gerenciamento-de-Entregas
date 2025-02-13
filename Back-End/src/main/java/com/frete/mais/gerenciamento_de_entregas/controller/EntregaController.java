package com.frete.mais.gerenciamento_de_entregas.controller;

import com.frete.mais.gerenciamento_de_entregas.dto.EntregasDTO;
import com.frete.mais.gerenciamento_de_entregas.entities.Entrega;
import com.frete.mais.gerenciamento_de_entregas.service.EntregasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    @Autowired
    private EntregasService entregaService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> criarEntrega(@RequestBody EntregasDTO entregasDTO) {
        try {
            EntregasDTO novaEntrega = entregaService.criarEntrega(entregasDTO);
            return ResponseEntity.ok(novaEntrega);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar entrega: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> atualizarEntrega(@PathVariable Long id, @RequestBody EntregasDTO entregasDTO) {
        try {
            EntregasDTO entregaAtualizada = entregaService.atualizarEntrega(id, entregasDTO);
            return ResponseEntity.ok(entregaAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar entrega: " + e.getMessage());
        }
    }

    @PutMapping("/{entregaId}/atualizar-status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long entregaId, @RequestBody EntregasDTO entregasDTO) {
        try {
            Entrega entregaAtualizada = entregaService.atualizarStatus(entregaId, entregasDTO.getUsuarioID());
            return ResponseEntity.ok(entregaAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar status da entrega: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteEntrega(@PathVariable Long id) {
        try {
            boolean isDeleted = entregaService.deleteEntrega(id);
            if (isDeleted) {
                return ResponseEntity.ok("Entrega deletada com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrega não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar entrega: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> listarEntregasUsuario(@PathVariable Long usuarioId) {
        try {
            List<Entrega> entregas = entregaService.listarEntregasPorUsuario(usuarioId);
            List<EntregasDTO> entregasDTO = entregas.stream().map(EntregasDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok(entregasDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar entregas do usuário: " + e.getMessage());
        }
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> listarTodasEntregas() {
        try {
            List<EntregasDTO> entregas = entregaService.listarTodasEntregas();
            return ResponseEntity.ok(entregas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar todas as entregas: " + e.getMessage());
        }
    }
}
