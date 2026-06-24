package com.example.usuario.core.serviceController.serviceControllerImpl;

import com.example.usuario.config.TokenService;
import com.example.usuario.core.dto.UsuarioLogin;
import com.example.usuario.core.exception.SearchException;
import com.example.usuario.core.model.Usuario;
import com.example.usuario.core.repository.UsuarioRepository;
import com.example.usuario.core.service.UsuarioService;
import com.example.usuario.core.serviceController.LoginControllerService;
import com.example.usuario.util.DescrifradoBase64Android;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class LoginControllerServiceImpl implements LoginControllerService {
    private final Logger logger = Logger.getLogger(LoginControllerServiceImpl.class.getName());
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public LoginControllerServiceImpl(UsuarioService usuarioService, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public String login(UsuarioLogin usuarioLogin, HttpServletRequest request) throws SearchException {
        Usuario user = usuarioRepository.findByUsernameEquals(usuarioLogin.username()).orElseThrow(() -> new SearchException("No se encontro el usuario especificado"));
        if (!DescrifradoBase64Android.getdesencriptacion(user.getPassword()).equals(usuarioLogin.password()) && !DescrifradoBase64Android.getdesencriptacion(user.getPassword()).equals(DescrifradoBase64Android.getdesencriptacion(usuarioLogin.password()))) {
            throw new SecurityException("No se encontró el usuario especificado");
        }
        return tokenService.generarToken(user);
    }
}
