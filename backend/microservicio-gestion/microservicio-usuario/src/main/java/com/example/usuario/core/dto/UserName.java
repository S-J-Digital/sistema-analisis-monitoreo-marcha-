package com.example.usuario.core.dto;

import com.example.usuario.core.model.Usuario;

public record UserName(Long id, Long id_rol, String nombre) {
    public static UserName fromUsuario(Usuario usuario) {
        return new UserName(usuario.getId(), usuario.getRol().getId(), usuario.getNombre());
    }
}
