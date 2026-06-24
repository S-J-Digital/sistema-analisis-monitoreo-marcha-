package com.example.sennal.core.dto;

import com.example.sennal.core.entities.Sennal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SennalDto {

    private Long id;
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

    public SennalDto(Sennal sennal) {
        this.id = sennal.getId();
        this.iddato = sennal.getIddato();
        this.fecha = sennal.getFecha().toString();
        this.acelerometrox = sennal.getAcelerometrox();
        this.acelerometroy = sennal.getAcelerometroy();
        this.acelerometroz = sennal.getAcelerometroz();
        this.giroscopiox = sennal.getGiroscopiox();
        this.giroscopioy = sennal.getGiroscopioy();
        this.giroscopioz = sennal.getGiroscopioz();
        this.magnetometrox = sennal.getMagnetometrox();
        this.magnetometroy = sennal.getMagnetometroy();
        this.magnetometroz = sennal.getMagnetometroz();
        this.temperatura = sennal.getTemperatura();
        this.rangoacelerometro = sennal.getRangoacelerometro();
        this.rangogiroscopio = sennal.getRangogiroscopio();
        this.rangomagnetometro = sennal.getRangomagnetometro();
        this.frecuenciademuestreo = sennal.getFrecuenciademuestreo();
    }
}
