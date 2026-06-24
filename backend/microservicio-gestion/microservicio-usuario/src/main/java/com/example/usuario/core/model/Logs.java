package com.example.usuario.core.model;

import com.example.usuario.core.dto.LogDto;
import com.example.usuario.util.Validacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log")
@Builder
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name="event_date", nullable = false)
    @NotNull(message = "La fecha y hora del log a generar no puede ser nullo o vacío")
    private LocalDate event_date;

    @Column(name="log_level", nullable = false)
    @NotBlank(message = "El estado del log no puede ser nullo o vacío")
    @Size(min = 1,max = 255,message = "El estado del log debe tener entre 1 y 255 caracteres")
    private String log_level;

    @Column(name="user_name")
    private String user_name;

    @Column(name="ip_address",nullable = false)
    @NotBlank(message = "La IP del usuario no puede ser nullo o vacío")
    @Size(min = 7,max = 16,message = "La dirección ip debe encontrarse entre 7 y 16 caracteres")
    private String ip_address;

    @Column(name="message",nullable = false)
    @Size(min = 5,max = 255,message = "El mensaje descriptivo debe estar en tre 5 y 255 caracteres")
    @NotBlank(message = "El mensaje descriptivo del log no puede ser nullo o vacío")
    private String message;

    public Logs(LogDto logDTO) {
        this.event_date  = LocalDateTime.now().toLocalDate();
        this.log_level = logDTO.getLog_level();
        this.user_name = logDTO.getUser_name();
        this.ip_address = logDTO.getIp_adress();
        this.message = logDTO.getMessage();
    }

    @PrePersist
    public void prePersist() {
        Validacion.validarElemento(this);
    }

    @PreUpdate
    @PreRemove
    public void preRemove() {
        throw new  UnsupportedOperationException("Operación no soportada");
    }
}
