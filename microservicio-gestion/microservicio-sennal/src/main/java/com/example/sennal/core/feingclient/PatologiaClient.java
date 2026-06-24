package com.example.sennal.core.feingclient;

import com.example.sennal.core.dto.entrenamiento.PatologiaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "microservicio-patologia", url = "http://localhost:8088/api/v1/patologia")
public interface PatologiaClient {

    @GetMapping("/all")
    List<PatologiaDto> obtenerPatologias();
}
