package com.example.model;

public class User {
    private String nombre;
    private String contrasenna;
    private String repetircontrasenna;
    private String id_rol;

    private String noprofesional;

    public User(String name, String password, String repetircontrasenna, String rol, String noprofesional) {
        this.nombre = name;
        this.contrasenna = password;
        this.repetircontrasenna = repetircontrasenna;
        this.id_rol = rol;
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
