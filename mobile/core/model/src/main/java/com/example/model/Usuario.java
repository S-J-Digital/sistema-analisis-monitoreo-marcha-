package com.example.model;




public class Usuario {
    private String nombre;
    private String contrasenna;
    private String repetircontrasenna;
    private String id_rol;

    private String noprofesional;

    public Usuario(String nombre, String contrasenna, String repetircontrasenna, String id_rol, String noprofesional) {
        this.nombre = nombre;
        this.contrasenna = contrasenna;
        this.repetircontrasenna = repetircontrasenna;
        this.id_rol = id_rol;
        this.noprofesional = noprofesional;
    }

    public String getName() {
        return nombre;
    }

    public void setName(String name) {
        this.nombre = name;
    }

    public String getPassword() {
        return contrasenna;
    }

    public void setPassword(String password) {
        this.contrasenna = password;
    }

    public String getRepetircontrasenna() {
        return repetircontrasenna;
    }

    public void setRepetircontrasenna(String repetircontrasenna) {
        this.repetircontrasenna = repetircontrasenna;
    }

    public String getRol() {
        return id_rol;
    }

    public void setRol(String rol) {
        this.id_rol = rol;
    }

    public String getNoprofesional() {
        return noprofesional;
    }

    public void setNoprofesional(String noprofesional) {
        this.noprofesional = noprofesional;
    }

}
