package com.example.enviodatos.Dto.Sennal;

import android.os.Build;

import com.example.model.Sennales;

import java.time.LocalDate;

public class SennalDto {
    private Long iddato;
    private String fecha;
    private double acelerometrox;
    private double acelerometroy;
    private double acelerometroz;
    private double giroscopiox;
    private double giroscopioy;
    private double giroscopioz;
    private double magnetometrox;
    private double magnetometroy;
    private double magnetometroz;
    private double temperatura;
    private double rangoacelerometro;
    private double rangogiroscopio;
    private double rangomagnetometro;
    private double frecuenciademuestreo;

    public SennalDto(Sennales sennales, Long iddato) {
        this.iddato = iddato;
        this.fecha = sennales.getFecha();
        this.acelerometrox = sennales.getAcelerometroX();
        this.acelerometroy = sennales.getAcelerometroY();
        this.acelerometroz = sennales.getAcelerometroZ();
        this.giroscopiox = sennales.getGiroscopioX();
        this.giroscopioy = sennales.getGiroscopioY();
        this.giroscopioz = sennales.getGiroscopioZ();
        this.magnetometrox = sennales.getMagnetometroX();
        this.magnetometroy = sennales.getMagnetometroY();
        this.magnetometroz = sennales.getMagnetometroZ();
        this.temperatura = sennales.getTemperatura();
        this.rangoacelerometro = sennales.getConfig_acel();
        this.rangogiroscopio = sennales.getConf_giros();
        this.rangomagnetometro = 128;
        this.frecuenciademuestreo = sennales.getConfig_frecuencia();
    }

    public Long getIddato() {
        return iddato;
    }

    public String getFecha() {
        return fecha;
    }

    public double getAcelerometrox() {
        return acelerometrox;
    }

    public double getAcelerometroy() {
        return acelerometroy;
    }

    public double getAcelerometroz() {
        return acelerometroz;
    }

    public double getGiroscopiox() {
        return giroscopiox;
    }

    public double getGiroscopioy() {
        return giroscopioy;
    }

    public double getGiroscopioz() {
        return giroscopioz;
    }

    public double getMagnetometrox() {
        return magnetometrox;
    }

    public double getMagnetometroy() {
        return magnetometroy;
    }

    public double getMagnetometroz() {
        return magnetometroz;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public double getRangoacelerometro() {
        return rangoacelerometro;
    }

    public double getRangogiroscopio() {
        return rangogiroscopio;
    }

    public double getRangomagnetometro() {
        return rangomagnetometro;
    }

    public double getFrecuenciademuestreo() {
        return frecuenciademuestreo;
    }
}
