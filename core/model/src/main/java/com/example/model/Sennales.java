package com.example.model;

import java.io.Serializable;

public class Sennales implements Serializable {
    private int  datoId;
    private String  fecha;
    private double acelerometroX;
    private double acelerometroY;
    private double acelerometroZ;
    private double giroscopioX;
    private double giroscopioY;
    private double giroscopioZ;
    private double magnetometroX;
    private double magnetometroY;
    private double magnetometroZ;
    private double temperatura;
    private int config_acel;
    private int conf_giros;
    private int config_frecuencia;

    public Sennales(int datoId,String fecha ,double acelerometroX, double acelerometroY, double acelerometroZ,
                    double giroscopioX, double giroscopioY, double giroscopioZ, double magnetometroX,
                    double magnetometroY, double magnetometroZ, double temperatura, int config_acel,int conf_giros,
                    int config_frecuencia) {

        this.datoId = datoId;
        this.fecha = fecha;
        this.acelerometroX = acelerometroX;
        this.acelerometroY = acelerometroY;
        this.acelerometroZ = acelerometroZ;
        this.giroscopioX = giroscopioX;
        this.giroscopioY = giroscopioY;
        this.giroscopioZ = giroscopioZ;
        this.magnetometroX = magnetometroX;
        this.magnetometroY = magnetometroY;
        this.magnetometroZ = magnetometroZ;
        this.temperatura = temperatura;
        this.config_acel = config_acel;
        this.conf_giros = conf_giros;
        this.config_frecuencia = config_frecuencia;
    }


    public int getDatoIdtoId() {
        return datoId;
    }

    public void setDatoId(int datoId) {
        this.datoId = datoId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getAcelerometroX() {
        return acelerometroX;
    }

    public void setAcelerometroX(double acelerometroX) {
        this.acelerometroX = acelerometroX;
    }

    public double getAcelerometroY() {
        return acelerometroY;
    }

    public void setAcelerometroY(double acelerometroY) {
        this.acelerometroY = acelerometroY;
    }

    public double getAcelerometroZ() {
        return acelerometroZ;
    }

    public void setAcelerometroZ(double acelerometroZ) {
        this.acelerometroZ = acelerometroZ;
    }

    public double getGiroscopioX() {
        return giroscopioX;
    }

    public void setGiroscopioX(double giroscopioX) {
        this.giroscopioX = giroscopioX;
    }

    public double getGiroscopioY() {
        return giroscopioY;
    }

    public void setGiroscopioY(double giroscopioY) {
        this.giroscopioY = giroscopioY;
    }

    public double getGiroscopioZ() {
        return giroscopioZ;
    }

    public void setGiroscopioZ(double giroscopioZ) {
        this.giroscopioZ = giroscopioZ;
    }

    public double getMagnetometroX() {
        return magnetometroX;
    }

    public void setMagnetometroX(double magnetometroX) {
        this.magnetometroX = magnetometroX;
    }

    public double getMagnetometroY() {
        return magnetometroY;
    }

    public void setMagnetometroY(double magnetometroY) {
        this.magnetometroY = magnetometroY;
    }

    public double getMagnetometroZ() {
        return magnetometroZ;
    }

    public void setMagnetometroZ(double magnetometroZ) {
        this.magnetometroZ = magnetometroZ;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public int getConfig_acel() {
        return config_acel;
    }

    public void setConfig_acel(int config_acel) {
        this.config_acel = config_acel;
    }

    public int getConf_giros() {
        return conf_giros;
    }

    public void setConf_giros(int conf_giros) {
        this.conf_giros = conf_giros;
    }

    public int getConfig_frecuencia() {
        return config_frecuencia;
    }

    public void setConfig_frecuencia(int config_frecuencia) {
        this.config_frecuencia = config_frecuencia;
    }
}
