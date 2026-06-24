package com.example.sennal.core.entities;

import com.example.sennal.core.dto.SennalDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="sennal")
public class Sennal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_dato")
    private Long iddato;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column (name = "acelerometrox")
    private double acelerometrox;

    @Column (name = "acelerometroy")
    private double acelerometroy;

    @Column (name = "acelerometroz")
    private double acelerometroz;

    @Column (name = "giroscopiox")
    private double giroscopiox;

    @Column (name = "giroscopioy")
    private double giroscopioy;

    @Column (name = "giroscopioz")
    private double giroscopioz;

    @Column (name = "magnetometrox")
    private double magnetometrox;

    @Column (name = "magnetometroy")
    private double magnetometroy;

    @Column (name = "magnetometroz")
    private double magnetometroz;

    @Column (name = "temperatura")
    private double temperatura;

    @Column (name = "rangoacelerometro")
    private double rangoacelerometro;

    @Column (name = "rangogiroscopio")
    private double rangogiroscopio;

    @Column (name = "rangomagnetometro")
    private double rangomagnetometro;

    @Column (name = "frecuenciademuestreo")
    private double frecuenciademuestreo;

    public Sennal(SennalDto sennalDto) {
        this.id = sennalDto.getId();
        this.iddato = sennalDto.getIddato();
        this.fecha = LocalDate.parse(sennalDto.getFecha());
        this.acelerometrox = sennalDto.getAcelerometrox();
        this.acelerometroy = sennalDto.getAcelerometroy();
        this.acelerometroz = sennalDto.getAcelerometroz();
        this.giroscopiox = sennalDto.getGiroscopiox();
        this.giroscopioy = sennalDto.getGiroscopioy();
        this.giroscopioz = sennalDto.getGiroscopioz();
        this.magnetometrox = sennalDto.getMagnetometrox();
        this.magnetometroy = sennalDto.getMagnetometroy();
        this.magnetometroz = sennalDto.getMagnetometroz();
        this.temperatura = sennalDto.getTemperatura();
        this.rangoacelerometro = sennalDto.getRangoacelerometro();
        this.rangogiroscopio = sennalDto.getRangogiroscopio();
        this.rangomagnetometro = sennalDto.getRangomagnetometro();
        this.frecuenciademuestreo = sennalDto.getFrecuenciademuestreo();
    }
}
