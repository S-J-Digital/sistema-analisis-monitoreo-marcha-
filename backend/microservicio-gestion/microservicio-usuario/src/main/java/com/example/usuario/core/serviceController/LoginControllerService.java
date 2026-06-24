package com.example.usuario.core.serviceController;

import com.example.usuario.core.dto.UsuarioLogin;
import com.example.usuario.core.exception.SearchException;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginControllerService {
    String login(UsuarioLogin usuarioLogin, HttpServletRequest request) throws SearchException;
}
