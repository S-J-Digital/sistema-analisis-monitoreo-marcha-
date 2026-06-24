package com.example.sennal.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaSenalesDTO {
    private List<SennalDto> senales;
    private List<String> enfermedades;
}
