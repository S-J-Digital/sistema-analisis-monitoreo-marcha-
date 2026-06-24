package com.example.microserviciodato.core.service;


import com.example.microserviciodato.core.dto.DatoIdDto;
import com.example.microserviciodato.core.dto.DatoParticipanteDto;
import com.example.microserviciodato.core.entities.DatoParticipante;
import com.example.microserviciodato.core.exception.SearchException;
import com.example.microserviciodato.core.http.response.PatologiaResponse;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface DatoParticipanteService {
    void insertarDato(DatoParticipante datoParticipante);
    void modificarDato(DatoParticipanteDto datoParticipante, Long id);
    void eliminarDato(Long id);
    Long obtenerIdbyparticipanteAndFecha(Long idParticipante, String fecha);
    List<PatologiaResponse> obtenerDatosXParticipante(Long idParticipante);
    Optional<DatoParticipante> obtenerDatoXId(Long id) throws SearchException;
    boolean existeDato(Long id) throws SearchException;
}
