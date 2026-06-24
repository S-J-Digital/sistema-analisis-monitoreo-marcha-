package com.example.usuario.core.serviceController;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenControllerService {
    boolean validarToken(HttpServletRequest request);
}
