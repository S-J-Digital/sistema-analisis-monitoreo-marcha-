package com.example.patologia.core.repository;

import com.example.patologia.core.entities.DatoPatologia;
import com.example.patologia.core.entities.Patologia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DatoPatologisRepository extends JpaRepository<DatoPatologia, Long> {
    void deleteByIddatoAndFechaevaluacion(Long iddato, LocalDate fecha);
    boolean existsByIddatoAndIdpatologiaAndFechaevaluacion(Long iddato, Long idpatologia, LocalDate fecha);
    @Query(
            "SELECT p FROM Patologia p JOIN DatoPatologia dp ON p.id = dp.idpatologia " +
                    "WHERE dp.iddato = :id_dato")
    List<Patologia> findAllByIddato(Long id_dato);
}
