package com.example.participante.core.serviceImpl;

import com.example.participante.core.dto.DatoDto;
import com.example.participante.core.dto.ParticipanteAllDto;
import com.example.participante.core.dto.ParticipanteDto;
import com.example.participante.core.dto.ParticipanteUpdateDto;
import com.example.participante.core.entities.Participante;
import com.example.participante.core.entities.UsuarioParticipante;
import com.example.participante.core.exception.SearchException;
import com.example.participante.core.feingclient.DatoClient;
import com.example.participante.core.feingclient.UsuarioClient;
import com.example.participante.core.http.response.DatosResponse;
import com.example.participante.core.repository.ParticipanteRepository;
import com.example.participante.core.service.ParticipanteService;
import com.example.participante.core.service.UsuarioParticipanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipanteServiceImpl implements ParticipanteService {
    private ParticipanteRepository participanteRepository;
    private UsuarioParticipanteService usuarioParticipanteService;
    private UsuarioClient usuarioClient;
    private DatoClient datoClient;

    @Autowired
    public ParticipanteServiceImpl(ParticipanteRepository participanteRepository, UsuarioParticipanteService usuarioParticipanteService, UsuarioClient usuarioClient, DatoClient datoClient) {
        this.participanteRepository = participanteRepository;
        this.usuarioParticipanteService = usuarioParticipanteService;
        this.usuarioClient = usuarioClient;
        this.datoClient = datoClient;
    }

    @Override
    public void insertarParticipante(ParticipanteDto participanteDto) throws SearchException {
        if(usuarioClient.existeUsuario(participanteDto.getUsuario())){
           if(!participanteRepository.existsByCi(participanteDto.getCi())){
               participanteRepository.save(new Participante(participanteDto));
               try{
                   if(participanteRepository.existsByCi(participanteDto.getCi())){
                       Participante participante = obtenerParticipanteXCI(participanteDto.getCi()).get();
                       usuarioParticipanteService.crearRelacion(new UsuarioParticipante(participanteDto.getUsuario(),participante.getId()));
                   }
               }catch (Exception e){
                   throw new SearchException(e.getMessage());
               }
           }
        }

    }

    @Override
    public void modificarParticipante(ParticipanteUpdateDto participante, Long id) throws SearchException {
        if(participanteRepository.existsById(id)){
            participante.setId(id);
            participanteRepository.save(new Participante(participante));
        }else{
            throw new SearchException("No existe el participante a modificar");
        }
    }

    @Override
    public void eliminarParticipante(Long id) throws SearchException {
        if(participanteRepository.existsById(id)){
            participanteRepository.deleteById(id);
        }else{
            throw new SearchException("No existe el participante a eliminar");
        }
    }

    @Override
    public List<Participante> obtenerParticipantes() {
        return participanteRepository.findAll();
    }

    @Override
    public Optional<Participante> obtenerParticipanteXId(Long id) throws SearchException {
        return Optional.ofNullable(participanteRepository.findById(id).orElseThrow(()-> new SearchException("No existe un participante con ese ID")));
    }

    @Override
    public Optional<Participante> obtenerParticipanteXCI(String ci) throws SearchException {
        return Optional.ofNullable(participanteRepository.findByCi(ci).orElseThrow(()-> new SearchException("No existe un participante con ese ID")));
    }

    @Override
    public List<Participante> obtenerParticipanteXUsuario(Long id_usuario) throws SearchException {
        return usuarioParticipanteService.obtenerParticipanteXUsuario(id_usuario);
    }


    @Override
    public DatosResponse obtenerDatosXParticipante(Long idparticipante) {
        Participante participante = participanteRepository.findById(idparticipante).orElse(new Participante());
        List<DatoDto> datoDtoList = datoClient.obtenerDatoXParticipante(idparticipante);
        datoDtoList.get(0);
        return DatosResponse.builder()
                .nombre(participante.getNombre())
                .ci(participante.getCi())
                .sexo(participante.getSexo())
                .telefono(participante.getTelefono())
                .datoDtoList(datoDtoList)
                .build();

    }

    @Override
    public Long existeParticipantebyCi(String ci) {
        return participanteRepository.getIdByCi(ci);
    }



}
