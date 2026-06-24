package com.example.redneuronal.core.serviceimpl.util;

import com.example.redneuronal.core.model.util.Sennal;
import com.example.redneuronal.core.model.util.SennalPreccesor;

import java.util.List;

public class ObtenerCaracteristicas {
    //Obtener las características de las señales de entrada
    public static double[] getInputFeatures(Sennal s) {
        double[] acc = SennalPreccesor.extractSensorFeatures(s.getAccX(), s.getAccY(), s.getAccZ());
        double[] gyro = SennalPreccesor.extractSensorFeatures(s.getGyroX(), s.getGyroY(), s.getGyroZ());
        double[] mag = SennalPreccesor.extractSensorFeatures(s.getMagX(), s.getMagY(), s.getMagZ());

        double[] all = new double[45];
        System.arraycopy(acc, 0, all, 0, 15);
        System.arraycopy(gyro, 0, all, 15, 15);
        System.arraycopy(mag, 0, all, 30, 15);
        return all;
    }

    //Obtener las enfermedades
    public static List<String> getExpectedOutput(Sennal s) {
        return s.getDiagnosticos();
    }
}
