package com.example.participante.core.serviceImpl;

import com.example.participante.core.entities.Participante;
import com.example.participante.core.entities.UsuarioParticipante;
import com.example.participante.core.exception.SearchException;
import com.example.participante.core.repository.UsuarioParticipanteRepository;
import com.example.participante.core.service.UsuarioParticipanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioParticipanteServiceImpl implements UsuarioParticipanteService {
    private UsuarioParticipanteRepository usuarioParticipanteRepository;

    @Autowired
    public UsuarioParticipanteServiceImpl(UsuarioParticipanteRepository usuarioParticipanteRepository) {
        this.usuarioParticipanteRepository = usuarioParticipanteRepository;
    }

    @Override
    public void crearRelacion(UsuarioParticipante usuarioParticipante) {
        usuarioParticipanteRepository.save(usuarioParticipante);
    }

    @Override
    public List<Participante> obtenerParticipanteXUsuario(Long id_usuario) throws SearchException {
        return usuarioParticipanteRepository.findAllByIdUser(id_usuario);
    }
}
