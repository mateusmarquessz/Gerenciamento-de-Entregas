package com.frete.mais.gerenciamento_de_entregas.service;

import com.frete.mais.gerenciamento_de_entregas.dto.EntregasDTO;
import com.frete.mais.gerenciamento_de_entregas.entities.Entrega;
import com.frete.mais.gerenciamento_de_entregas.entities.Usuario;
import com.frete.mais.gerenciamento_de_entregas.enuns.StatusEntrega;
import com.frete.mais.gerenciamento_de_entregas.enuns.UserRoles;
import com.frete.mais.gerenciamento_de_entregas.repository.EntregaRepository;
import com.frete.mais.gerenciamento_de_entregas.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EntregasService {

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Criar nova entrega
    public EntregasDTO criarEntrega(EntregasDTO entregasDTO) {
        Usuario usuario = usuarioRepository.findById(entregasDTO.getUsuarioID())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Entrega entrega = new Entrega();
        entrega.setDescricao(entregasDTO.getDescricao());
        entrega.setStatus(StatusEntrega.PENDENTE);
        entrega.setUsuario(usuario);

        entrega = entregaRepository.save(entrega);

        return new EntregasDTO(entrega);
    }


    // Atualizar o status da entrega (somente para 'Entregue' para o usuário comum)
    public Entrega atualizarStatus(Long entregaId, StatusEntrega novoStatus, Long usuarioId) {
        Entrega entrega = entregaRepository.findById(entregaId)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));

        if (!entrega.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Você não tem permissão para alterar esta entrega");
        }

        // Verificar se o usuário comum não tenta definir status inválidos
        if (novoStatus == StatusEntrega.PENDENTE || novoStatus == StatusEntrega.EM_TRANSITO) {
            throw new RuntimeException("Você não pode alterar o status para esse valor");
        }

        entrega.setStatus(novoStatus);
        return entregaRepository.save(entrega);
    }

    @Transactional
    public Entrega alterarStatus(Long entregaId, String status, Long usuarioId) {
        // Verifica se a entrega existe
        Entrega entrega = entregaRepository.findById(entregaId)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));

        // Verifica se o status atual é "PENDENTE" e o novo status é "EM_TRANSITO"
        if (entrega.getStatus() == StatusEntrega.PENDENTE && status.equals(StatusEntrega.EM_TRANSITO.name())) {
            // Verifica se o usuário é admin
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            if (usuario.getRole() == UserRoles.ADMIN) {
                entrega.setStatus(StatusEntrega.EM_TRANSITO); // Atualiza o status
                return entregaRepository.save(entrega); // Salva a entrega com o novo status
            } else {
                throw new RuntimeException("Usuário não tem permissão para alterar o status.");
            }
        } else {
            throw new RuntimeException("Status não pode ser alterado.");
        }
    }


    // Excluir entrega (somente para administradores)
    public boolean deleteEntrega(Long id) {
        Optional<Entrega> entrega = entregaRepository.findById(id);
        if (entrega.isPresent()) {
            entregaRepository.delete(entrega.get());
            return true;
        }
        return false;
    }


    // Listar entregas de um usuário específico (usuário comum)
    public List<Entrega> listarEntregasPorUsuario(Long usuarioId) {
        return entregaRepository.findByUsuarioId(usuarioId);
    }


    // Listar todas as entregas (apenas para administradores)
    public List<EntregasDTO> listarTodasEntregas() {
        List<Entrega> entregas = entregaRepository.findAll();
        return entregas.stream()
                .map(EntregasDTO::new)
                .collect(Collectors.toList());
    }
}
