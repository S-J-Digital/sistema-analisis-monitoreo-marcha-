package com.example.usuario.core.repository;

import com.example.usuario.core.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    @Override
    Optional<Rol> findById(Long aLong);

    boolean existsByNombreRolEquals(String nombreRol);

    Optional<Rol> findByNombreRolEquals(String nombreRol);
}
