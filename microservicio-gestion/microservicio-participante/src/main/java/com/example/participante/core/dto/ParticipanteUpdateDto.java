package com.example.participante.core.dto;

import com.example.participante.core.entities.Participante;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipanteUpdateDto {
    private Long id;

    private String nombre;

    private String ci;

    private String sexo;

    private String telefono;


    public ParticipanteUpdateDto(Participante participante) {
        this.id = participante.getId();
        this.nombre = participante.getNombre();
        this.ci = participante.getCi();
        this.sexo = participante.getSexo();
        this.telefono = participante.getTelefono();
    }
}
