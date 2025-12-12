package com.futplayers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // Importante para la seguridad
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futplayers.backend.dto.CompraRequestDTO;
import com.futplayers.backend.entities.Pedido;
import com.futplayers.backend.services.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // 1. CALCULAR CARRITO (Público/Autenticado)
    @PostMapping("/calcular")
    public ResponseEntity<?> calcularTotal(@RequestBody CompraRequestDTO compraRequest) {
        try {
            var resumen = pedidoService.calcularResumen(compraRequest);
            return ResponseEntity.ok(resumen);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. COMPRAR (Autenticado)
    // Usamos @Valid para asegurar que la cantidad no sea negativa
    @PostMapping("/comprar")
    public ResponseEntity<?> realizarCompra(@Valid @RequestBody CompraRequestDTO compraRequest) {
        try {
            Pedido nuevoPedido = pedidoService.procesarCompra(compraRequest);
            return ResponseEntity.ok(nuevoPedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. DASHBOARD ADMIN (Solo el Jefe entra aquí)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/todos")
    public ResponseEntity<List<Pedido>> verTodasLasVentas() {
        return ResponseEntity.ok(pedidoService.listarTodosLosPedidos());
    }
}