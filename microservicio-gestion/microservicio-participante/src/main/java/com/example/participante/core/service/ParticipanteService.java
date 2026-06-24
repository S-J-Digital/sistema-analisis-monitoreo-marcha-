package com.example.participante.core.service;

import com.example.participante.core.dto.ParticipanteAllDto;
import com.example.participante.core.dto.ParticipanteDto;
import com.example.participante.core.dto.ParticipanteUpdateDto;
import com.example.participante.core.entities.Participante;
import com.example.participante.core.exception.SearchException;
import com.example.participante.core.http.response.DatosResponse;

import java.util.List;
import java.util.Optional;

public interface ParticipanteService {
    void insertarParticipante(ParticipanteDto participanteDto) throws SearchException;
    void modificarParticipante(ParticipanteUpdateDto participante, Long id) throws SearchException;
    void eliminarParticipante(Long id) throws SearchException;
    List<Participante> obtenerParticipantes();
    Optional<Participante> obtenerParticipanteXId(Long id) throws SearchException;
    Optional<Participante> obtenerParticipanteXCI(String ci) throws SearchException;
    List<Participante> obtenerParticipanteXUsuario(Long id_usuario) throws SearchException;
    DatosResponse obtenerDatosXParticipante(Long idparticipante);
    Long existeParticipantebyCi(String ci);

}
