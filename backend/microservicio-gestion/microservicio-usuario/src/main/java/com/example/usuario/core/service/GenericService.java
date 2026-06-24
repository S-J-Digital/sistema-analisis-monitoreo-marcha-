package com.example.usuario.core.service;

import com.example.usuario.core.exception.SearchException;
import com.example.usuario.core.model.Usuario;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GenericService <E>{
    void insertarEntity(E e);
    @Transactional(rollbackFor = Exception.class)
    void eliminarEntity(Long id) throws SearchException;
    @Transactional(rollbackFor = Exception.class)
    List<E> obtenerEntitys();
    @Transactional(rollbackFor = Exception.class)
    Optional<E> obtenerEntityXId(Long id) throws SearchException;
}
