package com.example.redneuronal.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "modeloredneuronal")
public class RedNeuronalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name="nombre")
    public String nombre;

    @Column(name="fecha")
    public LocalDate fecha;

    @Column(name = "modelo", columnDefinition = "bytea")
    public byte[] modelo;
}
