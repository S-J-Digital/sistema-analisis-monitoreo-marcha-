package com.example.redneuronal.core.repository;

import com.example.redneuronal.core.entities.RedNeuronalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RedNeuronalRepository extends JpaRepository<RedNeuronalEntity, Long> {
    Optional<RedNeuronalEntity> findTopByOrderByFechaDesc();
}
