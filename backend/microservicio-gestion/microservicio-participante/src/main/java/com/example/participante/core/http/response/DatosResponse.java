package com.example.participante.core.http.response;

import com.example.participante.core.dto.DatoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DatosResponse {

    private String nombre;
    private String ci;
    private String sexo;
    private String telefono;
    private List<DatoDto> datoDtoList;
}
