package com.example.patologia.core.entities;

import com.example.patologia.core.dto.PatologiaDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patologia")
public class Patologia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre")
    private String nombre;

    public Patologia(PatologiaDto patologiaDto){
        this.id = patologiaDto.getId();
        this.nombre = patologiaDto.getNombre();
    }
}
