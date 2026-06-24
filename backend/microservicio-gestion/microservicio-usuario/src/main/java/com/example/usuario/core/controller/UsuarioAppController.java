package com.example.usuario.core.controller;

import com.example.usuario.core.dto.UsuarioDto;
import com.example.usuario.core.exception.SearchException;
import com.example.usuario.core.service.LogsService;
import com.example.usuario.core.service.UsuarioDtoService;
import com.example.usuario.core.service.UsuarioService;
import com.example.usuario.core.serviceController.UsuarioControllerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/usuarioApp")
@Tag(name = "UsuarioAppController", description = "Controlador de usuarios de la app sin seguridad")
public class UsuarioAppController {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final UsuarioService usuarioService;
    private final LogsService logsService;
    private final UsuarioControllerService usuarioControllerService;
    private final UsuarioDtoService usuarioDtoService;

    @Autowired
    public UsuarioAppController(UsuarioService usuarioService, LogsService logsService, UsuarioControllerService usuarioControllerService, UsuarioDtoService usuarioDtoService) {
        this.usuarioService = usuarioService;
        this.logsService = logsService;
        this.usuarioControllerService = usuarioControllerService;
        this.usuarioDtoService = usuarioDtoService;
    }

    @PostMapping("/create")
    @Operation(summary = "Insertar usuario",
            description = "Permite insertar un usuario paciente")
    public ResponseEntity<?> insertarUsuario(@RequestBody UsuarioDto usuario, HttpServletRequest request) throws SearchException {
        logger.info("Insertando un participante independiente");
        return ResponseEntity.ok(usuarioControllerService.insertarUsuario(usuarioDtoService.userModificacion(usuario),request));
    }
}
