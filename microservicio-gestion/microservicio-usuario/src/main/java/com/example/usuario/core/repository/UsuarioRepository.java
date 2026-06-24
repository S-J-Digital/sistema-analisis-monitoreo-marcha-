package com.example.usuario.core.repository;

import com.example.usuario.core.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("Select u.id from Usuario u where u.nombre = :nombre")
    Long findIdByNombre(String nombre);
    Optional<Usuario> findByNombre(String nombre);
    boolean existsByNombre(String name);
    Optional<Usuario> findByUsernameEqualsIgnoreCase(String username);

    Optional<Usuario> findByUsernameEquals(String username);

    boolean existsByUsernameEquals(String username);
}
