package com.example.patologia.core.dto;

import com.example.patologia.core.entities.DatoPatologia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatoPatologiaDto {
    private Long iddato;
    private Long idpatologia;
    private String fechaevaluacion;

    public DatoPatologiaDto (DatoPatologia datoPatologia){
        this.iddato = datoPatologia.getIddato();
        this.idpatologia = datoPatologia.getIdpatologia();
        this.fechaevaluacion = datoPatologia.getFechaevaluacion().toString();
    }
}
