package com.example.patologia.core.serviceimpl;

import com.example.patologia.core.dto.DatoPatologiaDto;
import com.example.patologia.core.entities.DatoPatologia;
import com.example.patologia.core.entities.Patologia;
import com.example.patologia.core.exception.SearchException;
import com.example.patologia.core.repository.DatoPatologisRepository;
import com.example.patologia.core.service.DatoPatologiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DatoPatologiaServiceImpl implements DatoPatologiaService {
    private DatoPatologisRepository datoPatologisRepository;

    @Autowired
    public DatoPatologiaServiceImpl(DatoPatologisRepository datoPatologisRepository) {
        this.datoPatologisRepository = datoPatologisRepository;
    }

    @Override
    public void crearRelacion(DatoPatologiaDto datoPatologiaDto) {
        if(!datoPatologisRepository.existsByIddatoAndIdpatologiaAndFechaevaluacion(datoPatologiaDto.getIddato(),
                datoPatologiaDto.getIdpatologia(), LocalDate.parse(datoPatologiaDto.getFechaevaluacion()))){
            datoPatologisRepository.save(new DatoPatologia(datoPatologiaDto));
        }

    }

    @Override
    public List<Patologia> obtenerPatologiaXDato(Long id_dato) throws SearchException {
        return datoPatologisRepository.findAllByIddato(id_dato);
    }

    @Override
    public void eliminarRelacion(Long iddato, String fecha) {
        datoPatologisRepository.deleteByIddatoAndFechaevaluacion(iddato, LocalDate.parse(fecha));
    }
}
