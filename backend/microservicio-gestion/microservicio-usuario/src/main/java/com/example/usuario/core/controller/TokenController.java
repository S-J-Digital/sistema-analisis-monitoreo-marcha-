package com.example.usuario.core.controller;

import com.example.usuario.core.service.LogsService;
import com.example.usuario.core.service.UsuarioService;
import com.example.usuario.core.serviceController.TokenControllerService;
import com.example.usuario.core.serviceController.UsuarioControllerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/token")
public class TokenController {
    private static Logger logger = Logger.getLogger(TokenController.class.getName());
    private final UsuarioService usuarioService;
    private final LogsService logsService;
    private final TokenControllerService tokenControllerService;
    private static final String ACEPTADO = "Aceptado";

    @Autowired
    public TokenController(UsuarioService usuarioService, LogsService logsService, TokenControllerService tokenControllerService) {
        this.usuarioService = usuarioService;
        this.logsService = logsService;
        this.tokenControllerService = tokenControllerService;
    }

    @GetMapping("/")
    @Operation(summary = "Valida el token del microservicio",description = "Valida el token del microservicio que lo necesite")
    @PreAuthorize("hasAnyRole('ROLE_Administrador','ROLE_Super Administrador','ROLE_Médico','ROLE_Paciente','ROLE_Investigador')")
    public ResponseEntity<Boolean> validarTokenMicroservicios(HttpServletRequest request){
        return ResponseEntity.ok(tokenControllerService.validarToken(request));
    }
}
