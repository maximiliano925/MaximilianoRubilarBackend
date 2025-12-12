package com.futplayers.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futplayers.backend.entities.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findByNombre(String nombre);
}
