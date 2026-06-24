package com.example.redneuronal.core.serviceimpl;

import com.example.redneuronal.core.dto.ModeloRequest;
import com.example.redneuronal.core.model.util.Prediccion;
import com.example.redneuronal.core.model.util.Sennal;
import com.example.redneuronal.core.service.ModeloService;
import com.example.redneuronal.core.serviceimpl.util.ObtenerCaracteristicas;
import org.neuroph.core.NeuralNetwork;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class ModeloServiceImpl implements ModeloService {

    private final int ENTRADAS = 45;

    @Override
    public NeuralNetwork crearModeloInicial(ModeloRequest request) {
        int salidas = request.enfermedades.size();
        int ocultas = (ENTRADAS + salidas) / 2;

        MultiLayerPerceptron red = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, ENTRADAS, ocultas, salidas);
        red.setLearningRule(new BackPropagation());
        DataSet dataSet = new DataSet(ENTRADAS, salidas);

        for (Sennal p : request.sennalList) {
            double[] entrada = ObtenerCaracteristicas.getInputFeatures(p);
            double[] salida = generarSalidasDesdeDiagnosticos(ObtenerCaracteristicas.getExpectedOutput(p), request.enfermedades);
            dataSet.add(new DataSetRow(entrada, salida));
        }

        red.learn(dataSet);

        return red;
    }

    @Override
    public NeuralNetwork entrenarModeloConDatosNuevos(ModeloRequest request,NeuralNetwork red) {
        if (red == null) throw new RuntimeException("No hay modelo entrenado previamente.");

        int salidas = request.getEnfermedades().size();
        DataSet dataset = new DataSet(ENTRADAS, salidas);

        for (Sennal p : request.getSennalList()) {
            double[] entrada = ObtenerCaracteristicas.getInputFeatures(p);
            List<String> diagnosticos = ObtenerCaracteristicas.getExpectedOutput(p);

            if (diagnosticos != null && !diagnosticos.isEmpty()) {
                // Señal etiquetada: entrenamos con su salida esperada
                double[] salida = generarSalidasDesdeDiagnosticos(diagnosticos, request.getEnfermedades());
                dataset.add(new DataSetRow(entrada, salida));
            } else {
                // Señal no etiquetada: usamos la red para pseudoetiquetar
                red.setInput(entrada);
                red.calculate();
                double[] salida = red.getOutput();

                double confianza = Arrays.stream(salida).max().orElse(0);
                if (confianza > 0.8) {
                    dataset.add(new DataSetRow(entrada, salida));
                }
            }
        }

        red.learn(dataset);
        return red;
    }

    @Override
    public List<String> predecir(ModeloRequest request, NeuralNetwork red) {
        if (red == null) throw new RuntimeException("No hay modelo cargado.");

        List<String> enfermedades = request.getEnfermedades();
        Sennal sennal = request.getSennalList().get(0); // Tomamos la primera señal para predecir

        double[] entrada = ObtenerCaracteristicas.getInputFeatures(sennal);
        red.setInput(entrada);
        red.calculate();
        double[] salida = red.getOutput();

        List<Prediccion> predicciones = new ArrayList<>();
        for (int i = 0; i < salida.length; i++) {
            predicciones.add(new Prediccion(i, salida[i]));
        }
        predicciones.sort((a, b) -> Double.compare(b.getValor(), a.getValor()));

        List<String> top3 = new ArrayList<>();
        for (int i = 0; i < Math.min(3, predicciones.size()); i++) {
            Prediccion p = predicciones.get(i);
            double valor = p.getValor();

            if (Double.isNaN(valor)) {
                top3.add("No se pudo predecir la enfermedad");
                continue; // pasa al siguiente si hay más
            }

            String nombre = (p.getIndice() < enfermedades.size())
                    ? enfermedades.get(p.getIndice())
                    : "Desconocido";

            top3.add(nombre + " " + String.format("%.2f%%", valor * 100));
        }
        return top3;
    }


    private double[] generarSalidasDesdeDiagnosticos(List<String> diagnosticos, List<String> enfermedades) {
        double[] salida = new double[enfermedades.size()];
        for (int i = 0; i < diagnosticos.size(); i++) {
            int idx = enfermedades.indexOf(diagnosticos.get(i));
            if (idx >= 0) salida[idx] = 1.0 - (i * 0.3);
        }
        return salida;
    }


}
