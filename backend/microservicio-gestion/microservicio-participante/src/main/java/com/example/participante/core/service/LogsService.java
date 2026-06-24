package com.example.participante.core.service;

import jakarta.servlet.http.HttpServletRequest;

public interface LogsService {
    void insertarLog(HttpServletRequest request, String estado,String mensaje);
}
