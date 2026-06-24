package com.example.sennal.core.serviceImpl;

import com.example.sennal.core.dto.ListaSenalesDTO;
import com.example.sennal.core.dto.ListaSensoresDto;
import com.example.sennal.core.dto.SennalDto;
import com.example.sennal.core.dto.entrenamiento.ModeloRequest;
import com.example.sennal.core.dto.entrenamiento.PatologiaDto;
import com.example.sennal.core.entities.Sennal;
import com.example.sennal.core.exception.SearchException;
import com.example.sennal.core.feingclient.DatoClient;
import com.example.sennal.core.feingclient.PatologiaClient;
import com.example.sennal.core.feingclient.RedNeuronalClient;
import com.example.sennal.core.repository.SennalRepository;
import com.example.sennal.core.service.SennalService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SennalServiceImpl implements SennalService {
    private SennalRepository sennalRepository;
    private DatoClient datoClient;
    private RedNeuronalClient red;
    private PatologiaClient enferm;

    @Autowired
    public SennalServiceImpl(SennalRepository sennalRepository, DatoClient datoClient, RedNeuronalClient red, PatologiaClient enferm) {
        this.sennalRepository = sennalRepository;
        this.datoClient = datoClient;
        this.red = red;
        this.enferm = enferm;
    }

    @Override
    public void insertarsennal(ListaSenalesDTO sennales) {
        for(SennalDto sennalDto: sennales.getSenales()) {
            sennalRepository.save(new Sennal(sennalDto));
        }

        /*List<PatologiaDto> enfermedades = enferm.obtenerPatologias();
        List<String> nombresPatologias = enfermedades.stream()
                .map(PatologiaDto::getNombre)
                .collect(Collectors.toList());
        //ModeloRequest modelo =new ModeloRequest(SenalesPredecir(sennales), nombresPatologias);
        //String respuesta = red.entrenarConNuevosDatos(modelo);
        /*HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8090/api/v1/modelo/entrenar"))
                .POST(new Gson().toJson(modelo))
                .wait(Duration.ofHours(2))
                .build();
        cliente.send(request);*/
    }

    @Override
    public void eliminarsennal(Long id) throws SearchException {
        if (sennalRepository.existsById(id)){
            sennalRepository.deleteById(id);
        }else{
            throw new SearchException("No existe la sennal a eliminar");
        }
    }

    @Override
    public ListaSensoresDto todassennal(Long iddato) {
        ListaSensoresDto listaSensoresDto = new ListaSensoresDto();

        if (datoClient.existeDato(iddato)) {
            List<Sennal> sennalList = sennalRepository.findAllByIddato(iddato);

            listaSensoresDto.setAcelerometroX(
                    sennalList.stream().map(Sennal::getAcelerometrox).collect(Collectors.toList())
            );
            listaSensoresDto.setAcelerometroY(
                    sennalList.stream().map(Sennal::getAcelerometroy).collect(Collectors.toList())
            );
            listaSensoresDto.setAcelerometroZ(
                    sennalList.stream().map(Sennal::getAcelerometroz).collect(Collectors.toList())
            );

            listaSensoresDto.setGiroscopioX(
                    sennalList.stream().map(Sennal::getGiroscopiox).collect(Collectors.toList())
            );
            listaSensoresDto.setGiroscopioY(
                    sennalList.stream().map(Sennal::getGiroscopioy).collect(Collectors.toList())
            );
            listaSensoresDto.setGiroscopioZ(
                    sennalList.stream().map(Sennal::getGiroscopioz).collect(Collectors.toList())
            );

            listaSensoresDto.setMagnetometroX(
                    sennalList.stream().map(Sennal::getMagnetometrox).collect(Collectors.toList())
            );
            listaSensoresDto.setMagnetometroY(
                    sennalList.stream().map(Sennal::getMagnetometroy).collect(Collectors.toList())
            );
            listaSensoresDto.setMagnetometroZ(
                    sennalList.stream().map(Sennal::getMagnetometroz).collect(Collectors.toList())
            );
        }

        return listaSensoresDto;
    }

    private List<com.example.sennal.core.dto.entrenamiento.Sennal> SenalesPredecir(ListaSenalesDTO senales){
        List<com.example.sennal.core.dto.entrenamiento.Sennal> sennalList = new ArrayList<>();
        com.example.sennal.core.dto.entrenamiento.Sennal sennal = null;

        List<Double> AccX = senales.getSenales().stream().map(SennalDto::getAcelerometrox).collect(Collectors.toList());
        List<Double> AccY = senales.getSenales().stream().map(SennalDto::getAcelerometroy).collect(Collectors.toList());
        List<Double> AccZ = senales.getSenales().stream().map(SennalDto::getAcelerometroz).collect(Collectors.toList());

        List<Double> GyroX = senales.getSenales().stream().map(SennalDto::getGiroscopiox).collect(Collectors.toList());
        List<Double> GyroY = senales.getSenales().stream().map(SennalDto::getGiroscopioy).collect(Collectors.toList());
        List<Double> GyroZ = senales.getSenales().stream().map(SennalDto::getGiroscopioz).collect(Collectors.toList());

        List<Double> MagX = senales.getSenales().stream().map(SennalDto::getMagnetometrox).collect(Collectors.toList());
        List<Double> MagY = senales.getSenales().stream().map(SennalDto::getMagnetometroy).collect(Collectors.toList());
        List<Double> MagZ = senales.getSenales().stream().map(SennalDto::getMagnetometroz).collect(Collectors.toList());

        List<String> diagnosticos = senales.getEnfermedades();

        sennal = new com.example.sennal.core.dto.entrenamiento.Sennal(AccX,AccY,AccZ,GyroX,GyroY,GyroZ,MagX,MagY,MagZ,diagnosticos);
        sennalList.add(sennal);

        return sennalList;
    }
}
