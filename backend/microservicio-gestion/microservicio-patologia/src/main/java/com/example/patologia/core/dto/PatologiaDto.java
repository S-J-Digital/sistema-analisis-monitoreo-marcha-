package com.example.patologia.core.dto;

import com.example.patologia.core.entities.Patologia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatologiaDto {
    private Long id;
    private String nombre;


   public PatologiaDto (Patologia patologia){
       this.id = patologia.getId();
       this.nombre= patologia.getNombre();
   }
}
