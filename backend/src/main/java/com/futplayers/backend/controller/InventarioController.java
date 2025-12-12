package com.futplayers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futplayers.backend.entities.Inventario;
import com.futplayers.backend.services.InventarioService;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // PÚBLICO
    @GetMapping("/{productoId}")
    public List<Inventario> verStockProducto(@PathVariable Long productoId) {
        return inventarioService.obtenerInventarioPorProducto(productoId);
    }

    // SOLO ADMIN
    @PreAuthorize("hasRole('ADMIN')") 
    @PostMapping("/actualizar")
    public ResponseEntity<?> actualizarStock(@RequestBody StockRequest request) {
        Inventario inventario = inventarioService.actualizarStock(request.productoId, request.talla, request.nuevoStock);
        
        if (inventario != null) {
            return ResponseEntity.ok(inventario);
        }
        return ResponseEntity.badRequest().body("Error: Producto o Talla no encontrados");
    }

    public static class StockRequest {
        public Long productoId;
        public String talla;
        public int nuevoStock;
    }
}