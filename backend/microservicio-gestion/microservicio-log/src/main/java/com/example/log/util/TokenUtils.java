package com.example.log.util;

// NOTA (2026-09-18): esta clase es una copia de com.example.usuario.util.TokenUtils
// pegada tal cual en microservicio-log, pero microservicio-log no depende de
// microservicio-usuario, así que los imports de com.example.usuario.config.TokenService
// y com.example.usuario.core.dto.TokenDto nunca existieron en este módulo — no compilaba.
// Nada en microservicio-log la usa (ver pendientes.md, sección "microservicio-log — módulo
// fantasma"). Se comenta el cuerpo completo para no romper el build hasta que se decida
// si este módulo se termina de implementar o se elimina.

/*
import com.example.usuario.config.TokenService;
import com.example.usuario.core.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class TokenUtils {
    // @Autowired private UsuarioServiceInterfaces usuarioService;
    @Autowired
    private TokenService tokenService;

    public static TokenDto requestToken(HttpServletRequest request){
        return new TokenDto(request.getHeader("Authorization"));
    }

    public static TokenDto requestTokenClean(TokenDto token){
        token.setToken(token.getToken().replace("Bearer ", "").replaceAll(" ","").replaceAll("\"",""));
        return token;
    }

    public static TokenDto requestTokenCleanHttp(HttpServletRequest request){
        return new TokenDto(request.getHeader("Authorization").replace("Bearer ", "").replaceAll(" ","").replaceAll("\"",""));
    }

    @Bean
    public String usernameToken(HttpServletRequest request){
        try {
            TokenDto token = requestTokenCleanHttp(request);
            var user = tokenService.getSubjetc(token.getToken());
            return user.toString();
        }catch(Exception e){
            return null;
        }
    }
}
*/
