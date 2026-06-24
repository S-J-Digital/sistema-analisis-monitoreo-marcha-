package com.example.enviodatos.Dto.Datos;

import android.os.Build;

import com.example.model.DataPeople;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Date;
import java.time.format.DateTimeFormatter;

public class DatoParticipanteDto {
    private Long idparticipante;
    private String fecha;
    private String hora;
    private int edad;
    private boolean patologia;
    private double numerodecalzado;
    private double medicioncinturatobillo;
    private double largopierna;
    private double alturadelsensor;

    public DatoParticipanteDto(DataPeople dataPeople, Long idparticipante) {

            this.idparticipante = Long.valueOf(String.valueOf(idparticipante));
            this.fecha = dataPeople.getDate();
            this.hora = dataPeople.getHour();
            this.edad = dataPeople.getAge();
            setPatologia(dataPeople.getAffection());
            this.numerodecalzado = dataPeople.getShoes();
            this.medicioncinturatobillo = dataPeople.getCinturaTobillo();
            this.largopierna = dataPeople.getLeg();
            this.alturadelsensor = dataPeople.getHeight();

    }

    public void setPatologia(int patologia) {
        if(patologia == 0){
            this.patologia = false;
        }else{
            this.patologia = true;
        }
    }

    public boolean getPatologia(){
        return patologia;
    }

}
