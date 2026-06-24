package com.example.usuario.core.service;

import jakarta.servlet.http.HttpServletRequest;


public interface LogsService {
    void insertarLog(HttpServletRequest request, String estado,String mensaje);
}
