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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    @Transactional
    public EntregasDTO criarEntrega(EntregasDTO entregasDTO) {
        Usuario usuario = usuarioRepository.findById(entregasDTO.getUsuarioID())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Entrega entrega = new Entrega();
        entrega.setDescricao(entregasDTO.getDescricao());
        entrega.setDataCriacao(LocalDate.now());
        entrega.setDataEntrega(LocalDate.now().plusDays(4));
        entrega.setStatus(StatusEntrega.PENDENTE);
        entrega.setUsuario(usuario);

        // Salva no banco
        entrega = entregaRepository.save(entrega);

        return new EntregasDTO(entrega);
    }


    // Atualizar o status da entrega (ADMIN)
    public EntregasDTO atualizarEntrega(Long id, EntregasDTO entregasDTO) {
        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));

        entrega.setDescricao(entregasDTO.getDescricao());
        entrega.setDataEntrega(LocalDate.now());
        entrega.setStatus(entregasDTO.getStatus());
        entrega = entregaRepository.save(entrega);
        return new EntregasDTO(entrega);
    }


    //Atualiza o status da entrega (User)
    @Transactional
    public Entrega atualizarStatus(Long entregaId, Long usuarioId) {
        Optional<Entrega> entregaOptional = entregaRepository.findById(entregaId);

        if (entregaOptional.isEmpty()) {
            throw new RuntimeException("Entrega não encontrada");
        }

        Entrega entrega = entregaOptional.get();
        if (!entrega.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Usuário não autorizado a atualizar esta entrega");
        }
        if (entrega.getStatus() == StatusEntrega.EM_TRANSITO) {
            entrega.setStatus(StatusEntrega.ENTREGUE);
            return entregaRepository.save(entrega);
        } else {
            throw new RuntimeException("Status não pode ser alterado. A entrega já está em outro status.");
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
