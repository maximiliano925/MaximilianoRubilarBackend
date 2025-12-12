package com.futplayers.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futplayers.backend.entities.Inventario;
import com.futplayers.backend.entities.Producto; // <--- Importante
import com.futplayers.backend.repositories.InventarioRepository;
import com.futplayers.backend.repositories.ProductoRepository; // <--- Importante

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository; // Necesario para crear stock nuevo

    public List<Inventario> obtenerInventarioPorProducto(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    // LÓGICA MEJORADA: Actualizar o Crear (Upsert)
    public Inventario actualizarStock(Long productoId, String talla, int nuevoStock) {
        // 1. Buscamos si ya existe inventario para esa talla
        Optional<Inventario> inventarioOpt = inventarioRepository.findByProductoIdAndTalla(productoId, talla);
        
        if (inventarioOpt.isPresent()) {
            // CASO A: Ya existe -> Solo actualizamos la cantidad
            Inventario inv = inventarioOpt.get();
            inv.setStock(nuevoStock);
            return inventarioRepository.save(inv);
        } else {
            // CASO B: No existe -> Creamos el registro desde cero
            // (Esto pasa cuando creas un producto nuevo o agregas una talla nueva)
            Producto producto = productoRepository.findById(productoId).orElse(null);
            
            if (producto != null) {
                Inventario nuevoInv = new Inventario();
                nuevoInv.setProducto(producto);
                nuevoInv.setTalla(talla);
                nuevoInv.setStock(nuevoStock);
                return inventarioRepository.save(nuevoInv);
            }
        }
        return null;
    }
}