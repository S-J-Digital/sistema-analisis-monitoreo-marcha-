package com.example.usuario.config;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.usuario.core.model.Usuario;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Logger;

@Service
public class TokenService {
    private final Logger logger = Logger.getLogger(TokenService.class.getName());
    public String generarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256("${jwt.secreto}");
            String token = JWT.create()
                    .withIssuer("Dianelis")
                    .withClaim("id",usuario.getId())
                    .withSubject(usuario.getUsername())
                    .withClaim("password",usuario.getPassword())
                    .withClaim("Rol",usuario.getRol().getNombreRol())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
            logger.info("Token generado para el usuario: " + usuario.getUsername());
            return token;
        } catch (Exception exception){
            logger.severe("Tipo de excepción: " + exception.getClass().getName());
            logger.severe("Error al generar el token: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private Instant generarFechaExpiracion(){
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-05:00"));
    }

    public String getSubjetc(String token) throws Exception {
        if(token == null)
            throw new Exception("Token nullo");
        DecodedJWT decodedJWT = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256("${jwt.secreto}");
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("Dianelis")
                    // reusable verifier instance
                    .build();
            decodedJWT = verifier.verify(token);
            logger.info(decodedJWT.getSubject());
        } catch (JWTVerificationException exception) {
            throw new Exception("No se puede validar el token");
        }
        if (decodedJWT.getSubject() == null)
            throw new RuntimeException("No se puede verificar el token");
        return decodedJWT.getSubject();
    }

    public boolean isActivoToken(String token) throws Exception{
        try {
            String persona = getSubjetc(token);
            if (persona == null || persona.equals(""))
                return false;
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
