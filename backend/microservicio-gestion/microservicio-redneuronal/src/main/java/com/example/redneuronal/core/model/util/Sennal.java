package com.example.redneuronal.core.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sennal {
    private List<Double> accX, accY, accZ;
    private List<Double> gyroX, gyroY, gyroZ;
    private List<Double> magX, magY, magZ;
    private List<String> diagnosticos;

}
