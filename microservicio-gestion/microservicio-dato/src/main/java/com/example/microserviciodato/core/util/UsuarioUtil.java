package com.example.microserviciodato.core.util;

import jakarta.servlet.http.HttpServletRequest;

public class UsuarioUtil {
    public static String Usuario(HttpServletRequest request) {
        String usuario = request.getHeader("user");
        return usuario != null ? usuario : "anonymous";
    }
}
