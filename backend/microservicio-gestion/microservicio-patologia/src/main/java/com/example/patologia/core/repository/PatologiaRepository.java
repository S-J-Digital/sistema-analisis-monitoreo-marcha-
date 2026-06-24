package com.example.patologia.core.repository;

import com.example.patologia.core.entities.Patologia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatologiaRepository extends JpaRepository<Patologia, Long> {
    boolean existsByNombre(String nombre);
    @Query("Select p.id from Patologia p where p.nombre = :nombre")
    Long getIdbyNombre(String nombre);
}
