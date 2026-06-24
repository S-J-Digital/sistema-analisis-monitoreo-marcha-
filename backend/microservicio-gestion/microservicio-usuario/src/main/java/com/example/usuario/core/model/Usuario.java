package com.example.usuario.core.model;

import com.example.usuario.core.dto.UsuarioDto;
import com.example.usuario.util.Validacion;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_id_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "rol_id",referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_usuario_rol"))
    private Rol rol;

    @Column(name = "nombre",nullable = false)
    @NotBlank(message = "El nombre no puede ser nullo o vacío")
    @Size(min = 5,max = 255,message = "El nombre debe ser entre 5 y 255 caracteres")
    private String nombre;

    @Column(name = "username",nullable = false,unique = true)
    @NotBlank(message = "El nombre de usuario no puede ser nullo o vacío")
    @Size(min = 5,max = 255,message = "El nombre de usuario debe ser entre 5 y 255 caracteres")
    private String username;

    @Column(name ="contrasenna")
    @NotBlank(message = "La contraseña no puede ser nullo o vacío")
    @Size(min = 5,max = 255,message = "La contraseña debe ser entre 5 y 255 caracteres")
    private String contrasenna;

    @Column(name ="noprofesional", unique = true,nullable = false)
    @NotBlank(message = "El número de profesional no puede ser nullo o vacío")
    @Size(min = 5,max = 255,message = "El número de profesional debe ser entre 5 y 255 caracteres")
    private String noprofesional;

    public Usuario(UsuarioDto usuario,Rol rol) {
        this.id = usuario.getId();
        this.rol = rol;
        this.nombre = usuario.getNombre();
        this.username = usuario.getUsername();
        this.contrasenna = usuario.getContrasenna();
        this.noprofesional = usuario.getNoprofesional();
    }

    public Usuario(UsuarioDto usuario,Rol rol,String password) {
        this.id = usuario.getId();
        this.rol = rol;
        this.nombre = usuario.getNombre();
        this.username = usuario.getUsername();
        this.contrasenna = password;
        this.noprofesional = usuario.getNoprofesional();
    }

    @PrePersist
    public void prePersist() {
        Validacion.validarElemento(this);
    }

    @PreUpdate
    public void preUpdate() {
        Validacion.validarElemento(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+this.rol.getNombreRol()));
    }

    @Override
    public String getPassword() {
        return getContrasenna();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
