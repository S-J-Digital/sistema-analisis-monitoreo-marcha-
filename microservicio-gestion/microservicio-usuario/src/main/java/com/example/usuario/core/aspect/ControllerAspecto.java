package com.example.usuario.core.aspect;

import com.example.usuario.core.service.LogsService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.logging.Logger;

@Aspect
@Component
public class ControllerAspecto {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final LogsService LogsService;

    @Autowired
    public ControllerAspecto(LogsService logsService) {
        LogsService = logsService;
    }

    @Before(value = "execution(* com.example.usuario.core.controller.UsuarioController.*(..))")
    public void logBeforeControllerUsuario(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String Token = request.getHeader("Authorization");
        log.info("Iniciando: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "execution(* com.example.usuario.core.controller.UsuarioController.*(..))",
            returning = "resultado", argNames = "joinPoint,resultado")
    public void logAfterControllerUsuario(JoinPoint joinPoint, ResponseEntity<?> resultado) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String Token = request.getHeader("Authorization");
        log.info("Terminado: " + joinPoint.getSignature().getName());
    }

    @Before(value = "execution(* com.example.usuario.core.controller.LoginController.*(..))")
    public void logBeforeController(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String Token = request.getHeader("Authorization");
        log.info("Iniciando: " + joinPoint.getSignature().getName());
    }
}
