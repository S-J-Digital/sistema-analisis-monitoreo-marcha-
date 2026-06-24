package com.example.usuario.core.model;

import com.example.usuario.util.Validacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "rol",schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true, nullable = false)
    private Long id;

    @Column(name = "nombre",unique = true,nullable = false)
    private String nombreRol;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "rol")
    private List<Usuario> usuarios;

    @PrePersist
    public void prePersist() {
        Validacion.validarElemento(this);
    }

    @PreUpdate
    public void preUpdate() {
        Validacion.validarElemento(this);
    }
}
