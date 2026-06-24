package com.example.participante.core.entities;

import com.example.participante.core.dto.UsuarioParticipanteDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario_participante")
public class UsuarioParticipante {
    @Id
    @Column(name="id_usuario")
    private Long id_usuario;

    @Column(name="id_participante")
    private Long id_participante;
    private UsuarioParticipante(UsuarioParticipanteDto usuarioParticipanteDto){
        this.id_usuario = usuarioParticipanteDto.getId_usuario();
        this.id_participante = usuarioParticipanteDto.getId_participante();
    }
}
