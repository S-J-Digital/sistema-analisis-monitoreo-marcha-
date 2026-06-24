package com.example.usuario.core.serviceController.serviceControllerImpl;

import com.example.usuario.core.controller.UsuarioController;
import com.example.usuario.core.dto.UsuarioDto;
import com.example.usuario.core.dto.UsuarioDtoSent;
import com.example.usuario.core.exception.SearchException;
import com.example.usuario.core.model.Rol;
import com.example.usuario.core.model.Usuario;
import com.example.usuario.core.repository.RolRepository;
import com.example.usuario.core.repository.UsuarioRepository;
import com.example.usuario.core.service.LogsService;
import com.example.usuario.core.service.UsuarioService;
import com.example.usuario.core.serviceController.UsuarioControllerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class UsuarioControllerServiceImpl implements UsuarioControllerService {
    private static Logger logger = Logger.getLogger(UsuarioController.class.getName());
    private final UsuarioService usuarioService;
    private final LogsService logsService;
    private final RolRepository  rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<Rol> rolesLista;

    public UsuarioControllerServiceImpl(UsuarioService usuarioService, LogsService logsService, RolRepository rolRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, List<Rol> rolesLista) {
        this.usuarioService = usuarioService;
        this.logsService = logsService;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesLista = rolesLista;
    }

    @Override
    public String insertarUsuario(UsuarioDto usuario, HttpServletRequest request) throws SearchException {
        Rol rol = rolesLista.stream()
                .filter(s -> s.getId().longValue() == usuario.getId_rol().longValue()).findFirst().orElseThrow(() -> new SearchException("No se ha encontrado el rol específico"));
        usuarioService.insertarEntity(new Usuario(usuario,rol,usuario.getContrasenna()));
        return "El usuario se insertó correctamente";
    }

    @Override
    public String modificarUsuario(UsuarioDto usuario,Long id, HttpServletRequest request) throws SearchException {
        Rol rol = rolesLista.stream()
                .filter(s -> s.getId().longValue() == usuario.getId_rol().longValue()).findFirst().orElseThrow(() -> new SearchException("No se ha encontrado el rol específico"));
        usuarioService.modificarUsuario(usuario, id,rol);
        return "Usuario Modificado correctamente";
    }

    @Override
    public String eliminarUsuario(Long id, HttpServletRequest request) throws SearchException {
        usuarioService.eliminarEntity(id);
        return "Se eliminó el usuario";
    }

    @Override
    public List<UsuarioDtoSent> listarUsuarios(HttpServletRequest request) throws SearchException {
        try{
            return usuarioRepository.findAll().stream().map(UsuarioDtoSent::fromUsuario).toList();
        } catch (Exception e){
            throw new SearchException("No se puede acceder a la lista de usuarios");
        }
    }

    @Override
    public UsuarioDtoSent obtenerDatosPorId(Long id, HttpServletRequest request) throws SearchException {
        UsuarioDtoSent usuario = UsuarioDtoSent.fromUsuario(usuarioService.obtenerEntityXId(id).orElseThrow(() -> new SearchException("No se ha encontrado el usuario especificado")));
        return usuario;
    }

    @Override
    public boolean existeUsuariobynombre(Long id, HttpServletRequest request) throws SearchException {
        return usuarioService.existeUsuario(id);
    }
}
