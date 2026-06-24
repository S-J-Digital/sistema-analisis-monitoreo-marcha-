package com.example.participante.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipanteAllDto {
    private String nombre;

    private String ci;

    private String sexo;

    private String telefono;
    private List<DatoDto> datoDtoList;
}
