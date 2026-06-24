package com.example.usuario.core.logs;

import com.example.usuario.core.repository.LogsRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {
    private String clientIp;
    private String user;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Captura la IP del cliente
        clientIp = request.getRemoteAddr();
        ThreadContext.put("ip", clientIp);

        // Simular obtención del usuario autenticado
        user = httpRequest.getHeader("user"); // Ajusta según tu sistema de autenticación
        ThreadContext.put("user", user != null ? user : "anonymous");

        try {
            chain.doFilter(request, response);
        } finally {
            // Limpia el contexto
            ThreadContext.clearAll();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
