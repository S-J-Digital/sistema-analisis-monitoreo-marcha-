package com.example.sennal.core.feingclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "microservicio-dato", url = "localhost:8087/api/v1/datoparticipante")
public interface DatoClient {
    @GetMapping("/findbyid/{id}")
     boolean existeDato(@PathVariable Long id);
}
