package com.example.usuario.core.serviceController.serviceControllerImpl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.usuario.core.dto.TokenDto;
import com.example.usuario.core.exception.SearchException;
import com.example.usuario.core.model.Rol;
import com.example.usuario.core.model.Usuario;
import com.example.usuario.core.repository.RolRepository;
import com.example.usuario.core.repository.UsuarioRepository;
import com.example.usuario.core.serviceController.TokenControllerService;
import com.example.usuario.util.TokenUtils;
import com.example.usuario.util.Validacion;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

@Service
public class TokenControllerServiceImpl implements TokenControllerService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private Logger logger = Logger.getLogger(TokenControllerServiceImpl.class.getName());

    @Autowired
    public TokenControllerServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
    }

    @Override
    public boolean validarToken(HttpServletRequest request) {
        try {
            TokenDto token = TokenUtils.requestToken(request);
            Validacion.validarElemento(token);
            token.setToken(token.getToken().replace("Bearer ", "").replaceAll(" ","").replaceAll("\"",""));
            Algorithm algorithm = Algorithm.HMAC256("${jwt.secreto}");
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("Dianelis")
                    // reusable verifier instance
                    .build();
            DecodedJWT decodedJWT =  verifier.verify(token.getToken());
            logger.info("JWT verifier: " + verifier.verify(token.getToken()));
            Usuario usuario = usuarioRepository.findByUsernameEquals(decodedJWT.getSubject()).orElseThrow(() -> new SearchException("Usuario no encontrado"));
            Rol rol = rolRepository.findByNombreRolEquals(decodedJWT.getClaim("Rol").asString()).orElseThrow(() -> new SearchException("Rol no encontrado"));
            logger.info("Solamente queda el retorno de la información");
            return (passwordEncoder.matches(decodedJWT.getClaim("password").asString(), usuario.getPassword())
                    && decodedJWT.getExpiresAt().after(new Date()));
        }
        catch (Exception e) {
            logger.severe(e.getMessage());
            return false;
        }
    }
}
