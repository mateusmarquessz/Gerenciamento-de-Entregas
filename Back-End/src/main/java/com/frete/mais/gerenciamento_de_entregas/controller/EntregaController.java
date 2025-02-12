package com.frete.mais.gerenciamento_de_entregas.controller;

import com.frete.mais.gerenciamento_de_entregas.dto.EntregasDTO;
import com.frete.mais.gerenciamento_de_entregas.entities.Entrega;
import com.frete.mais.gerenciamento_de_entregas.entities.Usuario;
import com.frete.mais.gerenciamento_de_entregas.enuns.StatusEntrega;
import com.frete.mais.gerenciamento_de_entregas.repository.EntregaRepository;
import com.frete.mais.gerenciamento_de_entregas.repository.UsuarioRepository;
import com.frete.mais.gerenciamento_de_entregas.service.EntregasService;
import com.frete.mais.gerenciamento_de_entregas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entregas")
@CrossOrigin(origins = "*")
public class EntregaController {

    @Autowired
    private EntregasService entregaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntregaRepository entregaRepository;

    // Criar nova entrega
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntregasDTO> criarEntrega(@RequestBody EntregasDTO entregasDTO) {
        try {
            // Verifica se o usuário existe
            Usuario usuario = usuarioRepository.findById(entregasDTO.getUsuarioID())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Cria a entrega com o status adequado
            Entrega entrega = new Entrega();
            entrega.setDescricao(entregasDTO.getDescricao());
            entrega.setStatus(entregasDTO.getStatus()); // Status vindo do DTO
            entrega.setUsuario(usuario);

            // Salva a entrega no banco de dados
            entrega = entregaRepository.save(entrega);

            // Retorna o DTO da entrega criada
            return ResponseEntity.status(HttpStatus.CREATED).body(new EntregasDTO(entrega));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }






    // Método para alterar o status de uma entrega
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Entrega> alterarStatusEntrega(@PathVariable Long id,
                                                        @RequestParam String status,
                                                        @RequestParam Long usuarioId) {
        try {
            // Chama o serviço para alterar o status
            Entrega updatedEntrega = entregaService.alterarStatus(id, status, usuarioId);
            return ResponseEntity.ok(updatedEntrega);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    // Atualizar status da entrega(Usuario)
    @PutMapping("/{id}/confirmacaoStatus")
    public ResponseEntity<EntregasDTO> atualizarStatus(@PathVariable Long id, @RequestBody StatusEntrega novoStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long usuarioId = Long.valueOf(authentication.getName());

        try {
            Entrega entregaAtualizada = entregaService.atualizarStatus(id, novoStatus, usuarioId);
            return ResponseEntity.ok(new EntregasDTO(entregaAtualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

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


    // Listar todas as entregas (somente para administradores)
    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<EntregasDTO>> listarTodasEntregas() {
        List<EntregasDTO> entregas = entregaService.listarTodasEntregas();
        return ResponseEntity.ok(entregas);
    }

}
