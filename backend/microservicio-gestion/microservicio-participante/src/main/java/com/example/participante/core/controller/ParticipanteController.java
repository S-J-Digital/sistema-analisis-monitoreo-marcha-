package com.example.participante.core.controller;

import com.example.participante.core.dto.ParticipanteDto;
import com.example.participante.core.dto.ParticipanteUpdateDto;
import com.example.participante.core.entities.Participante;

import com.example.participante.core.service.LogsService;
import com.example.participante.core.service.ParticipanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/participante")
@Tag(name = "Controlador de participates",
        description = "Controlador encargardo de todo lo referente con los participates a los que se le realizará las pruebas")
public class ParticipanteController {
    private ParticipanteService participanteService;
    private LogsService logsService;
    private String aceptado = "Aceptado";
    private String error = "Error";

    @Autowired
    public ParticipanteController(ParticipanteService participanteService, LogsService logsService) {
        this.participanteService = participanteService;
        this.logsService = logsService;
    }

    @PostMapping("/create")
    @Operation(summary = "Insertar participante",
            description = "Permite insertar un participante")
    public ResponseEntity<?> insertarParticipante(@RequestBody ParticipanteDto participanteDto,HttpServletRequest request){
        try{
            participanteService.insertarParticipante(participanteDto);
            logsService.insertarLog(request,aceptado,"Se insertó un participante");
            return ResponseEntity.ok("Se insertó correctamente un participante");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body("No se pudo insertar devido a un error");

        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modificar participante",
            description = "Permite modificar un participante")
    public ResponseEntity<?> modificarParticipante(@RequestBody ParticipanteUpdateDto participanteDto, @PathVariable Long id, HttpServletRequest request){
        try{
            participanteService.modificarParticipante(participanteDto,id);
            logsService.insertarLog(request,aceptado,"Se modificó un participante");
            return ResponseEntity.ok("Se modificó un participante correctamente");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body("No se modificó debido a un error");
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Listado de participantes",
            description = "Permite listar todos los participantes del sistema")
    public ResponseEntity<?> obtenerParticipantes(){
        try{
            List<Participante> participanteList = participanteService.obtenerParticipantes();
            return ResponseEntity.ok(participanteList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getParticipante/{id}")
    @Operation(summary = "Obtener participante por ID", description = "Permite obtener un participante a raíz de su ID")
    public ResponseEntity<?> obtenerParticipanteXId(@PathVariable Long id){
        try{
            ParticipanteDto participante = new ParticipanteDto(participanteService.obtenerParticipanteXId(id).get());
            return ResponseEntity.ok(participante);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/obtenerParticipanteXCi/{ci}")
    @Operation(summary = "Obtener participante por CI", description = "Permite obtener un participante a raíz de su CI")
    public ResponseEntity<?> obtenerParticipanteXCI(@RequestBody String ci){
        try{
            ParticipanteDto participante = new ParticipanteDto(participanteService.obtenerParticipanteXCI(ci).get());
            return ResponseEntity.ok(participante);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/obtenerParticipanteXUsuario/{id}")
    @Operation(summary = "Obtener participantes para un usuario", description = "Permite obtener una lista de participantes a raíz del usuario")
    public ResponseEntity<?> obtenerParticipanteXUsuario(@PathVariable Long id){
        try{
            List<Participante> participantes = participanteService.obtenerParticipanteXUsuario(id);
            return ResponseEntity.ok(participantes);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/obtenerDatoXParticipante/{idparticipante}")
    @Operation(summary = "Obtener datos para un participante", description = "Permite obtener una lista de datos a raíz del participante")
    public ResponseEntity<?> obtenerDatoXParticipante(@PathVariable Long idparticipante){
        try{
            return ResponseEntity.ok(participanteService.obtenerDatosXParticipante(idparticipante));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar participante por ID", description = "Permite eliminar un participante a raíz de su ID")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id, HttpServletRequest request){
        try{
            participanteService.eliminarParticipante(id);
            logsService.insertarLog(request,aceptado,"Se eliminó un participante");
            return ResponseEntity.ok("Se eliminó el participante");
        }catch (Exception e){
            logsService.insertarLog(request,error,e.getMessage());
            return ResponseEntity.badRequest().body("No se pudo eliminar devido a un error");
        }
    }

    @GetMapping("/getIdbyParticipantebyCi/{ci}")
    public ResponseEntity<?> existeParticipantebyCi(@PathVariable String ci) {
        try{
            Long idparticipante = participanteService.existeParticipantebyCi(ci);
            return ResponseEntity.ok(idparticipante);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
