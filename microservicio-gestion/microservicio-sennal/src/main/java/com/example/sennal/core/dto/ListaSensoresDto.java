package com.example.sennal.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListaSensoresDto {
    List<Double> acelerometroX;
    List<Double> acelerometroY;
    List<Double> acelerometroZ;
    List<Double> giroscopioX;
    List<Double> giroscopioY;
    List<Double> giroscopioZ;
    List<Double> magnetometroX;
    List<Double> magnetometroY;
    List<Double> magnetometroZ;
}
