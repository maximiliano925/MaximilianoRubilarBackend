package com.futplayers.backend.controller;

import com.futplayers.backend.entities.Comuna;
import com.futplayers.backend.repositories.ComunaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comunas")
@CrossOrigin(origins = "*")
public class ComunaController {

    @Autowired
    private ComunaRepository comunaRepository;

    // React llamará a esto para llenar el <select>
    @GetMapping
    public List<Comuna> listarTodas() {
        return comunaRepository.findAll();
    }
}