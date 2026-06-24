package com.example.enviodatos.Dto.Sennal;

import java.util.List;

public class ListaSenalesDTO {
    private List<SennalDto> senales;
    private List<String> enfermedades;

    public ListaSenalesDTO(List<SennalDto> senales,List<String> enfermedades ) {
        this.senales = senales;
        this.enfermedades = enfermedades;
    }

    public List<SennalDto> getSenales() {
        return senales;
    }

    public void setSenales(List<SennalDto> senales) {
        this.senales = senales;
    }

    public List<String> getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(List<String> enfermedades) {
        this.enfermedades = enfermedades;
    }
}
