package com.example.microserviciodato.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date event_date;
    @NotNull(message = "El estado no puede ser null")
    @NotBlank(message = "El estado no puede estar vacío")
    private String log_level;

    private String user_name;
    @Size(min = 7,max = 15,message = "La dirección IP con formato XXX.XXX.XXX es incorrecta")
    @NotBlank(message = "El IP no puede estar vacío")
    @NotNull(message = "El IP no puede ser null")
    private String ip_adress;

    private String message;


    public LogDto(@NotNull(message = "El estado no puede ser null") String log_level, String user_name, @NotNull(message = "El IP no puede ser null") String ip_adress, String message) {
        this.log_level = log_level;
        this.user_name = user_name;
        this.ip_adress = ip_adress;
        this.message = message;
    }
}
