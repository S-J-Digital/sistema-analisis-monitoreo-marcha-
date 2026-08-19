package com.example.usuario.core.aspect;

import com.example.usuario.core.service.LogsService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.logging.Logger;

@Aspect
@Component
public class ServiceAspecto {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final LogsService logsService;

    @Autowired
    public ServiceAspecto(LogsService logsService) {
        this.logsService = logsService;
    }

    // Excluye LogsServiceImpl (mismo paquete) para no interceptar el propio insertarLog y caer en recursión infinita.
    @Around(value = "execution(* com.example.usuario.core.service.serviceimpl.*.*(..)) && !within(com.example.usuario.core.service.serviceimpl.LogsServiceImpl)")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String Token = request.getHeader("Authorization");
        String method = joinPoint.getSignature().getName();
        long initMilisecond = System.currentTimeMillis(), finalMilisecond, time;
        try {
            Object resultado = joinPoint.proceed();
            finalMilisecond = System.currentTimeMillis();
            time= finalMilisecond - initMilisecond;
            logsService.insertarLog(
                    request,"Aceptado","Operacion finalizada con éxito",method, ((double) time)
            );
            return resultado;
        } catch (Throwable e) {
            String mensaje = e.getMessage();
            finalMilisecond = System.currentTimeMillis();
            time= finalMilisecond - initMilisecond;
            logsService.insertarLog(
                    request,"Rechazado",e.getMessage(),method, ((double) time)
            );
            throw new Exception(mensaje);
        }
    }
}
