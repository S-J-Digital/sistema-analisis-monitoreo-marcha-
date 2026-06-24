package com.example.usuario.core.service;

import com.example.usuario.core.dto.UsuarioDto;
import com.example.usuario.core.exception.SearchException;

public interface UsuarioDtoService {
    UsuarioDto userModificacion(UsuarioDto user) throws SearchException;
}
