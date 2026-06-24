package com.example.redneuronal.core.controller;

import com.example.redneuronal.core.dto.ModeloRequest;
import com.example.redneuronal.core.service.ModeloCRUDService;
import com.example.redneuronal.core.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/modelo")
public class ModeloController {
    private ModeloService modeloService;
    private ModeloCRUDService modeloCRUDService;

    @Autowired
    public ModeloController(ModeloService modeloService, ModeloCRUDService modeloCRUDService) {
        this.modeloService = modeloService;
        this.modeloCRUDService = modeloCRUDService;
    }

    // Endpoint para crear el primer modelo con datos preexistentes
    @PostMapping("/crear-inicial")
    public ResponseEntity<?> crearModeloInicial(@RequestBody ModeloRequest request) {
        try{
            modeloCRUDService.guardarModelo(modeloService.crearModeloInicial(request));
            return ResponseEntity.ok("Modelo creado con éxito.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Entrenamiento adicional
    @PostMapping("/entrenar")
    public ResponseEntity<?> entrenarConNuevosDatos(@RequestBody ModeloRequest request) {
      try{
          modeloCRUDService.guardarModelo( modeloService.entrenarModeloConDatosNuevos(request,modeloCRUDService.cargarModelo()));
          return ResponseEntity.ok("Modelo actualizado con nuevos datos.");
      }catch (Exception e){
          return ResponseEntity.badRequest().body(e.getMessage());
      }
    }

    // Predicción con señales
    @PostMapping("/predecir")
    public ResponseEntity<?> predecir(@RequestBody ModeloRequest request) {
        try{
            List<String> resultado = modeloService.predecir(request,modeloCRUDService.cargarModelo());
            return ResponseEntity.ok(resultado);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
