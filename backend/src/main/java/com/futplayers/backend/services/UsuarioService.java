package com.futplayers.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.futplayers.backend.entities.Usuario;
import com.futplayers.backend.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    // 1. REGISTRO (Encripta clave)
    public Usuario registrarUsuario(Usuario usuario) {
        String passHasheada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passHasheada);
        
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("CLIENTE");
        }
        return usuarioRepository.save(usuario);
    }

    // 2. LOGIN (Verifica clave)
    public Usuario login(String email, String passwordRaw) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario u = usuarioOpt.get();
            if (passwordEncoder.matches(passwordRaw, u.getPassword())) {
                return u;
            }
        }
        return null;
    }

    // 3. BUSCAR POR ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // 4. BUSCAR POR EMAIL (Auxiliar)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    // 5. ACTUALIZAR PERFIL (Dirección y Comuna)
    public Usuario actualizarPerfil(Long id, Usuario datosNuevos) {
        Usuario usuarioActual = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (datosNuevos.getDireccionCalle() != null) {
            usuarioActual.setDireccionCalle(datosNuevos.getDireccionCalle());
        }
        if (datosNuevos.getComuna() != null) {
            usuarioActual.setComuna(datosNuevos.getComuna());
        }
        // Si quieres permitir cambio de nombre:
        if (datosNuevos.getNombre() != null) {
            usuarioActual.setNombre(datosNuevos.getNombre());
        }

        return usuarioRepository.save(usuarioActual);
    }

    // 6. ELIMINAR USUARIO
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}