package com.example.usuario.core.aspect;

import com.example.usuario.core.service.LogsService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
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

    @Around(value = "execution(* com.example.usuario.core.controller.*(..))")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String Token = request.getHeader("Authorization");
        String method = joinPoint.getSignature().getName();
        long initMilisecond = System.currentTimeMillis();
        try {
            Object resultado = joinPoint.proceed();
            return resultado;
        } catch (Throwable e) {
            String mensaje = e.getMessage();
            long finalMilisecond = System.currentTimeMillis();
            throw new Exception(mensaje);
        }
    }
}
