package com.example.enviodatos.Dto.Participante;

import com.example.model.People;

public class Participantedto {
    private String nombre;

    private String ci;

    private String sexo;

    private String telefono;
    private Long usuario;

    public Participantedto(People people, Long id_user) {
        this.nombre = people.getName();
        this.ci = people.getCi();
        this.sexo = people.getSexo();
        this.telefono = people.getTelefono();
        this.usuario = id_user;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }
}
