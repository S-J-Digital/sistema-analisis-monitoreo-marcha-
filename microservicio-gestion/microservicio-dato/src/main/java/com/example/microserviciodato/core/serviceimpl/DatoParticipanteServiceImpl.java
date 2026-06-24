package com.example.microserviciodato.core.serviceimpl;

import com.example.microserviciodato.core.dto.DatoIdDto;
import com.example.microserviciodato.core.dto.DatoParticipanteDto;
import com.example.microserviciodato.core.dto.PatologiaDto;
import com.example.microserviciodato.core.dto.SennalDto;
import com.example.microserviciodato.core.entities.DatoParticipante;
import com.example.microserviciodato.core.exception.SearchException;
import com.example.microserviciodato.core.feingclient.PatologiaClient;
import com.example.microserviciodato.core.feingclient.SennalClient;
import com.example.microserviciodato.core.http.response.PatologiaResponse;
import com.example.microserviciodato.core.repository.DatoParticipanteRepository;
import com.example.microserviciodato.core.service.DatoParticipanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DatoParticipanteServiceImpl implements DatoParticipanteService {
    private DatoParticipanteRepository datoParticipanteRepository;
    private PatologiaClient patologiaClient;
    private SennalClient sennalClient;

    @Autowired
    public DatoParticipanteServiceImpl(DatoParticipanteRepository datoParticipanteRepository, PatologiaClient patologiaClient, SennalClient sennalClient) {
        this.datoParticipanteRepository = datoParticipanteRepository;
        this.patologiaClient = patologiaClient;
        this.sennalClient = sennalClient;
    }

    @Override
    public void insertarDato(DatoParticipante datoParticipante) {
        if(!datoParticipanteRepository.existsDatoParticipanteByFechaAndIdparticipante(datoParticipante.getFecha(),datoParticipante.getIdparticipante())){
            datoParticipanteRepository.save(datoParticipante);
        }
    }

    @Override
    public void modificarDato(DatoParticipanteDto datoParticipante, Long id) {
        if(datoParticipanteRepository.existsById(id)){
            datoParticipante.setId(id);
            datoParticipanteRepository.save(new DatoParticipante(datoParticipante));
        }
    }

    @Override
    public void eliminarDato(Long id) {
        if(datoParticipanteRepository.existsById(id)){
            datoParticipanteRepository.deleteById(id);
        }
    }

    @Override
    public Long obtenerIdbyparticipanteAndFecha(Long idParticipante,String fecha) {
        return datoParticipanteRepository.getIdByIdparticipanteAndFecha(idParticipante, LocalDate.parse(fecha));
    }

    @Override
    public List<PatologiaResponse> obtenerDatosXParticipante(Long id) {
        List<DatoParticipante> datoParticipanteList = datoParticipanteRepository.findAllByIdparticipante(id);

        if (datoParticipanteList.isEmpty()) {
            throw new RuntimeException("No se encontraron datos para el participante con ID: " + id);
        }

        return (List<PatologiaResponse>) datoParticipanteList.stream().map(dato -> {
            List<PatologiaDto> patologiaDtoList = patologiaClient.obtenerPatologiaXDato(dato.getId());
            SennalDto sennalDtoList = sennalClient.obtenerSennalXDato(dato.getId());

            return PatologiaResponse.builder()
                    .id(dato.getId())
                    .idparticipante(dato.getIdparticipante())
                    .fecha(dato.getFecha())
                    .hora(dato.getHora())
                    .edad(dato.getEdad())
                    .patologia(dato.isPatologia())
                    .patologiaList(patologiaDtoList)
                    .numerodecalzado(dato.getNumerodecalzado())
                    .medicioncinturatobillo(dato.getMedicioncinturatobillo())
                    .largopierna(dato.getLargopierna())
                    .alturadelsensor(dato.getAlturadelsensor())
                    .sennalList(sennalDtoList)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<DatoParticipante> obtenerDatoXId(Long id) throws SearchException {
        return Optional.ofNullable(datoParticipanteRepository.findById(id).orElseThrow(()-> new SearchException("No existe un usuario con ese ID")));
    }
    @Override
    public boolean existeDato(Long id) throws SearchException {
        return datoParticipanteRepository.existsById(id)? true : false;
    }
}
