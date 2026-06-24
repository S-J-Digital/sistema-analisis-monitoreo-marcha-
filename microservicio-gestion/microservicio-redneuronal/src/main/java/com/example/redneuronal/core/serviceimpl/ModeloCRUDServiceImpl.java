package com.example.redneuronal.core.serviceimpl;

import com.example.redneuronal.core.entities.RedNeuronalEntity;
import com.example.redneuronal.core.repository.RedNeuronalRepository;
import com.example.redneuronal.core.service.ModeloCRUDService;
import org.neuroph.core.NeuralNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
@Service
public class ModeloCRUDServiceImpl implements ModeloCRUDService {
    public RedNeuronalRepository redRepository;

    @Autowired
    public ModeloCRUDServiceImpl(RedNeuronalRepository redRepository) {
        this.redRepository = redRepository;
    }

    @Override
    public void guardarModelo(NeuralNetwork red) {
        try {
            // Crear archivo temporal
            File tempFile = File.createTempFile("modelo", ".nnet");
            red.save(tempFile.getAbsolutePath());

            // Leer el archivo como byte[]
            byte[] modelBytes = Files.readAllBytes(tempFile.toPath());

            // Guardar en la base de datos
            RedNeuronalEntity entity = new RedNeuronalEntity();
            entity.setNombre("modelo-principal"); // O usa un nombre generado
            entity.setFecha(LocalDate.now());
            entity.setModelo(modelBytes);

            redRepository.save(entity);

            // Eliminar archivo temporal si quieres
            tempFile.delete();
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el modelo: "+ e.getMessage());
        }
    }

    @Override
    public NeuralNetwork cargarModelo() {
        return redRepository.findTopByOrderByFechaDesc()
                .map(e -> NeuralNetwork.load(new ByteArrayInputStream(e.modelo)))
                .orElse(null);
    }

    @Override
    public List<String> cargarUltimasEnfermedades() {
        return redRepository.findTopByOrderByFechaDesc()
                .map(e -> List.of(e.nombre.split(",")))
                .orElse(List.of());
    }

}
