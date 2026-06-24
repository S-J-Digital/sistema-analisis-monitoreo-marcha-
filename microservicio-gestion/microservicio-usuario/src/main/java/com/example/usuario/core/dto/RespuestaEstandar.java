package com.example.usuario.core.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaEstandar<T> {
    @NotBlank(message = "El valor de la respuesta no puede ser nulo")
    private T respuesta;
}
