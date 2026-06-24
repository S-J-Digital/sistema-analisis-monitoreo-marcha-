package com.example.usuario.core.feignClient;

import com.example.usuario.core.dto.ParticipantesDto;
import com.zaxxer.hikari.util.UtilityElf;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="microservicio-participante", url = "localhost:8086/api/v1/participante")
public interface ParticipanteClient {
    @GetMapping("/obtenerParticipanteXUsuario/{id}")
    List<ParticipantesDto> obtenerParticipanteXUsuario(@PathVariable Long id);
}
