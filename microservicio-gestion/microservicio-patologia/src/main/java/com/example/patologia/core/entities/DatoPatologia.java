package com.example.patologia.core.entities;

import com.example.patologia.core.dto.DatoPatologiaDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dato_patologia")
public class DatoPatologia {
    @Id
    @Column(name = "id_dato")
    private Long iddato;
    @Column(name = "id_patologia")
    private Long idpatologia;
    @Column(name = "fecha_evaluacion")
    private LocalDate fechaevaluacion;

    public DatoPatologia(DatoPatologiaDto datoPatologiaDto) {
        this.iddato = datoPatologiaDto.getIddato();
        this.idpatologia = datoPatologiaDto.getIdpatologia();
        this.fechaevaluacion = LocalDate.parse(datoPatologiaDto.getFechaevaluacion());
    }
}
