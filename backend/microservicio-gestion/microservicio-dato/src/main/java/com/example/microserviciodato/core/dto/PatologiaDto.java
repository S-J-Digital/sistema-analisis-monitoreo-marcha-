package com.example.microserviciodato.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatologiaDto {
    private Long id;
    private String nombre;
}
