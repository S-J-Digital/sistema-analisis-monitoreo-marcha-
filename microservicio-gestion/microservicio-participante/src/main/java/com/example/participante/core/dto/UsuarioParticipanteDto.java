package com.example.participante.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioParticipanteDto {
    private Long id_usuario;

    private Long id_participante;

    public UsuarioParticipanteDto(ParticipanteDto participanteDto){
        this.id_participante = participanteDto.getId();
        this.id_usuario = participanteDto.getUsuario();
    }

}
