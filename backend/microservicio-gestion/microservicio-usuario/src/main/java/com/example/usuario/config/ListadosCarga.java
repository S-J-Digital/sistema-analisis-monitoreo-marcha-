package com.example.usuario.config;

import com.example.usuario.core.model.Estado;
import com.example.usuario.core.model.Rol;
import com.example.usuario.core.repository.EstadoRepository;
import com.example.usuario.core.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListadosCarga {
    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private RolRepository rolRepository;

    @Bean
    @Lazy(false)
    public List<Estado> estadosLista(){
        return estadoRepository.findAll().stream().toList();
    }

    @Bean
    @Lazy(false)
    public List<String> nombreEstadosLista(){
        return estadoRepository.findAll()
                .stream().map(Estado::getEstado).toList();
    }

    @Bean
    @Lazy(false)
    public List<Rol> rolesLista(){
        return rolRepository.findAll()
                .stream().toList();
    }

    @Bean
    @Lazy(false)
    public List<String> nombreRolesLista(){
        return rolRepository.findAll()
                .stream().map(Rol::getNombreRol).toList();
    }
}
