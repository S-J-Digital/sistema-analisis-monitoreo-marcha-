package com.example.microserviciodato.core.controller;

import com.example.microserviciodato.core.dto.DatoIdDto;
import com.example.microserviciodato.core.dto.DatoParticipanteDto;
import com.example.microserviciodato.core.entities.DatoParticipante;
import com.example.microserviciodato.core.exception.SearchException;
import com.example.microserviciodato.core.http.response.PatologiaResponse;
import com.example.microserviciodato.core.service.DatoParticipanteService;
import com.example.microserviciodato.core.service.LogsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/datoparticipante")
@Tag(name = "Controlador de los datos de los participates",
        description = "Controlador encargardo de todo lo referente con los datos de los participates")
public class DatoParticipanteController {
    private DatoParticipanteService datoParticipanteService;
    private LogsService logsService;
    private String aceptado = "Aceptado";
    private String error = "Error";

    @Autowired
    public DatoParticipanteController(DatoParticipanteService datoParticipanteService, LogsService logsService) {
        this.datoParticipanteService = datoParticipanteService;
        this.logsService = logsService;
    }

    @PostMapping("/create")
    @Operation(summary = "Insertar los datos del participante",
            description = "Permite insertar los datos de un participante")
    public ResponseEntity<?> insertarDato(@RequestBody DatoParticipanteDto datoParticipanteDto, HttpServletRequest request){
        try{
            datoParticipanteService.insertarDato(new DatoParticipante(datoParticipanteDto));
            logsService.insertarLog(request,aceptado,"Se insertó el dato del participante");
            return ResponseEntity.ok("Se insertó correctamente el dato");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body("Ocurrio un error");
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modificar los datos del participante",
            description = "Permite modificar los datos de un participante")
    public ResponseEntity<?> modificarDato(@RequestBody DatoParticipanteDto datoParticipanteDto, @PathVariable Long id,HttpServletRequest request){
        try{
            datoParticipanteService.modificarDato(datoParticipanteDto,id);
            logsService.insertarLog(request,aceptado,"Se modificó el dato del participante");
            return ResponseEntity.ok("Se modificó el dato correctamente");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body("Ocurrio un error");
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar los datos del participante",
            description = "Permite eliminar los datos de un participante")
    public ResponseEntity<?> eliminarDato(@PathVariable Long id,HttpServletRequest request){
        try{
            datoParticipanteService.eliminarDato(id);
            logsService.insertarLog(request,aceptado,"Se eliminó un dato");
            return ResponseEntity.ok("Se eliminó un dato correctamente");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body("Ocurrio un error");
        }
    }

    @GetMapping("/allbyParticipante/{idparticipante}")
    @Operation(summary = "Obtener los datos del participante",
            description = "Permite obtener los datos de un participante, las patologias y las señales")
    public ResponseEntity<?> obtenerDatoXParticipante(@PathVariable Long idparticipante){
        try{
            List<PatologiaResponse> datoParticipanteList = datoParticipanteService.obtenerDatosXParticipante(idparticipante);
            return ResponseEntity.ok(datoParticipanteList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/obtenerDato/{id}")
    @Operation(summary = "Obtener dato por ID", description = "Permite obtener un dato a raíz de su ID")
    public ResponseEntity<?> obtenerDatosPorId(@PathVariable Long id){
        try{
            DatoParticipanteDto datoParticipanteDto = new DatoParticipanteDto(datoParticipanteService.obtenerDatoXId(id).get());
            return ResponseEntity.ok(datoParticipanteDto);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<?> existeDato(@PathVariable Long id){
        try{
            if(datoParticipanteService.existeDato(id)){
                return ResponseEntity.ok(true);
            }else{
                return ResponseEntity.ok(false);
            }
        } catch (SearchException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping("/obtenerIdbyparticipanteAndFecha/idParticipante={idParticipante}&fecha={fecha}")
    public ResponseEntity<?> obtenerIdbyparticipanteAndFecha(@PathVariable Long idParticipante,@PathVariable String fecha){
        try{
            Long iddato=datoParticipanteService.obtenerIdbyparticipanteAndFecha(idParticipante,fecha);
           return ResponseEntity.ok(iddato);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
