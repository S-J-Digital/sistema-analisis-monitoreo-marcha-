package com.example.microserviciodato.core.entities;

import com.example.microserviciodato.core.dto.DatoParticipanteDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="datoparticipante")
public class DatoParticipante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_participante")
    private Long idparticipante;
    @Column(name = "fecha")
    private LocalDate fecha;
    @Column(name = "hora")
    private LocalTime hora;
    @Column(name = "edad")
    private int edad;
    @Column(name = "patologia")
    private boolean patologia;
    @Column(name = "numerodecalzado")
    private double numerodecalzado;
    @Column(name = "medicioncinturatobillo")
    private double medicioncinturatobillo;
    @Column(name = "largopierna")
    private double largopierna;
    @Column(name = "alturadelsensor")
    private double alturadelsensor;

    public DatoParticipante(DatoParticipanteDto datoParticipanteDto) {
        this.idparticipante = datoParticipanteDto.getIdparticipante();
        this.fecha = LocalDate.parse(datoParticipanteDto.getFecha());
        this.hora = LocalTime.parse(datoParticipanteDto.getHora());
        this.edad = datoParticipanteDto.getEdad();
        this.patologia = datoParticipanteDto.isPatologia();
        this.numerodecalzado = datoParticipanteDto.getNumerodecalzado();
        this.medicioncinturatobillo = datoParticipanteDto.getMedicioncinturatobillo();
        this.largopierna = datoParticipanteDto.getLargopierna();
        this.alturadelsensor = datoParticipanteDto.getAlturadelsensor();
    }
}
