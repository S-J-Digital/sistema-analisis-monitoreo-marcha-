package com.example.redneuronal.core.model.util;

import java.util.List;

public class SennalPreccesor {

    //Calcula la media de los valores
    public static double mean(List<Double> data) {
        double sum = 0;
        for (double d : data) sum += d;
        return sum / data.size();
    }

    //CAlcula la varianza
    public static double variance(List<Double> data) {
        double m = mean(data);
        double sum = 0;
        for (double d : data) sum += Math.pow(d - m, 2);
        return sum / data.size();
    }

    //Calcula la desviación estandar
    public static double stdDev(List<Double> data) {
        return Math.sqrt(variance(data));
    }

    //Valor mínimo
    public static double min(List<Double> data) {
        double min = Double.MAX_VALUE;
        for (double d : data) if (d < min) min = d;
        return min;
    }

    //VAlor máximo
    public static double max(List<Double> data) {
        double max = -Double.MAX_VALUE;
        for (double d : data) if (d > max) max = d;
        return max;
    }

    // Extrae 15 características por sensor (3 ejes × 5 estadísticas)
    public static double[] extractSensorFeatures(List<Double> x, List<Double> y, List<Double> z) {
        return new double[] {
                mean(x), stdDev(x), min(x), max(x), variance(x),
                mean(y), stdDev(y), min(y), max(y), variance(y),
                mean(z), stdDev(z), min(z), max(z), variance(z)
        };
    }
}
