package com.example.enviodatos.Dto.Patologia;

import android.os.Build;

import java.time.LocalDate;

public class RelaciónDatoPatologiaDto {
    private Long iddato;
    private Long idpatologia;
    private String fechaevaluacion;

    public RelaciónDatoPatologiaDto(Long iddato, Long idpatologia, String fechaevaluacion) {
        this.iddato = iddato;
        this.idpatologia = idpatologia;
        this.fechaevaluacion = fechaevaluacion;

    }
}
