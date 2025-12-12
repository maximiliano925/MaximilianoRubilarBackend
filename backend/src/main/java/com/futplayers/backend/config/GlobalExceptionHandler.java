package com.futplayers.backend.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Esta anotación indica que esta clase maneja excepciones de todos los controladores
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja errores de Lógica de Negocio (RUNTIME EXCEPTION)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> manejarErrorLogico(RuntimeException ex) {
        Map<String, String> respuesta = new HashMap<>();
        String mensajeCompleto = ex.getMessage(); 

        // 1. Lógica para categorizar el error
        if (mensajeCompleto.contains("Stock")) {
            respuesta.put("error", "Stock insuficiente");
        } else if (mensajeCompleto.contains("encontrado") || mensajeCompleto.contains("válida")) {
            respuesta.put("error", "Dato no válido o no encontrado");
        } else {
            respuesta.put("error", "Ingrese correctamente sus datos");
        }

        // 2. Línea que contiene el mensaje detallado de la excepción
        respuesta.put("mensaje", mensajeCompleto); 
        
        // Devolvemos status 400 (Bad Request) que indica un error en la petición del cliente
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}