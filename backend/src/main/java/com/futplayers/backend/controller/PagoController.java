package com.futplayers.backend.controller;

import com.futplayers.backend.dto.PagoRequest; // <--- Importaremos el DTO nuevo
import com.futplayers.backend.entities.Pedido;
import com.futplayers.backend.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired private PedidoRepository pedidoRepository;

    // 1. INICIAR PAGO
    @PostMapping("/webpay/iniciar")
    public ResponseEntity<?> iniciarPago(@RequestBody PagoRequest request) {
        // Retorna el link a la pantalla falsa de React
        return ResponseEntity.ok("http://localhost:5173/pago-simulado?orden=" + request.getPedidoId());
    }

    // 2. CONFIRMAR PAGO
    @PostMapping("/webpay/confirmar")
    public ResponseEntity<?> confirmarPago(@RequestBody PagoRequest request) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(request.getPedidoId());
        
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstado("PAGADO");
            pedidoRepository.save(pedido);
            return ResponseEntity.ok("Pago Exitoso. Pedido #" + pedido.getId() + " confirmado.");
        }
        return ResponseEntity.badRequest().body("Pedido no encontrado");
    }
}