package com.futplayers.backend.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futplayers.backend.entities.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByProductoIdAndTalla(Long productoId, String talla);
    List<Inventario> findByProductoId(Long productoId);
}