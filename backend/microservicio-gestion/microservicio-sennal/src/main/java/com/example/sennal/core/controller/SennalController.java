package com.example.sennal.core.controller;

import com.example.sennal.core.dto.ListaSenalesDTO;
import com.example.sennal.core.dto.SennalDto;
import com.example.sennal.core.entities.Sennal;
import com.example.sennal.core.service.LogsService;
import com.example.sennal.core.service.SennalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sennal")
@Tag(name = "Controlador de sennal",
        description = "Controlador encargardo de todo lo referente con las sennal recibidas desde el celular")
public class SennalController {
    private LogsService logsService;
    private SennalService sennalService;

    private String aceptado = "Aceptado";
    private String error = "Error";

    @Autowired
    public SennalController(LogsService logsService, SennalService sennalService) {
        this.logsService = logsService;
        this.sennalService = sennalService;
    }

    @PostMapping("/create")
    @Operation(summary = "Insertar sennal",
            description = "Permite insertar una sennal")
    public ResponseEntity<?> insertarPatologia(@RequestBody ListaSenalesDTO listaSenales, HttpServletRequest request){
        try{
            sennalService.insertarsennal(listaSenales);
            logsService.insertarLog(request,aceptado,"Se insertaron las sennales");

            return ResponseEntity.ok("Se insertó una sennal correctamente");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{idSennal}")
    @Operation(summary = "Eliminar sennal",
            description = "Permite eliminar una sennal")
    public ResponseEntity<?> eliminarSennal(@PathVariable Long idSennal,  HttpServletRequest request){
        try{
            sennalService.eliminarsennal(idSennal);
            logsService.insertarLog(request,aceptado,"Se eliminó una sennal");
            return ResponseEntity.ok("Se eliminó una sennal correctamente");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body("Ocurrio un error");
        }
    }

    @GetMapping("/findAll/{iddato}")
    @Operation(summary = "Obtener sennal por dato",
            description = "Permite obtener una sennal por el dato")
    public ResponseEntity<?> obtenerSennalXDato(@PathVariable  Long iddato){
        try{
            return ResponseEntity.ok(sennalService.todassennal(iddato));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
