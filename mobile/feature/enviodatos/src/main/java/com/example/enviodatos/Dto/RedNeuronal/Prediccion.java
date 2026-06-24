package com.example.enviodatos.Dto.RedNeuronal;

public class Prediccion {

    private String enfermedad1;
    private double prob1;

    private String enfermedad2;
    private double prob2;

    private String enfermedad3;
    private double prob3;


    // getters y setters

    public String getEnfermedad1() {
        return enfermedad1;
    }

    public void setEnfermedad1(String enfermedad1) {
        this.enfermedad1 = enfermedad1;
    }

    public double getProb1() {
        return prob1;
    }

    public void setProb1(double prob1) {
        this.prob1 = prob1;
    }

    public String getEnfermedad2() {
        return enfermedad2;
    }

    public void setEnfermedad2(String enfermedad2) {
        this.enfermedad2 = enfermedad2;
    }

    public double getProb2() {
        return prob2;
    }

    public void setProb2(double prob2) {
        this.prob2 = prob2;
    }

    public String getEnfermedad3() {
        return enfermedad3;
    }

    public void setEnfermedad3(String enfermedad3) {
        this.enfermedad3 = enfermedad3;
    }

    public double getProb3() {
        return prob3;
    }

    public void setProb3(double prob3) {
        this.prob3 = prob3;
    }
}
