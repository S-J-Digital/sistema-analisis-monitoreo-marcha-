package com.example.log.util;
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
