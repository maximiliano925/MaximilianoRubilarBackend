package com.futplayers.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futplayers.backend.dto.LoginRequest;
import com.futplayers.backend.entities.Usuario;
import com.futplayers.backend.security.JwtUtils;
import com.futplayers.backend.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UsuarioService usuarioService;
    @Autowired private JwtUtils jwtUtils;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Autenticar con Spring Security (Esto valida usuario y contraseña encriptada)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Si pasa, generamos el token
        if (authentication.isAuthenticated()) {
            String token = jwtUtils.generateToken(request.getEmail());
            
            // Buscamos los datos del usuario para devolverlos también
            Usuario usuario = usuarioService.buscarPorEmail(request.getEmail()); // Asegúrate de tener este método en UsuarioService que retorne el objeto Usuario

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("usuario", usuario);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}