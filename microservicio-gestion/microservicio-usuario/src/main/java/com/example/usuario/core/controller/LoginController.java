package com.example.usuario.core.controller;

import com.example.usuario.core.dto.UsuarioDto;
import com.example.usuario.core.dto.UsuarioLogin;
import com.example.usuario.core.exception.SearchException;
import com.example.usuario.core.service.LogsService;
import com.example.usuario.core.service.UsuarioService;
import com.example.usuario.core.serviceController.LoginControllerService;
import com.example.usuario.core.serviceController.UsuarioControllerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/login")
@Tag(name = "Login Controller",
        description = "Controller responsible for user authentication and login operations")
public class LoginController {
    private final Logger logger = Logger.getLogger(LoginController.class.getName());
    private final UsuarioService usuarioService;
    private final LoginControllerService loginControllerService;
    private final LogsService logsService;
    private final UsuarioControllerService usuarioControllerService;
    private static final String ACEPTADO = "Aceptado";

    @Autowired
    public LoginController(UsuarioService usuarioService, LoginControllerService loginControllerService, LogsService logsService, UsuarioControllerService usuarioControllerService) {
        this.usuarioService = usuarioService;
        this.loginControllerService = loginControllerService;
        this.logsService = logsService;
        this.usuarioControllerService = usuarioControllerService;
    }

    @PostMapping("/")
    @Operation(summary = "Login",description = "Endpoint empleado para el Login")
    public ResponseEntity<?> login(@RequestBody UsuarioLogin usuario, HttpServletRequest request) throws SearchException {
        logger.info("Iniciando login");
        return ResponseEntity.ok(loginControllerService.login(usuario,request));
    }
}
