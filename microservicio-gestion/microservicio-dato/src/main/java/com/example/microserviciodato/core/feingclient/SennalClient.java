package com.example.microserviciodato.core.feingclient;

import com.example.microserviciodato.core.dto.SennalDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@FeignClient(name = "microservicio-sennal", url = "localhost:8089/api/v1/sennal")
public interface SennalClient {

    @GetMapping("/findAll/{iddato}")
    SennalDto obtenerSennalXDato(@PathVariable Long iddato);
}
