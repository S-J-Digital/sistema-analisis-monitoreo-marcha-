package com.example.sennal.core.dto.entrenamiento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModeloRequest {
    public List<Sennal> sennalList;
    public List<String> enfermedades;
}
