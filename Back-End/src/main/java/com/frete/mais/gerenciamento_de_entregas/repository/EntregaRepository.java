package com.frete.mais.gerenciamento_de_entregas.repository;

import com.frete.mais.gerenciamento_de_entregas.entities.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
}
