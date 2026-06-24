package com.example.enviodatos.Dto.RedNeuronal;

import java.util.List;

public class Sennal {
    private List<Double> accX, accY, accZ;
    private List<Double> gyroX, gyroY, gyroZ;
    private List<Double> magX, magY, magZ;
    private List<String> diagnosticos;

   /* public Sennal(List<Double> accX, List<Double> accY, List<Double> accZ, List<Double> gyroX,
                  List<Double> gyroY, List<Double> gyroZ, List<Double> magX, List<Double> magY,
                  List<Double> magZ, List<String> diagnosticos) {
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.gyroX = gyroX;
        this.gyroY = gyroY;
        this.gyroZ = gyroZ;
        this.magX = magX;
        this.magY = magY;
        this.magZ = magZ;
        this.diagnosticos = diagnosticos;
    }*/

    public List<Double> getAccX() {
        return accX;
    }

    public void setAccX(List<Double> accX) {
        this.accX = accX;
    }

    public List<Double> getAccY() {
        return accY;
    }

    public void setAccY(List<Double> accY) {
        this.accY = accY;
    }

    public List<Double> getAccZ() {
        return accZ;
    }

    public void setAccZ(List<Double> accZ) {
        this.accZ = accZ;
    }

    public List<Double> getGyroX() {
        return gyroX;
    }

    public void setGyroX(List<Double> gyroX) {
        this.gyroX = gyroX;
    }

    public List<Double> getGyroY() {
        return gyroY;
    }

    public void setGyroY(List<Double> gyroY) {
        this.gyroY = gyroY;
    }

    public List<Double> getGyroZ() {
        return gyroZ;
    }

    public void setGyroZ(List<Double> gyroZ) {
        this.gyroZ = gyroZ;
    }

    public List<Double> getMagX() {
        return magX;
    }

    public void setMagX(List<Double> magX) {
        this.magX = magX;
    }

    public List<Double> getMagY() {
        return magY;
    }

    public void setMagY(List<Double> magY) {
        this.magY = magY;
    }

    public List<Double> getMagZ() {
        return magZ;
    }

    public void setMagZ(List<Double> magZ) {
        this.magZ = magZ;
    }

    public List<String> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(List<String> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }
}
