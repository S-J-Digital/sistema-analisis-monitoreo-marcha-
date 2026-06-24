package com.example.usuario.config;

import com.example.usuario.core.dto.TokenDto;
import com.example.usuario.core.exception.SearchException;
import com.example.usuario.core.model.Usuario;
import com.example.usuario.core.repository.UsuarioRepository;
import com.example.usuario.util.TokenUtils;
import com.example.usuario.util.Validacion;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TokenService tokenService;
    private final Logger logger = Logger.getLogger(SecurityFilter.class.getName());

    @SneakyThrows
    //@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Inicio doFilter");
        TokenDto token = TokenUtils.requestToken(request);
        String requesUri = request.getRequestURI();
        logger.info(requesUri);
        logger.info(request.getHeader("Referer"));
        logger.info(request.getHeader("Origin"));
        System.out.println(request.getRemotePort());
        System.out.println(request.getServerPort());
        logger.info(request.getContextPath());
        boolean swagger = !Arrays.stream(AUTH_WHITELIST).filter(s -> request.getRequestURI().contains(s)).toList().isEmpty();
        if(!swagger){
            logger.info(request.getRequestURI());
        }
        else if (!token.getToken().isBlank() && request.getRequestURI().contains("/api/v1/usuario")){
            Validacion.validarElemento(token);
            token.setToken(token.getToken().replace("Bearer ", "").replaceAll(" ","").replaceAll("\"",""));
            var user = tokenService.getSubjetc(token.getToken());
            System.out.println(token);
            if(user != null){
                Usuario usuario = usuarioRepository.findByUsernameEquals(user).orElseThrow(() -> new SearchException("Usuario no encontrado"));
                //Forzar inicio de sesión
                System.out.println(usuario.getAuthorities());
                var autenticacion = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            }
            else {
                throw new Exception("Usuario no encontrado");
            }
        }
        else if (token.getToken().isBlank() && request.getRequestURI().contains("/api/v1/usuario") && swagger){
            throw new SecurityException("Acción inválida");
        }
        filterChain.doFilter(request, response);
    }
}
