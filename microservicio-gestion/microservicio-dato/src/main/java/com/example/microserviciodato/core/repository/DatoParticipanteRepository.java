package com.example.microserviciodato.core.repository;

import com.example.microserviciodato.core.entities.DatoParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DatoParticipanteRepository extends JpaRepository<DatoParticipante,Long> {
    boolean existsDatoParticipanteByFechaAndIdparticipante( LocalDate fecha,Long idparticipante);
    List<DatoParticipante> findAllByIdparticipante(Long id);
    @Query("Select dp.id from DatoParticipante dp where dp.idparticipante = :idparticipante and dp.fecha = :fecha")
    Long getIdByIdparticipanteAndFecha(Long idparticipante, LocalDate fecha);
}
