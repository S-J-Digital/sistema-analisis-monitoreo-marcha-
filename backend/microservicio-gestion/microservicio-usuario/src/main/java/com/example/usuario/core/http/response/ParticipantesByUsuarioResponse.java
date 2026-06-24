package com.example.usuario.core.http.response;

import com.example.usuario.core.dto.ParticipantesDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantesByUsuarioResponse {
    private String nombre;
    private String noprofesional;
    private List<ParticipantesDto> participanteList;
}
