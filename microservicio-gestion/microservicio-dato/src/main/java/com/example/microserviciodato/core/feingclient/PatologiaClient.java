package com.example.microserviciodato.core.feingclient;

import com.example.microserviciodato.core.dto.PatologiaDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "microservicio-patologia", url = "localhost:8088/api/v1/patologia")
public interface PatologiaClient {

    @GetMapping("/obtenerPatologiaXDato/{id_dato}")
    List<PatologiaDto> obtenerPatologiaXDato(@PathVariable Long id_dato);

}
