package com.example.enviodatos.Dto.Participante;

import com.example.model.People;

public class ParticipanteUpdate {
    private String nombre;

    private String ci;

    private String sexo;

    private String telefono;

    public ParticipanteUpdate(People people) {
        this.nombre = people.getName();
        this.ci = people.getCi();
        this.sexo = people.getSexo();
        this.telefono = people.getTelefono();

    }
}
