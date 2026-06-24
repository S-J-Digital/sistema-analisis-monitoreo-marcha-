package com.example.patologia.core.service;

import com.example.patologia.core.dto.PatologiaDto;
import com.example.patologia.core.entities.Patologia;
import com.example.patologia.core.exception.SearchException;

import java.util.List;

public interface PatologiaService {
    void insertarPatologia(PatologiaDto patologiaDto) ;
    void modificarPatologia(PatologiaDto patologiaDto, Long idpatologia)throws SearchException;
    void eliminarPatologia(Long idpatologia)throws SearchException;
    Long obtenerIdbynombre(String nombre);
    List<Patologia> obtenerPatologias();
    List<Patologia> obtenerPatologiaXDato(Long id_dato) throws SearchException;

}
