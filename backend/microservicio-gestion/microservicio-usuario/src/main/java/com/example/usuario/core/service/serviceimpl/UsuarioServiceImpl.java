package com.example.usuario.core.service.serviceimpl;

import com.example.usuario.core.dto.ParticipantesDto;
import com.example.usuario.core.dto.UsuarioDto;
import com.example.usuario.core.model.Rol;
import com.example.usuario.core.model.Usuario;
import com.example.usuario.core.exception.SearchException;
import com.example.usuario.core.feignClient.ParticipanteClient;
import com.example.usuario.core.http.response.ParticipantesByUsuarioResponse;
import com.example.usuario.core.repository.UsuarioRepository;
import com.example.usuario.core.service.GenericService;
import com.example.usuario.core.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("usuario_service")
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{
    private final UsuarioRepository usuarioRepository;
    private final ParticipanteClient participanteClient;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertarEntity(Usuario usuario) {
        if(!usuarioRepository.existsByNombre(usuario.getNombre())){
            usuarioRepository.save(usuario);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modificarUsuario(UsuarioDto usuarioDto, Long id, Rol rol) throws SearchException {
        if(usuarioRepository.existsById(id)){
            usuarioDto.setId(id);
            usuarioRepository.save(new Usuario(usuarioDto,rol,usuarioDto.getContrasenna()));
        }else{
            throw new SearchException("No existe el usuario a modificar");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eliminarEntity(Long id) throws SearchException {
        if(usuarioRepository.existsById(id)){
            usuarioRepository.deleteById(id);
        }else{
            throw new SearchException("No existe el usuario a eliminar");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerEntitys() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerEntityXId(Long id) throws SearchException {
        return Optional.ofNullable(usuarioRepository.findById(id).orElseThrow(()-> new SearchException("No existe un usuario con ese ID")));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioXNombre(String nombre) throws SearchException {
        return usuarioRepository.findByNombre(nombre).orElseThrow(()-> new SearchException("No existe un usuario con ese nombre"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuario(Long id) throws SearchException {
        return usuarioRepository.existsById(id);
    }
}
