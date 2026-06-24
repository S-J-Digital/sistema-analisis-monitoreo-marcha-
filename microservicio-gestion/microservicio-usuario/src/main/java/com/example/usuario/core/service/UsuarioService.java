package com.example.usuario.core.service;

import com.example.usuario.core.dto.UsuarioDto;
import com.example.usuario.core.model.Rol;
import com.example.usuario.core.model.Usuario;
import com.example.usuario.core.exception.SearchException;
import com.example.usuario.core.http.response.ParticipantesByUsuarioResponse;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface UsuarioService extends GenericService<Usuario>{
    @Transactional(rollbackFor = Exception.class)
    void modificarUsuario(UsuarioDto usuario, Long id, Rol rol) throws SearchException;
    Usuario obtenerUsuarioXNombre(String nombre) throws SearchException;
    boolean existeUsuario(Long id) throws SearchException;
}
