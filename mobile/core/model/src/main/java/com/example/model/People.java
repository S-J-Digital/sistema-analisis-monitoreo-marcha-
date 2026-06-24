package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;

public class People implements Serializable {
    private ArrayList<DataPeople> dataPeople;
    private String nombre;
    private String ci;
    private String sexo;
    private String telefono;

    public People(String name, String ci, String sexo, String telefono) {
        this.nombre = name;
        this.ci = ci;
        this.sexo = sexo;
        this.telefono = telefono;
        this.dataPeople = new ArrayList<DataPeople>();
    }

    public ArrayList<DataPeople> getDataPerson() {
        return dataPeople;
    }

    public void setDataPerson(ArrayList<DataPeople> dataPeople) {
        this.dataPeople = dataPeople;
    }

    public String getName() {
        return nombre;
    }

    public void setName(String name) {
        this.nombre = name;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
