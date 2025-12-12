package com.futplayers.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futplayers.backend.entities.Comuna;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {
    // Al extender JpaRepository, ya tienes findAll() y findById() gratis
}