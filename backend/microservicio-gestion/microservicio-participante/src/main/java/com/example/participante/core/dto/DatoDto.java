package com.example.participante.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatoDto {
    private LocalDate fecha;
    private LocalTime hora;
    private int edad;
    private boolean patologia;
    private List<PatologiaDto> patologiaList;
    private double numerodecalzado;
    private double medicioncinturatobillo;
    private double largopierna;
    private double alturadelsensor;
    private SennalDto sennalList;
}
