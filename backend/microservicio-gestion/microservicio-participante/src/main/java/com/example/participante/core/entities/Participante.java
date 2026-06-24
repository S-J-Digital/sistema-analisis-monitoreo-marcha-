package com.example.participante.core.entities;

import com.example.participante.core.dto.ParticipanteDto;
import com.example.participante.core.dto.ParticipanteUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="participante")
public class Participante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nombre")
    private String nombre;
    @Column(name="ci")
    private String ci;
    @Column(name="sexo")
    private String sexo;
    @Column(name="telefono")
    private String telefono;

    public Participante(ParticipanteDto participanteDto) {
        this.id = participanteDto.getId();
        this.nombre = participanteDto.getNombre();
        this.ci = participanteDto.getCi();
        this.sexo = participanteDto.getSexo();
        this.telefono = participanteDto.getTelefono();
    }

    public Participante(ParticipanteUpdateDto participanteDto) {
        this.id = participanteDto.getId();
        this.nombre = participanteDto.getNombre();
        this.ci = participanteDto.getCi();
        this.sexo = participanteDto.getSexo();
        this.telefono = participanteDto.getTelefono();
    }
}
