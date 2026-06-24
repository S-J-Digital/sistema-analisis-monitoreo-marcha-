package com.example.microserviciodato.core.http.response;

import com.example.microserviciodato.core.dto.PatologiaDto;
import com.example.microserviciodato.core.dto.SennalDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatologiaResponse {
    private Long id;
    private Long idparticipante;
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
