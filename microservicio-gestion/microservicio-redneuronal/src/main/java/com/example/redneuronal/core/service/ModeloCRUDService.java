package com.example.redneuronal.core.service;

import org.neuroph.core.NeuralNetwork;

import java.util.List;

public interface ModeloCRUDService {
    void guardarModelo(NeuralNetwork red);
    NeuralNetwork cargarModelo();
    List<String> cargarUltimasEnfermedades();
}
