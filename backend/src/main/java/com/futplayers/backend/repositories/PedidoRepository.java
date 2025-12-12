package com.futplayers.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futplayers.backend.entities.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Método mágico de JPA: Busca por ID de usuario y ordena por fecha (lo más nuevo primero)
    List<Pedido> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}