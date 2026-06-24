package com.example.usuario.core.service.serviceimpl;

import com.example.usuario.core.dto.UsuarioDto;
import com.example.usuario.core.exception.SearchException;
import com.example.usuario.core.model.Rol;
import com.example.usuario.core.service.UsuarioDtoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDtoServiceImpl implements UsuarioDtoService {
    private final List<Rol> rolesLista;

    public UsuarioDtoServiceImpl(List<Rol> rolesLista) {
        this.rolesLista = rolesLista;
    }

    @Override
    public UsuarioDto userModificacion(UsuarioDto user) throws SearchException {
        Rol rol = rolesLista.stream().filter(s -> s.getNombreRol().contains("Paciente"))
                .findFirst().orElseThrow(() -> new SearchException("No se encontro el rol"));
        if (rol.getId().longValue() != user.getId_rol().longValue())
            return UsuarioDto.toUsuarioDtoRol(user, rol.getId());
        return user;
    }
}
