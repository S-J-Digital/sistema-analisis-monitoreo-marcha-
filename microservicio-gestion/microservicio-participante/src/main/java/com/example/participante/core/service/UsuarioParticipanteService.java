package com.example.participante.core.service;

import com.example.participante.core.entities.Participante;
import com.example.participante.core.entities.UsuarioParticipante;
import com.example.participante.core.exception.SearchException;

import java.util.List;
import java.util.Optional;

public interface UsuarioParticipanteService {
    void crearRelacion(UsuarioParticipante usuarioParticipante);
    List<Participante> obtenerParticipanteXUsuario(Long id_usuario) throws SearchException;
}
