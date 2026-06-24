package com.example.participante.core.feingclient;

import com.example.participante.core.dto.DatoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "microservicio-dato", url = "localhost:8087/api/v1/datoparticipante")
public interface DatoClient {
    @GetMapping("/allbyParticipante/{idparticipante}")
    List<DatoDto> obtenerDatoXParticipante(@PathVariable Long idparticipante);
}
