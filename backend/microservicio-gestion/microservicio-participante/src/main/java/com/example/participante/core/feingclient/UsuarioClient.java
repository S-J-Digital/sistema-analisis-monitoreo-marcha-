package com.example.participante.core.feingclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="microservicio-usuario", url = "localhost:8085/api/v1/usuario")
public interface UsuarioClient {

    @GetMapping("/existeUsuario/{id}")
    boolean existeUsuario(@PathVariable Long id);


}
