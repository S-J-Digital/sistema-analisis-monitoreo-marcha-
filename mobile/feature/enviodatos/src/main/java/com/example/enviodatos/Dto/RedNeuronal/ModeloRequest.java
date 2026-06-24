package com.example.enviodatos.Dto.RedNeuronal;

import java.util.List;

public class ModeloRequest {
    public List<Sennal> sennalList;
    public List<String> enfermedades;

    public ModeloRequest(List<Sennal> sennalList, List<String> enfermedades) {
        this.sennalList = sennalList;
        this.enfermedades = enfermedades;
    }

    public List<Sennal> getSennalList() {
        return sennalList;
    }

    public void setSennalList(List<Sennal> sennalList) {
        this.sennalList = sennalList;
    }

    public List<String> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(List<String> enfermedades) {
        this.enfermedades = enfermedades;
    }
}
