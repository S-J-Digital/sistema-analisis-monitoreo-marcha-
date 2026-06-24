package com.example.enviodatos.Dto;

import com.example.database.Bd_manager_Rol;
import com.example.database.Bd_manager_Usuario;
import com.example.model.User;


public class Userdto {
    private Long id_rol;
    private String nombre;

    private String username;
    private String contrasenna;
    private String noprofesional;

    public Userdto(User user, Bd_manager_Rol manager_rol) {
        this.id_rol = manager_rol.getIdRol(user.getRol());
        this.nombre = user.getName();
        this.username = user.getPassword();
        this.contrasenna = user.getRepetircontrasenna();
        this.noprofesional = user.getNoprofesional();
    }
}
