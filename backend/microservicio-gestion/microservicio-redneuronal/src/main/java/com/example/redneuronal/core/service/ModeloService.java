package com.example.redneuronal.core.service;

import com.example.redneuronal.core.dto.ModeloRequest;
import com.example.redneuronal.core.model.util.Sennal;
import org.neuroph.core.NeuralNetwork;

import java.util.List;

public interface ModeloService {
    NeuralNetwork crearModeloInicial(ModeloRequest request);
    NeuralNetwork entrenarModeloConDatosNuevos(ModeloRequest request,NeuralNetwork red);
    List<String> predecir(ModeloRequest request, NeuralNetwork red);


}
