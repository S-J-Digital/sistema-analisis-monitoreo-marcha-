package com.example.usuario.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estados",schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estado {
    @Id
    @Column(name = "estado_id", nullable = false)
    private Long estado_id;

    @Column(name = "nombre_estado", nullable = false)
    private String estado;

    @Column(name = "descripcion_estado",nullable = false)
    private String descripcion;

    @PrePersist
    @PreUpdate
    @PostRemove
    public void preOperation(){
        throw new UnsupportedOperationException("Operación no soportada");
    }
}
