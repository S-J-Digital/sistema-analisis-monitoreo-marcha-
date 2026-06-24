package com.example.usuario.core.dto;

import com.example.usuario.core.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDto {
    private Long id;
    private Long id_rol;
    private String nombre;
    private String username;
    private String contrasenna;
    private String noprofesional;

    public UsuarioDto(Long id, Long id_rol, String nombre,String username ,String contrasenna, String noprofesional) {
        this.id = id;
        this.id_rol = id_rol;
        this.nombre = nombre;
        this.username = username;
        this.contrasenna = contrasenna;
        this.noprofesional = noprofesional;
    }

    public UsuarioDto(Usuario usuario) {
        this.id = usuario.getId();
        this.id_rol = usuario.getRol().getId();
        this.nombre = usuario.getNombre();
        this.username = usuario.getUsername();
        this.contrasenna = usuario.getContrasenna();
        this.noprofesional = usuario.getNoprofesional();
    }

    public static UsuarioDto toUsuarioDtoRol(UsuarioDto dto , Long id_rol){
        return new UsuarioDto(dto.getId(), id_rol, dto.getNombre(), dto.getUsername(), dto.getContrasenna(), dto.getNoprofesional());
    }
}
