package com.example.model;

import java.sql.Time;

import java.io.Serializable;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class DataPeople implements Serializable {

    private ArrayList<Sennales> sennales;
    private String fecha ;
    private String hora;
    private int edad;
    private int patologia;
    private double numerodecalzado;
    private double medicioncinturatobillo;
    private double largopierna;
    private double alturadelsensor;
    private List<String> observation;

    public DataPeople(String date, String hour, int age, int affection, double shoes, double cinturaTobillo, double leg, double height, List<String> observation) {
        this.fecha = date;
        this.hora = hour;
        this.edad = age;
        this.patologia = affection;
        this.numerodecalzado = shoes;
        this.medicioncinturatobillo = cinturaTobillo;
        this.largopierna = leg;
        this.alturadelsensor = height;
        this.observation = observation;
        this.sennales = new ArrayList<Sennales>();
    }

    public ArrayList<Sennales> getSennales() {
        return sennales;
    }

    public void setSennales(ArrayList<Sennales> sennales) {
        this.sennales = sennales;
    }

    public String getDate() {
        return fecha;
    }

    public void setDate(String date) {
        this.fecha = date;
    }

    public String getHour() {
        return hora;
    }

    public void setHour(String hour) {
        this.hora = hour;
    }

    public int getAge() {
        return edad;
    }

    public void setAge(int age) {
        this.edad = age;
    }

    public int getAffection() {
        return patologia;
    }

    public void setAffection(int affection) {
        this.patologia = affection;
    }

    public double getShoes() {
        return numerodecalzado;
    }

    public void setShoes(double shoes) {
        this.numerodecalzado = shoes;
    }

    public double getCinturaTobillo() {
        return medicioncinturatobillo;
    }

    public void setCinturaTobillo(double cinturaTobillo) {
        this.medicioncinturatobillo = cinturaTobillo;
    }

    public double getLeg() {
        return largopierna;
    }

    public void setLeg(double leg) {
        this.largopierna = leg;
    }

    public double getHeight() {
        return alturadelsensor;
    }

    public void setHeight(double height) {
        this.alturadelsensor = height;
    }

    public List<String> getObservation() {
        return observation;
    }

    public void setObservation(List<String> observation) {
        this.observation = observation;
    }
}
