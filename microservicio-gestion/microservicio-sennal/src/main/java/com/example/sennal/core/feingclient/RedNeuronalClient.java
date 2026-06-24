package com.example.sennal.core.feingclient;

import com.example.sennal.core.dto.entrenamiento.ModeloRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-redneuronal", url = "http://localhost:8092/api/v1/modelo")
public interface RedNeuronalClient {

    @PostMapping("/entrenar")
    String entrenarConNuevosDatos(@RequestBody ModeloRequest request);
}
